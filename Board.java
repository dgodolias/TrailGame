import java.util.ArrayList;

public class Board
{
    public static final int RED = 1;
    public static final int BLUE = -1; 
    public static final int EMPTY = 0;
    public int borderX;
    public int borderY;

    private int[][] gameBoard;

    /* Variable containing who played last; whose turn resulted in this board
     * Even a new Board has lastLetterPlayed value; it denotes which player will play first
     */
    private int lastPlayer;

    //Immediate move that lead to this board
    private Move lastMove;

    Board(int borderX, int borderY)
    {
        this.borderX = borderX;
        this.borderY = borderY;
        this.lastMove = new Move();
        this.lastPlayer = BLUE; // Changed from O to BLUE
        this.gameBoard = new int[borderY][borderX];
        for(int i = 0; i < this.gameBoard.length; i++)
        {
            for(int j = 0; j < this.gameBoard.length; j++)
            {
                this.gameBoard[i][j] = EMPTY;
            }
        }
    }

    Board(Board board)
    {
        board.borderX = this.borderX;
        board.borderY = this.borderY;
        this.lastMove = board.lastMove;
        this.lastPlayer = board.lastPlayer;
        this.gameBoard = new int[borderY][borderX];
        for(int i = 0; i < this.gameBoard.length; i++)
        {
            for(int j = 0; j < this.gameBoard.length; j++)
            {
                this.gameBoard[i][j] = board.gameBoard[i][j];
            }
        }
    }

    //Make a move; it places a letter in the board
    void makeMove(int row, int col, int letter)
    {
        this.gameBoard[row][col] = letter;
        this.lastMove = new Move(row, col);
        this.lastPlayer = letter;
    }

    //Checks whether a move is valid; whether a square is empty
    boolean isValidMove(int row, int col)
    {
        if((row > 2) || (col > 2) || (row < 0) || (col < 0)) return false;
        if(this.gameBoard[row][col] != EMPTY) return false;
        return true;
    }

    ArrayList<Board> getChildren(int letter)
    {
        ArrayList<Board> children = new ArrayList<>();
        for(int row = 0; row < this.gameBoard.length; row++)
        {
            for(int col = 0; col < this.gameBoard.length; col++)
            {
                if(this.isValidMove(row, col))
                {
                    Board child = new Board(this);
                    child.makeMove(row, col, letter);
                    children.add(child);
                }
            }
        }
        return children;
    }


    /*
     * The heuristic we use to evaluate is
     * the number of almost complete tic-tac-toes (having 2 letter in a row, column or diagonal)
     * minus the number of the opponent's almost complete tic-tac-toes
     * Special case: if a complete tic-tac-toe is present it counts as ten
     */
    int evaluate()
    {
        int scoreRED = 0;
        int scoreBLUE = 0; // Changed from scoreO to scoreBLUE
        int sum;

        //checking rows
        for(int row = 0; row < this.gameBoard.length; row++)
        {
            sum = this.gameBoard[row][0] + this.gameBoard[row][1] + this.gameBoard[row][2];
            if(sum == 3) scoreRED += 10;
            else if(sum == 2) scoreRED++;
            else if(sum == -3) scoreBLUE += 10; // Changed from scoreO to scoreBLUE
            else if(sum == -2) scoreBLUE ++; // Changed from scoreO to scoreBLUE
        }

        // checking columns
        for(int col = 0; col < this.gameBoard.length; col++)
        {
            sum = this.gameBoard[0][col] + this.gameBoard[1][col] + this.gameBoard[2][col];
            if(sum == 3) scoreRED += 10;
            else if(sum == 2) scoreRED++;
            else if(sum == -3) scoreBLUE += 10; // Changed from scoreO to scoreBLUE
            else if(sum == -2) scoreBLUE ++; // Changed from scoreO to scoreBLUE
        }

        //checking diagonals
        sum = this.gameBoard[0][0] + this.gameBoard[1][1] + this.gameBoard[2][2];

        if(sum == 3) scoreRED += 10;
        else if(sum == 2) scoreRED++;
        else if(sum == -3) scoreBLUE += 10; // Changed from scoreO to scoreBLUE
        else if(sum == -2) scoreBLUE ++; // Changed from scoreO to scoreBLUE

        sum = this.gameBoard[0][2] + this.gameBoard[1][1] + this.gameBoard[2][0];
        if(sum == 3) scoreRED += 10;
        else if(sum == 2) scoreRED++;
        else if(sum == -3) scoreBLUE += 10; // Changed from scoreO to scoreBLUE
        else if(sum == -2) scoreBLUE ++; // Changed from scoreO to scoreBLUE

        return scoreRED - scoreBLUE; // Changed from scoreO to scoreBLUE
    }

    /*
     * A state is terminal if there is a tic-tac-toe
     * or no empty tiles are available
     */
    boolean isTerminal()
    {
        // horizontal tic-tac-toe
        for(int row = 0; row < this.gameBoard.length; row++)
        {
            if((this.gameBoard[row][0] == this.gameBoard[row][1]) &&
                    (this.gameBoard[row][1] == this.gameBoard[row][2]) && (this.gameBoard[row][0]!=0))
            {
                return true;
            }
        }

        // vertical tic-tac-toe
        for(int col = 0; col < this.gameBoard.length; col++)
        {
            if((this.gameBoard[0][col] == this.gameBoard[1][col]) &&
                    (this.gameBoard[1][col] == this.gameBoard[2][col]) && (this.gameBoard[0][col]!=0))
            {
                return true;
            }
        }

        // diagonal tic-tac-toe
        if((this.gameBoard[0][0] == this.gameBoard[1][1]) &&
                (this.gameBoard[1][1] == this.gameBoard[2][2]) && (this.gameBoard[0][0]!=0))
        {
            return true;
        }

        if((this.gameBoard[0][2] == this.gameBoard[1][1]) &&
                (this.gameBoard[1][1] == this.gameBoard[2][0]) && (this.gameBoard[0][2]!=0))
        {
            return true;
        }

        // check for empty
        for(int row = 0; row < this.gameBoard.length; row++)
        {
            for(int col = 0; col < this.gameBoard.length; col++)
            {
                if(this.gameBoard[row][col] == EMPTY) return false;
            }
        }

        return true;
    }

    void print()
    {
        System.out.println("*********");
        for(int row=0; row<3; row++)
        {
            System.out.print("* ");
            for(int col=0; col<3; col++)
            {
                switch (this.gameBoard[row][col]) {
                    case RED -> System.out.print("RED ");
                    case BLUE -> System.out.print("BLUE "); // Changed from O to BLUE
                    case EMPTY -> System.out.print("- ");
                    default -> {
                    }
                }
            }
            System.out.println("*");
        }
        System.out.println("*********");
    }

    Move getLastMove()
    {
        return this.lastMove;
    }

    int getLastPlayer()
    {
        return this.lastPlayer;
    }

    int[][] getGameBoard()
    {
        return this.gameBoard;
    }

    void setGameBoard(int[][] gameBoard)
    {
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<3; j++)
            {
                this.gameBoard[i][j] = gameBoard[i][j];
            }
        }
    }

    void setLastMove(Move lastMove)
    {
        this.lastMove.setRow(lastMove.getRow());
        this.lastMove.setCol(lastMove.getCol());
        this.lastMove.setValue(lastMove.getValue());
    }

    void setLastPlayer(int lastPlayer)
    {
        this.lastPlayer = lastPlayer;
    }
}