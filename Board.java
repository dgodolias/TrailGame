import java.util.ArrayList;

public class Board {
    public static final int B = -1;
    public static final int R = 1;
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
        this.lastPlayer = B;
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

    int evaluate() {
        int scoreRED = 0;
        int scoreBLUE = 0;
        int sum;

        for (int row = 0; row < this.gameBoard.length; row++) {
            sum = 0;
            for (int col = 0; col < this.gameBoard[row].length; col++) {
                sum += this.gameBoard[row][col];
            }
            if (sum == borderX) scoreRED += 10;
            else if (sum == borderX - 1) scoreRED++;
            else if (sum == -borderX) scoreBLUE += 10;
            else if (sum == -(borderX - 1)) scoreBLUE++;
        }

        for (int col = 0; col < this.gameBoard[0].length; col++) {
            sum = 0;
            for (int row = 0; row < this.gameBoard.length; row++) {
                sum += this.gameBoard[row][col];
            }
            if (sum == borderY) scoreRED += 10;
            else if (sum == borderY - 1) scoreRED++;
            else if (sum == -borderY) scoreBLUE += 10;
            else if (sum == -(borderY - 1)) scoreBLUE++;
        }

        sum = 0;
        for (int i = 0; i < Math.min(borderX, borderY); i++) {
            sum += this.gameBoard[i][i];
        }
        if (sum == Math.min(borderX, borderY)) scoreRED += 10;
        else if (sum == Math.min(borderX, borderY) - 1) scoreRED++;
        else if (sum == -Math.min(borderX, borderY)) scoreBLUE += 10;
        else if (sum == -(Math.min(borderX, borderY) - 1)) scoreBLUE++;

        sum = 0;
        for (int i = 0; i < Math.min(borderX, borderY); i++) {
            sum += this.gameBoard[i][borderX - i - 1];
        }
        if (sum == Math.min(borderX, borderY)) scoreRED += 10;
        else if (sum == Math.min(borderX, borderY) - 1) scoreRED++;
        else if (sum == -Math.min(borderX, borderY)) scoreBLUE += 10;
        else if (sum == -(Math.min(borderX, borderY) - 1)) scoreBLUE++;

        return scoreRED - scoreBLUE;
    }

    boolean isFull(){
        for(int i=0;i<this.gameBoard.length;i++){
            for(int j=0; j<this.gameBoard.length;j++){
                if(this.gameBoard[i][j]==EMPTY){
                    return false;
                }
            }
        }
        return true;
    }

    boolean isTerminal() {
        return this.isFull();
    }

    public void longestSequence(int color) {
        boolean[][] visited = new boolean[borderY][borderX];
        int longest = 0;
    
        for (int row = 0; row < borderY; row++) {
            for (int col = 0; col < borderX; col++) {
                if (gameBoard[row][col] == color && !visited[row][col]) {
                    longest = Math.max(longest, dfs(row, col, color, visited));
                }
            }
        }
        
        System.out.println(longest);
    }
    
    private int dfs(int row, int col, int color, boolean[][] visited) {
        // Directions for up, down, left, right
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
    
    

    void print() {
        System.out.println("**************");
        for (int row = 0; row < borderY; row++) {
            System.out.print("* ");
            for (int col = 0; col < borderX; col++) {
                switch (this.gameBoard[row][col]) {
                    case R -> System.out.print("R ");
                    case B -> System.out.print("B ");
                    case EMPTY -> System.out.print("- ");
                    default -> {
                    }
                }
            }
            System.out.println("*");
        }
        System.out.println("**************");
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