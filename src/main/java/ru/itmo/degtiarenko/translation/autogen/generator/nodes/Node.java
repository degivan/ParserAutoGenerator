package ru.itmo.degtiarenko.translation.autogen.generator.nodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Иван on 06.06.2017.
 */
public class Node {
    private final String name;

    public List<Node> getChildren() {
        return children;
    }

    private List<Node> children = new ArrayList<>();

    public Node(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addChild(Node node) {
        children.add(node);
    }

    @Override
    public boolean equals(Object o) {
        return (getClass() == o.getClass() && name.equals(((Node) o).name));
    }

    @Override
    public String toString() {
        return "Node{" +
                "name='" + name + '\'' +
                ", children=" + children +
                "}\n";
    }

    public int size() {
        return children.size();
    }

    public Node get(int i) {
        return children.get(i);
    }
}
