package ru.itmo.degtiarenko.translation.autogen.generator;

import ru.itmo.degtiarenko.translation.autogen.generator.nodes.NTNode;
import ru.itmo.degtiarenko.translation.autogen.generator.nodes.Production;
import ru.itmo.degtiarenko.translation.autogen.generator.nodes.TermNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Иван on 06.06.2017.
 */
public class FirstFollowComputer {
    public static final String EPS = "EPS";
    public static final String EOF = "EOF";
    private final Map<String, TermNode> terminals;
    private final Map<String, NTNode> nonTerminals;
    private final NTNode start;

    private Map<String, Set<String>> first = new HashMap<>();
    private Map<String, Set<String>> follow = new HashMap<>();

    public FirstFollowComputer(Map<String, TermNode> terminals, Map<String, NTNode> nonTerminals, NTNode start) {
        this.terminals = terminals;
        this.nonTerminals = nonTerminals;
        this.start = start;
    }

    public Map<String, Set<String>> getFirst() {
        return first;
    }

    public Map<String, Set<String>> getFollow() {
        return follow;
    }

    public void computeFirst() {
        for (String name : terminals.keySet()) {
            HashSet<String> set = new HashSet<>();
            set.add(name);
            first.put(name, set);
        }
        for (String name : nonTerminals.keySet()) {
            first.put(name, new HashSet<>());
            for (Production production : nonTerminals.get(name).getProductions()) {
                if (production.get(0).getName().equals(EPS)) {
                    first.get(name).add(EPS);
                }
            }
        }

        boolean changed;
        do {
            changed = false;
            for (String name : nonTerminals.keySet()) {
                for (Production production : nonTerminals.get(name).getProductions()) {
                    for (int i = 0; i < production.size(); i++) {
                        String label = production.get(i).getName();
                        if (first.get(label).contains(EPS)) {
                            for (String cur : first.get(label)) {
                                if (!first.get(name).contains(cur)) {
                                    first.get(name).add(cur);
                                    changed = true;
                                }
                            }
                            if (i == production.size() - 1) {
                                if (!first.get(name).contains(EPS)) {
                                    first.get(name).add(EPS);
                                    changed = true;
                                }
                            }
                        } else {
                            for (String cur : first.get(label)) {
                                if (!first.get(name).contains(cur)) {
                                    first.get(name).add(cur);
                                    changed = true;
                                }
                            }
                            break;
                        }
                    }
                }
            }
        } while (changed);
    }

    public void computeFollow() {
        for (String name : nonTerminals.keySet()) {
            follow.put(name, new HashSet<>());
        }

        follow.get(start.getName()).add(EOF);

        boolean changed;
        do {
            changed = false;
            for (String name : nonTerminals.keySet()) {
                for (Production production : nonTerminals.get(name).getProductions()) {
                    for (int i = 0; i < production.size() - 1; i++) {
                        if (nonTerminals.containsKey(production.get(i).getName())) {
                            for (String cur : first.get(production.get(i + 1).getName())) {
                                if (!cur.equals(EPS) && !follow.get(production.get(i).getName()).contains(cur)) {
                                    follow.get(production.get(i).getName()).add(cur);
                                    changed = true;
                                }
                            }
                        }
                    }
                    int i = production.size() - 1;
                    if (nonTerminals.containsKey(production.get(i).getName())) {
                        for (String cur : follow.get(name)) {
                            if (!cur.equals(EPS) && !follow.get(production.get(i).getName()).contains(cur)) {
                                follow.get(production.get(i).getName()).add(cur);
                                changed = true;
                            }
                        }
                    }
                    if (production.size() > 1 && first.get(production.get(production.size() - 1).getName()).contains(EPS)) {
                        i = production.size() - 2;
                        if (nonTerminals.containsKey(production.get(i).getName())) {
                            for (String cur : follow.get(production.get(i + 1).getName())) {
                                if (!cur.equals(EPS) && !follow.get(production.get(i).getName()).contains(cur)) {
                                    follow.get(production.get(i).getName()).add(cur);
                                    changed = true;
                                }
                            }
                        }
                    }
                }
            }
        } while (changed);
    }
}
