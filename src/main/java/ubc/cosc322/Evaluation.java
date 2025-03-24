package ubc.cosc322;

import java.util.LinkedList;
import java.util.Queue;

public class Heuristic {
	private static int WHITE_PIECE = 1;
    private static int BLACK_PIECE = 2;

    private static double TURN_BONUS = 0.15;

	public static double evaluateState(int[][][] gameState, int currentPlayer) {
		int[][] gameBoard = gameState[0];
		int[][] moveMap = gameState[1];

		double[] queenStats = calculateQueenInfluence(gameBoard, currentPlayer);
		double queenScore = queenStats[0];
		double kingScore = 0.0;

		double queenControl = queenStats[1];
		double kingControl = 0.0;

		double sharedControl = queenStats[2];

		double mobilityScore = 0.0;

		if(sharedControl > 10.0) {
			double[] kingStats = calculateKingInfluence(gameBoard, currentPlayer);
			kingScore = kingStats[0];
			kingControl = kingStats[1];
			mobilityScore = calculateMobility(gameBoard, moveMap, sharedControl);
		} else {
			sharedControl = 0.0;
		}

		double territoryScore = weight1(sharedControl)*queenScore + weight2(sharedControl)*queenControl + weight3(sharedControl)*kingScore + weight4(sharedControl)*kingControl;

		return territoryScore + mobilityScore;
	}

	private static double weight1(double controlWeight) {
		return (100.0 - controlWeight) / 100.0;
	}

	private static double weight2(double controlWeight) {
		return (1.0 - weight1(controlWeight)) / 4.0;
	}

	private static double weight3(double controlWeight) {
		return (1.0 - weight1(controlWeight)) / 4.0;
	}

	private static double weight4(double controlWeight) {
		return (1.0 - weight1(controlWeight)) / 4.0;
	}

	private static double weight5(double controlWeight, double mobilityFactor) {
		return controlWeight * Math.pow(1.2, -mobilityFactor) / 45.0;
	}

	private static double[] calculateQueenInfluence(int[][] board, int turnPlayer) {
		int[][] whiteReach = new int[10][10];
		int[][] blackReach = new int[10][10];

		for(int y = 0; y < 10; y++) {
			for(int x = 0; x < 10; x++) {
				whiteReach[y][x] = Integer.MAX_VALUE;
				blackReach[y][x] = Integer.MAX_VALUE;
			}
		}

		Queue<int[]> whiteQueue = new LinkedList<>();
		Queue<int[]> blackQueue = new LinkedList<>();

		for(int y = 0; y < 10; y++) {
			for(int x = 0; x < 10; x++) {
				if(board[y][x] == WHITE_PIECE) {
					whiteQueue.add(new int[]{y, x});
					whiteReach[y][x] = 0;
				} else if(board[y][x] == BLACK_PIECE) {
					blackQueue.add(new int[]{y, x});
					blackReach[y][x] = 0;
				}
			}
		}

		floodFillReach(board, whiteQueue, whiteReach);
		floodFillReach(board, blackQueue, blackReach);

		double queenScore = 0.0;
		double controlDifference = 0.0;
		double weightedReach = 0.0;

		for(int y = 0; y < 10; y++) {
			for(int x = 0; x < 10; x++) {
				controlDifference += Math.pow(2, -whiteReach[y][x]) - Math.pow(2, -blackReach[y][x]);
				if(whiteReach[y][x] != Integer.MAX_VALUE && blackReach[y][x] != Integer.MAX_VALUE) {
					weightedReach += Math.pow(2, -Math.abs(whiteReach[y][x] - blackReach[y][x]));
				}
				if(whiteReach[y][x] < blackReach[y][x]) {
					queenScore += 1.0;
				} else if(whiteReach[y][x] > blackReach[y][x]) {
					queenScore -= 1.0;
				} else if(whiteReach[y][x] != Integer.MAX_VALUE) {
					queenScore += (turnPlayer == 1) ? TURN_BONUS : -TURN_BONUS;
				}
			}
		}
		return new double[]{queenScore, 2.0 * controlDifference, weightedReach};
	}

	private static void floodFillReach(int[][] board, Queue<int[]> queue, int[][] reachMap) {
		while(!queue.isEmpty()) {
			int[] pos = queue.poll();
			int y = pos[0];
			int x = pos[1];
			int distance = reachMap[y][x] + 1;

			int[][] directions = {{-1,0}, {-1,-1}, {0,-1}, {1,-1}, {1,0}, {1,1}, {0,1}, {-1,1}};
			boolean[] validDirs = new boolean[8];
			for(int d = 0; d < 8; d++) validDirs[d] = true;

			for(int step = 1; step < 10; step++) {
				for(int d = 0; d < 8; d++) {
					if(!validDirs[d]) continue;
					int newY = y + step * directions[d][0];
					int newX = x + step * directions[d][1];
					if(Utility.isSpotValid(board, newY, newX)) {
						if(reachMap[newY][newX] > distance) {
							reachMap[newY][newX] = distance;
							queue.add(new int[]{newY, newX});
						}
					} else {
						validDirs[d] = false;
					}
				}
			}
		}
	}

