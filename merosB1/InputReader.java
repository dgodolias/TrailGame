package forward_Chaining;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class InputReader {
    private String filePath;
    private ArrayList<String> BaseKnowledge;
    InputReader(){
        this.filePath = "C:\\Users\\swkra\\OneDrive - aueb.gr\\Uni\\5th-Semester\\Τεχνητή Νοημοσύνη\\Εργασίες\\ergasia1\\forward_Chaining\\example3.txt";
        this.BaseKnowledge=new ArrayList<String>();
    }

    public ArrayList<String> read_data(){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null ) {
                if(line.strip()==""){
                    continue;
                }
                this.BaseKnowledge.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.BaseKnowledge;
    }
}
