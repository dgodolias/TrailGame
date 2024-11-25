import java.util.*;

class Conclusion {

    public static List<Map<String, String>> folBcAsk(KnowledgeBase kb, Clause query) {
        List<Map<String, String>> answers = new ArrayList<>();
        ProofNode proofTree = new ProofNode(query, new HashMap<>());
        folBcAsk(kb, List.of(query), new HashMap<>(), answers, proofTree);
        // Ektyponei to dentro apodeixis
        printProofTree(proofTree, 0, new HashMap<>());
        return answers;
    }

    private static void folBcAsk(KnowledgeBase kb, List<Clause> goals, Map<String, String> theta, List<Map<String, String>> answers, ProofNode proofNode) {
        if (goals.isEmpty()) {
            answers.add(new HashMap<>(theta));
            return;
        }

        Clause firstGoal = goals.get(0);
        List<Clause> restGoals = goals.subList(1, goals.size());

        for (Clause fact : kb.getClauses()) {
            Map<String, String> thetaPrime = Unifier.unify(firstGoal, fact, kb);
            if (thetaPrime != null) {
                Map<String, String> newTheta = compose(theta, thetaPrime);
                List<Clause> newGoals = new ArrayList<>(restGoals);
                substInGoals(newGoals, thetaPrime);
                ProofNode childNode = new ProofNode(fact, thetaPrime);
                proofNode.addChild(childNode);
                folBcAsk(kb, newGoals, newTheta, answers, childNode);
            }
        }

        for (Rule rule : kb.getRules()) {
            // Dimiourgia neou antigrafou tou kanona me nees metavlites
            Rule standardizedRule = standardizeVariables(rule);
            Clause conclusion = standardizedRule.getConclusion();
            Map<String, String> thetaPrime = Unifier.unify(firstGoal, conclusion, kb);
            if (thetaPrime != null) {
                Map<String, String> newTheta = compose(theta, thetaPrime);
                List<Clause> newGoals = new ArrayList<>(standardizedRule.getPremises());
                substInGoals(newGoals, thetaPrime);
                newGoals.addAll(restGoals);
                ProofNode childNode = new ProofNode(conclusion, thetaPrime);
                proofNode.addChild(childNode);
                folBcAsk(kb, newGoals, newTheta, answers, childNode);
            }
        }
    }

    private static void substInGoals(List<Clause> goals, Map<String, String> theta) {
        for (int i = 0; i < goals.size(); i++) {
            Clause oldGoal = goals.get(i);
            List<String> newArgs = new ArrayList<>();
            for (String arg : oldGoal.arguments) {
                String value = theta.getOrDefault(arg, arg);
                newArgs.add(value);
            }
            goals.set(i, new Clause(oldGoal.predicate, newArgs.toArray(new String[0])));
        }
    }

    private static Map<String, String> compose(Map<String, String> theta1, Map<String, String> theta2) {
        Map<String, String> composition = new HashMap<>(theta1);
        for (Map.Entry<String, String> entry : theta2.entrySet()) {
            String var = entry.getKey();
            String value = entry.getValue();
            if (theta1.containsKey(value)) {
                composition.put(var, theta1.get(value));
            } else {
                composition.put(var, value);
            }
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

    private static void printProofTree(ProofNode node, int depth, Map<String, String> theta) {
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
            String value = theta.getOrDefault(arg, arg);
            newArgs.add(value);
        }
        return new Clause(clause.predicate, newArgs.toArray(new String[0]));
    }
}

class ProofNode {
    Clause goal;
    Map<String, String> theta;
    List<ProofNode> children;

    public ProofNode(Clause goal, Map<String, String> theta) {
        this.goal = goal;
        this.theta = theta;
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
