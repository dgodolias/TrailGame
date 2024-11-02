public class Main
{
    public static void main(String[] args)
    {
        //We create the players and the board
        //MaxDepth for the MiniMax algorithm is set to 2; feel free to change the values
        Player playerRED = new Player(2, Board.RED);
        Player playerBLUE = new Player(2, Board.BLUE);
        Board board = new Board(3, 3);
        board.print();

        //Put this out of comments for the BLUE to play first
        //board.setLastPlayer(Board.RED);

        while (!board.isTerminal())
        {
            System.out.println();
            switch (board.getLastPlayer())
            {
                //If RED played last, then BLUE plays now
                case Board.RED:
                    System.out.println("BLUE plays");
                    Move moveBLUE = playerBLUE.MiniMax(board);
                    board.makeMove(moveBLUE.getRow(), moveBLUE.getCol(), Board.BLUE);
                    break;
                //If BLUE played last, then RED plays now
                case Board.BLUE:
                    System.out.println("RED plays");
                    Move moveRED = playerRED.MiniMax(board);
                    board.makeMove(moveRED.getRow(), moveRED.getCol(), Board.RED);
                    break;
                default:
                    break;
            }
            board.print();
        }
    }
}