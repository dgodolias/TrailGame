import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Δημιουργία βάσης γνώσεων
        KnowledgeBase knowledgeBase = new KnowledgeBase();

        // Προσθήκη σταθερών και μεταβλητών
        knowledgeBase.addConstant("Xristos");
        knowledgeBase.addConstant("Babis");
        knowledgeBase.addConstant("Kiriakos");
        knowledgeBase.addConstant("Dimos");
        knowledgeBase.addConstant("Giannis");
        knowledgeBase.addConstant("Markos");
        knowledgeBase.addConstant("Sofia");
        knowledgeBase.addConstant("Eleni");

        knowledgeBase.addVariable("x");
        knowledgeBase.addVariable("y");
        knowledgeBase.addVariable("z");

        // Προσθήκη οριστικών προτάσεων για το νέο σενάριο σχέσεων
        knowledgeBase.addClause(new Clause("isFriendOf", "Xristos", "Babis"));
        knowledgeBase.addClause(new Clause("isFriendOf", "Babis", "Kiriakos"));
        knowledgeBase.addClause(new Clause("isFriendOf", "Kiriakos", "Dimos"));
        knowledgeBase.addClause(new Clause("isFatherOf", "Giannis", "Xristos"));
        knowledgeBase.addClause(new Clause("isFatherOf", "Xristos", "Babis"));
        knowledgeBase.addClause(new Clause("isMotherOf", "Sofia", "Xristos"));
        knowledgeBase.addClause(new Clause("isMotherOf", "Eleni", "Babis"));
        knowledgeBase.addClause(new Clause("isFriendOf", "Dimos", "Eleni"));

        // Προσθήκη κανόνα (isFatherOf(x, y) AND isFatherOf(y, z)) => isGrandadOf(x, z)
        List<Clause> premises = new ArrayList<>();
        premises.add(new Clause("isFatherOf", "x", "y"));
        premises.add(new Clause("isFatherOf", "y", "z"));
        Clause conclusion = new Clause("isGrandadOf", "x", "z");

        Rule rule = new Rule(premises, conclusion);
        knowledgeBase.addRule(rule);

        // Εκτύπωση της βάσης γνώσεων
        knowledgeBase.printKnowledgeBase();

        // Ερώτηση για το αν ο Giannis είναι παππούς του Babis
        System.out.println("\nΕρώτηση: Είναι ο Giannis παππούς του Babis;");
        Clause query = new Clause("isGrandadOf", "Giannis", "Babis");

        List<Map<String, String>> results = Conclusion.folBcAsk(knowledgeBase, query);

        // Εκτύπωση αποτελεσμάτων
        if (!results.isEmpty()) {
            System.out.println("Αποτέλεσμα: Ναι, ο Giannis είναι παππούς του Babis.");
            System.out.println("Υποκαταστάσεις:");
            for (Map<String, String> substitution : results) {
                System.out.println(substitution);
            }
        } else {
            System.out.println("Αποτέλεσμα: Όχι, δεν μπορεί να αποδειχθεί ότι ο Giannis είναι παππούς του Babis.");
        }
    }
}