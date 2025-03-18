package ubc.cosc322;

import java.util.ArrayList;

public class Queens extends Tiles {
    private int previousRow, previousColumn; // Previous position coordinates
    private int shotX, shotY;   // Arrow shot coordinates
    private boolean opponent;   // opponent status\
    public Actions actions;


    // Constructor to initialize the queen's position and opponent status
    public Queens(int xCoord, int yCoord, boolean opponent) {
        super(xCoord, yCoord); // coordinates for current position
        this.previousRow = xCoord;
        this.previousColumn = yCoord;
        this.opponent = opponent;
        this.actions = new Actions(); 
    }

    // Getter and setter for previous row position
    public int getPriorX() {
        return previousRow;
    }

    public void setPriorX(int previousRow) {
        this.previousRow = previousRow;
    }

    // Getter and setter for previous column position
    public int getPriorY() {
        return previousColumn;
    }

    public void setPriorY(int previousColumn) {
        this.previousColumn = previousColumn;
    }

    // Getter and setter for shot X coordinate
    public int getShotX() {
        return this.shotX;
    }

    public void setShotX(int shotX) {
        this.shotX = shotX;
    }

    // Getter and setter for shot Y coordinate
    public int getShotY() {
        return this.shotY;
    }

    public void setShotY(int shotY) {
        this.shotY = shotY;
    }

    // Check if the queen is an opponent
    public boolean isOpponent() {
        return opponent;
    }

    public ArrayList<Integer> selectOptimalShot(BoardState board){
        int[][] evalGrid = new int[10][10];
        for(int i = 0; i < board.player.length; i++) {
            evalGrid[board.player[i].getRow()][board.player[i].getColumn()] = -3;
        }
        for(int i = 0; i < board.enemy.length; i++) {
            evalGrid[board.enemy[i].getRow()][board.enemy[i].getColumn()] = 3;
        }

        for(int i = 0; i < 3; i++){
            int score;
            for(int row = 0; row < 10; row++) {
                for(int col = 0; col < 10; col++) {
                    if(evalGrid[row][col] == 0) {
                    	score = 0;
                        if(row > 0) {
                        	if(evalGrid[row-1][col] == 3) {
                        		score += evalGrid[row-1][col];
                        	} 
                        }
                        if(row < 9) {
                        	if(evalGrid[row+1][col] == 3) {
                        		score += evalGrid[row+1][col];
                        	} 
                        }
                        if(col > 0) {
                        	if(evalGrid[row][col-1] == 3) {
                        		score += evalGrid[row][col-1];
                        	} 
                        }
                        if(col < 9) {
                        	if(evalGrid[row][col+1] == 3) {
                        		score += evalGrid[row][col+1];
                        	} 
                        }
                        if(score != 0) {
                            score -= score / Math.abs(score);
                        }
                        evalGrid[row][col] = score;
                    }
                }
            }
        }

        this.actions.availableArrows(board, this);
        ArrayList<Integer> bestMove = new ArrayList<Integer>();
        int bestScore = -99;

        for(int i = 0; i < this.actions.arrowShots.size(); i++) {
            int moveScore = evalGrid[this.actions.arrowShots.get(i).get(0) + this.getRow()][this.actions.arrowShots.get(i).get(1) + this.getColumn()];
            if(moveScore > bestScore) {
                bestMove = this.actions.arrowShots.get(i);
                bestScore = moveScore;
            }
        }
        return bestMove;

    }
 
}