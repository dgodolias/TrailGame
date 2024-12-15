import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class InputReader {
    // Dilosi metavliton gia to monopati arxeiou kai ti lista tis vasikis gnosis
    private String filePath;
    private ArrayList<String> BaseKnowledge;

    // Constructor tis klasis
    InputReader() {
        // Arxikopoiisi tou monopatiou arxeiou, mporeite na dokimasete kai to example2.txt
        this.filePath = "example.txt";
        // Arxikopoiisi tis listas BaseKnowledge
        this.BaseKnowledge = new ArrayList<String>();
    }

    // Methodos gia tin anagnosi dedomenon apo arxeio
    public ArrayList<String> read_data() {
        //elegxos tou BufferedReader gia anagnosi dedomenon
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Diavasma kathe grammis mexri na teleiosei to arxeio
            while ((line = br.readLine()) != null) {
                // Elegxos an grammi adeia
                if (line.strip() == "") {
                    continue; // Skip
                }
                // Prosthesi tis grammis sti lista BaseKnowledge
                this.BaseKnowledge.add(line);
            }
        } catch (IOException e) {
            // Emfanisi sfalmaton
            e.printStackTrace();
        }

        // Epistrofi tis listas me ta dedomena
        return this.BaseKnowledge;
    }
}
