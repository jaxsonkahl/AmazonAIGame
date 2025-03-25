package ubc.cosc322;

public class Actions {
    // Coordinates for the queen's initial and destination positions
    public int queenSpotX;
    public int queenSpotY;
    public int queenDestinationX;
    public int queenDestinationY;
    
    // Coordinates for the arrow's destination position
    public int arrowDestinationX;
    public int arrowDestinationY;

    // Constructor to initialize the action with queen and arrow positions
    public Actions(int queenSpotX, int queenSpotY, int queenDestinationX, int queenDestinationY, int arrowDestinationX, int arrowDestinationY) {
        this.queenSpotX = queenSpotX;
        this.queenSpotY = queenSpotY;
        this.queenDestinationX = queenDestinationX;
        this.queenDestinationY = queenDestinationY;
        this.arrowDestinationX = arrowDestinationX;
        this.arrowDestinationY = arrowDestinationY;
    }

    // Method to perform an action and update the board state
    public static int[][][] performAction(Actions action, int[][][] boardState){
        int[][] board = boardState[0]; // Current board state
        int[][] actionMap = boardState[1]; // Current action map
        int[][] updateBoard = new int[10][10]; // New board state after the action
        int[][] updateActionMap = new int[10][10]; // New action map after the action

        // Copy the current board and action map to the new ones
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                updateBoard[i][j] = board[i][j];
                updateActionMap[i][j] = actionMap[i][j];
            }
        }

        // Update the board with the queen's new position and the arrow's position
        updateBoard[action.queenDestinationY][action.queenDestinationX] = board[action.queenSpotY][action.queenSpotX];
        updateBoard[action.queenSpotY][action.queenSpotX] = 0;
        updateBoard[action.arrowDestinationY][action.arrowDestinationX] = 3;

        // Update the action map around the queen's initial position
        if(Utility.validSpot(action.queenSpotY-1, action.queenSpotX)) {
            updateActionMap[action.queenSpotY-1][action.queenSpotX]++;
        }
        if(Utility.validSpot(action.queenSpotY-1, action.queenSpotX-1)) {
            updateActionMap[action.queenSpotY-1][action.queenSpotX-1]++;
        }
        if(Utility.validSpot(action.queenSpotY-1, action.queenSpotX+1)) {
            updateActionMap[action.queenSpotY-1][action.queenSpotX+1]++;
        }
        if(Utility.validSpot(action.queenSpotY, action.queenSpotX-1)) {
            updateActionMap[action.queenSpotY][action.queenSpotX-1]++;
        }
        if(Utility.validSpot(action.queenSpotY, action.queenSpotX+1)) {
            updateActionMap[action.queenSpotY][action.queenSpotX+1]++;
        }
        if(Utility.validSpot(action.queenSpotY+1, action.queenSpotX)) {
            updateActionMap[action.queenSpotY+1][action.queenSpotX]++;
        }
        if(Utility.validSpot(action.queenSpotY+1, action.queenSpotX-1)) {
            updateActionMap[action.queenSpotY+1][action.queenSpotX-1]++;
        }
        if(Utility.validSpot(action.queenSpotY+1, action.queenSpotX+1)) {
            updateActionMap[action.queenSpotY+1][action.queenSpotX+1]++;
        }

        // Update the action map around the queen's destination position
        if(Utility.validSpot(action.queenDestinationY-1, action.queenDestinationX)) {
            updateActionMap[action.queenDestinationY-1][action.queenDestinationX]--;
        }
        if(Utility.validSpot(action.queenDestinationY-1, action.queenDestinationX-1)) {
            updateActionMap[action.queenDestinationY-1][action.queenDestinationX-1]--;
        }
        if(Utility.validSpot(action.queenDestinationY-1, action.queenDestinationX+1)) {
            updateActionMap[action.queenDestinationY-1][action.queenDestinationX+1]--;
        }
        if(Utility.validSpot(action.queenDestinationY, action.queenDestinationX-1)) {
            updateActionMap[action.queenDestinationY][action.queenDestinationX-1]--;
        }
        if(Utility.validSpot(action.queenDestinationY, action.queenDestinationX+1)) {
            updateActionMap[action.queenDestinationY][action.queenDestinationX+1]--;
        }
        if(Utility.validSpot(action.queenDestinationY+1, action.queenDestinationX)) {
            updateActionMap[action.queenDestinationY+1][action.queenDestinationX]--;
        }
        if(Utility.validSpot(action.queenDestinationY+1, action.queenDestinationX-1)) {
            updateActionMap[action.queenDestinationY+1][action.queenDestinationX-1]--;
        }
        if(Utility.validSpot(action.queenDestinationY+1, action.queenDestinationX+1)) {
            updateActionMap[action.queenDestinationY+1][action.queenDestinationX+1]--;
        }

        // Update the action map around the arrow's destination position
        if(Utility.validSpot(action.arrowDestinationY-1, action.arrowDestinationX)) {
            updateActionMap[action.arrowDestinationY-1][action.arrowDestinationX]--;
        }
        if(Utility.validSpot(action.arrowDestinationY-1, action.arrowDestinationX-1)) {
            updateActionMap[action.arrowDestinationY-1][action.arrowDestinationX-1]--;
        }
        if(Utility.validSpot(action.arrowDestinationY-1, action.arrowDestinationX+1)) {
            updateActionMap[action.arrowDestinationY-1][action.arrowDestinationX+1]--;
        }
        if(Utility.validSpot(action.arrowDestinationY, action.arrowDestinationX-1)) {
            updateActionMap[action.arrowDestinationY][action.arrowDestinationX-1]--;
        }
        if(Utility.validSpot(action.arrowDestinationY, action.arrowDestinationX+1)) {
            updateActionMap[action.arrowDestinationY][action.arrowDestinationX+1]--;
        }
        if(Utility.validSpot(action.arrowDestinationY+1, action.arrowDestinationX)) {
            updateActionMap[action.arrowDestinationY+1][action.arrowDestinationX]--;
        }
        if(Utility.validSpot(action.arrowDestinationY+1, action.arrowDestinationX-1)) {
            updateActionMap[action.arrowDestinationY+1][action.arrowDestinationX-1]--;
        }
        if(Utility.validSpot(action.arrowDestinationY+1, action.arrowDestinationX+1)) {
            updateActionMap[action.arrowDestinationY+1][action.arrowDestinationX+1]--;
        }

        // Return the updated board and action map
        return new int[][][]{updateBoard, updateActionMap};
    }

    // Method to perform a queen move and update the board state
    public static int[][] performQueenMove(int queenSpotX, int queenSpotY, int queenDestinationX, int queenDestinationY, int [][][] boardState){
        int[][] board = boardState[0]; // Current board state
        int[][] updateBoard = new int[10][10]; // New board state after the move

        // Copy the current board to the new one
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                updateBoard[i][j] = board[i][j];
            }
        }

        // Update the board with the queen's new position
        updateBoard[queenDestinationY][queenDestinationX] = board[queenSpotY][queenSpotX];
        updateBoard[queenSpotY][queenSpotX] = 0;
        return updateBoard;
    }

    // Method to check if two actions are equal
    public boolean Equal(Actions action){
        return (this.queenSpotX == action.queenSpotX
                && this.queenSpotY == action.queenSpotY
                && this.queenDestinationX == action.queenDestinationX
                && this.queenDestinationY == action.queenDestinationY
                && this.arrowDestinationX == action.arrowDestinationX
                && this.arrowDestinationY == action.arrowDestinationY);
    }

    // Method to print the move details
    public void printMove() {
        System.out.println(this.queenSpotX + ", " + this.queenSpotY + ", " + this.queenDestinationX + ", " + this.queenDestinationY + ", " + this.arrowDestinationX + ", " + this.arrowDestinationY);
    }
}