import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Dimiourgia tis vasis gnoseon
        KnowledgeBase knowledgeBase = new KnowledgeBase();

        // Diavazoume ti vasi gnoseon apo to arxeio
        try {
            BufferedReader reader = new BufferedReader(new FileReader("knowledge_base.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("Constants:")) {
                    // Epexergasia statheron
                    String constantsLine = line.substring("Constants:".length()).trim();
                    String[] constants = constantsLine.split(",");
                    for (String constant : constants) {
                        constant = constant.trim();
                        knowledgeBase.addConstant(constant);
                    }
                } else if (line.startsWith("Relations:")) {
                    // Epexergasia sxeseon
                    String relationsLine = line.substring("Relations:".length()).trim();
                    String[] relations = relationsLine.split(",");
                    for (String relation : relations) {
                        relation = relation.trim();
                        knowledgeBase.addRelation(relation);
                    }
                } else if (line.contains("=>")) {
                    // Epexergasia kanona
                    if (!parseRule(line, knowledgeBase)) {
                        System.err.println("Lathos stin epexergasia kanona: " + line);
                        System.err.println("Parakaloume vevaiotheite oti o kanonas einai sti sosti morfi:");
                        System.err.println("[Proypothesi1 AND Proypothesi2 AND ...] => Symperasma");
                        reader.close();
                        return;
                    }
                } else {
                    // Epexergasia gegonotos (clause)
                    Clause clause = parseClause(line, knowledgeBase);
                    if (clause != null) {
                        knowledgeBase.addClause(clause);
                    } else {
                        System.err.println("Lathos stin epexergasia protasis: " + line);
                        System.err.println("Parakaloume vevaiotheite oti i protasi einai sti sosti morfi:");
                        System.err.println("Predicate(arg1, arg2, ...)");
                        reader.close();
                        return;
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Lathos kata tin anagnosi tou arxeiou tis vasis gnoseon: " + e.getMessage());
            return;
        }

        // Ektyponei ti vasi gnoseon
        knowledgeBase.printKnowledgeBase();

        // Dexetai to erotima apo ton xristi
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEisagete to erotima pros apodeixi (px, isGrandadOf(Giannis, Babis)):");
        String queryInput = scanner.nextLine().trim();

        Clause query = parseClause(queryInput, knowledgeBase);
        if (query == null) {
            System.err.println("Lathos stin epexergasia tou erotimatos: " + queryInput);
            System.err.println("Parakaloume vevaiotheite oti to erotima einai sti sosti morfi:");
            System.err.println("Predicate(arg1, arg2, ...)");
            scanner.close();
            return;
        }

        Conclusion.FolBcAskResult result = Conclusion.folBcAsk(knowledgeBase, query);

                // Ektyponei to apotelesma
        if (!result.answers.isEmpty()) {
            System.out.println("Apotelesma: Alithes. To erotima apodeixthike.");
            System.out.println("Ypokatastaseis:");
            for (Map<String, String> substitution : result.answers) {
                System.out.println(substitution);
            }
            // Ektyponei to dentro apodeixis
            System.out.println("\nDentro Apodeixis:");
            Conclusion.printProofTree(result.proofTree, 0, new HashMap<>());
        } else {
            System.out.println("Apotelesma: Pseudes. To erotima den mporese na apodeixthei.");
        }
        
        scanner.close();
    }

    // Methodos gia tin epexergasia enos kanona apo to arxeio
    private static boolean parseRule(String line, KnowledgeBase kb) {
        // Afairei tis exoterikes parentheseis i agkyles
        String originalLine = line; // Kratame to arxiko line gia minima sfalmatos
        line = removeOuterBrackets(line);
        if (line == null) {
            System.err.println("Lathos stin epexergasia kanona: " + originalLine);
            System.err.println("Asymfoni amentoboli stis agkyles.");
            return false;
        }

        // Diaxorizoume tis proypotheseis kai to symperasma me ton '=>' telesti
        String[] parts = line.split("=>");
        if (parts.length != 2) {
            return false;
        }
        String premisesPart = parts[0].trim();
        String conclusionPart = parts[1].trim();

        // Afairei tis exoterikes parentheseis apo tis proypotheseis
        premisesPart = removeOuterBrackets(premisesPart);
        if (premisesPart == null) {
            System.err.println("Lathos stin epexergasia ton proypotheseon tou kanona: " + originalLine);
            System.err.println("Asymfoni amentoboli stis agkyles twn proypotheseon.");
            return false;
        }

        // Diaxorizoume tis proypotheseis me tin 'AND'
        String[] premiseStrings = premisesPart.split("AND");
        List<Clause> premises = new ArrayList<>();
        for (String premiseString : premiseStrings) {
            premiseString = premiseString.trim();
            Clause premise = parseClause(premiseString, kb);
            if (premise != null) {
                premises.add(premise);
            } else {
                System.err.println("Lathos stin epexergasia tis proypothesis: " + premiseString);
                return false;
            }
        }

        // Epexergasia symperasmatos
        Clause conclusion = parseClause(conclusionPart, kb);
        if (conclusion == null) {
            System.err.println("Lathos stin epexergasia tou symperasmatos: " + conclusionPart);
            return false;
        }

        // Prosthiki tou kanona sti vasi gnoseon
        Rule rule = new Rule(premises, conclusion);
        kb.addRule(rule);
        return true;
    }

    // Methodos gia tin afairesi exoterikon parenthesewn i agkylon
    private static String removeOuterBrackets(String s) {
        s = s.trim();
        if (s.isEmpty()) return s;
        char openChar = s.charAt(0);
        char closeChar = s.charAt(s.length() - 1);
        if ((openChar == '(' && closeChar == ')') || (openChar == '[' && closeChar == ']')) {
            int count = 0;
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c == openChar) count++;
                else if (c == closeChar) count--;
                if (count == 0 && i < s.length() - 1) {
                    // Brike kleisimo agkylis prin to telos tou string
                    return s;
                }
            }
            if (count != 0) {
                // Asymfoni amentoboli stis agkyles
                return null;
            }
            s = s.substring(1, s.length() - 1).trim();
            return s;
        }
        return s;
    }

    // Methodos gia tin epexergasia mias protasis
    private static Clause parseClause(String line, KnowledgeBase kb) {
        line = line.trim();

        int start = line.indexOf('(');
        int end = line.lastIndexOf(')');
        if (start == -1 || end == -1 || end < start) {
            return null;
        }
        String predicate = line.substring(0, start).trim();
        if (!kb.isRelation(predicate)) {
            System.err.println("To predicate '" + predicate + "' den exei dilothei.");
            return null;
        }
        String argsString = line.substring(start + 1, end).trim();
        String[] args = argsString.split(",");
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].trim();
            String arg = args[i];
            if (!kb.isConstant(arg) && !kb.isRelation(arg)) {
                // It's a variable, do nothing
            } else if (kb.isRelation(arg)) {
                System.err.println("To orisma '" + arg + "' den mporei na einai sxesi mesa se orisma.");
                return null;
            } else {
                // It's a constant
            }
        }
        Clause clause = new Clause(predicate, args);

        return clause;
    }
}
