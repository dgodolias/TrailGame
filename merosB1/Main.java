package forward_Chaining;

public class Main {
    public static void main(String[] args) {
        // Initialize the knowledge base
        BaseKnowledge base_knowledge = new BaseKnowledge();

        plfcEntails Entails= new plfcEntails(base_knowledge);
        boolean result=Entails.solve('U');

        System.out.println(result);
        // Entails.printComputationTree();

    }
}
