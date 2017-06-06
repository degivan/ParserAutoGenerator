package ru.itmo.degtiarenko.translation.autogen.generator.nodes;

/**
 * Created by Иван on 06.06.2017.
 */
public class TermNode extends Node {
    private boolean skip = false;

    public boolean isSkip() {
        return skip;
    }

    public TermNode(String name) {
        super(name);
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }
}
