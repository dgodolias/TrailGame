import java.util.*;

class KnowledgeBase {
    private List<Clause> clauses;
    private List<Rule> rules;
    private Set<String> constants;
    private Set<String> variables;

    public KnowledgeBase() {
        clauses = new ArrayList<>();
        rules = new ArrayList<>();
        constants = new HashSet<>();
        variables = new HashSet<>();
    }

    public void addClause(Clause clause) {
        clauses.add(clause);
        updateTerms(clause);
    }
    

    public void addRule(Rule rule) {
        rules.add(rule);
        // Ενημέρωση των σταθερών και μεταβλητών
        for (Clause clause : rule.getPremises()) {
            updateTerms(clause);
        }
        updateTerms(rule.getConclusion());
    }

    private void updateTerms(Clause clause) {
        for (String arg : clause.arguments) {
            if (Character.isUpperCase(arg.charAt(0))) {
                constants.add(arg);
            } else {
                variables.add(arg);
            }
        }
    }
    

    public List<Clause> getClauses() {
        return clauses;
    }

    public List<Rule> getRules() {
        return rules;
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
        System.out.println("\nΚανόνες:");
        for (Rule r : rules) {
            System.out.println(r);
        }
        System.out.println("\nΣταθερές: " + constants);
        System.out.println("Μεταβλητές: " + variables);
    }
}
