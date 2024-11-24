import java.util.*;

class Conclusion {
    public static List<Map<String, String>> folBcAsk(List<Clause> kb, Clause query) {
        List<Map<String, String>> answers = new ArrayList<>();
        folBcAsk(kb, List.of(query), new HashMap<>(), answers);
        return answers;
    }

    private static void folBcAsk(List<Clause> kb, List<Clause> goals, Map<String, String> theta, List<Map<String, String>> answers) {
        if (goals.isEmpty()) {
            answers.add(new HashMap<>(theta));
            return;
        }

        Clause firstGoal = goals.get(0);
        List<Clause> restGoals = goals.subList(1, goals.size());

        for (Clause rule : kb) {
            Map<String, String> thetaPrime = Unifier.unify(firstGoal, rule, new KnowledgeBase());
            if (thetaPrime != null) {
                List<Clause> newGoals = new ArrayList<>(restGoals);
                substInGoals(newGoals, thetaPrime);
                folBcAsk(kb, newGoals, compose(theta, thetaPrime), answers);
            }
        }
    }

    private static void substInGoals(List<Clause> goals, Map<String, String> theta) {
        for (int i = 0; i < goals.size(); i++) {
            Clause oldGoal = goals.get(i);
            List<String> newArgs = new ArrayList<>();
            for (String arg : oldGoal.arguments) {
                newArgs.add(theta.getOrDefault(arg, arg));
            }
            goals.set(i, new Clause(oldGoal.predicate, newArgs.toArray(new String[0])));
        }
    }

    private static Map<String, String> compose(Map<String, String> theta1, Map<String, String> theta2) {
        Map<String, String> composition = new HashMap<>(theta1);
        for (Map.Entry<String, String> entry : theta2.entrySet()) {
            composition.put(entry.getKey(), entry.getValue());
        }
        return composition;
    }
}
