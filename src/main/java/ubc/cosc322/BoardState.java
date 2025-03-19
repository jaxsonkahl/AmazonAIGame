package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;

import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;

public class BoardState {
    Tiles[][] boardState; // 2D array representing the board
    Queens[] player; // Array of player's queens
    Queens[] enemy; // Array of enemy's queens
    Queens current; // Currently selected queen
    ArrayList<ArrayList<Integer>> makeMove; // List to store moves

    // Constructor to initialize the board state
    public BoardState(boolean isBlack) {
        if(!isBlack) {
            // Initialize board for white player
            boardState = new Tiles[][] 
            {
                {null,null,null,new Queens(0,3,false),null,null,new Queens(0,6,false),null,null,null},
                {null,null,null,null,null,null,null,null,null,null},
                {null,null,null,null,null,null,null,null,null,null},
                {new Queens(3,0,false),null,null,null,null,null,null,null,null,new Queens(3,9,false)},
                {null,null,null,null,null,null,null,null,null,null},
                {null,null,null,null,null,null,null,null,null,null},
                {new Queens(6,0,true),null,null,null,null,null,null,null,null,new Queens(6,9,true)},
                {null,null,null,null,null,null,null,null,null,null},
                {null,null,null,null,null,null,null,null,null,null},
                {null,null,null,new Queens(9,3,true),null,null,new Queens(9,6,true),null,null,null}
            };
            player = new Queens[] {(Queens) boardState[0][3],(Queens) boardState[0][6],(Queens) boardState[3][0],(Queens) boardState[3][9]};
            enemy = new Queens[] {(Queens) boardState[6][0],(Queens) boardState[6][9],(Queens) boardState[9][3],(Queens) boardState[9][6]};
        }
        else {
            // Initialize board for black player
            boardState = new Tiles[][] 
            {
                {null,null,null,new Queens(0,3,true),null,null,new Queens(0,6,true),null,null,null},
                {null,null,null,null,null,null,null,null,null,null},
                {null,null,null,null,null,null,null,null,null,null},
                {new Queens(3,0,true),null,null,null,null,null,null,null,null,new Queens(3,9,true)},
                {null,null,null,null,null,null,null,null,null,null},
                {null,null,null,null,null,null,null,null,null,null},
                {new Queens(6,0,false),null,null,null,null,null,null,null,null,new Queens(6,9,false)},
                {null,null,null,null,null,null,null,null,null,null},
                {null,null,null,null,null,null,null,null,null,null},
                {null,null,null,new Queens(9,3,false),null,null,new Queens(9,6,false),null,null,null}
            };
            player = new Queens[] {(Queens) boardState[6][0],(Queens) boardState[6][9],(Queens) boardState[9][3],(Queens) boardState[9][6]};
            enemy = new Queens[] {(Queens) boardState[0][3],(Queens) boardState[0][6],(Queens) boardState[3][0],(Queens) boardState[3][9]};
        }
    }

    // Constructor to create a new board state from an old one
    public BoardState(BoardState oldBoard, boolean opponent) {
        this.boardState = new Tiles[10][10];
        this.enemy = new Queens[4];
        this.player = new Queens[4];
        for(int row = 0; row < 10; row++) {
            for(int col = 0; col < 10; col++) {
                if(oldBoard.boardState[row][col] instanceof Queens) {
                    Queens oldQueen = (Queens) oldBoard.boardState[row][col];
                    Queens newQueen = new Queens(row, col, oldQueen.isOpponent());
                    this.boardState[row][col] = newQueen;
                    if(oldQueen.isOpponent()) {
                        for(int i = 0; i < enemy.length; i++) {
                            if(enemy[i] == null) {
                                enemy[i] = newQueen;
                                break;
                            }
                        }
                    } else {
                        for(int i = 0; i < player.length; i++) {
                            if(player[i] == null) {
                                player[i] = newQueen;
                                break;
                            }
                        }
                    }
                } else if(oldBoard.boardState[row][col] instanceof Arrows) {
                    this.boardState[row][col] = new Arrows(row, col);
                }
            }
        }
        if(opponent) {
            Queens[] temp = player;
            player = enemy;
            enemy = temp;
        }
    }

    // Update the board with new queen and arrow positions
    public void updateBoard(ArrayList<Integer> queenPrevPos, ArrayList<Integer> queenNewPos, ArrayList<Integer> arrowPos) {
        updateBoard(queenPrevPos, queenNewPos);
        updateBoard(arrowPos);
    }

