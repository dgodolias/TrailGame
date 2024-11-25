import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Δημιουργία βάσης γνώσεων
        KnowledgeBase knowledgeBase = new KnowledgeBase();

        // Διαβάζουμε τη βάση γνώσεων από το αρχείο
        try {
            BufferedReader reader = new BufferedReader(new FileReader("knowledge_base.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.contains("=>")) {
                    // Επεξεργασία κανόνα
                    parseRule(line, knowledgeBase);
                } else {
                    // Επεξεργασία γεγονότος
                    Clause clause = parseClause(line);
                    if (clause != null) {
                        knowledgeBase.addClause(clause);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την ανάγνωση του αρχείου βάσης γνώσεων: " + e.getMessage());
            return;
        }

        // Εκτύπωση της βάσης γνώσεων
        knowledgeBase.printKnowledgeBase();

        // Δέχεται τον προς απόδειξη τύπο από το πληκτρολόγιο
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nΕισάγετε τον προς απόδειξη τύπο (π.χ., isGrandadOf(Giannis, Babis)):");
        String queryInput = scanner.nextLine().trim();

        Clause query = parseClause(queryInput);
        if (query == null) {
            System.err.println("Λάθος μορφή πρότασης: " + queryInput);
            scanner.close();
            return;
        }

        List<Map<String, String>> results = Conclusion.folBcAsk(knowledgeBase, query);

        // Εκτύπωση αποτελέσματος
        if (!results.isEmpty()) {
            System.out.println("Αποτέλεσμα: Αληθές. Ο τύπος αποδείχθηκε.");
            System.out.println("Υποκαταστάσεις:");
            for (Map<String, String> substitution : results) {
                System.out.println(substitution);
            }
        } else {
            System.out.println("Αποτέλεσμα: Ψευδές. Ο τύπος δεν μπόρεσε να αποδειχθεί.");
        }

        scanner.close();
    }

    private static void parseRule(String line, KnowledgeBase kb) {
        // Remove outer brackets or parentheses
        line = removeOuterBrackets(line);
    
        // Split premises and conclusion using the '=>' operator
        String[] parts = line.split("=>");
        if (parts.length != 2) {
            System.err.println("Λάθος μορφή κανόνα: " + line);
            return;
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
            Clause premise = parseClauseWithNegation(premiseString);
            if (premise != null) {
                premises.add(premise);
            } else {
                System.err.println("Λάθος μορφή προϋπόθεσης: " + premiseString);
                return;
            }
        }
    
        // Parse conclusion
        Clause conclusion = parseClause(conclusionPart);
        if (conclusion == null) {
            System.err.println("Λάθος μορφή συμπεράσματος: " + conclusionPart);
            return;
        }
    
        // Add the rule to the knowledge base
        Rule rule = new Rule(premises, conclusion);
        kb.addRule(rule);
    }
    
    private static Clause parseClauseWithNegation(String line) {
        boolean isNegative = false;
        line = line.trim();
        if (line.startsWith("NOT")) {
            isNegative = true;
            line = line.substring(3).trim(); // Remove 'NOT'
        }
        Clause clause = parseClause(line);
        if (clause != null && isNegative) {
            clause.setNegative(true);
        }
        return clause;
    }

    
    

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
    

    private static Clause parseClause(String line) {
        int start = line.indexOf('(');
        int end = line.lastIndexOf(')');
        if (start == -1 || end == -1 || end < start) {
            System.err.println("Λάθος μορφή πρότασης: " + line);
            return null;
        }
        String predicate = line.substring(0, start).trim();
        String argsString = line.substring(start + 1, end).trim();
        String[] args = argsString.split(",");
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].trim();
        }
        return new Clause(predicate, args);
    }
    
}
