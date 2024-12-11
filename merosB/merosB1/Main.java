package merosB1;

public class Main {
    public static void main(String[] args) {
        // Initialize the knowledge base
        BaseKnowledge base_knowledge = new BaseKnowledge();

        plfcEntails Entails= new plfcEntails(base_knowledge);
        boolean result=Entails.solve('Q');

        System.out.println(result);
        // Entails.printComputationTree();

    }
}
