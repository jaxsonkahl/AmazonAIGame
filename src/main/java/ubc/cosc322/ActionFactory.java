package ubc.cosc322;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class ActionFactory {
	
	/**
	 * Generates all possible actions for a given board state and player color.
	 * 
	 * @param state The current board state represented as a 3D array.
	 * @param color The color of the player (1 for white, 2 for black).
	 * @return An ArrayList of all possible actions for the given player.
	 */
	public static ArrayList<Actions> getActions(int[][][] state, int color) {
		int[][] board = state[0]; // Extract the board from the state.
		ArrayList<Actions> queenMoves = new ArrayList<>();
		
		// Queue to store the positions of all queens of the given color.
		Queue<int[]> queenPositions = new LinkedList<>();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				// Add the position of each queen of the given color to the queue.
				if (board[i][j] == color) {
					queenPositions.add(new int[] { i, j });
				}
			}
		}
		
		// Process each queen's position to generate possible moves.
		while (!queenPositions.isEmpty()) {
			int[] queenPosition = queenPositions.poll();
			int queenSrcY = queenPosition[0];
			int queenSrcX = queenPosition[1];
			
			// Flags to track valid movement directions.
			boolean leftValid = true; 
			boolean upLeftValid = true;
			boolean upValid = true;
			boolean upRightValid = true;
			boolean rightValid = true;
			boolean downRightValid = true;
			boolean downValid = true;
			boolean downLeftValid = true;
			
			// Iterate through all possible movement distances (1 to 9).
			for (int i = 1; i < 10; i++) {
				// Check each direction and add valid moves to the arrowMoves list.
				
				// Up
				if (upValid && Utility.validSpot(queenSrcY - i, queenSrcX, board)) {
					queenMoves.addAll(getArrowMoves(queenSrcX, queenSrcY, queenSrcX, queenSrcY - i, state));
				} else upValid = false;
				
				// Up-left
				if (upLeftValid && Utility.validSpot(queenSrcY - i, queenSrcX - i, board)) {
					queenMoves.addAll(getArrowMoves(queenSrcX, queenSrcY, queenSrcX - i, queenSrcY - i, state));
				} else upLeftValid = false;
				
				// Left
				if (leftValid && Utility.validSpot(queenSrcY, queenSrcX - i, board)) {
					queenMoves.addAll(getArrowMoves(queenSrcX, queenSrcY, queenSrcX - i, queenSrcY, state));
				} else leftValid = false;
				
				// Down-left
				if (downLeftValid && Utility.validSpot(queenSrcY + i, queenSrcX - i, board)) {
					queenMoves.addAll(getArrowMoves(queenSrcX, queenSrcY, queenSrcX - i, queenSrcY + i, state));
				} else downLeftValid = false;
				
				// Down
				if (downValid && Utility.validSpot(queenSrcY + i, queenSrcX, board)) {
					queenMoves.addAll(getArrowMoves(queenSrcX, queenSrcY, queenSrcX, queenSrcY + i, state));
				} else downValid = false;
				
				// Down-right
				if (downRightValid && Utility.validSpot(queenSrcY + i, queenSrcX + i, board)) {
					queenMoves.addAll(getArrowMoves(queenSrcX, queenSrcY, queenSrcX + i, queenSrcY + i, state));
				} else downRightValid = false;
				
				// Right
				if (rightValid && Utility.validSpot(queenSrcY, queenSrcX + i, board)) {
					queenMoves.addAll(getArrowMoves(queenSrcX, queenSrcY, queenSrcX + i, queenSrcY, state));
				} else rightValid = false;
				
				// Up-right
				if (upRightValid && Utility.validSpot(queenSrcY - i, queenSrcX + i, board)) {
					queenMoves.addAll(getArrowMoves(queenSrcX, queenSrcY, queenSrcX + i, queenSrcY - i, state));
				} else upRightValid = false;
			}
		}
		
		return queenMoves; // Return the list of all possible actions.
	}
	
	/**
	 * Generates all possible arrow moves for a given queen move.
	 * 
	 * @param queenSrcX The source X-coordinate of the queen.
	 * @param queenSrcY The source Y-coordinate of the queen.
	 * @param queenDestX The destination X-coordinate of the queen.
	 * @param queenDestY The destination Y-coordinate of the queen.
	 * @param state The current board state represented as a 3D array.
	 * @return An ArrayList of all possible arrow moves for the given queen move.
	 */
	private static ArrayList<Actions> getArrowMoves(int queenSrcX, int queenSrcY, int queenDestX, int queenDestY, int[][][] state) {
		int[][] board = state[0]; // Extract the board from the state.
		ArrayList<Actions> arrowMoves = new ArrayList<>();
		
		// Simulate the board state after the queen move.
		int[][] newBoard = Actions.performQueenMove(queenSrcX, queenSrcY, queenDestX, queenDestY, state);
		
		// Flags to track valid arrow movement directions.
		boolean leftValid = true; 
		boolean upLeftValid = true;
		boolean upValid = true;
		boolean upRightValid = true;
		boolean rightValid = true;
		boolean downRightValid = true;
		boolean downValid = true;
		boolean downLeftValid = true;
		
		// Iterate through all possible arrow movement distances (1 to 9).
		for (int i = 1; i < 10; i++) {
			// Check each direction and add valid arrow moves to the arrowMoves list.
			
			// Up
			if (upValid && Utility.validSpot(queenDestY - i, queenDestX, newBoard)) {
				arrowMoves.add(new Actions(queenSrcX, queenSrcY, queenDestX, queenDestY, queenDestX, queenDestY - i));
			} else upValid = false;

			// Up-left
			if (upLeftValid && Utility.validSpot(queenDestY - i, queenDestX - i, newBoard)) {
				arrowMoves.add(new Actions(queenSrcX, queenSrcY, queenDestX, queenDestY, queenDestX - i, queenDestY - i));
			} else upLeftValid = false;
			
			// Left
			if (leftValid && Utility.validSpot(queenDestY, queenDestX - i, newBoard)) {
				arrowMoves.add(new Actions(queenSrcX, queenSrcY, queenDestX, queenDestY, queenDestX - i, queenDestY));
			} else leftValid = false;
			
			// Down-left
			if (downLeftValid && Utility.validSpot(queenDestY + i, queenDestX - i, newBoard)) {
				arrowMoves.add(new Actions(queenSrcX, queenSrcY, queenDestX, queenDestY, queenDestX - i, queenDestY + i));
			} else downLeftValid = false;
			
			// Down
			if (downValid && Utility.validSpot(queenDestY + i, queenDestX, newBoard)) {
				arrowMoves.add(new Actions(queenSrcX, queenSrcY, queenDestX, queenDestY, queenDestX, queenDestY + i));
			} else downValid = false;
			
			// Down-right
			if (downRightValid && Utility.validSpot(queenDestY + i, queenDestX + i, newBoard)) {
				arrowMoves.add(new Actions(queenSrcX, queenSrcY, queenDestX, queenDestY, queenDestX + i, queenDestY + i));
			} else downRightValid = false;
			
			// Right
			if (rightValid && Utility.validSpot(queenDestY, queenDestX + i, newBoard)) {
				arrowMoves.add(new Actions(queenSrcX, queenSrcY, queenDestX, queenDestY, queenDestX + i, queenDestY));
			} else rightValid = false;
			
			// Up-right
			if (upRightValid && Utility.validSpot(queenDestY - i, queenDestX + i, newBoard)) {
				arrowMoves.add(new Actions(queenSrcX, queenSrcY, queenDestX, queenDestY, queenDestX + i, queenDestY - i));
			} else upRightValid = false;
		}
		
		return arrowMoves; // Return the list of all possible arrow moves.
	}
}