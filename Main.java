import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int[] colors = new int[] { Board.RED, Board.BLUE };
        int userColor = getRandomColor(colors);
        int agentColor = userColor == Board.RED ? Board.BLUE : Board.RED;

        Player aiAgent = new Player(2, agentColor);
        Board board = new Board(5, 5);
        board.print();

        board.setLastPlayer(Board.RED);

        Scanner scanner = new Scanner(System.in);

        while (!board.isTerminal()) {
            System.out.println();
            switch (board.getLastPlayer()) {
                case Board.RED:
                    if (userColor == Board.BLUE) {
                        System.out.println("BLUE plays");
                        Move moveBLUE = aiAgent.MiniMax(board);
                        board.makeMove(moveBLUE.getRow(), moveBLUE.getCol(), Board.BLUE);
                    } else {
                        System.out.println("Your turn (BLUE). Enter row and column: ");
                        int row, col;
                        do {
                            System.out.print("Row: ");
                            row = scanner.nextInt();
                            System.out.print("Column: ");
                            col = scanner.nextInt();
                        } while (!board.isValidMove(row, col));
                        board.makeMove(row, col, Board.BLUE);
                    }
                    break;
                case Board.BLUE:
                    if (userColor == Board.RED) {
                        System.out.println("RED plays");
                        Move moveRED = aiAgent.MiniMax(board);
                        board.makeMove(moveRED.getRow(), moveRED.getCol(), Board.RED);
                    } else {
                        System.out.println("Your turn (RED). Enter row and column: ");
                        int row, col;
                        do {
                            System.out.print("Row: ");
                            row = scanner.nextInt();
                            System.out.print("Column: ");
                            col = scanner.nextInt();
                        } while (!board.isValidMove(row, col));
                        board.makeMove(row, col, Board.RED);
                    }
                    break;
                default:
                    break;
            }
            board.print();
        }

        scanner.close();
    }

    public static int getRandomColor(int[] colors) {
        Random random = new Random();
        return colors[random.nextInt(colors.length)];
    }
}