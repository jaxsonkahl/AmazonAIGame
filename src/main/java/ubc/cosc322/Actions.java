package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;

public class Actions {
	List<ArrayList<Integer>> moves;
	List<ArrayList<Integer>> arrowShots;
	
	public Actions() {
		this.moves = new ArrayList<ArrayList<Integer>>();
		this.arrowShots = new ArrayList<ArrayList<Integer>>();
	}

	// Method to get all possible moves for a given queen
	public void getActions(BoardState board, Queens queen) { // waiting for BoardState Implementation
		moves = new ArrayList<ArrayList<Integer>>();
		int queenRow = queen.getRow();
		int queenCol = queen.getColumn();
		ArrayList<Integer> move;

		// QUEEN MOVING UP
		for(int i=1; i<10; ++i) {
			if(queenRow - i < 0) { // Check if out of bounds
				break;
			} else if(board.board[queenRow - i][queenCol] != null) { // Check if the cell is occupied
				break;
			}
			move = new ArrayList<>();
			move.add(-i);
			move.add(0);
			moves.add(move);
		}

		// QUEEN MOVING DOWN
		for(int i=1; i<10; ++i) {
			if(queenRow + i > 9) { // Check if out of bounds
				break;
			} else if(board.board[queenRow + i][queenCol] != null) { // Check if the cell is occupied
				break;
			}
			move = new ArrayList<>();
			move.add(i);
			move.add(0);
			moves.add(move);
		}

		// QUEEN MOVING LEFT
		for(int i=1; i<10; ++i) {
			if(queenCol - i < 0) { // Check if out of bounds
				break;
			} else if(board.board[queenRow][queenCol - i] != null) { // Check if the cell is occupied
				break;
			}
			move = new ArrayList<>();
			move.add(0);
			move.add(-i);
			moves.add(move);
		}

		// QUEEN MOVING RIGHT
		for(int i=1; i<10; ++i) {
			if(queenCol + i > 9) { // Check if out of bounds
				break;
			} else if(board.board[queenRow][queenCol + i] != null) { // Check if the cell is occupied
				break;
			}
			move = new ArrayList<>();
			move.add(0);
			move.add(i);
			moves.add(move);
		}
		
		// QUEEN MOVING UP AND LEFT
		for(int i=1; i<10; ++i) {
			if(queenRow - i < 0 || queenCol - i < 0) { // Check if out of bounds
				break;
			} else if(board.board[queenRow - i][queenCol - i] != null) { // Check if the cell is occupied
				break;
			}
			move = new ArrayList<>();
			move.add(-i);
			move.add(-i);
			moves.add(move);
		}

		// QUEEN MOVING UP AND RIGHT
		for(int i=1; i<10; ++i) {
			if(queenRow - i < 0 || queenCol + i > 9) { // Check if out of bounds
				break;
			} else if(board.board[queenRow - i][queenCol + i] != null) { // Check if the cell is occupied
				break;
			}
			move = new ArrayList<>();
			move.add(-i);
			move.add(i);
			moves.add(move);
		}

		// QUEEN MOVING DOWN AND LEFT
		for(int i=1; i<10; ++i) {
			if(queenRow + i > 9 || queenCol - i < 0) { // Check if out of bounds
				break;
			} else if(board.board[queenRow + i][queenCol - i] != null) { // Check if the cell is occupied
				break;
			}
			move = new ArrayList<>();
			move.add(i);
			move.add(-i);
			moves.add(move);
		}

		// QUEEN MOVING DOWN AND RIGHT
		for(int i=1; i<10; ++i) {
			if(queenRow + i > 9 || queenCol + i > 9) { // Check if out of bounds
				break;
			} else if(board.board[queenRow + i][queenCol + i] != null) { // Check if the cell is occupied
				break;
			}
			move = new ArrayList<>();
			move.add(i);
			move.add(i);
			moves.add(move);
		}
	}

