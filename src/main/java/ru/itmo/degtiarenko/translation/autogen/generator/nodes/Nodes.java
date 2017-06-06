package ru.itmo.degtiarenko.translation.autogen.generator.nodes;

/**
 * Created by Иван on 06.06.2017.
 */
public class Nodes {
    public static TermNode termNode(Node node) {
        if (node instanceof TermNode) {
            return (TermNode) node;
        }
        return null;
    }

    public static NTNode nonTermNode(Node node) {
        if (node instanceof NTNode) {
            return (NTNode) node;
        }
        return null;
    }
}
