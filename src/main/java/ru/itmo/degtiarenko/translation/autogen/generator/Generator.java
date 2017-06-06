package ru.itmo.degtiarenko.translation.autogen.generator;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import ru.itmo.degtiarenko.translation.autogen.grammar.GrammarLexer;
import ru.itmo.degtiarenko.translation.autogen.grammar.GrammarParser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

/**
 * Start class.
 */
public class Generator {
    public static void main(String[] args) throws IOException {
        InputStream input = new FileInputStream(Paths.get("src/main/antlr4/Java.gramm")
                .toFile());
        GrammarLexer lexer = new GrammarLexer(new ANTLRInputStream(input));
        GrammarParser parser = new GrammarParser(new CommonTokenStream(lexer));

        ParseTreeListener listener = new GrammarTestListener();
        parser.addParseListener(listener);
        parser.gramm();
        parser.removeParseListener(listener);
    }
}
