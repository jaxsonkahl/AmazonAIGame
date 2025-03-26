package ubc.cosc322;

import java.util.ArrayList;

public class TreeNode {
    public static int maxTreeDepth = 0; 
    int nodeDepth;                 

    int playerColor;              
    double totalReward;             
    int visitCount;           
    int[][][] boardState;           
    Actions moveTaken;               
    TreeNode parentNode;          
    ArrayList<TreeNode> childNodes; 
    ArrayList<Actions> availableMoves; 
    boolean nodeExpanded;            
    boolean movesGenerated;       

    // Constructor for creating the root node of the Monte Carlo tree search
    TreeNode(int[][][] boardState, int playerColor) {
        this.childNodes = new ArrayList<>();
        this.moveTaken = null;
        this.boardState = boardState;
        this.parentNode = null;
        this.playerColor = playerColor;
        this.visitCount = 0;
        this.totalReward = 0;
        this.nodeExpanded = false;
        this.movesGenerated = false;
        this.nodeDepth = 0;
    }

    // Constructor for creating child nodes
    TreeNode(int[][][] boardState, TreeNode parentNode, Actions moveTaken) {
        this.boardState = boardState;
        this.parentNode = parentNode;
        // Alternate player's color based on parent's color
        this.playerColor = (parentNode.playerColor == 2) ? 1 : 2;
        this.visitCount = 0;
        this.totalReward = 0;
        this.nodeExpanded = false;
        this.movesGenerated = false;
        this.childNodes = new ArrayList<>();
        this.moveTaken = moveTaken;
        this.nodeDepth = parentNode.nodeDepth + 1;
        if (this.nodeDepth > maxTreeDepth) {
            maxTreeDepth = this.nodeDepth;
            System.out.println("Depth: " + maxTreeDepth);
        }
    }

    // Constructor to make a copy of a node for rollout purposes
    TreeNode(TreeNode nodeToCopy) {
        this.boardState = nodeToCopy.boardState;
        this.availableMoves = nodeToCopy.availableMoves;
        this.playerColor = nodeToCopy.playerColor;
        this.nodeExpanded = false;
        this.movesGenerated = nodeToCopy.movesGenerated;
        this.childNodes = new ArrayList<>();
    }

    public double getUCB(double explorationCoefficient) {
        // If this node hasn't been visited yet, return a high value
        if (visitCount == 0) return 10000.0;
        return totalReward / visitCount + explorationCoefficient * Math.sqrt(Math.log(parentNode.visitCount) / visitCount);
    }

    // Generate a child node given a move that leads to a new state
    public TreeNode generateChild(Actions actionMove) {
        // Adjusted to use the static performAction method (if that's what you intend)
        TreeNode child = new TreeNode(Actions.performAction(actionMove, this.boardState), this, actionMove);
        this.childNodes.add(child);
        return child;
    }

    private void generateMoves() {
        this.availableMoves = ActionFactory.getActions(this.boardState, this.playerColor);
        this.movesGenerated = true;
    }

    // Expands the current node by generating all possible child nodes
    public void expand() {
        if (!this.movesGenerated) {
            this.generateMoves();
        }
        for (Actions act : this.availableMoves) {
            this.childNodes.add(new TreeNode(Actions.performAction(act, this.boardState), this, act));
        }
        this.availableMoves.clear();
        this.nodeExpanded = true;
    }

    // Expands a random move for rollout traversal
    public TreeNode expandAtRandom() {
        if (!this.movesGenerated) {
            this.generateMoves();
        }
        int randomIndex = (int) (Math.random() * this.getNumPossibleMoves());
        Actions randomMove = this.availableMoves.get(randomIndex);
        this.availableMoves.remove(randomIndex);
        if (this.availableMoves.isEmpty()) {
            this.nodeExpanded = true;
        }
        return generateChild(randomMove);
    }

    // Checks if the node is terminal (no available moves and no children)
    public boolean isTerminal() {
        if (!this.movesGenerated) {
            this.generateMoves();
        }
        return this.availableMoves.isEmpty() && this.childNodes.isEmpty();
    }

    public boolean hasUnexpandedChildren() {
        if (!this.movesGenerated) {
            this.generateMoves();
        }
        return !this.availableMoves.isEmpty();
    }

    public boolean hasExpandedChildren() {
        return !this.childNodes.isEmpty();
    }

    public int getPlayerColor() {
        return this.playerColor;
    }

    public int getNumPossibleMoves() {
        if (!this.movesGenerated) {
            this.generateMoves();
        }
        return this.availableMoves.size();
    }

    public void displayBoard() {
        Utility.printBoard(this.boardState[0]);
    }
}