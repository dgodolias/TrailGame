import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int[] colors = new int[] { Board.R, Board.B };
        int userColor = colors[0];
        int agentColor = colors[1];

        Player aiAgent = new Player(2, agentColor);
        Board board = new Board(5, 5);
        board.print();

        board.setLastPlayer(Board.R);

        Scanner scanner = new Scanner(System.in);

        while (!board.isTerminal()) {
            System.out.println();
            switch (board.getLastPlayer()) {
                case Board.R:
                    if (userColor == Board.B) {
                        System.out.println("BLUE plays");
                        Move moveBLUE = aiAgent.MiniMax(board);
                        board.makeMove(moveBLUE.getRow(), moveBLUE.getCol(), Board.B);
                        board.longestSequence(-1);
                    } else {
                        System.out.println("Your turn (BLUE). Enter row and column: ");
                        int row, col;
                        do {
                            System.out.print("Row: ");
                            row = scanner.nextInt()-1;
                            System.out.print("Column: ");
                            col = scanner.nextInt()-1;
                        } while (!board.isValidMove(row, col));
                        board.makeMove(row, col, Board.B);
                        board.longestSequence(-1);
                    }
                    break;
                case Board.B:
                    if (userColor == Board.R) {
                        System.out.println("R plays");
                        Move moveRED = aiAgent.MiniMax(board);
                        board.makeMove(moveRED.getRow(), moveRED.getCol(), Board.R);
                        board.longestSequence(1);
                    } else {
                        System.out.println("Your turn (RED). Enter row and column: ");
                        int row, col;
                        do {
                            System.out.print("Row: ");
                            row = scanner.nextInt()-1;
                            System.out.print("Column: ");
                            col = scanner.nextInt()-1;
                        } while (!board.isValidMove(row, col));
                        board.makeMove(row, col, Board.R);
                        board.longestSequence(1);
                    }
                    break;
                default:
                    break;
            }
            board.print();
        }

        scanner.close();
    }

}