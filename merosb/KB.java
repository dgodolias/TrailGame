import java.util.*;

class KnowledgeBase {
    private List<Clause> clauses;
    private List<Rule> rules;
    private Set<String> constants;
    private Set<String> variables;
    private Set<String> relations;

    public KnowledgeBase() {
        clauses = new ArrayList<>();
        rules = new ArrayList<>();
        constants = new HashSet<>();
        variables = new HashSet<>();
        relations = new HashSet<>();
    }

    public void addClause(Clause clause) {
        clauses.add(clause);
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public void addConstant(String constant) {
        constants.add(constant);
    }

    public void addVariable(String variable) {
        variables.add(variable);
    }

    public void addRelation(String relation) {
        relations.add(relation);
    }

    public boolean isConstant(String term) {
        return constants.contains(term);
    }

    public boolean isVariable(String term) {
        return variables.contains(term);
    }

    public boolean isRelation(String predicate) {
        return relations.contains(predicate);
    }

    public List<Clause> getClauses() {
        return clauses;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void printKnowledgeBase() {
        System.out.println("Vasi Gnoseon:");
        for (Clause c : clauses) {
            System.out.println(c);
        }
        System.out.println("\nKanones:");
        for (Rule r : rules) {
            System.out.println(r);
        }
        System.out.println("\nStatheres: " + constants);
        System.out.println("Metavlites: " + variables);
        System.out.println("Sxeseis: " + relations);
    }
}
