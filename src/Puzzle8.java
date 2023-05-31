import java.util.PriorityQueue;
import java.util.Stack;
import java.util.Scanner;

public class Puzzle8 {

    private static final int[][] goal = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
    private int[][] board;
    private int moves;
    private Puzzle8 prev;

    public Puzzle8(int[][] blocks) {
        board = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = blocks[i][j];
            }
        }
    }

    public int manhattan() {
        int dist = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] != 0) {
                    int row = (board[i][j] - 1) / 3;
                    int col = (board[i][j] - 1) % 3;
                    dist += Math.abs(row - i) + Math.abs(col - j);
                }
            }
        }
        return dist;
    }

    public boolean isGoal() {
        return manhattan() == 0;
    }

    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        if (y == null || y.getClass() != this.getClass()) {
            return false;
        }
        Puzzle8 that = (Puzzle8) y;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (this.board[i][j] != that.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    private int[][] cloneBoard(int[][] board) {
        int[][] copy = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }

    private void exch(int[][] a, int i1, int j1, int i2, int j2) {
        int temp = a[i1][j1];
        a[i1][j1] = a[i2][j2];
        a[i2][j2] = temp;
    }

    public Iterable<Puzzle8> neighbors() {
        PriorityQueue<Puzzle8> neighbors = new PriorityQueue<Puzzle8>((a, b) -> a.manhattan() - b.manhattan() + a.moves - b.moves);
        int row = 0, col = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    row = i;
                    col = j;
                }
            }
        }
        if (row > 0) {
            int[][] copy = cloneBoard(board);
            exch(copy, row, col, row - 1, col);
            Puzzle8 neighbor = new Puzzle8(copy);
            neighbor.moves = moves + 1;
            neighbor.prev = this;
            neighbors.add(neighbor);
        }
        if (row < 2) {
            int[][] copy = cloneBoard(board);
            exch(copy, row, col, row + 1, col);
            Puzzle8 neighbor = new Puzzle8(copy);
            neighbor.moves = moves + 1;
            neighbor.prev = this;
            neighbors.add(neighbor);
        }
        if (col > 0) {
            int[][] copy = cloneBoard(board);
            exch(copy, row, col, row, col - 1);
            Puzzle8 neighbor = new Puzzle8(copy);
            neighbor.moves = moves + 1;
            neighbor.prev = this;
            neighbors.add(neighbor);
        }
        if (col < 2) {
            int[][] copy = cloneBoard(board);
            exch(copy, row, col, row, col + 1);
            Puzzle8 neighbor = new Puzzle8(copy);
            neighbor.moves = moves + 1;
            neighbor.prev = this;
            neighbors.add(neighbor);
        }
        return neighbors;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // read the initial board position
        int[][] board = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = scanner.nextInt();
            }
        }

        // create the initial puzzle and add it to the queue
        Puzzle8 initialPuzzle = new Puzzle8(board);
        initialPuzzle.moves = 0;
        initialPuzzle.prev = null;
        PriorityQueue<Puzzle8> queue = new PriorityQueue<Puzzle8>((a, b) -> a.manhattan() - b.manhattan() + a.moves - b.moves);
        queue.add(initialPuzzle);

        // perform the search
        Puzzle8 solution = null;
        while (!queue.isEmpty()) {
            Puzzle8 puzzle = queue.poll();
            if (puzzle.isGoal()) {
                solution = puzzle;
                break;
            }
            for (Puzzle8 neighbor : puzzle.neighbors()) {
                if (neighbor.equals(puzzle.prev)) {
                    continue;
                }
                queue.add(neighbor);
            }
        }

        // print the solution sequence
        System.out.println(solution.moves);
        System.out.println();
        // print the solution sequence
        Stack<Puzzle8> stack = new Stack<Puzzle8>();
        while (solution != null) {
            stack.push(solution);
            solution = solution.prev;
        }
        while (!stack.isEmpty()) {
            solution = stack.pop();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    System.out.print(solution.board[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}

