
package ubc.cosc322;

public class Utility{
   public static int[][] getActionMap(int[][] board){
    int[][] actionMap = new int[10][10];
    for(int i = 0; i <10; i++){
        for(int j= 0; j < 10; j++){
            actionMap[i][j] = 0;
            //Move up
            if(validSpot(i-1, j, board)){
                actionMap[i][j] ++;
            }
            //Move up and left
            if(validSpot(i-1, j-1, board)){
                actionMap[i][j] ++;
            }
            //Move left
            if(validSpot(i, j-1, board)){
                actionMap[i][j] ++;
            }
            //Move down left
            if(validSpot(i+1, j-1, board)){
                actionMap[i][j] ++;
            }
            //Move down
            if(validSpot(i+1, j, board)){
                actionMap[i][j] ++;
            }
            //Move down right
            if(validSpot(i+1, j+1, board)){
                actionMap[i][j] ++;
            }
            //Move right
            if(validSpot(i, j+1, board)){
                actionMap[i][j] ++;
            }
            //Move up and right
            if(validSpot(i-1, j+1, board)){
                actionMap[i][j] ++; 
            }
        }   
    }
    return actionMap;
}
public static void printBoard(int[][] board) {
    System.out.println("-----------------------------------------");
    for(int i = 0; i < 10; i++) {
        for(int j = 0; j < 10; j++) {
            if(j == 0) {
                System.out.print("| ");
            }
            if(board[i][j] == 0) {
                System.out.print(' ');
            } else if(board[i][j] == 3) {
                System.out.print('X');
            } else {
                System.out.print(board[i][j]);
            }
            System.out.print(" | ");
        }
        System.out.println("\n-----------------------------------------");
    }
    System.out.println();
}



   public static boolean validSpot(int col, int row){
    return row >= 0 && row < 9 && col >= 0 && col < 9;
   }
   public static boolean validSpot(int row, int col, int[][] board){
    return row >= 0 && row < 9 && col >= 0 && col < 9 && board[row][col] == 0;
   }
   public static double sigmoid(double x) {
    return 1 / (1 + Math.exp(-x/5));
    }

}