import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class InputReader {
    private String filePath;
    private ArrayList<String> BaseKnowledge;
    InputReader(){
        this.filePath = "example2.txt";
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
