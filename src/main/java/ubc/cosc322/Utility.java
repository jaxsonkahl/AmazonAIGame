package ubc.cosc322;

public class Utility {
    // Method to generate an action map based on the current board state
    public static int[][] getActionMap(int[][] board){
        int[][] actionMap = new int[10][10]; // Initialize the action map
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                actionMap[i][j] = 0; // Initialize each cell to 0
                // Check if moving up is valid
                if(validSpot(i-1, j, board)){
                    actionMap[i][j]++;
                }
                // Check if moving up and left is valid
                if(validSpot(i-1, j-1, board)){
                    actionMap[i][j]++;
                }
                // Check if moving left is valid
                if(validSpot(i, j-1, board)){
                    actionMap[i][j]++;
                }
                // Check if moving down and left is valid
                if(validSpot(i+1, j-1, board)){
                    actionMap[i][j]++;
                }
                // Check if moving down is valid
                if(validSpot(i+1, j, board)){
                    actionMap[i][j]++;
                }
                // Check if moving down and right is valid
                if(validSpot(i+1, j+1, board)){
                    actionMap[i][j]++;
                }
                // Check if moving right is valid
                if(validSpot(i, j+1, board)){
                    actionMap[i][j]++;
                }
                // Check if moving up and right is valid
                if(validSpot(i-1, j+1, board)){
                    actionMap[i][j]++;
                }
            }  
        }
        return actionMap; // Return the generated action map
    }

    // Method to print the board state
    public static void printBoard(int[][] board) {
        System.out.println("-----------------------------------------");
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                if(j == 0) {
                    System.out.print("| ");
                }
                // Print empty space for 0
                if(board[i][j] == 0) {
                    System.out.print(' ');
                // Print 'X' for 3
                } else if(board[i][j] == 3) {
                    System.out.print('X');
                // Print the value for other numbers
                } else {
                    System.out.print(board[i][j]);
                }
                System.out.print(" | ");
            }
            System.out.println("\n-----------------------------------------");
        }
        System.out.println();
    }

    // Method to check if a spot is valid (within bounds)
    public static boolean validSpot(int col, int row){
        return row >= 0 && row < 9 && col >= 0 && col < 9;
    }

    // Method to check if a spot is valid and empty (within bounds and value is 0)
    public static boolean validSpot(int col, int row, int[][] board){
        return row >= 0 && row < 9 && col >= 0 && col < 9 && board[col][row] == 0;
    }

    // Method to calculate the sigmoid function
    public static double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x/5));
    }
}
