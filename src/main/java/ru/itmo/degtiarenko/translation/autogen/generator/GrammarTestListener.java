package ru.itmo.degtiarenko.translation.autogen.generator;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.TerminalNode;
import ru.itmo.degtiarenko.translation.autogen.grammar.GrammarBaseListener;

/**
 * Printing all tokens.
 */
public class GrammarTestListener extends GrammarBaseListener {
    @Override
    public void visitTerminal(@NotNull TerminalNode node) {
        super.visitTerminal(node);

        System.err.println(node.getSymbol().getText());
    }
}
