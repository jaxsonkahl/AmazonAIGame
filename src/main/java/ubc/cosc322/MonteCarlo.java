package ubc.cosc322;

public class MonteCarlo {	
	long allowedTimeMs;
	TreeNode root;
	double explorationCoefficient;

	
	MonteCarlo(TreeNode root, long allowedTimeMs, double explorationCoefficient){
		this.root = root;
		this.allowedTimeMs = allowedTimeMs;
		this.explorationCoefficient = explorationCoefficient;
	}
	
	// performs an MCTS from the current root and returns the best action
	public Actions MCTS() {
		TreeNode.maxTreeDepth = 0;
		long currentTime = System.currentTimeMillis();
		int iterations = 0;
		for(long startTime = System.currentTimeMillis(); currentTime - startTime < allowedTimeMs; currentTime = System.currentTimeMillis()) {
			TreeNode leaf = traverse(root);
			double result;
			if(leaf.isTerminal()) {
				result = 1;
			} else {
				leaf = leaf.expandAtRandom();
				result = heuristicRollout(leaf);
			}
			iterations++;
			backpropogate(leaf, result);
		}
		System.out.println(iterations + " iterations were run");

		// returns an action based on child with highest winrate
		Actions bestAction = null;
		double bestWinrate = -10000;
		for (TreeNode t : root.childNodes) {
			double winrate = 0;
			double Q = t.totalReward;
			double N = t.visitCount;
			if (N != 0) {
				winrate = Q / N;
			}
			if (winrate > bestWinrate) {
				bestWinrate = winrate;
				bestAction = t.moveTaken;
			}
		}
		return bestAction;
	}

	public TreeNode traverse(TreeNode node) {
		// if the node is not a leaf node, traverse to its best child
		if (!node.hasUnexpandedChildren() && node.hasExpandedChildren()) {
			// get the child with the highest UCB score
			double maxUCB = -1; // all UCB values will be >=0
			TreeNode bestChild = null;
			for (TreeNode n : node.childNodes) {
				double currentUCB = n.getUCB(explorationCoefficient);
				if (currentUCB > maxUCB) {
					maxUCB = currentUCB;
					bestChild = n;
				}
			}
			// traverse recursively
			return traverse(bestChild);
		}
		return node;
	}
	
	//playerColor is the color of the player we desire to win, so the root node's color will be passed in
	public int rollout(TreeNode start) {
		TreeNode currentNode = new TreeNode(start); //copy start node
		while(true) {	
			if(currentNode.isTerminal()) { //if a node is terminal, the current color loses
				if(currentNode.getPlayerColor() == start.getPlayerColor()) return 0;
				else return 1;
			}
			
			//expand that child and continue looping
			currentNode = currentNode.expandAtRandom();
		}
	}
	
	public double heuristicRollout(TreeNode node) {
		double heuristicResult = Evaluator.getHeuristicEval(node.boardState, node.getPlayerColor());
		double result = Utility.sigmoid(heuristicResult);
		
		if(node.getPlayerColor() == 1) {
			return 1 - result;
		} else {
			return result;
		}
	}
	
	public void backpropogate(TreeNode leaf, double result) {
		leaf.visitCount++; leaf.totalReward += result;
		if(leaf.parentNode != null) {
			backpropogate(leaf.parentNode, 1 - result);
		}
	}
	
	public void rootFromAction(Actions a) {
		this.root.expand();
		boolean found = false;
		for(TreeNode n: root.childNodes) {
			if(n.moveTaken.Equal(a)) {
				root = n;
				root.parentNode = null;
				found = true;
				break;
			}
		}
		if(!found) {
			//RECORD THE ILLEGAL MOVE STUFF
			int newColor;
			if(root.playerColor == 2) {
				newColor = 1;
			} else {
				newColor = 2;
			}
			int[][][] postCheatState = Actions.performAction(a, root.boardState);
			root = new TreeNode(postCheatState, newColor);
		}

	}
	
}
