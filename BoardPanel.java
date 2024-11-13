import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoardPanel extends JPanel {
    private final int rows;
    private final int cols;
    private final JButton[][] buttons;
    private final Board board;
    private final Player aiAgent;
    private int userColor;
    private int agentColor;

    public BoardPanel(int rows, int cols, int userColor, int agentColor, Player aiAgent) {
        this.rows = rows;
        this.cols = cols;
        this.userColor = userColor;
        this.agentColor = agentColor;
        this.aiAgent = aiAgent;
        this.board = new Board(rows, cols);
        this.buttons = new JButton[rows][cols];
        
        setLayout(new GridLayout(rows, cols));
        initializeBoard();
    }

    private void initializeBoard() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                JButton button = new JButton();
                button.setBackground(Color.WHITE);
                int finalRow = row;
                int finalCol = col;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (board.isValidMove(finalRow, finalCol)) {
                            makeUserMove(finalRow, finalCol);
                        }
                    }
                });
                buttons[row][col] = button;
                add(button);
            }
        }
    }

    private void makeUserMove(int row, int col) {
        board.makeMove(row, col, userColor);
        updateButtonColor(row, col, userColor);
        if (!board.isTerminal()) {
            makeAIMove();
        } else {
            evaluateGame();
        }
    }

    private void makeAIMove() {
        Move aiMove = aiAgent.MiniMax(board);
        if (aiMove != null) {
            board.makeMove(aiMove.getRow(), aiMove.getCol(), agentColor);
            updateButtonColor(aiMove.getRow(), aiMove.getCol(), agentColor);
            if (board.isTerminal()) {
                evaluateGame();
            }
        }
    }

    private void updateButtonColor(int row, int col, int color) {
        if (color == Board.RED) {
            buttons[row][col].setBackground(Color.RED);
        } else if (color == Board.BLUE) {
            buttons[row][col].setBackground(Color.BLUE);
        }
        buttons[row][col].setEnabled(false);
    }

    private void evaluateGame() {
        int largestRedComponent = board.largestConnectedComponent(Board.RED);
        int largestBlueComponent = board.largestConnectedComponent(Board.BLUE);
        int evaluation = largestRedComponent - largestBlueComponent;
    
        System.out.println("Largest connected component for RED: " + largestRedComponent);
        System.out.println("Largest connected component for BLUE: " + largestBlueComponent);

        
        if ((userColor == Board.RED && evaluation > 0) || (userColor == Board.BLUE && evaluation < 0)) {
            showGameWon();
        } else if ((userColor == Board.RED && evaluation < 0) || (userColor == Board.BLUE && evaluation > 0)) {
            showGameLost();
        } else {
            showGameDraw();
        }
    }
    
    

    private void showGameWon() {
        JOptionPane.showMessageDialog(this, "You Won!");
    }

    private void showGameLost() {
        JOptionPane.showMessageDialog(this, "You Lost!");
    }

    private void showGameDraw() {
        JOptionPane.showMessageDialog(this, "It's a Draw!");
    }
}