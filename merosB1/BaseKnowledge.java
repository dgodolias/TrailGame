package forward_Chaining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BaseKnowledge {
    private ArrayList<String> base_knowledge; // List of clauses as strings
    private InputReader reader;              // Reads data
    private HashMap<Character, List<Character>> premise; // Maps conclusion to premises

    BaseKnowledge() {
        this.reader = new InputReader();
        this.base_knowledge = reader.read_data(); // Read the clauses
        this.premise = fix_premises();           // Process premises
    }

    public ArrayList<String> get_bs() {
        return this.base_knowledge;
    }

    private HashMap<Character, List<Character>> fix_premises() {
        HashMap<Character, List<Character>> premisesMap = new HashMap<>(); // Initialize map
    
        for (String clause : this.base_knowledge) {
            // Split clause into premise and conclusion
            String[] parts = clause.split(">");
            if (parts.length == 2) {
                String premisePart = parts[0].replaceAll("[ &|]", ""); // Remove '&' and '|' characters
                char conclusion = parts[1].trim().charAt(0);           // Right of '>'
    
                // Split the premise into individual characters
                List<Character> premiseList = new ArrayList<>();
                for (char c : premisePart.toCharArray()) {
                    premiseList.add(c);
                }
    
                // Add to the HashMap
                premisesMap.put(conclusion, premiseList);
            }
        }
        return premisesMap;
    }
    


    public HashMap<Character, List<Character>> getPremises() {
        return this.premise;
    }
}