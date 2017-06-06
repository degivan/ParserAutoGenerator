package ru.itmo.degtiarenko.translation.autogen.generator;

import ru.itmo.degtiarenko.translation.autogen.generator.nodes.Node;
import ru.itmo.degtiarenko.translation.autogen.generator.nodes.TermNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

import static java.util.stream.Collectors.joining;

/**
 * Created by Иван on 06.06.2017.
 */
public class LexerGenerator {
    private final Map<String, TermNode> terminals;
    private final String header;

    public LexerGenerator(Map<String, TermNode> terminals, String header) {
        this.terminals = terminals;
        this.header = header;
    }

    public void generateTokenFile(File tokenFile) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(tokenFile);

        out.print(header);

        out.print("public enum Token {\n\t");
        out.print(terminals.keySet().stream().collect(joining(", ")));
        out.print("\n}");

        out.flush();
        out.close();
    }

    public void generateLexerFile(File lexerFile) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(lexerFile);

        out.println(header);

        out.println("\nimport java.io.IOException;");
        out.println("import java.io.InputStream;");
        out.println("import java.text.ParseException;\n");
        out.println("import java.util.ArrayList;\n" +
                "import java.util.HashMap;\n" +
                "import java.util.List;\n" +
                "import java.util.Map;");

        out.println("import java.util.regex.Pattern;\n");

        out.println("public class " + "OurLexer" + " {");
        out.println("\tprivate InputStream is;");
        out.println("\tprivate int curChar;");
        out.println("\tprivate int curPos;");
        out.println("\tprivate Token curToken;");
        out.println("\tprivate String curString;\n");
        out.println("\tprivate Map<Token, List<Pattern>> patterns = new HashMap<>();\n");

        out.println("\t{\n" + patternsCompilation() + "\n\t}");
        out.println("\tpublic " + "OurLexer" + "(InputStream is) throws ParseException, IOException {");
        out.println("\t\tthis.is = is;");
        out.println("\t\tcurPos = 0;");
        out.println("\t\tnextChar();");
        out.println("\t}\n");

        out.println(
                "\tprivate boolean isBlank(int c) { return c == ' ' || c == '\\r' || c == '\\n' || c == '\\t'; }\n");

        out.println("\tprivate void nextChar() throws ParseException, IOException {");
        out.println("\t\tcurPos++;");
        out.println("\t\ttry {");
        out.println("\t\t\tcurChar = is.read();");
        out.println("\t\t} catch (IOException e) {");
        out.println("\t\t\tthrow new ParseException(e.getMessage(), curPos);");
        out.println("\t\t}");
        out.println("\t}\n");

        out.println("\tpublic Token curToken() {\n\t\treturn curToken;\n\t}\n");

        out.println("\tpublic int curPos() {\n\t\treturn curPos;\n\t}\n");

        out.println("\tpublic String curString() {\n\t\treturn curString;\n\t}\n");

        out.println("\tpublic void nextToken() throws ParseException, IOException {");
        out.println("\t\tcurString = \"\";");

        out.println("\t\tif (curChar == -1) {");
        out.println("\t\t\tcurToken = Token.EOF;");
        out.println("\t\t\treturn;");
        out.println("\t\t}");

        boolean first = true;
        for (String curStringTerminal : terminals.keySet()) {
            out.println(String.format((first ? "\t\tif" : "\t\telse if") +
                    " (patterns.get(Token.%1$s).stream().anyMatch(p -> p.matcher(curString).matches())) {\n" +
                    "\t\t\tcurToken = Token.%1$s;\n" +
                    "\t\t}", curStringTerminal.toUpperCase()));
            first = false;
        }
        out.println("\t\telse throw new AssertionError(\"Illegal character \" + (char) curChar);");
        out.println("\t\tcurString += (char) curChar;");
        out.println("\t\tnextChar();");
        out.println("\t}\n}");
        out.close();
    }

    private String patternsCompilation() {
        StringBuilder builder = new StringBuilder();

        for (TermNode node : terminals.values()) {
            String listName = node.getName().toLowerCase() + "_Patterns";

            builder.append("\t\tList<Pattern> ")
                    .append(listName)
                    .append("= new ArrayList<>();\n\n");

            for (Node child : node.getChildren()) {
                builder.append("\t\t")
                        .append(listName)
                        .append(".add(Pattern.compile(")
                        .append(child.getName()).append("));\n");
            }

            builder.append("\n\t\tpatterns.put(Token.")
                    .append(node.getName().toUpperCase())
                    .append(", ").append(listName)
                    .append(");\n\n");
        }

        return builder.toString();
    }
}
