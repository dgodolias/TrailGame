import java.util.HashMap;
import java.util.Map;

class Unifier {
    public static Map<String, String> unify(Clause c1, Clause c2, KnowledgeBase kb) {
        Map<String, String> substitution = new HashMap<>();
        if (!c1.predicate.equals(c2.predicate) || c1.arguments.size() != c2.arguments.size()) {
            return null;
        }
        for (int i = 0; i < c1.arguments.size(); i++) {
            String arg1 = c1.arguments.get(i);
            String arg2 = c2.arguments.get(i);

            if (arg1.equals(arg2)) continue;

            boolean arg1IsConstant = kb.isConstant(arg1);
            boolean arg2IsConstant = kb.isConstant(arg2);

            if (arg1IsConstant && arg2IsConstant) {
                return null;
            }

            if (!arg1IsConstant) { // arg1 is variable
                substitution.put(arg1, arg2);
            } else if (!arg2IsConstant) { // arg2 is variable
                substitution.put(arg2, arg1);
            } else {
                return null;
            }
        }
        return substitution;
    }
}
