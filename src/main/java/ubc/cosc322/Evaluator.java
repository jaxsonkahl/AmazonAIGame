package ubc.cosc322;

import java.util.LinkedList;
import java.util.Queue;

public class Evaluator {
	private static int whiteQueen = 1;
    private static int blackQueen = 2;
    
    private static double turnAdvantage = 0.15;

    // Method to get the heuristic evaluation of the game state
	public static double getHeuristicEval(int state[][][], int playerTurn) {
		int[][] board = state[0];
		int[][] mobilityMap = state[1];
		
		double[] queenMinDistance = queenMinDistance(board, playerTurn);
		double t1 = queenMinDistance[0];
		double t2 = 0.0;
		double c1 = queenMinDistance[1];
		double c2 = 0.0;
		double w = queenMinDistance[2];
		
		double mobilityEval = 0.0;
		
		if(w > 10.0) {
			double[] kingMinDistance = kingMinDistance(board, playerTurn);
			t2 = kingMinDistance[0];
			c2 = kingMinDistance[1];
			mobilityEval = getMobilityEval(board, mobilityMap, w);
		} else {
			w = 0.0;
		}
		
		double territoryEval = f1(w)*t1 + f2(w)*c1 + f3(w)*t2 + f4(w)*c2;

		return territoryEval + mobilityEval;
	}
	
	// Function to calculate weight factor f1
	private static double f1(double w) {
		return (100.0 - w) / 100.0;
	}
	
	// Function to calculate weight factor f2
	private static double f2(double w) {
		return (1.0 - f1(w)) / 4.0;
	}
	
	// Function to calculate weight factor f3
	private static double f3(double w) {
		return (1.0 - f1(w)) / 4.0;
	}
	
	// Function to calculate weight factor f4
	private static double f4(double w) {
		return (1.0 - f1(w)) / 4.0;
	}
	
	// Function to calculate weight factor f5 based on mobility
	private static double f5(double w, double mobility) {
		return  w * Math.pow(1.2, -mobility) / 45.0;
	}
	