    // Update the board with new queen positions
    public void updateBoard(ArrayList<Integer> queenPrevPos, ArrayList<Integer> queenNewPos) {
        int prevRow = queenPrevPos.get(0)-1, prevCol = queenPrevPos.get(1)-1, 
            newRow = queenNewPos.get(0)-1, newCol = queenNewPos.get(1)-1;
        setSelected((Queens) boardState[prevRow][prevCol]);
        getSelected().setPriorX(prevRow);
        getSelected().setPriorY(prevCol);
        getSelected().setRow(newRow);
        getSelected().setColumn(newCol);
        boardState[newRow][newCol] = getSelected();
        boardState[prevRow][prevCol] = null;
    }

    // Update the board with new arrow positions
    public void updateBoard(ArrayList<Integer> arrowPos) {
        int arrRow = arrowPos.get(0)-1, arrCol = arrowPos.get(1)-1;
        getSelected().setShotX(arrRow);
        getSelected().setShotY(arrCol);
        boardState[arrRow][arrCol] = new Arrows(arrRow,arrCol);
    }

    // Generate a random move for the player or enemy
    public ArrayList<ArrayList<Integer>> randomMove(boolean enemy) {
        ArrayList<Integer> queenPrevPos;
        ArrayList<Integer> queenNewPos;
        ArrayList<Integer> arrowPos;
        if(enemy) {
            // Check if the game is over for the enemy
            if(this.gameOverCheck(true) != 1) {
                // Get all possible actions for each enemy queen
                for(Queens queen: this.enemy) {
                    queen.actions.getActions(this,queen);
                }
                // Select a random enemy queen with available moves
                current = this.enemy[(int) (Math.random()*4)];
                while(current.actions.moves.size()==0) {
                    current = this.enemy[(int) (Math.random()*4)];
                }
                // Select a random move for the selected queen
                ArrayList<Integer> action = current.actions.moves.get((int) (Math.random()*current.actions.moves.size()));
                queenPrevPos = new ArrayList<Integer>();
                queenPrevPos.add(current.getRow()+1); queenPrevPos.add(current.getColumn()+1);
                queenNewPos = new ArrayList<Integer>();
                queenNewPos.add(current.getRow()+action.get(0)+1); queenNewPos.add(current.getColumn()+action.get(1)+1);
                // Update the board with the new queen position
                updateBoard(queenPrevPos, queenNewPos);
                // Get all possible arrow shots for the selected queen
                current.actions.availableArrows(this, current);
                // Select a random arrow shot
                ArrayList<Integer> arrowShot = current.actions.arrowShots.get((int) (Math.random()*current.actions.arrowShots.size()));
                arrowPos = new ArrayList<Integer>();
                arrowPos.add(current.getRow()+arrowShot.get(0)+1); arrowPos.add(current.getColumn()+arrowShot.get(1)+1);
                // Update the board with the new arrow position
                updateBoard(arrowPos);
                // Store the move
                makeMove = new ArrayList<>();
                makeMove.add(queenPrevPos); 
                makeMove.add(queenNewPos); 
                makeMove.add(arrowPos);
                return makeMove;
            }
        }
        else {
            // Check if the game is over for the player
            if(this.gameOverCheck(false) != 0) {
                // Get all possible actions for each player queen
                for(Queens queen: this.player) {
                    queen.actions.getActions(this,queen);
                }
                // Select a random player queen with available moves
                current = this.player[(int) (Math.random()*4)];
                while(current.actions.moves.size()==0) {
                    current = this.player[(int) (Math.random()*4)];
                }
                // Select a random move for the selected queen
                ArrayList<Integer> action = current.actions.moves.get((int) (Math.random()*current.actions.moves.size()));
                queenPrevPos = new ArrayList<Integer>();
                queenPrevPos.add(current.getRow()+1); queenPrevPos.add(current.getColumn()+1);
                queenNewPos = new ArrayList<Integer>();
                queenNewPos.add(current.getRow()+action.get(0)+1); queenNewPos.add(current.getColumn()+action.get(1)+1);
                // Update the board with the new queen position
                updateBoard(queenPrevPos, queenNewPos);
                // Get all possible arrow shots for the selected queen
                current.actions.availableArrows(this, current);
                // Select the optimal arrow shot
                ArrayList<Integer> arrowShot = current.selectOptimalShot(this);
                arrowPos = new ArrayList<Integer>();
                arrowPos.add(current.getRow()+arrowShot.get(0)+1); arrowPos.add(current.getColumn()+arrowShot.get(1)+1);
                // Update the board with the new arrow position
                updateBoard(arrowPos);
                // Store the move
                makeMove = new ArrayList<>();
                makeMove.add(queenPrevPos); makeMove.add(queenNewPos); makeMove.add(arrowPos);
                return makeMove;
            }
        }
        // If no move is possible, return null moves
        makeMove = new ArrayList<>();
        makeMove.add(null); 
        makeMove.add(null); 
        makeMove.add(null);
        return makeMove;
    }

