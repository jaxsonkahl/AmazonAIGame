package ubc.cosc322;
import java.util.Stack;

public class MiniMax {
    private Node rootNode; // Root node of the tree
    private int depth; // Depth of the tree

    // Constructor to create a MiniMax tree with a root node and depth
    public MiniMax(Node rootNode, int depth){
        this.rootNode = rootNode;
        this.depth = 0;
    }

    // Method to expand the tree to a specified depth
    public void expand(Node currentNode, int toDepth){
        // If depth is 0, stop expanding the tree
        if(toDepth == 0) return;
        Node child;
        // Loop to create child nodes
        for(int i = 0; i < currentNode.getBalanceFactor(); i++){
            // Alternate between opponent and player moves
            if(toDepth % 2 == 1){
                child = new Node(new BoardState(currentNode.getBoardState(), true), true);
                child.getBoardState().randomMove(true);
            } else {
                child = new Node(new BoardState(currentNode.getBoardState(), false), false);
                child.getBoardState().randomMove(false);
            }
            // If the child node already exists, skip it
            if(currentNode.getChildren().contains(child)){
                continue;
            } else {
                // Add the child node and expand the tree further
                addChild(currentNode, child);
                expand(child, toDepth - 1);
            }
        }
        // Increase the depth of the tree
        this.depth++;
    }

    // Method to add a child node to a parent node
    public void addChild(Node parent, Node child){
        parent.addChild(child);
        child.setParent(parent);
    }

    // Method to search for the best route from the current node
    public Stack<Node> searchRoute(Node currentNode){
        Node tmp = currentNode;
        Stack<Node> route = new Stack<>();
        // Traverse the tree to find the best route
        while(tmp.getChildren().size() != 0){
            tmp = MiniMaxSearch(tmp);
            if(tmp.getGameOver() == 1){
                break;
            }
        }
        // Add nodes to the route stack
        while(!tmp.equals(currentNode)){
            route.add(tmp);
            tmp = tmp.getParent();
        }
        return route;
    }

    // Method to perform MiniMax search and find the optimal node
    public Node MiniMaxSearch(Node currentNode){
        Node optimalNode = currentNode.getChildren().get(0);
        int num = 0;
        // Loop through the children to find the optimal node
        for(int i = 0; i < currentNode.getChildren().size(); i++){
            int value = 0;
            // Check if the node is in danger
            if(currentNode.getChildren().get(i).getBoardState().inDanger()){
                value = -1000;
            }
            // Check if the game is over
            if(currentNode.getChildren().get(i).getGameOver() == 1){
                optimalNode = currentNode.getChildren().get(i);
                break;
            } else if(optimalNode.getBoardState().evaluateBoard() + num < currentNode.getChildren().get(i).getBoardState().evaluateBoard() + value){
                optimalNode = currentNode.getChildren().get(i);
                num = value;
            }
        }
        return optimalNode;
    }
}