	// Method to calculate the minimum distance of queens from each position
	private static double[] queenMinDistance(int board[][], int playerTurn) {
		int whiteMinDistance[][] = new int[10][10];
		int blackMinDistance[][] = new int[10][10];
		
		 // Initialize distance arrays to infinity
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				whiteMinDistance[i][j] = Integer.MAX_VALUE;
				blackMinDistance[i][j] = Integer.MAX_VALUE;
			}
		}
		
		Queue<int[]> whiteQueue = new LinkedList<>();
		Queue<int[]> blackQueue = new LinkedList<>();
		
		 // Get initial positions of all queens and add them to respective queues
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				if(board[i][j] == whiteQueen) {
					whiteQueue.add(new int[]{i, j});
					whiteMinDistance[i][j] = 0;
				}
				else if(board[i][j] == blackQueen) {
					blackQueue.add(new int[]{i, j});
					blackMinDistance[i][j] = 0;
				}
			}
		 }
		
		 // Process white queen positions
		while(!whiteQueue.isEmpty()) {
			int[] whitePosition = whiteQueue.poll();
			int posY = whitePosition[0];
			int posX = whitePosition[1];
			int minDistance = whiteMinDistance[posY][posX] + 1;
			
			boolean leftValid = true; 
			boolean upLeftValid = true;
			boolean upValid = true;
			boolean upRightValid = true;
			boolean rightValid = true;
			boolean downRightValid = true;
			boolean downValid = true;
			boolean downLeftValid = true;
			for(int i = 1; i < 10; i++) {
				// up
				if(upValid && Utility.isSpotValid(board, posY-i, posX)) {
					if(whiteMinDistance[posY-i][posX] > minDistance) {
						whiteMinDistance[posY-i][posX] = minDistance;
						whiteQueue.add(new int[]{posY-i, posX});
					}
				} else upValid = false;
				
				// up left
				if(upLeftValid && Utility.isSpotValid(board, posY-i, posX-i)) {
					if(whiteMinDistance[posY-i][posX-i] > minDistance) {
						whiteMinDistance[posY-i][posX-i] = minDistance;
						whiteQueue.add(new int[]{posY-i, posX-i});
					}
				} else upLeftValid = false;
				
				// left
				if(leftValid && Utility.isSpotValid(board, posY, posX-i)) {
					if(whiteMinDistance[posY][posX-i] > minDistance) {
						whiteMinDistance[posY][posX-i] = minDistance;
						whiteQueue.add(new int[]{posY, posX-i});
					}
				} else leftValid = false;
				
				// down left
				if(downLeftValid && Utility.isSpotValid(board, posY+i, posX-i)) {
					if(whiteMinDistance[posY+i][posX-i] > minDistance) {
						whiteMinDistance[posY+i][posX-i] = minDistance;
						whiteQueue.add(new int[]{posY+i, posX-i});
					}
				} else downLeftValid = false;
				
				// down
				if(downValid && Utility.isSpotValid(board, posY+i, posX)) {
					if(whiteMinDistance[posY+i][posX] > minDistance) {
						whiteMinDistance[posY+i][posX] = minDistance;
						whiteQueue.add(new int[]{posY+i, posX});
					}
				} else downValid = false;
				
				// down right
				if(downRightValid && Utility.isSpotValid(board, posY+i, posX+i)) {
					if(whiteMinDistance[posY+i][posX+i] > minDistance) {
						whiteMinDistance[posY+i][posX+i] = minDistance;
						whiteQueue.add(new int[]{posY+i, posX+i});
					}
				} else downRightValid = false;
				
				// right
				if(rightValid && Utility.isSpotValid(board, posY, posX+i)) {
					if(whiteMinDistance[posY][posX+i] > minDistance) {
						whiteMinDistance[posY][posX+i] = minDistance;
						whiteQueue.add(new int[]{posY, posX+i});
					}
				} else rightValid = false;
				
				// up right
				if(upRightValid && Utility.isSpotValid(board, posY-i, posX+i)) {
					if(whiteMinDistance[posY-i][posX+i] > minDistance) {
						whiteMinDistance[posY-i][posX+i] = minDistance;
						whiteQueue.add(new int[]{posY-i, posX+i});
					}
				} else upRightValid = false;
			}
		}
		
		 // Process black queen positions
		while(!blackQueue.isEmpty()) {
			int[] whitePosition = blackQueue.poll();
			int posY = whitePosition[0];
			int posX = whitePosition[1];
			int minDistance = blackMinDistance[posY][posX] + 1;
			
			boolean leftValid = true; 
			boolean upLeftValid = true;
			boolean upValid = true;
			boolean upRightValid = true;
			boolean rightValid = true;
			boolean downRightValid = true;
			boolean downValid = true;
			boolean downLeftValid = true;
			for(int i = 1; i < 10; i++) {
				// up
				if(upValid && Utility.isSpotValid(board, posY-i, posX)) {
					if(blackMinDistance[posY-i][posX] > minDistance) {
						blackMinDistance[posY-i][posX] = minDistance;
						blackQueue.add(new int[]{posY-i, posX});
					}
				} else upValid = false;
				
				// up left
				if(upLeftValid && Utility.isSpotValid(board, posY-i, posX-i)) {
					if(blackMinDistance[posY-i][posX-i] > minDistance) {
						blackMinDistance[posY-i][posX-i] = minDistance;
						blackQueue.add(new int[]{posY-i, posX-i});
					}
				} else upLeftValid = false;
				
				// left
				if(leftValid && Utility.isSpotValid(board, posY, posX-i)) {
					if(blackMinDistance[posY][posX-i] > minDistance) {
						blackMinDistance[posY][posX-i] = minDistance;
						blackQueue.add(new int[]{posY, posX-i});
					}
				} else leftValid = false;
				
				// down left
				if(downLeftValid && Utility.isSpotValid(board, posY+i, posX-i)) {
					if(blackMinDistance[posY+i][posX-i] > minDistance) {
						blackMinDistance[posY+i][posX-i] = minDistance;
						blackQueue.add(new int[]{posY+i, posX-i});
					}
				} else downLeftValid = false;
				
				// down
				if(downValid && Utility.isSpotValid(board, posY+i, posX)) {
					if(blackMinDistance[posY+i][posX] > minDistance) {
						blackMinDistance[posY+i][posX] = minDistance;
						blackQueue.add(new int[]{posY+i, posX});
					}
				} else downValid = false;
				
				// down right
				if(downRightValid && Utility.isSpotValid(board, posY+i, posX+i)) {
					if(blackMinDistance[posY+i][posX+i] > minDistance) {
						blackMinDistance[posY+i][posX+i] = minDistance;
						blackQueue.add(new int[]{posY+i, posX+i});
					}
				} else downRightValid = false;
				
				// right
				if(rightValid && Utility.isSpotValid(board, posY, posX+i)) {
					if(blackMinDistance[posY][posX+i] > minDistance) {
						blackMinDistance[posY][posX+i] = minDistance;
						blackQueue.add(new int[]{posY, posX+i});
					}
				} else rightValid = false;
				
				// up right
				if(upRightValid && Utility.isSpotValid(board, posY-i, posX+i)) {
					if(blackMinDistance[posY-i][posX+i] > minDistance) {
						blackMinDistance[posY-i][posX+i] = minDistance;
						blackQueue.add(new int[]{posY-i, posX+i});
					}
				} else upRightValid = false;
			}
		}

		double score = 0.0;
		double c1 = 0.0;
		double w = 0.0;
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				c1 += Math.pow(2, -whiteMinDistance[i][j]) - Math.pow(2, -blackMinDistance[i][j]);
				
				if(whiteMinDistance[i][j] != Integer.MAX_VALUE && blackMinDistance[i][j] != Integer.MAX_VALUE) {
					w += Math.pow(2, -Math.abs(whiteMinDistance[i][j] - blackMinDistance[i][j]));
				}
				
				// Calculate score based on queen distances
				if(whiteMinDistance[i][j] < blackMinDistance[i][j]) {
					score += 1.0;
				}
				else if(whiteMinDistance[i][j] > blackMinDistance[i][j]) {
					score -= 1.0;
				}
				else if(whiteMinDistance[i][j] != Integer.MAX_VALUE) {
					if(playerTurn == 1) {
						score += turnAdvantage;
					} else {
						score -= turnAdvantage;
					}
				}
			}
		}
		return new double[]{score, 2.0*c1, w};
	}
	
	// Method to calculate the minimum distance of kings from each position
	private static double[] kingMinDistance(int board[][], int playerTurn) {
		int whiteMinDistance[][] = new int[10][10];
		int blackMinDistance[][] = new int[10][10];
		
		// Initialize distance arrays to infinity
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				whiteMinDistance[i][j] = Integer.MAX_VALUE;
				blackMinDistance[i][j] = Integer.MAX_VALUE;
			}
		}
		
		Queue<int[]> whiteQueue = new LinkedList<>();
		Queue<int[]> blackQueue = new LinkedList<>();
		
		// Get initial positions of all queens and add them to respective queues
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				if(board[i][j] == whiteQueen) {
					whiteQueue.add(new int[]{i, j});
					whiteMinDistance[i][j] = 0;
				}
				else if(board[i][j] == blackQueen) {
					blackQueue.add(new int[]{i, j});
					blackMinDistance[i][j] = 0;
				}
			}
		}
		
		// Process white king positions
		while(!whiteQueue.isEmpty()) {
			int[] whitePosition = whiteQueue.poll();
			int posY = whitePosition[0];
			int posX = whitePosition[1];
			int minDistance = whiteMinDistance[posY][posX] + 1;
			
			// up
			if(Utility.isSpotValid(board, posY-1, posX)) {
				if(whiteMinDistance[posY-1][posX] > minDistance) {
					whiteMinDistance[posY-1][posX] = minDistance;
					whiteQueue.add(new int[]{posY-1, posX});
				}
			}
			
			// up left
			if(Utility.isSpotValid(board, posY-1, posX-1)) {
				if(whiteMinDistance[posY-1][posX-1] > minDistance) {
					whiteMinDistance[posY-1][posX-1] = minDistance;
					whiteQueue.add(new int[]{posY-1, posX-1});
				}
			}
			
			// left
			if(Utility.isSpotValid(board, posY, posX-1)) {
				if(whiteMinDistance[posY][posX-1] > minDistance) {
					whiteMinDistance[posY][posX-1] = minDistance;
					whiteQueue.add(new int[]{posY, posX-1});
				}
			}
			
			// down left
			if(Utility.isSpotValid(board, posY+1, posX-1)) {
				if(whiteMinDistance[posY+1][posX-1] > minDistance) {
					whiteMinDistance[posY+1][posX-1] = minDistance;
					whiteQueue.add(new int[]{posY+1, posX-1});
				}
			}
			
			// down
			if(Utility.isSpotValid(board, posY+1, posX)) {
				if(whiteMinDistance[posY+1][posX] > minDistance) {
					whiteMinDistance[posY+1][posX] = minDistance;
					whiteQueue.add(new int[]{posY+1, posX});
				}
			}
			
			// down right
			if(Utility.isSpotValid(board, posY+1, posX+1)) {
				if(whiteMinDistance[posY+1][posX+1] > minDistance) {
					whiteMinDistance[posY+1][posX+1] = minDistance;
					whiteQueue.add(new int[]{posY+1, posX+1});
				}
			}
			
			// right
			if(Utility.isSpotValid(board, posY, posX+1)) {
				if(whiteMinDistance[posY][posX+1] > minDistance) {
					whiteMinDistance[posY][posX+1] = minDistance;
					whiteQueue.add(new int[]{posY, posX+1});
				}
			}
			
			// up right
			if(Utility.isSpotValid(board, posY-1, posX+1)) {
				if(whiteMinDistance[posY-1][posX+1] > minDistance) {
					whiteMinDistance[posY-1][posX+1] = minDistance;
					whiteQueue.add(new int[]{posY-1, posX+1});
				}
			}
		}
		
		// Process black king positions
		while(!blackQueue.isEmpty()) {
			int[] blackPosition = blackQueue.poll();
			int posY = blackPosition[0];
			int posX = blackPosition[1];
			int minDistance = blackMinDistance[posY][posX] + 1;
			
			// up
			if(Utility.isSpotValid(board, posY-1, posX)) {
				if(blackMinDistance[posY-1][posX] > minDistance) {
					blackMinDistance[posY-1][posX] = minDistance;
					blackQueue.add(new int[]{posY-1, posX});
				}
			}
			
			// up left
			if(Utility.isSpotValid(board, posY-1, posX-1)) {
				if(blackMinDistance[posY-1][posX-1] > minDistance) {
					blackMinDistance[posY-1][posX-1] = minDistance;
					blackQueue.add(new int[]{posY-1, posX-1});
				}
			}
			
			// left
			if(Utility.isSpotValid(board, posY, posX-1)) {
				if(blackMinDistance[posY][posX-1] > minDistance) {
					blackMinDistance[posY][posX-1] = minDistance;
					blackQueue.add(new int[]{posY, posX-1});
				}
			}
			
			// down left
			if(Utility.isSpotValid(board, posY+1, posX-1)) {
				if(blackMinDistance[posY+1][posX-1] > minDistance) {
					blackMinDistance[posY+1][posX-1] = minDistance;
					blackQueue.add(new int[]{posY+1, posX-1});
				}
			}
			
			// down
			if(Utility.isSpotValid(board, posY+1, posX)) {
				if(blackMinDistance[posY+1][posX] > minDistance) {
					blackMinDistance[posY+1][posX] = minDistance;
					blackQueue.add(new int[]{posY+1, posX});
				}
			}
			
			// down right
			if(Utility.isSpotValid(board, posY+1, posX+1)) {
				if(blackMinDistance[posY+1][posX+1] > minDistance) {
					blackMinDistance[posY+1][posX+1] = minDistance;
					blackQueue.add(new int[]{posY+1, posX+1});
				}
			}
			
			// right
			if(Utility.isSpotValid(board, posY, posX+1)) {
				if(blackMinDistance[posY][posX+1] > minDistance) {
					blackMinDistance[posY][posX+1] = minDistance;
					blackQueue.add(new int[]{posY, posX+1});
				}
			}
			
			// up right
			if(Utility.isSpotValid(board, posY-1, posX+1)) {
				if(blackMinDistance[posY-1][posX+1] > minDistance) {
					blackMinDistance[posY-1][posX+1] = minDistance;
					blackQueue.add(new int[]{posY-1, posX+1});
				}
			}
		}
		
		// Calculate score based on king distances
		double score = 0.0;
		double c2 = 0.0;
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				c2 += Math.min(1, Math.max(-1, (blackMinDistance[i][j] - whiteMinDistance[i][j])/6.0));

				if(whiteMinDistance[i][j] < blackMinDistance[i][j]) {
					score += 1.0;
				}
				else if(whiteMinDistance[i][j] > blackMinDistance[i][j]) {
					score -= 1.0;
				}
				else if(whiteMinDistance[i][j] != Integer.MAX_VALUE) {
					if(playerTurn == 1) {
						score += turnAdvantage;
					} else {
						score -= turnAdvantage;
					}
				}
			}
		}
		return new double[]{score, c2};
	}
	
	// Method to evaluate mobility of queens
	private static double getMobilityEval(int[][] board, int[][] mobilityMap, double w) {
		double whiteScore = 0.0;
		double blackScore = 0.0;
		
		Queue<int[]> whiteQueue = new LinkedList<>();
		Queue<int[]> blackQueue = new LinkedList<>();
		
		// Get initial positions of all queens and add them to respective queues
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				if(board[i][j] == whiteQueen) {
					whiteQueue.add(new int[]{i, j});
				}
				else if(board[i][j] == blackQueen) {
					blackQueue.add(new int[]{i, j});
				}
			}
		}
		
		// Process white queen mobility
		while(!whiteQueue.isEmpty()) {
			int[] whitePosition = whiteQueue.poll();
			int posY = whitePosition[0];
			int posX = whitePosition[1];
			
			double queenEval = 0.0;
			
			boolean leftValid = true; 
			boolean upLeftValid = true;
			boolean upValid = true;
			boolean upRightValid = true;
			boolean rightValid = true;
			boolean downRightValid = true;
			boolean downValid = true;
			boolean downLeftValid = true;
			
			for(int i = 1; i < 10; i++) {
				// up
				if(upValid && Utility.isSpotValid(board, posY-i, posX)) {
					queenEval += Math.pow(2, -(i-1)) * mobilityMap[posY-i][posX];
				} else upValid = false;
				
				// up left
				if(upLeftValid && Utility.isSpotValid(board, posY-i, posX-i)) {
					queenEval += Math.pow(2, -(i-1)) * mobilityMap[posY-i][posX-i];
				} else upLeftValid = false;
				
				// left
				if(leftValid && Utility.isSpotValid(board, posY, posX-i)) {
					queenEval += Math.pow(2, -(i-1)) * mobilityMap[posY][posX-i];
				} else leftValid = false;
				
				// down left
				if(downLeftValid && Utility.isSpotValid(board, posY+i, posX-i)) {
					queenEval += Math.pow(2, -(i-1)) * mobilityMap[posY+i][posX-i];
				} else downLeftValid = false;
				
				// down
				// down
				if(downValid && Utility.isSpotValid(board, posY+i, posX)) {
					queenEval += Math.pow(2, -(i-1)) * mobilityMap[posY+i][posX];
				} else downValid = false;
				
				// down right
				if(downRightValid && Utility.isSpotValid(board, posY+i, posX+i)) {
					queenEval += Math.pow(2, -(i-1)) * mobilityMap[posY+i][posX+i];
				} else downRightValid = false;
				
				// right
				if(rightValid && Utility.isSpotValid(board, posY, posX+i)) {
					queenEval += Math.pow(2, -(i-1)) * mobilityMap[posY][posX+i];
				} else rightValid = false;
				
				// up right
				if(upRightValid && Utility.isSpotValid(board, posY-i, posX+i)) {
					queenEval += Math.pow(2, -(i-1)) * mobilityMap[posY-i][posX+i];
				} else upRightValid = false;
				
				whiteScore += f5(w, queenEval);
			}
		}
		
		while(!blackQueue.isEmpty()) {
			int[] blackPosition = blackQueue.poll();
			int posY = blackPosition[0];
			int posX = blackPosition[1];
			
			double queenEval = 0.0;
			
			boolean leftValid = true; 
			boolean upLeftValid = true;
			boolean upValid = true;
			boolean upRightValid = true;
			boolean rightValid = true;
			boolean downRightValid = true;
			boolean downValid = true;
			boolean downLeftValid = true;
			
			for(int i = 1; i < 10; i++) {
				// up
				if(upValid && Utility.isSpotValid(board, posY-i, posX)) {
					queenEval += Math.pow(2, -(i-1)) * mobilityMap[posY-i][posX];
				} else upValid = false;
				
				// up left
				if(upLeftValid && Utility.isSpotValid(board, posY-i, posX-i)) {
					queenEval += Math.pow(2, -(i-1)) * mobilityMap[posY-i][posX-i];
				} else upLeftValid = false;
				
				// left
				if(leftValid && Utility.isSpotValid(board, posY, posX-i)) {
					queenEval += Math.pow(2, -(i-1)) * mobilityMap[posY][posX-i];
				} else leftValid = false;
				
				// down left
				if(downLeftValid && Utility.isSpotValid(board, posY+i, posX-i)) {
					queenEval += Math.pow(2, -(i-1)) * mobilityMap[posY+i][posX-i];
				} else downLeftValid = false;
				
				// down
				if(downValid && Utility.isSpotValid(board, posY+i, posX)) {
					queenEval += Math.pow(2, -(i-1)) * mobilityMap[posY+i][posX];
				} else downValid = false;
				
				// down right
				if(downRightValid && Utility.isSpotValid(board, posY+i, posX+i)) {
					queenEval += Math.pow(2, -(i-1)) * mobilityMap[posY+i][posX+i];
				} else downRightValid = false;
				
				// right
				if(rightValid && Utility.isSpotValid(board, posY, posX+i)) {
					queenEval += Math.pow(2, -(i-1)) * mobilityMap[posY][posX+i];
				} else rightValid = false;
				
				// up right
				if(upRightValid && Utility.isSpotValid(board, posY-i, posX+i)) {
					queenEval += Math.pow(2, -(i-1)) * mobilityMap[posY-i][posX+i];
				} else upRightValid = false;
				
				blackScore += f5(w, queenEval);
			}
		}
		
		return blackScore - whiteScore;
	}
}
