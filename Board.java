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
                    longestTrailRED = Math.max(longestTrailRED, findLongestPathDFS(row, col, RED, new boolean[borderY][borderX]));
                } else if (this.gameBoard[row][col] == BLUE && !visited[row][col]) {
                    // Find the longest path starting from this BLUE cell
                    longestTrailBLUE = Math.max(longestTrailBLUE, findLongestPathDFS(row, col, BLUE, new boolean[borderY][borderX]));
                }
            }
        }
    
        System.out.println("Longest trail for RED: " + longestTrailRED);
        System.out.println("Longest trail for BLUE: " + longestTrailBLUE);
        return longestTrailRED - longestTrailBLUE;
    }
    
    private int findLongestPathDFS(int row, int col, int color, boolean[][] visited) {
        // Base case: If out of bounds, color doesn't match, or already visited in this path, return 0
        if (row < 0 || row >= borderY || col < 0 || col >= borderX || gameBoard[row][col] != color || visited[row][col]) {
            return 0;
        }
    
        // Mark the cell as visited for this path calculation
        visited[row][col] = true;
    
        // Explore all four directions (up, down, left, right)
        int maxPath = 0;
        maxPath = Math.max(maxPath, findLongestPathDFS(row + 1, col, color, visited));
        maxPath = Math.max(maxPath, findLongestPathDFS(row - 1, col, color, visited));
        maxPath = Math.max(maxPath, findLongestPathDFS(row, col + 1, color, visited));
        maxPath = Math.max(maxPath, findLongestPathDFS(row, col - 1, color, visited));
    
        // Unmark the cell to allow backtracking for other paths from this starting point
        visited[row][col] = false;
    
        // Return the length of the path including this cell
        return 1 + maxPath;
    }      
    

    int findTrailLength(int row, int col) {
        if (row < 0 || row >= borderY || col < 0 || col >= borderX || gameBoard[row][col] == EMPTY) {
            return 0;
        }

        int color = gameBoard[row][col];
        boolean[][] visited = new boolean[borderY][borderX];
        return dfs(row, col, color, visited);
    }

    int dfs(int row, int col, int color, boolean[][] visited) {
        visited[row][col] = true;
        int maxLength = 1;

        int[][] directions = {
                { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } // right, down, left, up
        };

        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];

            if (newRow >= 0 && newRow < borderY && newCol >= 0 && newCol < borderX &&
                    !visited[newRow][newCol] && gameBoard[newRow][newCol] == color) {
                maxLength = Math.max(maxLength, 1 + dfs(newRow, newCol, color, visited));
            }
        }

        visited[row][col] = false;
        return maxLength;
    }

    int[] neighborNode(int row, int col, String direction) {
        int newRow = row, newCol = col;
        switch (direction) {
            case "up" -> newRow = row - 1;
            case "down" -> newRow = row + 1;
            case "left" -> newCol = col - 1;
            case "right" -> newCol = col + 1;
            default -> {
                return new int[] { -1, -1 };
            }
        }
        if (newRow < 0 || newRow >= borderY || newCol < 0 || newCol >= borderX) {
            return new int[] { -1, -1 };
        }
        return new int[] { newRow, newCol };
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