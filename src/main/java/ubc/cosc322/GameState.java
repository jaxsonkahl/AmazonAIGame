package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private static final int SIZE = 10;
    private static final int EMPTY = 0;
    private static final int PLAYER1 = 1;
    private static final int PLAYER2 = 2;
    private static final int ARROW = 3;
    
    private int[][] board;
    private List<int[]> player1Positions;
    private List<int[]> player2Positions;
    
    public GameState() {
        board = new int[SIZE][SIZE];
        player1Positions = new ArrayList<>();
        player2Positions = new ArrayList<>();
        initializeBoard();
    }
    
    private void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }
        
        // Initial positions of amazons
        int[][] p1Start = {{0, 3}, {0, 6}, {3, 0}, {3, 9}};
        int[][] p2Start = {{6, 0}, {6, 9}, {9, 3}, {9, 6}};
        
        for (int[] pos : p1Start) {
            board[pos[0]][pos[1]] = PLAYER1;
            player1Positions.add(pos);
        }
        
        for (int[] pos : p2Start) {
            board[pos[0]][pos[1]] = PLAYER2;
            player2Positions.add(pos);
        }
    }
    
    public boolean isValidMove(int fromX, int fromY, int toX, int toY) {
        if (toX < 0 || toX >= SIZE || toY < 0 || toY >= SIZE) return false;
        if (board[toX][toY] != EMPTY) return false;
        
        int dx = Integer.compare(toX, fromX);
        int dy = Integer.compare(toY, fromY);
        
        int x = fromX + dx, y = fromY + dy;
        while (x != toX || y != toY) {
            if (board[x][y] != EMPTY) return false;
            x += dx;
            y += dy;
        }
        return true;
    }
    
    public boolean makeMove(int fromX, int fromY, int toX, int toY, int arrowX, int arrowY) {
        if (!isValidMove(fromX, fromY, toX, toY)) return false;
        if (!isValidMove(toX, toY, arrowX, arrowY)) return false;
        
        int player = board[fromX][fromY];
        board[fromX][fromY] = EMPTY;
        board[toX][toY] = player;
        board[arrowX][arrowY] = ARROW;
        
        List<int[]> positions = (player == PLAYER1) ? player1Positions : player2Positions;
        
        for (int[] pos : positions) {
            if (pos[0] == fromX && pos[1] == fromY) {
                pos[0] = toX;
                pos[1] = toY;
                break;
            }
        }
        return true;
    }
    
    public boolean isGameOver() {
        return player1Positions.stream().noneMatch(pos -> !getValidMoves(PLAYER1).isEmpty()) ||
               player2Positions.stream().noneMatch(pos -> !getValidMoves(PLAYER2).isEmpty());
    }
    
    public List<int[]> getValidMoves(int player) {
        List<int[]> validMoves = new ArrayList<>();
        List<int[]> positions = (player == PLAYER1) ? player1Positions : player2Positions;
        
        for (int[] pos : positions) {
            int x = pos[0], y = pos[1];
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;
                    
                    int nx = x + dx, ny = y + dy;
                    while (nx >= 0 && nx < SIZE && ny >= 0 && ny < SIZE && board[nx][ny] == EMPTY) {
                        validMoves.add(new int[]{x, y, nx, ny});
                        nx += dx;
                        ny += dy;
                    }
                }
            }
        }
        return validMoves;
    }

    public int[][] getBoardState() {
        return this.board;
    }

    public List<Queens> getPlayerQueens(int player) {
        List<Queens> queens = new ArrayList<>();
        List<int[]> positions = (player == PLAYER1) ? player1Positions : player2Positions;
        for (int[] pos : positions) {
            queens.add(new Queens(pos[0], pos[1], player == PLAYER2));
        }
        return queens;
    }
}
