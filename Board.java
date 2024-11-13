import java.awt.Point;
import java.util.*;

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
            Arrays.fill(this.gameBoard[i], EMPTY);
        }
    }

    Board(Board board) {
        this.borderX = board.borderX;
        this.borderY = board.borderY;
        this.lastMove = board.lastMove;
        this.lastPlayer = board.lastPlayer;
        this.gameBoard = new int[borderY][borderX];
        for (int i = 0; i < this.gameBoard.length; i++) {
            this.gameBoard[i] = board.gameBoard[i].clone();
        }
    }

    void makeMove(int row, int col, int color) {
        this.gameBoard[row][col] = color;
        this.lastMove = new Move(row, col);
        this.lastPlayer = color;
    }

    boolean isValidMove(int row, int col) {
        return row >= 0 && row < borderY && col >= 0 && col < borderX && this.gameBoard[row][col] == EMPTY;
    }

    ArrayList<Board> getChildren(int color) {
        ArrayList<Board> children = new ArrayList<>();
        for (int row = 0; row < borderY; row++) {
            for (int col = 0; col < borderX; col++) {
                if (isValidMove(row, col)) {
                    Board child = new Board(this);
                    child.makeMove(row, col, color);
                    children.add(child);
                }
            }
        }
        return children;
    }

    public int evaluate() {
        int aiColor = -this.lastPlayer;
        int h1 = heuristic1();
        int h2 = heuristic2(aiColor);
        int h3 = heuristic3(aiColor);
        int h4 = heuristic4();
        int weight1 = 0;
        int weight2 = 0;
        int weight3 = 0;
        int weight4 = 1;
        return weight1 * h1 + weight2 * h2 + weight3 * h3 + h4 * weight4;
    }

    public int heuristic1() {
        int largestRed = largestConnectedComponent(RED);
        int largestBlue = largestConnectedComponent(BLUE);
        return largestRed - largestBlue;
    }

    public int heuristic2(int playerColor) {
        return computeComponentDistanceHeuristic(playerColor);
    }

    public int heuristic3(int playerColor) {
        int score = 0;
        double centerRow = (borderY - 1) / 2.0;
        double centerCol = (borderX - 1) / 2.0;

        for (int row = 0; row < borderY; row++) {
            for (int col = 0; col < borderX; col++) {
                if (gameBoard[row][col] == playerColor) {
                    double distance = Math.sqrt(Math.pow(row - centerRow, 2) + Math.pow(col - centerCol, 2));
                    double maxDistance = Math.sqrt(Math.pow(centerRow, 2) + Math.pow(centerCol, 2));
                    double normalizedScore = (maxDistance - distance) / maxDistance;
                    score += normalizedScore * 10;
                }
            }
        }
        return score;
    }
    public int heuristic4() {
        Point bestPointForBlue = findPointWithMostFreeSlots(BLUE);
        Point bestPointForRed = findPointWithMostFreeSlots(RED);
    
        int blueFreeSlots = 0;
        int redFreeSlots = 0;
    
        if (bestPointForBlue != null) {
            blueFreeSlots = countFreeSlotsAroundPoint(bestPointForBlue);
        }
    
        if (bestPointForRed != null) {
            redFreeSlots = countFreeSlotsAroundPoint(bestPointForRed);
        }
        System.out.println(bestPointForRed.x+" "+bestPointForRed.y+" "+(blueFreeSlots-redFreeSlots));
        return blueFreeSlots - redFreeSlots;
    }

    public Point findPointWithMostFreeSlots(int color) {
        ArrayList<ArrayList<Point>> components = this.getConnectedComponents(color);
        int[][] directions = {
            {-1, 0}, // up
            {1, 0},  // down
            {0, -1}, // left
            {0, 1}   // right
        };
    
        Point pointWithMostFreeSlots = null;
        int maxFreeSlots = -1;
    
        for (ArrayList<Point> component : components) {
            for (Point point : component) {
                int freeSlots = 0;
                int x = point.x;
                int y = point.y;
    
                for (int[] direction : directions) {
                    int newX = x + direction[0];
                    int newY = y + direction[1];
    
                    // Ensure newX and newY are within the gameBoard boundaries
                    if (newX >= 0 && newX < this.gameBoard[0].length && newY >= 0 && newY < this.gameBoard.length) {
                        if (this.gameBoard[newY][newX] == EMPTY) {
                            freeSlots++;
                        }
                    }
                }
    
                if (freeSlots > maxFreeSlots) {
                    maxFreeSlots = freeSlots;
                    pointWithMostFreeSlots = point;
                }
            }
        }
    
        return pointWithMostFreeSlots;
    }
    
 
    public int countFreeSlotsAroundPoint(Point point) {
        int freeSlots = 0;
        int[][] directions = {
            {-1, 0}, // up
            {1, 0},  // down
            {0, -1}, // left
            {0, 1}   // right
        };
    
        int x = point.x;
        int y = point.y;
    
        for (int[] direction : directions) {
            int newX = x + direction[0];
            int newY = y + direction[1];
    
            // Ensure newX and newY are within the gameBoard boundaries
            if (newX >= 0 && newX < this.gameBoard[0].length && newY >= 0 && newY < this.gameBoard.length) {
                if (this.gameBoard[newY][newX] == EMPTY) {
                    freeSlots++;
                }
            }
        }
    
        return freeSlots;
    }
    
    
        

    public ArrayList<ArrayList<Point>> findFreeBlocksForEachComponent(int color) {
        ArrayList<ArrayList<Point>> components = this.getConnectedComponents(color);
        ArrayList<ArrayList<Point>> componentFreeBlocks = new ArrayList<>(); // Stores free blocks for each component
        
        int[][] directions = {
            {-1, 0}, // up
            {1, 0},  // down
            {0, -1}, // left
            {0, 1}   // right
        };
        
        for (ArrayList<Point> component : components) {
            HashSet<Point> freeBlocksSet = new HashSet<>(); // Temporary set to store unique free blocks for the component
            for (Point point : component) {
                int x = point.x;
                int y = point.y;
                
                for (int[] direction : directions) {
                    int newX = x + direction[0];
                    int newY = y + direction[1];
                    
                    // Ensure newX and newY are within the gameBoard boundaries
                    if (newX >= 0 && newX < this.gameBoard[0].length && newY >= 0 && newY < this.gameBoard.length) {
                        if (this.gameBoard[newY][newX] == EMPTY) {
                            freeBlocksSet.add(new Point(newX, newY)); // Add the empty block to the set
                        }
                    }
                }
            }
            componentFreeBlocks.add(new ArrayList<>(freeBlocksSet));
        }
        
        return componentFreeBlocks; // Return the calculated list of free blocks for each component
    }
    
 

    private int computeComponentDistanceHeuristic(int playerColor) {
        ArrayList<ArrayList<Point>> components = getConnectedComponents(playerColor);
        int totalScore = 0;

        for (int i = 0; i < components.size(); i++) {
            List<Point> componentA = components.get(i);
            int sizeA = componentA.size();

            for (int j = i + 1; j < components.size(); j++) {
                List<Point> componentB = components.get(j);
                int sizeB = componentB.size();

                Integer minDistance = computeMinimumDistance(componentA, componentB, playerColor);

                if (minDistance != null) {
                    int heuristicValue = sizeA + sizeB - minDistance;
                    totalScore += heuristicValue;
                }
            }
        }

        return totalScore;
    }

    private Integer computeMinimumDistance(List<Point> componentA, List<Point> componentB, int playerColor) {
        int minDistance = Integer.MAX_VALUE;
        boolean pathExists = false;

        for (Point pointA : componentA) {
            int[][] distances = bfsDistancesFrom(pointA, playerColor);

            for (Point pointB : componentB) {
                int distance = distances[pointB.y][pointB.x];

                if (distance != -1) {
                    pathExists = true;
                    if (distance < minDistance) {
                        minDistance = distance;
                    }
                }
            }
        }

        if (pathExists) {
            return minDistance;
        } else {
            return null;
        }
    }

    private int[][] bfsDistancesFrom(Point start, int playerColor) {
        int[][] distances = new int[borderY][borderX];
        for (int i = 0; i < borderY; i++) {
            Arrays.fill(distances[i], -1);
        }

        Queue<Point> queue = new LinkedList<>();
        queue.add(start);
        distances[start.y][start.x] = 0;

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            int currentDistance = distances[current.y][current.x];

            int[] dRow = {-1, 1, 0, 0};
            int[] dCol = {0, 0, -1, 1};

            for (int i = 0; i < 4; i++) {
                int newRow = current.y + dRow[i];
                int newCol = current.x + dCol[i];

                if (newRow >= 0 && newRow < borderY && newCol >= 0 && newCol < borderX) {
                    if (distances[newRow][newCol] == -1) {
                        int cellValue = gameBoard[newRow][newCol];
                        if (cellValue == EMPTY || cellValue == playerColor) {
                            distances[newRow][newCol] = currentDistance + 1;
                            queue.add(new Point(newCol, newRow));
                        }
                    }
                }
            }
        }

        return distances;
    }

    ArrayList<ArrayList<Point>> getConnectedComponents(int color) {
        ArrayList<ArrayList<Point>> components = new ArrayList<>();
        boolean[][] visited = new boolean[borderY][borderX];

        for (int row = 0; row < borderY; row++) {
            for (int col = 0; col < borderX; col++) {
                if (gameBoard[row][col] == color && !visited[row][col]) {
                    ArrayList<Point> component = new ArrayList<>();
                    dfsComponent(row, col, color, visited, component);
                    components.add(component);
                }
            }
        }
        return components;
    }

    private int dfsComponent(int row, int col, int color, boolean[][] visited, ArrayList<Point> component) {
        visited[row][col] = true;
        component.add(new Point(col, row));

        int size = 1;
        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int newRow = row + dRow[i];
            int newCol = col + dCol[i];

            if (isValidCell(newRow, newCol, color, visited)) {
                size += dfsComponent(newRow, newCol, color, visited, component);
            }
        }
        return size;
    }

    public int largestConnectedComponent(int color) {
        int largest = 0;
        boolean[][] visited = new boolean[borderY][borderX];

        for (int row = 0; row < borderY; row++) {
            for (int col = 0; col < borderX; col++) {
                if (gameBoard[row][col] == color && !visited[row][col]) {
                    int size = dfsComponent(row, col, color, visited, new ArrayList<>());
                    largest = Math.max(largest, size);
                }
            }
        }
        return largest;
    }

    private boolean isValidCell(int row, int col, int color, boolean[][] visited) {
        return row >= 0 && row < borderY && col >= 0 && col < borderX
                && gameBoard[row][col] == color && !visited[row][col];
    }

    boolean isTerminal() {
        for (int row = 0; row < borderY; row++) {
            for (int col = 0; col < borderX; col++) {
                if (gameBoard[row][col] == EMPTY) {
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
            this.gameBoard[i] = gameBoard[i].clone();
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
