package ru.itmo.degtiarenko.translation.autogen.generator;

import ru.itmo.degtiarenko.translation.autogen.generator.nodes.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Иван on 06.06.2017.
 */
public class ParserGenerator {
    private static final String EPS = "EPS";
    private final Map<String, NTNode> nonTerminals;
    private final Map<String, TermNode> terminals;
    private final FirstFollowComputer firstFollowComputer;
    private final NTNode start;
    private final String header;

    public ParserGenerator(Map<String, TermNode> terminals, Map<String, NTNode> nonTerminals, NTNode start, String header) {
        this.terminals = terminals;
        this.nonTerminals = nonTerminals;
        this.firstFollowComputer = new FirstFollowComputer(terminals, nonTerminals, start);
        this.start = start;
        this.header = header;
    }


    public void generateParser(File parserFile) throws FileNotFoundException {
        firstFollowComputer.computeFirst();
        firstFollowComputer.computeFollow();

        generateCode(firstFollowComputer.getFirst(), firstFollowComputer.getFollow(), parserFile);
    }

    private void generateCode(Map<String, Set<String>> first, Map<String, Set<String>> follow, File file) throws FileNotFoundException {
        file.getParentFile().mkdirs();
        PrintWriter out = new PrintWriter(file);

        out.println(header);

        out.println("\nimport java.io.InputStream;");
        out.println("import java.text.ParseException;");
        out.println("import java.io.IOException;");
        out.println("import java.util.ArrayList;");
        out.println("import java.util.List;\n");
        out.println("import java.util.function.Supplier;");

        out.println("public class OurParser {");
        out.println("\tprivate OurLexer lex;");


        out.println("\tpublic " + start.getReturnType() + " parse(InputStream is" + (start.getDeclAttrs(true).isEmpty() ? "" : ", " + start.getDeclAttrs(true)) + ") throws ParseException, IOException {");
        out.println("\t\tlex = new OurLexer(is);");
        out.println("\t\tlex.nextToken();");
        out.println("\t\t" + (start.getReturnType().equals("void") ? "" : "return ") + start.getName() + "(" + start.getDeclAttrs(false) + ");");
        out.println("\t}\n");

        for (String nonTerm : nonTerminals.keySet()) {
            out.println("\tprivate " + nonTerminals.get(nonTerm).getReturnType() + " " + nonTerm + "(" + nonTerminals.get(nonTerm).getDeclAttrs(true) + ") throws ParseException, IOException {");
            out.println("\t\tswitch (lex.curToken()) {");

            Set<String> set = new HashSet<>(first.get(nonTerm));
            if (set.contains(EPS)) {
                set.addAll(follow.get(nonTerm));
            }
            set.remove(EPS);

            for (String term : set) {
                out.println("\t\t\tcase " + term + ": {");

                boolean ret = false;
                int suitableProds = 0;
                Set<String> rules = new HashSet<>();
                for (Production production : nonTerminals.get(nonTerm).getProductions()) {
                    if (suitableProds == 0 &&
                            production.get(0).getName().equals(EPS)) {
                        if (!production.getCode().isEmpty()) {
                            out.println(concat("\t\t\t\t", production.getCode()));
                            ret = true;
                        }
                    } else if (first.get(production.get(0).getName()).contains(term)) {
                        for (Node node : production.getChildren()) {
                            String name = node.getName();
                            if (!rules.contains(node.getName())) {
                                if (nonTerminals.containsKey(name) && !nonTerminals.get(name).getReturnType().equals("void")) {
                                    out.println("\t\t\t\tList<" + nonTerminals.get(name).getReturnType() + "> " + name + " = new ArrayList<>();");
                                    rules.add(node.getName());
                                } else if (terminals.containsKey(name)) {
                                    out.println("\t\t\t\tList<String> " + name + " = new ArrayList<>();");
                                    rules.add(name);
                                }
                            }
                        }

                        suitableProds++;

                        for (Node node : production.getChildren()) {
                            String name = node.getName();
                            if (terminals.containsKey(node.getName())) {
                                out.println("\t\t\t\tif (lex.curToken().toString().equals(\"" + name + "\")) {");
                                out.println("\t\t\t\t\t" + name + ".add(lex.curString());");
                                out.println("\t\t\t\t} else {");
                                out.println("\t\t\t\t\tthrow new AssertionError(\"" + name + " expected, instead of \" + lex.curToken().toString());");
                                out.println("\t\t\t\t}");
                                out.println("\t\t\t\tlex.nextToken();");
                            } else if (nonTerminals.containsKey(name) && !nonTerminals.get(name).getReturnType().equals("void")) {
                                out.print(String.format("\t\t\t\t" + name + ".add(" + name + "(" + Nodes.nonTermNode(node).getCallAttrs() + "));\n", name));
                            } else {
                                out.println("\t\t\t\t" + name + "(" + Nodes.nonTermNode(node).getCallAttrs() + ");");
                            }
                        }
                        if (!production.getCode().isEmpty()) {
                            out.println(concat("\t\t\t\t", production.getCode()));
                            ret = true;
                        }
                    }
                }

                if (suitableProds > 1) {
                    throw new AssertionError(String.format("Ambigous rule: %s", nonTerm));
                } else {
                    if (nonTerminals.get(nonTerm).getReturnType().equals("String"))
                        out.print(
                                (ret ? "" : "\t\t\t\treturn \"\";\n") +
                                        "\t\t\t}\n");
                    else
                        out.print(
                                (ret ? "" : "\t\t\t\treturn;\n") +
                                        "\t\t\t}\n");
                }
            }

            out.print(
                    "\t\t\tdefault:\n" +
                            "\t\t\t\tthrow new AssertionError();\n" +
                            "\t\t}\n" +
                            "\t}\n\n"
            );
        }

        out.print("}\n");
        out.close();
    }

    private String concat(String prefix, String content) {
        StringBuilder builder = new StringBuilder();

        for (String s : content.split("\\n")) {
            builder.append(prefix);
            builder.append(s);
        }

        return builder.toString();
    }
}
