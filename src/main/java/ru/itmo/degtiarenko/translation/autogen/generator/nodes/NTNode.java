package ru.itmo.degtiarenko.translation.autogen.generator.nodes;

import java.util.*;

/**
 * Created by Иван on 06.06.2017.
 */
public class NTNode extends Node {

    private Map<String, String> declAttributes = new HashMap<>();
    private String returnType = "";

    private List<Production> productions = new ArrayList<>();
    private List<String> callAttrs = new ArrayList<>();

    public NTNode(String text) {
        super(text);
    }

    public void putDeclAttribute(String type, String name) {
        declAttributes.put(type, name);
    }

    public Map<String, String> getDeclAttributes() {
        return declAttributes;
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
}
