import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Create the knowledge base
        KnowledgeBase knowledgeBase = new KnowledgeBase();

        // Read the knowledge base from the file
        try {
            BufferedReader reader = new BufferedReader(new FileReader("knowledge_base.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.contains("=>")) {
                    // Process rule
                    if (!parseRule(line, knowledgeBase)) {
                        System.err.println("Error parsing rule: " + line);
                        System.err.println("Please ensure that the rule is in the correct format:");
                        System.err.println("[Premise1 AND Premise2 AND ...] => Conclusion");
                        reader.close();
                        return;
                    }
                } else {
                    // Process fact (clause)
                    Clause clause = parseClause(line);
                    if (clause != null) {
                        knowledgeBase.addClause(clause);
                    } else {
                        System.err.println("Error parsing clause: " + line);
                        System.err.println("Please ensure that the clause is in the correct format:");
                        System.err.println("Predicate(arg1, arg2, ...)");
                        reader.close();
                        return;
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading knowledge base file: " + e.getMessage());
            return;
        }

        // Print the knowledge base
        knowledgeBase.printKnowledgeBase();

        // Accept the query from the user
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEnter the query to prove (e.g., isGrandadOf(Giannis, Babis)):");
        String queryInput = scanner.nextLine().trim();

        Clause query = parseClause(queryInput);
        if (query == null) {
            System.err.println("Error parsing query: " + queryInput);
            System.err.println("Please ensure that the query is in the correct format:");
            System.err.println("Predicate(arg1, arg2, ...)");
            scanner.close();
            return;
        }

        List<Map<String, String>> results = Conclusion.folBcAsk(knowledgeBase, query);

        // Print the result
        if (!results.isEmpty()) {
            System.out.println("Result: True. The query was proved.");
            System.out.println("Substitutions:");
            for (Map<String, String> substitution : results) {
                System.out.println(substitution);
            }
        } else {
            System.out.println("Result: False. The query could not be proved.");
        }

        scanner.close();
    }

    // Method to parse a rule from the file
    private static boolean parseRule(String line, KnowledgeBase kb) {
        // Remove outer brackets or parentheses
        line = removeOuterBrackets(line);

        // Split premises and conclusion using the '=>' operator
        String[] parts = line.split("=>");
        if (parts.length != 2) {
            return false;
        }
        String premisesPart = parts[0].trim();
        String conclusionPart = parts[1].trim();

        // Remove outer brackets from premises
        premisesPart = removeOuterBrackets(premisesPart);

        // Split premises using 'AND'
        String[] premiseStrings = premisesPart.split("AND");
        List<Clause> premises = new ArrayList<>();
        for (String premiseString : premiseStrings) {
            premiseString = premiseString.trim();
            Clause premise = parseClause(premiseString);
            if (premise != null) {
                premises.add(premise);
            } else {
                return false;
            }
        }

        // Parse conclusion
        Clause conclusion = parseClause(conclusionPart);
        if (conclusion == null) {
            return false;
        }

        // Add the rule to the knowledge base
        Rule rule = new Rule(premises, conclusion);
        kb.addRule(rule);
        return true;
    }

    // Method to remove outer brackets or parentheses
    private static String removeOuterBrackets(String s) {
        s = s.trim();
        while ((s.startsWith("(") && s.endsWith(")")) || (s.startsWith("[") && s.endsWith("]"))) {
            char openChar = s.charAt(0);
            char closeChar = s.charAt(s.length() - 1);
            if ((openChar == '(' && closeChar == ')') || (openChar == '[' && closeChar == ']')) {
                s = s.substring(1, s.length() - 1).trim();
            } else {
                break;
            }
        }
        return s;
    }

    // Method to parse a clause
    private static Clause parseClause(String line) {
        line = line.trim();

        int start = line.indexOf('(');
        int end = line.lastIndexOf(')');
        if (start == -1 || end == -1 || end < start) {
            return null;
        }
        String predicate = line.substring(0, start).trim();
        String argsString = line.substring(start + 1, end).trim();
        String[] args = argsString.split(",");
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].trim();
        }
        Clause clause = new Clause(predicate, args);

        return clause;
    }
}
