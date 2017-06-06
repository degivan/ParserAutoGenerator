package ru.itmo.degtiarenko.translation.autogen.generator;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
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
        InputStream input = new FileInputStream(Paths.get("src/main/resources/Java.gramm")
                .toFile());
        GrammarLexer lexer = new GrammarLexer(new ANTLRInputStream(input));
        GrammarParser parser = new GrammarParser(new CommonTokenStream(lexer));
        ParseTreeWalker walker = new ParseTreeWalker();
        GenerateGrammarListener listener = new GenerateGrammarListener();

        walker.walk(listener, parser.gramm());

        System.out.println(listener.getHeader());
        System.out.println(listener.getTerminals().toString());
        System.out.println(listener.getNonTerminals().toString());
    }
}