	// Method to check if a queen can escape
	public boolean escape(BoardState board, Queens queen) { // waiting for BoardState Implementation
		for(int i=0;i<board.player.length;i++) {
			int numofmove = moves.size();
			if(numofmove <2) { // If the number of moves is less than 2, the queen can escape
				return true;
			}
			
		} return false;
	}

	// Method to get all possible arrow shots for a given queen
	public void availableArrows(BoardState board, Queens queen) { // waiting for BoardState Implementation
		arrowShots = new ArrayList<ArrayList<Integer>>();
		int queenRow = queen.getRow();
		int queenCol = queen.getColumn();
		ArrayList<Integer> move;

		// SHOOT ARROW UP
		for(int i=1; i<10; ++i) {
			if(queenRow - i < 0) { // Check if out of bounds
				break;
			} else if(board.board[queenRow - i][queenCol] != null) { // Check if the cell is occupied
				break;
			}
			move = new ArrayList<>();
			move.add(-i);
			move.add(0);
			arrowShots.add(move);
		}

		// SHOOT ARROW DOWN
		for(int i=1; i<10; ++i) {
			if(queenRow + i > 9) { // Check if out of bounds
				break;
			}
			else if(board.board[queenRow + i][queenCol] != null) { // Check if the cell is occupied
				break;
			}
			move = new ArrayList<>();
			move.add(i);
			move.add(0);
			arrowShots.add(move);
		}

		// SHOOT ARROW LEFT
		for(int i=1; i<10; ++i) {
			if(queenCol - i < 0) { // Check if out of bounds
				break;
			} else if(board.board[queenRow][queenCol - i] != null) { // Check if the cell is occupied
				break;
			}
			move = new ArrayList<>();
			move.add(0);
			move.add(-i);
			arrowShots.add(move);
		}

		// SHOOT ARROW RIGHT
		for(int i=1; i<10; ++i) {
			if(queenCol + i > 9) { // Check if out of bounds
				break;
			} else if(board.board[queenRow][queenCol + i] != null) { // Check if the cell is occupied
				break;
			}
			move = new ArrayList<>();
			move.add(0);
			move.add(i);
			arrowShots.add(move);
		}

		// SHOOT ARROW LEFT AND UP
		for(int i=1; i<10; ++i) {
			if(queenRow - i < 0 || queenCol - i < 0) { // Check if out of bounds
				break;
			} else if(board.board[queenRow - i][queenCol - i] != null) { // Check if the cell is occupied
				break;
			}
			move = new ArrayList<>();
			move.add(-i);
			move.add(-i);
			arrowShots.add(move);
		}

		// SHOOT ARROW RIGHT AND UP
		for(int i=1; i<10; ++i) {
			if(queenRow - i < 0 || queenCol + i > 9) { // Check if out of bounds
				break;
			} else if(board.board[queenRow - i][queenCol + i] != null) { // Check if the cell is occupied
				break;
			}
			move = new ArrayList<>();
			move.add(-i);
			move.add(i);
			arrowShots.add(move);
		}
	
		// SHOOT ARROW LEFT AND DOWN
		for(int i=1; i<10; ++i) {
			if(queenRow + i > 9 || queenCol - i < 0) { // Check if out of bounds
				break;
			} else if(board.board[queenRow + i][queenCol - i] != null) { // Check if the cell is occupied
				break;
			}
			move = new ArrayList<>();
			move.add(i);
			move.add(-i);
			arrowShots.add(move);
		}

		// SHOOT ARROW DOWN AND RIGHT
		for(int i=1; i<10; ++i) {
			if(queenRow + i > 9 || queenCol + i > 9) { // Check if out of bounds
				break;
			} else if(board.board[queenRow + i][queenCol + i] != null) { // Check if the cell is occupied
				break;
			}
			move = new ArrayList<>();
			move.add(i);
			move.add(i);
			arrowShots.add(move);
		}
	}
}