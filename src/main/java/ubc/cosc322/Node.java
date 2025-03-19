package ubc.cosc322;

import java.util.ArrayList;

public class Node {
    private BoardState boardState; // The state of the board
    private boolean opponent; // Whether this node is for the opponent
    private ArrayList<Node> children; // List of child nodes
    private Node parent; // Parent node
    private int balanceFactor = 10; // Balance factor for the node
    private int gameOver; // Game over status

    // Constructor to create a new node
    public Node(BoardState boardState, boolean opponent){
        // Create a new board state for this node
        this.boardState = new BoardState(boardState, opponent);
        // Set whether this node is for the opponent
        this.opponent = opponent;
        // Check if the game is over for this node
        this.gameOver = this.getBoardState().gameOverCheck(opponent);
        // Initialize the list of child nodes
        this.children = new ArrayList<>();
        // Set the parent node to null
        this.parent = null;
    }

    // Get the board state of this node
    public BoardState getBoardState(){
        return this.boardState;
    }

    // Get whether this node is for the opponent
    public boolean getOpponent(){
        return this.opponent;
    }

    // Set the parent node
    public void setParent(Node parent){
        this.parent = parent;
    }

    // Get the parent node
    public Node getParent() {
        return this.parent;
    }

    // Set the game over status
    public void setGameOver(int gameOver) {
        this.gameOver = gameOver;
    }

    // Get the game over status
    public int getGameOver() {
        return this.gameOver;
    }

    // Add a child node
    public void addChild(Node child) {
        this.children.add(child);
    }

    // Get a specific child node
    public Node getChild(int child) {
        return this.children.get(child);
    }

    // Get the list of child nodes
    public ArrayList<Node> getChildren() {
        return this.children;
    }

    // Get the balance factor of this node
    public int getBalanceFactor() {
        return this.balanceFactor;
    }
}