    // Check if the game is over
    public int gameOverCheck(boolean opponent) {
        if(opponent) {
            // Get all possible actions for each enemy queen
            for(Queens queen: this.enemy) {
                queen.actions.getActions(this,queen);
            }
            // Check if all enemy queens have no available moves
            if(this.enemy[0].actions.moves.size() == 0 && this.enemy[1].actions.moves.size() == 0 
            && this.enemy[2].actions.moves.size() == 0 && this.enemy[3].actions.moves.size() == 0) {
                return 1; // Enemy loses
            }
        }
        else {
            // Get all possible actions for each player queen
            for(Queens queen: this.player) {
                queen.actions.getActions(this,queen);
            }
            // Check if all player queens have no available moves
            if(this.player[0].actions.moves.size() == 0 && this.player[1].actions.moves.size() == 0 
            && this.player[2].actions.moves.size() == 0 && this.player[3].actions.moves.size() == 0) {
                return 0; // Player loses
            }
        }
        return -1; // Game is not over
    }

    // Evaluate the board and return a score
    public int evaluateBoard() {
        int score = 0;
        // Calculate score based on the number of available moves for each player queen
        for(int i = 0; i < player.length; i ++) {
            player[i].actions.getActions(this, player[i]);
            score += player[i].actions.moves.size();
        }
        // Subtract score based on the number of available moves for each enemy queen
        for(int i = 0; i < enemy.length; i ++) {
            enemy[i].actions.getActions(this, enemy[i]);
            score -= enemy[i].actions.moves.size();
        }
        return score;
    }

    // Check if any player's queen is in danger
    public boolean inDanger() {
        for(int i=0; i<player.length; i++) {
            // See cells around player[i] to check if it is in danger
            int queenRow = player[i].getRow();
            int queenCol = player[i].getColumn();
            
            // Make a list of empty positions
            ArrayList<Tiles> emptyPositions = new ArrayList<>();
            // Checks bottom 3 tiles
            for(int j=-1; j<1; j++) {
                if(queenRow+1 < 10 && queenCol+j >= 0 && queenCol+j < 10 &&
                    boardState[queenRow+1][queenCol+j] == null) {
                    emptyPositions.add(new Tiles(queenRow+1,queenCol+j));
                }
            }
            // Checks top 3 tiles
            for(int j=-1; j<1; j++) {
                if(queenRow-1 >= 0 && queenCol+j >= 0 && queenCol+j < 10 &&
                    boardState[queenRow-1][queenCol+j] == null) {
                    emptyPositions.add(new Tiles(queenRow-1,queenCol+j));
                }
            }
            // Checks right tile
            if(queenCol+1 < 10 && boardState[queenRow][queenCol+1] == null) {
                emptyPositions.add(new Tiles(queenRow,queenCol+1));
            }
            // Checks left tile
            if(queenCol-1 >= 0 && boardState[queenRow][queenCol-1] == null) {
                emptyPositions.add(new Tiles(queenRow,queenCol-1));
            }
            
            // Check if the queen is surrounded by one or two empty positions
            if (emptyPositions.size() == 1) {
                return true;
            }else if (emptyPositions.size() == 2) {
                if(emptyPositions.get(0).getRow() == emptyPositions.get(1).getRow() &&
                        Math.abs(emptyPositions.get(0).getColumn()-emptyPositions.get(1).getColumn()) == 1) {
                    return true;
                } else if(emptyPositions.get(0).getColumn() == emptyPositions.get(1).getColumn() &&
                        Math.abs(emptyPositions.get(0).getRow()-emptyPositions.get(1).getRow()) == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    // Print the last move made
    public void printMove() {
        System.out.println("QCurr: "+ makeMove.get(0).toString());
        System.out.println("QNew: "+ makeMove.get(1).toString());
        System.out.println("Arrow: "+ makeMove.get(2).toString());
    }

    // Print the current board state
    public void printBoard() {
        for(int r=9; r>=0; --r) {
            for(int c=0;c<10;++c) {
                if(boardState[r][c] instanceof Queens)System.out.print(" Q");
                else if(boardState[r][c] instanceof Arrows)System.out.print(" A");
                else System.out.print(" N");
            }
            System.out.println();
        }
    }

    // Get the currently selected queen
    public Queens getSelected() {
        return this.current;
    }

    // Set the currently selected queen
    public void setSelected(Queens current) {
        this.current = current;
    }

}
