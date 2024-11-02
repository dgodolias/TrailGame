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

    boolean isTerminal() {
        for (int row = 0; row < this.gameBoard.length; row++) {
            if ((this.gameBoard[row][0] == this.gameBoard[row][1]) &&
                (this.gameBoard[row][1] == this.gameBoard[row][2]) && (this.gameBoard[row][0] != 0)) {
                return true;
            }
        }

        for (int col = 0; col < this.gameBoard[0].length; col++) {
            if ((this.gameBoard[0][col] == this.gameBoard[1][col]) &&
                (this.gameBoard[1][col] == this.gameBoard[2][col]) && (this.gameBoard[0][col] != 0)) {
                return true;
            }
        }

        if ((this.gameBoard[0][0] == this.gameBoard[1][1]) &&
            (this.gameBoard[1][1] == this.gameBoard[2][2]) && (this.gameBoard[0][0] != 0)) {
            return true;
        }

        if ((this.gameBoard[0][2] == this.gameBoard[1][1]) &&
            (this.gameBoard[1][1] == this.gameBoard[2][0]) && (this.gameBoard[0][2] != 0)) {
            return true;
        }

        for (int row = 0; row < this.gameBoard.length; row++) {
            for (int col = 0; col < this.gameBoard[row].length; col++) {
                if (this.gameBoard[row][col] == EMPTY) return false;
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