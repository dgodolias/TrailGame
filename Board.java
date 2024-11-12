import java.awt.Point;
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

    // Constructor initializing the board with empty cells
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

    // Copy constructor for deep copy of the board
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

    // Make a move on the board
    void makeMove(int row, int col, int letter) {
        this.gameBoard[row][col] = letter;
        this.lastMove = new Move(row, col);
        this.lastPlayer = letter;
    }

    // Check if a move is valid
    boolean isValidMove(int row, int col) {
        if ((row >= borderY) || (col >= borderX) || (row < 0) || (col < 0))
            return false;
        if (this.gameBoard[row][col] != EMPTY)
            return false;
        return true;
    }

    // Generate children states for possible moves
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

    // Evaluate the board state
    public int evaluate() {
        int largestRed = largestConnectedComponent(RED);
        int largestBlue = largestConnectedComponent(BLUE);
        return largestRed - largestBlue;
    }
    
    // Get connected components of a specific color
    ArrayList<ArrayList<Point>> getConnectedComponents(int color) {
        ArrayList<ArrayList<Point>> components = new ArrayList<>();
        boolean[][] visited = new boolean[borderY][borderX];

        for (int row = 0; row < borderY; row++) {
            for (int col = 0; col < borderX; col++) {
                if (gameBoard[row][col] == color && !visited[row][col]) {
                    ArrayList<Point> component = new ArrayList<>();
                    dfsComponent(row, col, color, visited, component);  // Builds the component list
                    components.add(component);
                }
            }
        }
        return components;
    }

    // DFS method to find a connected component and populate the component list while returning the size
    private int dfsComponent(int row, int col, int color, boolean[][] visited, ArrayList<Point> component) {
        // Mark the cell as visited
        visited[row][col] = true;
        component.add(new Point(col, row));  // Add Point with (x=col, y=row) for accurate representation

        int size = 1;  // Start size with the current cell
        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};

        // Explore all four directions
        for (int i = 0; i < 4; i++) {
            int newRow = row + dRow[i];
            int newCol = col + dCol[i];

            if (isValidCell(newRow, newCol, color, visited)) {
                size += dfsComponent(newRow, newCol, color, visited, component);  // Accumulate size
            }
        }
        return size;
    }

    // Method to calculate the size of the largest connected component for a given color
    public int largestConnectedComponent(int color) {
        int largest = 0;
        boolean[][] visited = new boolean[borderY][borderX];

        for (int row = 0; row < borderY; row++) {
            for (int col = 0; col < borderX; col++) {
                if (gameBoard[row][col] == color && !visited[row][col]) {
                    int size = dfsComponent(row, col, color, visited, new ArrayList<>());  // Call dfsComponent for size only
                    largest = Math.max(largest, size);
                }
            }
        }
        return largest;
    }

    // Check if a cell is valid for DFS traversal
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
