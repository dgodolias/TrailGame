import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BaseKnowledge {
    // Dilosi metavliton gia tin vasiki gnosi, ton reader kai to premise.
    private ArrayList<String> base_knowledge;
    private InputReader reader;
    private HashMap<Character, List<Character>> premise;

    // Constructor tis klasis
    BaseKnowledge() {
        // Arxikopoiisi tou reader gia anagnosi dedomenon
        this.reader = new InputReader();
        // Fortosi dedomenon sti lista base_knowledge
        this.base_knowledge = reader.read_data();
        // Fixarei ta premises kai ta apothikevei sto premise
        this.premise = fix_premises();
    }

    // Methodos epistrofis tis base_knowledge
    public ArrayList<String> get_bs() {
        return this.base_knowledge;
    }

    // Idiotiki methodos gia dimiourgia kai epidiorthosi premises
    private HashMap<Character, List<Character>> fix_premises() {
        // Dilosi kai arxikopoiisi tou HashMap gia ta premises
        HashMap<Character, List<Character>> premisesMap = new HashMap<>();
    
        // Gia kathe protasi sti lista base_knowledge
        for (String clause : this.base_knowledge) {
            // Diakopi tis protasis stin syllogi (premise) kai symperasma (conclusion)
            String[] parts = clause.split(">");
            if (parts.length == 2) {
                // Katharismos tou merous tis syllogis apo xaraktires opws '&' kai '|'
                String premisePart = parts[0].replaceAll("[ &|]", "");
                // Lambanei to proto xaraktira tou symperasmatos
                char conclusion = parts[1].trim().charAt(0);
    
                // Dimiourgia listas gia tous xaraktires tis syllogis
                List<Character> premiseList = new ArrayList<>();
                for (char c : premisePart.toCharArray()) {
                    premiseList.add(c);
                }
    
                // Eisagogi tou symperasmatos kai tis syllogis sto HashMap
                premisesMap.put(conclusion, premiseList);
            }
        }
        // Epistrofi tou HashMap
        return premisesMap;
    }

    // Methodos epistrofis tou premise
    public HashMap<Character, List<Character>> getPremises() {
        return this.premise;
    }
}
