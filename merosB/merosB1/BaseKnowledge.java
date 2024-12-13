import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BaseKnowledge {
    private ArrayList<String> base_knowledge;
    private InputReader reader;
    private HashMap<Character, List<Character>> premise;

    BaseKnowledge() {
        this.reader = new InputReader();
        this.base_knowledge = reader.read_data();
        this.premise = fix_premises();
    }

    public ArrayList<String> get_bs() {
        return this.base_knowledge;
    }

    private HashMap<Character, List<Character>> fix_premises() {
        HashMap<Character, List<Character>> premisesMap = new HashMap<>();
    
        for (String clause : this.base_knowledge) {
            String[] parts = clause.split(">");
            if (parts.length == 2) {
                String premisePart = parts[0].replaceAll("[ &|]", "");
                char conclusion = parts[1].trim().charAt(0);
    
                List<Character> premiseList = new ArrayList<>();
                for (char c : premisePart.toCharArray()) {
                    premiseList.add(c);
                }
    
                premisesMap.put(conclusion, premiseList);
            }
        }
        return premisesMap;
    }

    public HashMap<Character, List<Character>> getPremises() {
        return this.premise;
    }
}