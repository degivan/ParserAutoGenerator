package ru.itmo.degtiarenko.translation.autogen.generator.nodes;

import java.util.*;

/**
 * Created by Иван on 06.06.2017.
 */
public class NTNode extends Node {

    private Map<String, String> declAttributes = new HashMap<>();
    private String returnType = "void";

    private List<Production> productions = new ArrayList<>();
    private List<String> callAttrs = new ArrayList<>();

    public NTNode(String text) {
        super(text);
    }

    public void putDeclAttribute(String type, String name) {
        declAttributes.put(type, name);
    }

    public String getDeclAttrs(boolean withTypes) {
        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = declAttributes.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            if (withTypes) {
                builder.append(entry.getValue()).append(" ");
            }
            builder.append(entry.getKey());
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getReturnType() {
        return returnType;
    }

    public void addProduction(Production production) {
        productions.add(production);
    }

    public List<Production> getProductions() {
        return productions;
    }

    public  void setCallAttrs(List<String> callAttrs) {
        this.callAttrs = callAttrs;
    }

    @Override
    public String toString() {
        return "NTNode{" +
                (declAttributes.size() != 0 ? "declAttributes=" + declAttributes : "") +
                (!Objects.equals(returnType, "") ? ", returnType='" + returnType + '\'' : "") +
                ", productions=" + productions +
                (callAttrs.size() != 0 ? ", callAttrs=" + callAttrs : "")+
                "}\n";
    }

    public String getCallAttrs() {
        StringBuilder builder = new StringBuilder();
        Iterator<String> iterator = callAttrs.iterator();
        while (iterator.hasNext()) {
            String entry = iterator.next();
            builder.append("((Supplier<Integer>) () ->");
            builder.append(entry);
            builder.append(").get()");
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }
}
