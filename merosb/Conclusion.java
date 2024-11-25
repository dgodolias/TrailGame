import java.util.*;

class Conclusion {

    public static class FolBcAskResult {
        public List<Map<String, String>> answers;
        public ProofNode proofTree;

        public FolBcAskResult(List<Map<String, String>> answers, ProofNode proofTree) {
            this.answers = answers;
            this.proofTree = proofTree;
        }
    }

    public static FolBcAskResult folBcAsk(KnowledgeBase kb, Clause query) {
        ProofNode proofTree = new ProofNode(query, new HashMap<>());
        boolean success = folBcAsk(kb, List.of(query), new HashMap<>(), proofTree, new HashSet<>());
        // Only print the proof tree if a successful proof was found
        if (success) {
            Map<String, String> totalTheta = collectSubstitutions(proofTree);
            printProofTree(proofTree, 0, new HashMap<>());
            List<Map<String, String>> answers = new ArrayList<>();
            answers.add(totalTheta);
            return new FolBcAskResult(answers, proofTree);
        } else {
            return new FolBcAskResult(new ArrayList<>(), proofTree);
        }
    }

    private static boolean folBcAsk(KnowledgeBase kb, List<Clause> goals, Map<String, String> theta,
            ProofNode proofNode, Set<String> visitedGoals) {
        if (goals.isEmpty()) {
            proofNode.theta.putAll(theta);
            return true; // Proof succeeded
        }

        Clause firstGoal = goals.get(0);
        Clause substitutedGoal = substituteInClause(firstGoal, theta);
        String goalKey = getGoalKey(substitutedGoal);

        // Check for loops
        if (visitedGoals.contains(goalKey)) {
            return false; // Already attempted this goal in current path
        }

        // Add to visitedGoals
        visitedGoals.add(goalKey);

        List<Clause> restGoals = goals.subList(1, goals.size());
        boolean success = false;

        // Try to unify with facts
        for (Clause fact : kb.getClauses()) {
            Map<String, String> thetaPrime = Unifier.unify(substitutedGoal, fact, kb);
            if (thetaPrime != null) {
                Map<String, String> newTheta = compose(theta, thetaPrime);
                List<Clause> newGoals = substituteInClauses(restGoals, thetaPrime);
                ProofNode childNode = new ProofNode(fact, thetaPrime);
                boolean result = folBcAsk(kb, newGoals, newTheta, childNode, visitedGoals);
                if (result) {
                    proofNode.addChild(childNode);
                    proofNode.theta.putAll(newTheta);
                    success = true;
                    break; // Stop after finding one successful proof
                }
            }
        }

        // Try to unify with rules
        for (Rule rule : kb.getRules()) {
            Rule standardizedRule = standardizeVariables(rule);
            Clause conclusion = substituteInClause(standardizedRule.getConclusion(), theta);
            Map<String, String> thetaPrime = Unifier.unify(substitutedGoal, conclusion, kb);
            if (thetaPrime != null) {
                Map<String, String> newTheta = compose(theta, thetaPrime);
                List<Clause> premises = standardizedRule.getPremises();
                List<Clause> newPremises = substituteInClauses(premises, thetaPrime);
                ProofNode childNode = new ProofNode(conclusion, thetaPrime);
                boolean premisesProved = folBcAsk(kb, newPremises, newTheta, childNode, visitedGoals);
                if (premisesProved) {
                    List<Clause> newGoals = substituteInClauses(restGoals, newTheta);
                    boolean result = folBcAsk(kb, newGoals, newTheta, childNode, visitedGoals);
                    if (result) {
                        proofNode.addChild(childNode);
                        proofNode.theta.putAll(newTheta);
                        success = true;
                        break; // Stop after finding one successful proof
                    }
                }
            }
        }

        // Remove from visitedGoals when backtracking
        visitedGoals.remove(goalKey);

        return success;
    }

    private static List<Clause> substituteInClauses(List<Clause> clauses, Map<String, String> theta) {
        List<Clause> substitutedClauses = new ArrayList<>();
        for (Clause clause : clauses) {
            substitutedClauses.add(substituteInClause(clause, theta));
        }
        return substitutedClauses;
    }

