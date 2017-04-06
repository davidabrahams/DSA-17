/**
 * Solver definition for the 8 Puzzle challenge
 * Construct a tree of board states using A* to find a path to the goal
 */

import java.util.*;

public class Solver {

    public int minMoves = -1;
    private State solutionState;
    private boolean solved = false;
    private Board currBoard;

    /**
     * State class to make the cost calculations simple
     * This class holds a board state and all of its attributes
     */
    private class State implements Comparable<State>{
        // Each state needs to keep track of its cost and the previous state
        private Board board;
        private int moves;
        public int cost;
        private State prev;

        public State(Board board, int moves, State prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            // TODO: Compute cost of board state according to A*
            cost = this.moves + this.board.manhattan();
        }

        @Override
        public boolean equals(Object s) {
            if (s == this) return true;
            if (s == null) return false;
            if (!(s instanceof State)) return false;
            return ((State) s).board.equals(this.board);
        }

        /*
         * Return the cost difference between two states
         */
        @Override
        public int compareTo(State s) {
            return this.cost - s.cost;
        }
    }

    /*
     * Return the root state of a given state
     */
    private State root(State state) {
    	// TODO: Uma's code here
        State s = state;
        while (s.prev != null) {
            s = s.prev;
        }
        return s;
    }

    /*
     * A* Solver
     * Find a solution to the initial board using A* to generate the state tree
     * and a identify the shortest path to the the goal state
     */
    public Solver(Board initial) {
    	// TODO: Your code here
        this.currBoard = initial;
        HashSet<State> availableStates = new HashSet<>(); //store States we haven't been to yet
        HashSet<State> closedStates = new HashSet<>();
        State startState = new State(initial, 0, null);
        availableStates.add(startState); //make sure list isn't empty to start with

        while (availableStates.size() != 0) {
            //GET CLOSEST STATE
            State nearestState = Collections.min(availableStates);
            availableStates.remove(nearestState);
            //GET NEIGHBOR FRIENDS
            List<Board> neighborFriends = (List<Board>) nearestState.board.neighbors();
            for (Board neighbor: neighborFriends) {
                State neighborState = new State(neighbor, nearestState.moves + 1, nearestState);
                if (neighborState.board.isGoal()) {
                    minMoves = neighborState.moves;
                    solutionState = neighborState;
                    availableStates.clear();
                } else {
                    State A = find(availableStates, neighbor);
                    State B = find(closedStates, neighbor);
                    boolean ignore = false;
                    if (A != null && neighborState.cost > A.cost)
                        ignore = true;
                    if (B != null && neighborState.cost > B.cost)
                        ignore = true;
                    if (!ignore)
                        availableStates.add(neighborState);
                }
            }
            closedStates.add(nearestState);
        }
    }

    /*
     * Is the input board a solvable state
     */
    public boolean isSolvable() {
    	// TODO: Your code here
        return this.currBoard.solvable();
    }

    /*
     * Return the sequence of boards in a shortest solution, null if unsolvable
     */
    public Iterable<Board> solution() {
    	// TODO: Your code here
//        HashSet<State> availableStates = new HashSet<>(); //store States we haven't been to yet
//        ArrayList<Board> solutionsList = new ArrayList(); //add to once we've reached goal State
//        if (!isSolvable()) {
//            return null;
//        }
//        State startState = new State(this.currBoard, 0, null);
//        availableStates.add(startState); //make sure list isn't empty to start with
//
//
//        while (availableStates.size() != 0) {
//            int lowestCost = 0;
//            State nearestState = startState;
//            //GET CLOSEST STATE
//            for (State state: availableStates) {
//                if (lowestCost == 0 || state.cost < lowestCost) {
//                    nearestState = state;
//                    lowestCost = nearestState.cost;
//                }
//            }
        //CHECK IF GOAL
        List<Board> solutionsList = new ArrayList<>();
        while (solutionState.prev != null) {
            solutionsList.add(0, solutionState.board);
            solutionState = solutionState.prev;
        }
        return solutionsList;
        }

//            for (int[] row: nearestState.board.tiles) {
//                for (int item: row) {
//                    System.out.print(item);
//                }
//                System.out.println();
//            }
//            System.out.println();
//
//            //GET NEIGHBOR FRIENDS
//            List<Board> neighborFriends = (List<Board>) nearestState.board.neighbors();
//            for (Board neighbor: neighborFriends) {
//                State neighborState = new State(neighbor, nearestState.moves + 1, nearestState);
//                availableStates.add(neighborState);
//            }
//            availableStates.remove(nearestState);
//        }
//        return null;
//    }

    public State find(Iterable<State> iter, Board b) {
        for (State s : iter) {
            if (s.board.equals(b)) {
                return s;
            }
        }
        return null;
    }

    /*
     * Debugging space: Your solution can have whatever output you find useful
     * Optional challenge: create a command line interaction for users to input game
     * states
     */
    public static void main(String[] args) {
        int[][] initState = {{8, 6, 7}, {2, 5, 4}, {3, 0, 1}};
        Board initial = new Board(initState);

        // Solve the puzzle
        Solver solver = new Solver(initial);
        if (!solver.isSolvable())
            System.out.println("No solution");
        else {
            for (Board board : solver.solution()) {
                board.printBoard();
            }
            System.out.println(solver.minMoves);
        }
    }


}
