package ubc.cosc322;

public class MonteCarlo {
    long timeLimitMS; // Time limit for the search in milliseconds
    TreeNode root; // Root node of the search tree
    double explorationParameter; // Parameter to balance exploration and exploitation

    // Constructor to initialize the Monte Carlo search with root node, time limit, and exploration parameter
    MonteCarlo(TreeNode root, long timeLimitMS, double explorationParameter) {
        this.root = root;
        this.timeLimitMS = timeLimitMS;
        this.explorationParameter = explorationParameter;
    }

    // Method to search the tree and find the best action
    public Actions searchTree(){
        TreeNode.maxTreeDepth = 0; // Reset the maximum tree depth
        long currentTime = System.currentTimeMillis(); // Get the current time
        int iterations = 0; // Initialize the iteration counter
        for(long startTime = System.currentTimeMillis(); currentTime - startTime < timeLimitMS; currentTime = System.currentTimeMillis()){
            TreeNode childNode = TraverseNode(root); // Traverse the tree to find a node to expand
            double result;
            if(childNode.isTerminal()){ // Check if the node is a terminal node
                result = 1; // Terminal node result
            }else{
                childNode = childNode.expandAtRandom(); // Expand the node at random
                result = simulateHeuristic(childNode); // Simulate the heuristic result
            }
            iterations++; // Increment the iteration counter
            backTrack(childNode, result); // Backtrack and update the tree with the result
        }

        System.out.println("Iterations: " + iterations); // Print the number of iterations

        Actions bestAction = null; // Initialize the best action
        double bestWinRate = -10000; // Initialize the best win rate
        for(TreeNode T: root.childNodes){ // Iterate through the child nodes of the root
            double winRate = 0;
            double A  = T.totalReward; // Total reward of the child node
            double B = T.visitCount; // Visit count of the child node
            if(A!=0){
                winRate = A/B; // Calculate the win rate
            }
            if(winRate > bestWinRate){ // Check if the win rate is better than the best win rate
                bestWinRate = winRate; // Update the best win rate
                bestAction = T.moveTaken; // Update the best action
            }
        }
        return bestAction; // Return the best action
    }

    // Method to traverse the tree and find the best node to expand
    public TreeNode TraverseNode(TreeNode node){
        if(!node.hasExpandedChildren() && node.hasExpandedChildren()){
            double maxConfidence = -1; // Initialize the maximum confidence
            TreeNode bestChild = null; // Initialize the best child node
            for(TreeNode N : node.childNodes){ // Iterate through the child nodes
                double currentConfidence = N.getUCB(explorationParameter); // Calculate the UCB value
                if(currentConfidence > maxConfidence){ // Check if the current confidence is better than the maximum confidence
                    maxConfidence = currentConfidence; // Update the maximum confidence
                    bestChild = N; // Update the best child node
                }
            }
            return TraverseNode(bestChild); // Recursively traverse the best child node
        }
        return node; // Return the node if it has no expanded children
    }

    // Method to simulate a game from a given node
    public int simulate(TreeNode start){
        TreeNode currentNode = new TreeNode(start); // Create a new node from the start node
        while(true){
            if(currentNode.isTerminal()){ // Check if the current node is a terminal node
                if(currentNode.getPlayerColor() == start.getPlayerColor()){ // Check if the player color matches the start node
                    return 1; // Return 1 if the player wins
                } else {
                    return 0; // Return 0 if the player loses
                }
            }
            currentNode  = currentNode.expandAtRandom(); // Expand the current node at random
        }
    }

    // Method to simulate a game with a heuristic evaluation
    public double simulateHeuristic(TreeNode node){
        double heuristic = Evaluator.getHeuristicEval(node.boardState, node.getPlayerColor()); // Get the heuristic evaluation
        double result = Utility.sigmoid(heuristic); // Calculate the sigmoid of the heuristic
        if(node.getPlayerColor() == 1){
            return 1 - result; // Return the result for player 1
        }else{
            return result; // Return the result for player 2
        }
    }

    // Method to backtrack and update the tree with the simulation result
    public void backTrack(TreeNode childNode, double result){
        childNode.visitCount++; // Increment the visit count of the child node
        childNode.totalReward += result; // Add the result to the total reward of the child node
        if(childNode.parentNode != null){
            backTrack(childNode.parentNode, 1 - result); // Recursively backtrack to the parent node
        }
    }

    // Method to update the tree from a given action
    public void upDateFromAction(Actions action){
        this.root.expand(); // Expand the root node
        boolean discovered = false; // Initialize the discovered flag
        for(TreeNode N : root.childNodes){ // Iterate through the child nodes of the root
            if(N.moveTaken.equals(action)){ // Check if the move matches the action
                root = N; // Update the root node
                root.parentNode = null; // Set the parent node to null
                discovered = true; // Set the discovered flag to true
                break;
            }
        }
        if(!discovered){ // If the action was not discovered
            int newColor;
            if(root.playerColor == 2){
                newColor = 1; // Set the new color to 1 if the current color is 2
            }else{
                newColor = 2; // Set the new color to 2 if the current color is 1
            }
            int[][][] afterIllegalState = Actions.performAction(action, root.boardState); // Perform the action and get the new board state
            root = new TreeNode(afterIllegalState, newColor); // Create a new root node with the new board state and color
        }
    }
}
