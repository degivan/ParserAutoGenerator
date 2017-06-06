package ru.itmo.degtiarenko.translation.autogen.generator.nodes;

import java.util.stream.Collectors;

/**
 * Created by Иван on 06.06.2017.
 */
public class Production extends Node {
    private String code = "";

    public Production() {
        super("production");
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "prod{" +
                (code != null ? "code='" + code + '\'' : "")
                + getChildren().stream()
                .map(Node::getName)
                .collect(Collectors.joining()) +
                '}';
    }

    public String getCode() {
        return code;
    }
}
