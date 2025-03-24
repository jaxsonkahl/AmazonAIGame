package ubc.cosc322;

public class Actions {
	public int queenSpotX;
	public int queenSpotY;
	public int queenDestinationX;
	public int queenDestinationY;
	public int arrowDestinationX;
	public int arrowDestinationY;

	public Actions(int queenSpotX, int queenSpotY, int queenDestinationX, int queenDestinationY, int arrowDestinationX, int arrowDestinationY) {
		this.queenSpotX = queenSpotX;
		this.queenSpotY = queenSpotY;
		this.queenDestinationX = queenDestinationX;
		this.queenDestinationY = queenDestinationY;
		this.arrowDestinationX = arrowDestinationX;
		this.arrowDestinationY = arrowDestinationY;
	}

	public static int[][][] performAction(Actions action, int[][][] boardState){
		int[][] board = boardState[0];
		int[][] actionMap = boardState[1];
		int[][] updateBoard = new int[10][10];
		int[][] updateActionMap = new int[10][10];

		for(int i =0; i <10; i++){
			for(int j = 0; j < 10; j++){
				updateBoard[i][j] = board[i][j];
				updateActionMap[i][j] = actionMap[i][j];
			}
		}

		updateBoard[action.queenDestinationY][action.queenDestinationX] = board[action.queenSpotY][action.queenSpotX];
		updateBoard[action.queenSpotY][action.queenSpotX] = 0;
		updateBoard[action.arrowDestinationY][action.arrowDestinationX] = 3;

		if(Utility.validSpot(action.queenSpotY-1, action.queenSpotX)) {
			updateActionMap[action.queenSpotY-1][action.queenSpotX]++;
		}
		if(Utility.validSpot(action.queenSpotX-1, action.queenSpotX-1)) {
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

		return new int[][][]{updateBoard, updateActionMap};
	}

	public static int[][] performQueenMove(int queenSpotX, int queenSpotY, int queenDestinationX, int queenDestinationY, int [][][] boardState){
		int[][] board = boardState[0];
		int[][] updateBoard = new int[10][10];

		for(int i = 0; i < 10; i++){
			for(int j = 0; j <10; j++){
				updateBoard[i][j] = board[i][j];
			}
		}

		updateBoard[queenDestinationY][queenDestinationX] = board[queenSpotY][queenSpotX];
		updateBoard[queenSpotY][queenSpotX] = 0;
		return updateBoard;
	}

	public boolean Equal(Actions action){
		return (this.queenSpotX == action.queenSpotX
				&& this.queenSpotY == action.queenSpotY
				&& this.queenDestinationX == action.queenDestinationX
				&& this.queenDestinationY == action.queenDestinationY
				&& this.arrowDestinationX == action.arrowDestinationX
				&& this.arrowDestinationY == action.arrowDestinationY);
	}

	public void printMove() {
		System.out.println(this.queenSpotX + ", " + this.queenSpotY + ", " + this.queenDestinationX + ", " + this.queenDestinationY + ", " + this.arrowDestinationX + ", " + this.arrowDestinationY);
	}




}