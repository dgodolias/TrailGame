public class Main {
    public static void main(String[] args) {
        // Dimiourgia antikeimenou BaseKnowledge gia na fortosei ta dedomena
        BaseKnowledge base_knowledge = new BaseKnowledge();

        // Dimiourgia antikeimenou plfcEntails kai arxikopoiisi me ti vasiki gnosi
        plfcEntails Entails = new plfcEntails(base_knowledge);

        // Klisi tis methodou solve gia na elegxei an i metavliti 'Q' symperainetai
        boolean result = Entails.solve('Q');

        // Ektyposi tou apotelesmatos (true/false)
        System.out.println(result);

        // Ektyposi tou dendrou ypologismou
        Entails.printComputationTree();
    }
}