    // New method to generate the goalKey
    private static String getGoalKey(Clause clause) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(clause.predicate);
        keyBuilder.append("(");
        for (int i = 0; i < clause.arguments.size(); i++) {
            String arg = clause.arguments.get(i);
            if (Character.isUpperCase(arg.charAt(0))) {
                // It's a constant
                keyBuilder.append(arg);
            } else {
                // It's a variable
                keyBuilder.append("Var");
            }
            if (i < clause.arguments.size() - 1) {
                keyBuilder.append(", ");
            }
        }
        keyBuilder.append(")");
        return keyBuilder.toString();
    }

    private static String substitute(String arg, Map<String, String> theta) {
        String value = arg;
        Set<String> visited = new HashSet<>();
        while (theta.containsKey(value) && !visited.contains(value)) {
            visited.add(value);
            value = theta.get(value);
        }
        return value;
    }

    private static Map<String, String> compose(Map<String, String> theta1, Map<String, String> theta2) {
        Map<String, String> composition = new HashMap<>(theta1);
        for (Map.Entry<String, String> entry : theta2.entrySet()) {
            String var = entry.getKey();
            String value = substitute(entry.getValue(), theta1);
            composition.put(var, value);
        }
        return composition;
    }

    private static int varIndex = 0;

    private static Rule standardizeVariables(Rule rule) {
        Map<String, String> variableMapping = new HashMap<>();
        List<Clause> newPremises = new ArrayList<>();
        for (Clause premise : rule.getPremises()) {
            Clause newPremise = standardizeClauseVariables(premise, variableMapping);
            newPremises.add(newPremise);
        }
        Clause newConclusion = standardizeClauseVariables(rule.getConclusion(), variableMapping);
        return new Rule(newPremises, newConclusion);
    }

    private static Clause standardizeClauseVariables(Clause clause, Map<String, String> variableMapping) {
        List<String> newArguments = new ArrayList<>();
        for (String arg : clause.arguments) {
            if (!Character.isUpperCase(arg.charAt(0))) { // If it's a variable
                String newVar = variableMapping.get(arg);
                if (newVar == null) {
                    newVar = arg + "_" + varIndex++;
                    variableMapping.put(arg, newVar);
                }
                newArguments.add(newVar);
            } else {
                newArguments.add(arg);
            }
        }
        return new Clause(clause.predicate, newArguments.toArray(new String[0]));
    }

    public static void printProofTree(ProofNode node, int depth, Map<String, String> theta) {
        for (int i = 0; i < depth; i++) {
            System.out.print("  ");
        }
        Map<String, String> currentTheta = compose(theta, node.theta);
        Clause substitutedGoal = substituteInClause(node.goal, currentTheta);
        if (depth == 0) {
            System.out.println(substitutedGoal + " Θέτα: {}");
        } else {
            System.out.println("└─ " + substitutedGoal + " Θέτα: " + node.theta);
        }
        for (ProofNode child : node.children) {
            printProofTree(child, depth + 1, currentTheta);
        }
    }

    private static Clause substituteInClause(Clause clause, Map<String, String> theta) {
        List<String> newArgs = new ArrayList<>();
        for (String arg : clause.arguments) {
            String value = substitute(arg, theta);
            newArgs.add(value);
        }
        return new Clause(clause.predicate, newArgs.toArray(new String[0]));
    }

    private static Map<String, String> collectSubstitutions(ProofNode node) {
        Map<String, String> totalTheta = new HashMap<>(node.theta);
        for (ProofNode child : node.children) {
            totalTheta.putAll(collectSubstitutions(child));
        }
        return totalTheta;
    }
}

class ProofNode {
    Clause goal;
    Map<String, String> theta;
    List<ProofNode> children;

    public ProofNode(Clause goal, Map<String, String> theta) {
        this.goal = goal;
        this.theta = new HashMap<>(theta);
        this.children = new ArrayList<>();
    }

    public void addChild(ProofNode child) {
        children.add(child);
    }

    @Override
    public String toString() {
        return goal.toString() + " Θέτα: " + theta.toString();
    }
}
