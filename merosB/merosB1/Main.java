public class Main {
    public static void main(String[] args) {
        BaseKnowledge base_knowledge = new BaseKnowledge();

        plfcEntails Entails= new plfcEntails(base_knowledge);
        boolean result=Entails.solve('Q');

        System.out.println(result);
         Entails.printComputationTree();

    }
}
