package ubc.cosc322;

import java.util.ArrayList;

public class Queens extends Tiles {
    private int previousRow, previousColumn; // Previous position coordinates
    private int shotX, shotY;   // Arrow shot coordinates
    private boolean opponent;   // pponent status

    public Queens(int xCoord, int yCoord, boolean opponent) {
        super(xCoord, yCoord); // coordinates for current position
        this.previousRow = xCoord;
        this.previousColumn = yCoord;
        this.opponent = opponent;
    }

    public int getPriorX() {
        return previousRow;
    }

    public void setPriorX(int previousRow) {
        this.previousRow = previousRow;
    }

    public int getPriorY() {
        return previousColumn;
    }

    public void setPriorY(int previousColumn) {
        this.previousColumn = previousColumn;
    }

    public int getShotX() {
        return this.shotX;
    }

    public void setShotX(int shotX) {
        this.shotX = shotX;
    }

    public int getShotY() {
        return this.shotY;
    }

    public void setShotY(int shotY) {
        this.shotY = shotY;
    }

    public boolean isOpponent() {
        return opponent;
    }

    public ArrayList<Integer> selectOptimalShot(GameState gameState) {
        int[][] evalGrid = new int[10][10]; // Scored board for evaluation

        // Penalize player's positions, reward opponent's
        for (Queens ally : gameState.getPlayerQueens(1)) { // player 1
            evalGrid[ally.getRow()][ally.getColumn()] = -3;
        }
        for (Queens enemy : gameState.getPlayerQueens(2)) {
            evalGrid[enemy.getRow()][enemy.getColumn()] = 3;
        }

        // Scores for each position
        for (int iter = 0; iter < 3; iter++) {
            int pointValue;
            for (int xCoord = 0; xCoord < 10; xCoord++) {
                for (int yCoord = 0; yCoord < 10; yCoord++) {
                    if (evalGrid[xCoord][yCoord] == 0) {
                        pointValue = 0;
                        if (xCoord > 0 && evalGrid[xCoord - 1][yCoord] == 3) pointValue += 3;
                        if (xCoord < 9 && evalGrid[xCoord + 1][yCoord] == 3) pointValue += 3;
                        if (yCoord > 0 && evalGrid[xCoord][yCoord - 1] == 3) pointValue += 3;
                        if (yCoord < 9 && evalGrid[xCoord][yCoord + 1] == 3) pointValue += 3;
                        if (pointValue != 0) pointValue -= pointValue / Math.abs(pointValue);
                        evalGrid[xCoord][yCoord] = pointValue;
                    }
                }
            }
        }

        Moves moveGenerator = new Moves(); // new Moves instance when implemented
        moveGenerator.availableArrows(gameState.getBoardState(), this);
        ArrayList<Integer> bestShot = new ArrayList<>();
        int topScore = -99;

        for (ArrayList<Integer> option : moveGenerator.arrowShots) {
            int newX = this.getRow() + option.get(0);
            int newY = this.getColumn() + option.get(1);
            int shotValue = evalGrid[newX][newY];
            if (shotValue > topScore) {
                bestShot.clear();
                bestShot.add(newX - this.getRow());
                bestShot.add(newY - this.getColumn());
                topScore = shotValue;
            }
        }
        return bestShot;
    }

    public void setPosition(int newX, int newY) {
        this.previousRow = this.getRow();
        this.previousColumn = this.getColumn();
        setRow(newX);
        setColumn(newY);
    }
}