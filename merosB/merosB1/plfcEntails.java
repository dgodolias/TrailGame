import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class plfcEntails {
    // Dilosi metavliton gia to agenda, count, inferred kai premises
    private Queue<Character> agenda;
    private HashMap<Character, Integer> count;
    private HashMap<Character, Boolean> inferred;
    private HashMap<Character, List<Character>> premises;
    
    // Constructor tis klasis plfcEntails
    plfcEntails(BaseKnowledge knowledge) {
        // Fortosi ton premises apo tin vasiki gnosi
        this.premises = knowledge.getPremises();
        // Arxikopoiisi tou agenda me ta dedomena
        this.agenda = make_agenda(knowledge);
        // Arxikopoiisi tou count gia na metrame poses sylloges xreiazontai gia kathe symperasma
        this.count = make_count(knowledge);
        // Arxikopoiisi tou inferred gia na parakolouthoume poies metavlites exoun ekserevnithei
        this.inferred = make_inferred(knowledge);
    }

    // Methodos gia dimiourgia tou agenda (lista me ta facts)
    private static Queue<Character> make_agenda(BaseKnowledge knowledge) {
        Queue<Character> queue = new LinkedList<>();
        ArrayList<String> data = knowledge.get_bs();

        // Prosthesi oloklirwn protasewn (monoi xaraktires) sto agenda
        for (String clause : data) {
            clause = clause.strip();
            if (clause.length() == 1) { 
                queue.add(clause.charAt(0)); 
            }
        }
        return queue;
    }

    // Methodos gia dimiourgia tou count (arithmos syllogon gia kathe symperasma)
    private HashMap<Character, Integer> make_count(BaseKnowledge knowledge) {
        HashMap<Character, Integer> count = new HashMap<>();
        // Prosthesi tou plithous syllogon gia kathe symperasma
        for (Character conclusion : this.premises.keySet()) {
            count.put(conclusion, this.premises.get(conclusion).size());
        }
        return count;
    }

    // Methodos gia dimiourgia tou inferred (katastasi gnosis gia kathe metavliti)
    private HashMap<Character, Boolean> make_inferred(BaseKnowledge knowledge) {
        HashMap<Character, Boolean> inferred = new HashMap<>();
    
        // Prosthesi olwn ton metavliton pou emfanizontai sta premises
        for (Character conclusion : this.premises.keySet()) {
            inferred.put(conclusion, false); // Default timi false
            for (Character premise : this.premises.get(conclusion)) {
                inferred.putIfAbsent(premise, false);
            }
        }
    
        // Prosthesi ton facts sto inferred
        for (Character fact : make_agenda(knowledge)) {
            inferred.putIfAbsent(fact, false);
        }
    
        return inferred;
    }

    // Methodos epilisis PL-FC-Entails
    public boolean solve(Character Q) {
        while (!this.agenda.isEmpty()) { 
            Character p = this.agenda.poll(); // Pernei to epomeno stoixeio apo to agenda
            System.out.println("Processing: " + p);

            if (p.equals(Q)) { // Elegxei an to Q vrethike
                return true;
            }

            if (!this.inferred.get(p)) { 
                this.inferred.put(p, true); // Simainei oti to p exerevnithike
                for (Character conclusion : this.premises.keySet()) {
                    List<Character> premiseList = this.premises.get(conclusion);
                    if (premiseList.contains(p)) { // An to p einai stis sylloges
                        this.count.put(conclusion, this.count.get(conclusion) - 1); // Meionei to count
                        if (this.count.get(conclusion) == 0) { 
                            if (!this.inferred.get(conclusion)) { // An den exei ekserevnithei
                                System.out.println("Adding to agenda: " + conclusion);
                                this.agenda.add(conclusion); // Prosthetei sto agenda
                            }
                        }
                    }
                }
            }
        }
        return false; // Epistrefei false an den vrethei to Q
    }

    // Methodos gia ektyposi tou dendrou ypologismou
    public void printComputationTree() {
        System.out.println("Computation Tree:");
        for (Character conclusion : this.premises.keySet()) {
            System.out.println("Conclusion: " + conclusion + ", Premises: " + this.premises.get(conclusion));
        }
    }
}
