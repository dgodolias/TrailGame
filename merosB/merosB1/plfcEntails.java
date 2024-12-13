import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class plfcEntails {
    private Queue<Character> agenda;
    private HashMap<Character, Integer> count;
    private HashMap<Character, Boolean> inferred;
    private HashMap<Character, List<Character>> premises;
    
    plfcEntails(BaseKnowledge knowledge){
        this.premises = knowledge.getPremises();
        this.agenda = make_agenda(knowledge);
        this.count = make_count(knowledge);
        this.inferred = make_inferred(knowledge);
    }

    private static Queue<Character> make_agenda(BaseKnowledge knowledge) {
    Queue<Character> queue = new LinkedList<>();
    ArrayList<String> data = knowledge.get_bs();

    for (String clause : data) {
        clause=clause.strip();
        if (clause.length() == 1) {
            queue.add(clause.charAt(0)); 
        }
    }
    System.out.println(queue);
    return queue;
}




    private HashMap<Character, Integer> make_count(BaseKnowledge knowledge) {
        HashMap<Character, Integer> count = new HashMap<>();

        for (Character conclusion : this.premises.keySet()) {
            count.put(conclusion, this.premises.get(conclusion).size()); 
        }

        return count;
    }

    private HashMap<Character, Boolean> make_inferred(BaseKnowledge knowledge) {
        HashMap<Character, Boolean> inferred = new HashMap<>();
    
        for (Character conclusion : this.premises.keySet()) {
            inferred.put(conclusion, false);
            for (Character premise : this.premises.get(conclusion)) {
                inferred.putIfAbsent(premise, false);
            }
        }
    
        for (Character fact : make_agenda(knowledge)) {
            inferred.putIfAbsent(fact, false);
        }
    
        return inferred;
    }
    


    public boolean solve(Character Q) {
        while (!this.agenda.isEmpty()) {
            Character p = this.agenda.poll();
            System.out.println("Processing: " + p);
    
            if (p.equals(Q)) {
                return true;
            }
    
            if (!this.inferred.get(p)) {
                this.inferred.put(p, true); 
                for (Character conclusion : this.premises.keySet()) {
                    List<Character> premiseList = this.premises.get(conclusion);
                    if (premiseList.contains(p)) {
                        this.count.put(conclusion, this.count.get(conclusion) - 1); 
                        if (this.count.get(conclusion) == 0) { 
                            if (!this.inferred.get(conclusion)) {
                                System.out.println("Adding to agenda: " + conclusion);
                                this.agenda.add(conclusion);
                            }
                        }
                    }
                }
            }
        }
    
        return false; 
    }
    


    public void printComputationTree() {
        System.out.println("Computation Tree:");
        for (Character conclusion : this.premises.keySet()) {
            System.out.println("Conclusion: " + conclusion + ", Premises: " + this.premises.get(conclusion));
        }
    }
    
}
