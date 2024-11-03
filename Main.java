import javax.swing.*;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int[] colors = new int[]{Board.RED, Board.BLUE};
                int userColor = getRandomColor(colors);
                int agentColor = userColor == Board.RED ? Board.BLUE : Board.RED;

                Player aiAgent = new Player(2, agentColor);

                JFrame frame = new JFrame("Game Board");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(500, 500);

                BoardPanel boardPanel = new BoardPanel(5, 5, userColor, agentColor, aiAgent);
                frame.add(boardPanel);
                frame.setVisible(true);
            }
        });
    }

    public static int getRandomColor(int[] colors) {
        Random random = new Random();
        return colors[random.nextInt(colors.length)];
    }
}
