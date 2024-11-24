import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class KnowledgeBase {
    private List<Clause> clauses; // Λίστα με όλες τις προτάσεις
    private List<Rule> rules;     // Λίστα με όλους τους κανόνες
    private Set<String> constants; // Σετ για τις σταθερές
    private Set<String> variables; // Σετ για τις μεταβλητές

    public KnowledgeBase() {
        clauses = new ArrayList<>();
        rules = new ArrayList<>();
        constants = new HashSet<>();
        variables = new HashSet<>();
    }

    public void addClause(Clause clause) {
        clauses.add(clause);
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public List<Clause> getClauses() {
        return clauses;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void addConstant(String constant) {
        constants.add(constant);
    }

    public void addVariable(String variable) {
        variables.add(variable);
    }

    public boolean isConstant(String term) {
        return constants.contains(term);
    }

    public boolean isVariable(String term) {
        return variables.contains(term);
    }

    public void printKnowledgeBase() {
        System.out.println("Βάση Γνώσεων:");
        for (Clause c : clauses) {
            System.out.println(c);
        }
        System.out.println("\nΣταθερές: " + constants);
        System.out.println("Μεταβλητές: " + variables);
    }
}
