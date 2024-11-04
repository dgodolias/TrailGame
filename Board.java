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
        int longestTrailRED = 0;
        int longestTrailBLUE = 0;
        boolean[][] visited = new boolean[borderY][borderX];
    
        // Find longest path for RED and BLUE using DFS without revisiting in the same path calculation
        for (int row = 0; row < this.gameBoard.length; row++) {
            for (int col = 0; col < this.gameBoard[row].length; col++) {
                if (this.gameBoard[row][col] == RED && !visited[row][col]) {
                    // Find the longest path starting from this RED cell
                    longestTrailRED = this.longestSequence(1);
                } else if (this.gameBoard[row][col] == BLUE && !visited[row][col]) {
                    // Find the longest path starting from this BLUE cell
                    longestTrailBLUE = this.longestSequence(-1);
                }
            }
        }
        return longestTrailRED - longestTrailBLUE;
    }
    
    public int longestSequence(int color) {
        int longest = 0;
    
        for (int row = 0; row < borderY; row++) {
            for (int col = 0; col < borderX; col++) {
                if (gameBoard[row][col] == color) {
                    // Reset visited for each new start cell
                    boolean[][] visited = new boolean[borderY][borderX];
                    int currentLength = dfs(row, col, color, visited);
                    longest = Math.max(longest, currentLength);
                }
            }
        }
        return longest;
    }
    
    private int dfs(int row, int col, int color, boolean[][] visited) {
        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};
    
        visited[row][col] = true;
        int maxLength = 1;
    
        for (int i = 0; i < 4; i++) {
            int newRow = row + dRow[i];
            int newCol = col + dCol[i];
    
            if (isValidCell(newRow, newCol, color, visited)) {
                maxLength = Math.max(maxLength, 1 + dfs(newRow, newCol, color, visited));
            }
        }
    
        return maxLength;
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
    
    // The board is full, so we print the longest sequence for each color
    int longestRedSequence = longestSequence(RED);
    int longestBlueSequence = longestSequence(BLUE);
    System.out.println("Game Over!");
    System.out.println("Longest sequence for RED: " + longestRedSequence);
    System.out.println("Longest sequence for BLUE: " + longestBlueSequence);
    
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