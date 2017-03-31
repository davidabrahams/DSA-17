import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Board definition for the 8 Puzzle challenge
 */
public class Board {

    private int n;
    public int[][] tiles;
    private int[][] goal = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
    public int[][] goalPoints = {{2,2},{0,0},{0,1},{0,2},{1,0},{1,1},{1,2},{2,0},{2,1}}; //index is key; 0 is actually where 9 would go
    public int[] blankPoint = {0,0};

    /*
     * Set the global board size and tile state
     */
    public Board(int[][] b) {
        // TODO: Your code here
        tiles = b;
    }

    /*
     * Size of the board (equal to 3 for 8 puzzle, but in theory the Board
     * class should  work for any puzzle size)
     */
    private int size() {
    	// TODO: Your code here
        return tiles.length;
    }

    /*
     * Sum of the manhattan distances between the tiles and the goal
     * Estimated cost from the current node to the goal for A* (h(n))
     */
    public int manhattan() {
		// TODO: Your code here
        int sum = 0;
        for (int row = 0; row < size(); row++) {
            for (int col = 0; col < size(); col++) {
                int num = tiles[row][col];
                if (num != 0) { //don't want no zeroes in here
                    int[] goalPoint = goalPoints[num];
                    int horizontalDist = row - goalPoint[0];
                    int verticalDist = col - goalPoint[1];
                    sum += horizontalDist + verticalDist;
                } else {
                    blankPoint = new int[]{row, col};
                }
            }
        }
        return sum;
    }

    /*
     * Compare the current state to the goal state
     */
    public boolean isGoal() {
    	// TODO: Your code here
        for (int row = 0; row < size(); row++) {
            for (int col = 0; col < size(); col++) {
                if (tiles[row][col] != goal[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    /*
     * Returns true if the board is solvable
     */
    public boolean solvable() {
    	// TODO: Your code here
        return false;
    }

    /*
     * Return the neighboring boards in the state tree
     * One possible method: Create a duplicate board state, try moving the
     * blank space up, down, left, and right, and if the resulting board state
     * is valid, add it to an accumulator.
     */
    public Iterable<Board> neighbors() {
    	// TODO: Your code here

        //check up

        int[][] duplicateTiles = copyOf(tiles); //TODO: copy copyOf
        return null;
    }

    /*
     * Prints out the board state nicely for debugging purposes
     */
    public void printBoard() {
        for (int[] tile : tiles) {
            for (int aTile : tile) System.out.print(aTile + " ");
            System.out.println();
        }
        System.out.println();
    }

    /*
     * Check if this board equals a given board state
     */
    @Override
    public boolean equals(Object x) {
        // Check if the board equals an input Board object
        if (x == this) return true;
        if (x == null) return false;
        if (!(x instanceof Board)) return false;
        // Check if the same size
        Board y = (Board) x;
        if (y.tiles.length != n || y.tiles[0].length != n) {
            return false;
        }
        // Check if the same tile configuration
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] != y.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        // DEBUG - Your solution can include whatever output you find useful
        int[][] initState = {{1, 2, 3}, {4, 0, 6}, {7, 8, 5}};
        Board board = new Board(initState);

        board.printBoard();
        System.out.println("Size: " + board.size());
        System.out.println("Solvable: " + board.solvable());
        System.out.println("Manhattan: " + board.manhattan());
        System.out.println("Is goal: " + board.isGoal());
        System.out.println("Neighbors:");
        Iterable<Board> it = board.neighbors();
        for (Board b : it)
            b.printBoard();
    }
}
