import java.util.ArrayList;
import java.util.Random;

public class Player
{
    private int maxDepth;
    private int playerColor;

    Player(int maxDepth, int playerColor)
    {
        this.maxDepth = maxDepth;
        this.playerColor = playerColor;
    }

    Move MiniMax(Board board)
    {
        if(playerColor == Board.RED)
        {
            // If RED plays, it wants to maximize the heuristic value
            return max(new Board(board), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
        else
        {
            // If BLUE plays, it wants to minimize the heuristic value
            return min(new Board(board), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
    }

    // Max function with alpha-beta pruning
    Move max(Board board, int depth, int alpha, int beta)
    {
        Random r = new Random();

        // Terminal condition: game over or maximum depth reached
        if(board.isTerminal() || (depth == this.maxDepth))
        {
            Move finalmove = new Move(board.getLastMove().getRow(), board.getLastMove().getCol(), board.evaluate());
            //board.print();
            return finalmove;
        }

        ArrayList<Board> children = board.getChildren(Board.RED);
        Move maxMove = new Move(Integer.MIN_VALUE);

        for(Board child: children)
        {
            Move move = min(child, depth + 1, alpha, beta);

            if(move.getValue() > maxMove.getValue())
            {
                maxMove.setRow(child.getLastMove().getRow());
                maxMove.setCol(child.getLastMove().getCol());
                maxMove.setValue(move.getValue());
            }
            else if(move.getValue() == maxMove.getValue())
            {
                // Randomly choose between moves of equal value
                if(r.nextInt(2) == 0)
                {
                    maxMove.setRow(child.getLastMove().getRow());
                    maxMove.setCol(child.getLastMove().getCol());
                    maxMove.setValue(move.getValue());
                }
            }

            // Update alpha and check for beta cutoff
            alpha = Math.max(alpha, maxMove.getValue());
            if(beta <= alpha)
            {
                // Beta cut-off
                break;
            }
        }
        return maxMove;
    }

    // Min function with alpha-beta pruning
    Move min(Board board, int depth, int alpha, int beta)
    {
        Random r = new Random();

        // Terminal condition: game over or maximum depth reached
        if(board.isTerminal() || (depth == this.maxDepth))
        {
            Move finalmove = new Move(board.getLastMove().getRow(), board.getLastMove().getCol(), board.evaluate());
            return finalmove;
        }

        ArrayList<Board> children = board.getChildren(Board.BLUE);
        Move minMove = new Move(Integer.MAX_VALUE); 

        for(Board child: children)
        {
            Move move = max(child, depth + 1, alpha, beta);

            if(move.getValue() < minMove.getValue())
            {
                minMove.setRow(child.getLastMove().getRow());
                minMove.setCol(child.getLastMove().getCol());
                minMove.setValue(move.getValue());
            }
            else if(move.getValue() == minMove.getValue())
            {
                // Randomly choose between moves of equal value
                if(r.nextInt(2) == 0)
                {
                    minMove.setRow(child.getLastMove().getRow());
                    minMove.setCol(child.getLastMove().getCol());
                    minMove.setValue(move.getValue());
                }
            }

            // Update beta and check for alpha cutoff
            beta = Math.min(beta, minMove.getValue());
            if(beta <= alpha)
            {
                // Alpha cut-off
                break;
            }
        }
        return minMove;
    }
}
