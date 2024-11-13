import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int agentColor = Board.RED;

                Player aiAgent = new Player(2, agentColor);

                JFrame frame = new JFrame("Game Board");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(500, 500);

                BoardPanel boardPanel = new BoardPanel(5, 5, Board.BLUE, agentColor, aiAgent);
                frame.add(boardPanel);
                frame.setVisible(true);
            }
        });
    }

}