	private static double[] calculateKingInfluence(int[][] board, int turnPlayer) {
		int[][] whiteReach = new int[10][10];
		int[][] blackReach = new int[10][10];

		for(int y = 0; y < 10; y++) {
			for(int x = 0; x < 10; x++) {
				whiteReach[y][x] = Integer.MAX_VALUE;
				blackReach[y][x] = Integer.MAX_VALUE;
			}
		}

		Queue<int[]> whiteQueue = new LinkedList<>();
		Queue<int[]> blackQueue = new LinkedList<>();

		for(int y = 0; y < 10; y++) {
			for(int x = 0; x < 10; x++) {
				if(board[y][x] == WHITE_PIECE) {
					whiteQueue.add(new int[]{y, x});
					whiteReach[y][x] = 0;
				} else if(board[y][x] == BLACK_PIECE) {
					blackQueue.add(new int[]{y, x});
					blackReach[y][x] = 0;
				}
			}
		}

		calculateOneStepReach(board, whiteQueue, whiteReach);
		calculateOneStepReach(board, blackQueue, blackReach);

		double kingScore = 0.0;
		double controlScore = 0.0;
		for(int y = 0; y < 10; y++) {
			for(int x = 0; x < 10; x++) {
				controlScore += Math.min(1, Math.max(-1, (blackReach[y][x] - whiteReach[y][x]) / 6.0));
				if(whiteReach[y][x] < blackReach[y][x]) {
					kingScore += 1.0;
				} else if(whiteReach[y][x] > blackReach[y][x]) {
					kingScore -= 1.0;
				} else if(whiteReach[y][x] != Integer.MAX_VALUE) {
					kingScore += (turnPlayer == 1) ? TURN_BONUS : -TURN_BONUS;
				}
			}
		}
		return new double[]{kingScore, controlScore};
	}

	private static void calculateOneStepReach(int[][] board, Queue<int[]> queue, int[][] reachMap) {
		int[][] directions = {{-1,0}, {-1,-1}, {0,-1}, {1,-1}, {1,0}, {1,1}, {0,1}, {-1,1}};
		while(!queue.isEmpty()) {
			int[] pos = queue.poll();
			int y = pos[0];
			int x = pos[1];
			int distance = reachMap[y][x] + 1;
			for(int[] dir : directions) {
				int newY = y + dir[0];
				int newX = x + dir[1];
				if(Utility.isSpotValid(board, newY, newX)) {
					if(reachMap[newY][newX] > distance) {
						reachMap[newY][newX] = distance;
						queue.add(new int[]{newY, newX});
					}
				}
			}
		}
	}

	private static double calculateMobility(int[][] board, int[][] mobilityMap, double sharedWeight) {
		double whiteMobility = 0.0;
		double blackMobility = 0.0;

		Queue<int[]> whiteUnits = new LinkedList<>();
		Queue<int[]> blackUnits = new LinkedList<>();

		for(int y = 0; y < 10; y++) {
			for(int x = 0; x < 10; x++) {
				if(board[y][x] == WHITE_PIECE) {
					whiteUnits.add(new int[]{y, x});
				} else if(board[y][x] == BLACK_PIECE) {
					blackUnits.add(new int[]{y, x});
				}
			}
		}

		whiteMobility = sumMobilityScore(board, mobilityMap, sharedWeight, whiteUnits);
		blackMobility = sumMobilityScore(board, mobilityMap, sharedWeight, blackUnits);

		return blackMobility - whiteMobility;
	}

	private static double sumMobilityScore(int[][] board, int[][] mobilityMap, double controlWeight, Queue<int[]> unitQueue) {
		double totalScore = 0.0;
		int[][] directions = {{-1,0}, {-1,-1}, {0,-1}, {1,-1}, {1,0}, {1,1}, {0,1}, {-1,1}};
		while(!unitQueue.isEmpty()) {
			int[] pos = unitQueue.poll();
			int y = pos[0];
			int x = pos[1];
			double mobilityFactor = 0.0;
			boolean[] validDirs = new boolean[8];
			for(int d = 0; d < 8; d++) validDirs[d] = true;

			for(int step = 1; step < 10; step++) {
				for(int d = 0; d < 8; d++) {
					if(!validDirs[d]) continue;
					int newY = y + step * directions[d][0];
					int newX = x + step * directions[d][1];
					if(Utility.isSpotValid(board, newY, newX)) {
						mobilityFactor += Math.pow(2, -(step - 1)) * mobilityMap[newY][newX];
					} else {
						validDirs[d] = false;
					}
				}
			}
			totalScore += weight5(controlWeight, mobilityFactor);
		}
		return totalScore;
	}
}
