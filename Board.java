import java.util.ArrayList;

public class Board {
    public static final int BLUE = -1;
    public static final int RED = 1;
    public static final int EMPTY = 0;
    public int borderX;
    public int borderY;

    private int[][] gameBoard;
    private int lastPlayer;
    private Move lastMove;

    Board(int borderX, int borderY) {
        this.borderX = borderX;
        this.borderY = borderY;
        this.lastMove = new Move();
        this.lastPlayer = BLUE;
        this.gameBoard = new int[borderY][borderX];
        for (int i = 0; i < this.gameBoard.length; i++) {
            for (int j = 0; j < this.gameBoard[i].length; j++) {
                this.gameBoard[i][j] = EMPTY;
            }
        }
    }

    Board(Board board) {
        this.borderX = board.borderX;
        this.borderY = board.borderY;
        this.lastMove = board.lastMove;
        this.lastPlayer = board.lastPlayer;
        this.gameBoard = new int[borderY][borderX];
        for (int i = 0; i < this.gameBoard.length; i++) {
            for (int j = 0; j < this.gameBoard[i].length; j++) {
                this.gameBoard[i][j] = board.gameBoard[i][j];
            }
        }
    }

    void makeMove(int row, int col, int letter) {
        this.gameBoard[row][col] = letter;
        this.lastMove = new Move(row, col);
        this.lastPlayer = letter;

    }

    boolean isValidMove(int row, int col) {
        if ((row >= borderY) || (col >= borderX) || (row < 0) || (col < 0))
            return false;
        if (this.gameBoard[row][col] != EMPTY)
            return false;
        return true;
    }

    ArrayList<Board> getChildren(int letter) {
        ArrayList<Board> children = new ArrayList<>();
        for (int row = 0; row < this.gameBoard.length; row++) {
            for (int col = 0; col < this.gameBoard[row].length; col++) {
                if (this.isValidMove(row, col)) {
                    Board child = new Board(this);
                    child.makeMove(row, col, letter);
                    children.add(child);
                }
            }
        }
        return children;
    }

    public int evaluate() {
        int largestRed = largestConnectedComponent(RED);
        int largestBlue = largestConnectedComponent(BLUE);
        return largestRed - largestBlue;
    }
    
    public int largestConnectedComponent(int color) {
        int largest = 0;
        boolean[][] visited = new boolean[borderY][borderX];
    
        for (int row = 0; row < borderY; row++) {
            for (int col = 0; col < borderX; col++) {
                if (gameBoard[row][col] == color && !visited[row][col]) {
                    int size = dfsComponent(row, col, color, visited);
                    largest = Math.max(largest, size);
                }
            }
        }
        return largest;
    }
    
    private int dfsComponent(int row, int col, int color, boolean[][] visited) {
        visited[row][col] = true;
        int size = 1;
    
        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};
    
        for (int i = 0; i < 4; i++) {
            int newRow = row + dRow[i];
            int newCol = col + dCol[i];
    
            if (isValidCell(newRow, newCol, color, visited)) {
                size += dfsComponent(newRow, newCol, color, visited);
            }
        }
    
        return size;
    }
    
    private boolean isValidCell(int row, int col, int color, boolean[][] visited) {
        return row >= 0 && row < borderY && col >= 0 && col < borderX
               && gameBoard[row][col] == color && !visited[row][col];
    }
    boolean isTerminal() {
    for (int row = 0; row < this.gameBoard.length; row++) {
        for (int col = 0; col < this.gameBoard[row].length; col++) {
            if (this.gameBoard[row][col] == EMPTY) {
                return false;
            }
        }
    }
    return true;
}


    void print() {
        System.out.println("*********");
        for (int row = 0; row < borderY; row++) {
            System.out.print("* ");
            for (int col = 0; col < borderX; col++) {
                switch (this.gameBoard[row][col]) {
                    case RED -> System.out.print("RED ");
                    case BLUE -> System.out.print("BLUE ");
                    case EMPTY -> System.out.print("- ");
                    default -> {
                    }
                }
            }
            System.out.println("*");
        }
        System.out.println("*********");
    }

    Move getLastMove() {
        return this.lastMove;
    }

    int getLastPlayer() {
        return this.lastPlayer;
    }

    int[][] getGameBoard() {
        return this.gameBoard;
    }

    void setGameBoard(int[][] gameBoard) {
        for (int i = 0; i < borderY; i++) {
            for (int j = 0; j < borderX; j++) {
                this.gameBoard[i][j] = gameBoard[i][j];
            }
        }
    }

    void setLastMove(Move lastMove) {
        this.lastMove.setRow(lastMove.getRow());
        this.lastMove.setCol(lastMove.getCol());
        this.lastMove.setValue(lastMove.getValue());
    }

    void setLastPlayer(int lastPlayer) {
        this.lastPlayer = lastPlayer;
    }
}