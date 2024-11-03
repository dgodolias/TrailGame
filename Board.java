import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

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
        if ((row >= borderY) || (col >= borderX) || (row < 0) || (col < 0)) return false;
        if (this.gameBoard[row][col] != EMPTY) return false;
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
    
        // Check rows for trails
        for (int row = 0; row < this.gameBoard.length; row++) {
            for (int col = 0; col < this.gameBoard[row].length; col++) {
                if (this.gameBoard[row][col] == RED) {
                    longestTrailRED = Math.max(longestTrailRED, findTrailLength(row, col));
                } else if (this.gameBoard[row][col] == BLUE) {
                    longestTrailBLUE = Math.max(longestTrailBLUE, findTrailLength(row, col));
                }
            }
        }
    
        // Check columns for trails
        for (int col = 0; col < this.gameBoard[0].length; col++) {
            for (int row = 0; row < this.gameBoard.length; row++) {
                if (this.gameBoard[row][col] == RED) {
                    longestTrailRED = Math.max(longestTrailRED, findTrailLength(row, col));
                } else if (this.gameBoard[row][col] == BLUE) {
                    longestTrailBLUE = Math.max(longestTrailBLUE, findTrailLength(row, col));
                }
            }
        }
    
        return longestTrailRED - longestTrailBLUE;
    }

    int findTrailLength(int row, int col) {
        if (row < 0 || row >= borderY || col < 0 || col >= borderX || gameBoard[row][col] == EMPTY) {
            return 0;
        }
    
        int color = gameBoard[row][col];
        boolean[][] visited = new boolean[borderY][borderX];
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{row, col});
        visited[row][col] = true;
        int trailLength = 0;
    
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            trailLength++;
    
            String[] directions = {"right", "down", "left", "up"};
    
            for (String direction : directions) {
                int[] neighbor = neighborNode(current[0], current[1], direction);
                int newRow = neighbor[0];
                int newCol = neighbor[1];
    
                if (newRow != -1 && newCol != -1 && !visited[newRow][newCol] && gameBoard[newRow][newCol] == color) {
                    queue.add(new int[]{newRow, newCol});
                    visited[newRow][newCol] = true;
                }
            }
        }
    
        return trailLength;
    }

    int[] neighborNode(int row, int col, String direction) {
        int newRow = row, newCol = col;
        switch (direction) {
            case "up" -> newRow = row - 1;
            case "down" -> newRow = row + 1;
            case "left" -> newCol = col - 1;
            case "right" -> newCol = col + 1;
            default -> {
                return new int[]{-1, -1};
            }
        }
        if (newRow < 0 || newRow >= borderY || newCol < 0 || newCol >= borderX) {
            return new int[]{-1, -1};
        }
        return new int[]{newRow, newCol};
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