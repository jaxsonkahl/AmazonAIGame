package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;
import ygraph.ai.smartfox.games.amazons.HumanPlayer;

/**
 * An example illustrating how to implement a GamePlayer for the Amazons game.
 * This class handles game initialization, communication with the server, and
 * decision-making for moves.
 * 
 * @author Yong Gao (yong.gao@ubc.ca) Jan 5, 2021
 */
public class COSC322Test extends GamePlayer {

	// Game client for communication with the server
	private GameClient gameClient = null;

	// GUI for visualizing the game
	private BaseGameGUI gamegui = null;

	// User credentials
	private String userName = null;
	private String passwd = null;

	// Monte Carlo Tree Search instance for AI decision-making
	MonteCarlo mc;

	// Constants for game pieces
	private int whiteQueen = 1;
	private int blackQueen = 2;
	private int arrow = 3; // Arrow marker on the board

	// Variables to track the player's and opponent's queens
	private int myQueen = -1;
	private int opponentQueen = -1;

	/**
	 * The main method to start the game.
	 * 
	 * @param args Command-line arguments for username and password.
	 */
	public static void main(String[] args) {
		String uname = "COSC322_GROUP_9"; // Default username
		if (args.length > 0) {
			uname = args[0];
		}
		COSC322Test player = new COSC322Test(uname, "pass");

		// Initialize the game GUI and start the game
		if (player.getGameGUI() == null) {
			player.Go();
		} else {
			BaseGameGUI.sys_setup();
			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
					player.Go();
				}
			});
		}
	}

	/**
	 * Constructor to initialize the player with a username and password.
	 * 
	 * @param userName Username for the player.
	 * @param passwd   Password for the player.
	 */
	public COSC322Test(String userName, String passwd) {
		this.userName = userName;
		this.passwd = passwd;

		// Initialize the GUI for the game
		this.gamegui = new BaseGameGUI(this);
	}

	/**
	 * Called when the player successfully logs in to the server.
	 */
	@Override
	public void onLogin() {
		userName = gameClient.getUserName();
		if (gamegui != null) {
			gamegui.setRoomInformation(gameClient.getRoomList());
		}
	}

	/**
	 * Handles game-related messages received from the server.
	 * 
	 * @param messageType Type of the message.
	 * @param msgDetails  Details of the message.
	 * @return true if the message was handled successfully.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
		switch (messageType) {
			case GameMessage.GAME_STATE_BOARD:
				// Handle the initial game state
				ArrayList<Integer> stateArr = (ArrayList<Integer>) (msgDetails.get(AmazonsGameMessage.GAME_STATE));
				this.getGameGUI().setGameState(stateArr);
				this.mc = null;
				System.out.println("GAME_STATE_BOARD");
				break;

			case GameMessage.GAME_ACTION_MOVE:
				// Handle opponent's move
				ArrayList<Integer> queenPosCurr = (ArrayList<Integer>) (msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR));
				ArrayList<Integer> queenPosNext = (ArrayList<Integer>) (msgDetails.get(AmazonsGameMessage.QUEEN_POS_NEXT));
				ArrayList<Integer> arrowPos = (ArrayList<Integer>) (msgDetails.get(AmazonsGameMessage.ARROW_POS));
				ApplyOpponentMove(queenPosCurr, queenPosNext, arrowPos);
				if (this.mc != null) {
					MakeMove();
				}
				break;

			case GameMessage.GAME_ACTION_START:
				// Handle the start of the game
				String playingWhiteQueens = (String) msgDetails.get(AmazonsGameMessage.PLAYER_WHITE);
				String playingBlackQueens = (String) msgDetails.get(AmazonsGameMessage.PLAYER_BLACK);
				SetMyQueen(playingWhiteQueens, playingBlackQueens);
				InitalizeBoard();
				if (this.myQueen == this.blackQueen) {
					MakeMove(); // Make the first move if playing as black
				}
				break;

			default:
				assert (false); // Unexpected message type
				break;
		}
		return true;
	}

	/**
	 * Sets the player's queen type (white or black) based on the game start message.
	 * 
	 * @param playingWhiteQueens Username of the player with white queens.
	 * @param playingBlackQueens Username of the player with black queens.
	 */
	public void SetMyQueen(String playingWhiteQueens, String playingBlackQueens) {
		assert (!playingWhiteQueens.equals(playingBlackQueens));
		if (this.userName().equals(playingWhiteQueens)) {
			this.myQueen = this.whiteQueen;
			this.opponentQueen = this.blackQueen;
		} else if (this.userName().equals(playingBlackQueens)) {
			this.myQueen = this.blackQueen;
			this.opponentQueen = this.whiteQueen;
		} else {
			System.out.println("Fatal error, invalid queen value received " + this.myQueen + ", please restart");
			assert (false);
		}
		System.out.println("SetMyQueen " + this.myQueen);
	}

	/**
	 * Makes a move using the Monte Carlo Tree Search algorithm.
	 */
	public void MakeMove() {
		assert (this.mc.root.getPlayerColor() == this.myQueen);

		Actions action = this.mc.MCTS();
		if (action != null) {
			// Prepare the move details
			ArrayList<Integer> aiQueenPosCurr = new ArrayList<Integer>();
			aiQueenPosCurr.add(action.queenSpotY + 1);
			aiQueenPosCurr.add(action.queenSpotX + 1);

			ArrayList<Integer> aiQueenPosNext = new ArrayList<Integer>();
			aiQueenPosNext.add(action.queenDestinationY + 1);
			aiQueenPosNext.add(action.queenDestinationX + 1);

			ArrayList<Integer> aiArrowPos = new ArrayList<Integer>();
			aiArrowPos.add(action.arrowDestinationY + 1);
			aiArrowPos.add(action.arrowDestinationX + 1);

			// Print the AI's move details
			System.out.println("\n|My Move:");
			System.out.println("|Queen moved from (" + (action.queenSpotY + 1) + ", " + (action.queenSpotX + 1) + ") to (" +
					(action.queenDestinationY + 1) + ", " + (action.queenDestinationX + 1) + ")");
			System.out.println("|Arrow shot to (" + (action.arrowDestinationY + 1) + ", " + (action.arrowDestinationX + 1) + ")\n");

			// Update the game state and send the move to the server
			this.getGameGUI().updateGameState(aiQueenPosCurr, aiQueenPosNext, aiArrowPos);
			this.getGameClient().sendMoveMessage(aiQueenPosCurr, aiQueenPosNext, aiArrowPos);
			this.mc.rootFromAction(action);
		} else {
			// No valid moves available, the player loses
			System.out.println("YOU LOST!");
		}
	}

	/**
	 * Applies the opponent's move to the game state.
	 * 
	 * @param queenPosCurr Current position of the opponent's queen.
	 * @param queenPosNext New position of the opponent's queen.
	 * @param arrowPos     Position of the arrow shot by the opponent.
	 */
	public void ApplyOpponentMove(ArrayList<Integer> queenPosCurr, ArrayList<Integer> queenPosNext, ArrayList<Integer> arrowPos) {
		Actions action = new Actions(queenPosCurr.get(1) - 1, queenPosCurr.get(0) - 1, queenPosNext.get(1) - 1, queenPosNext.get(0) - 1, arrowPos.get(1) - 1, arrowPos.get(0) - 1);
		
		// Print the opponent's move details
		System.out.println("\n|Opponent Move:");
		System.out.println("|Queen moved from (" + queenPosCurr.get(0) + ", " + queenPosCurr.get(1) + ") to (" +
				queenPosNext.get(0) + ", " + queenPosNext.get(1) + ")");
		System.out.println("|Arrow shot to (" + arrowPos.get(0) + ", " + arrowPos.get(1) + ")\n");
	
		
		this.getGameGUI().updateGameState(queenPosCurr, queenPosNext, arrowPos);
		if (this.mc != null) {
			this.mc.rootFromAction(action);
		}
	}

	/**
	 * Initializes the game board with the starting positions of the queens.
	 */
	public void InitalizeBoard() {
		System.out.println("Initializing board");

		int[][][] state = new int[2][10][10];

		// Hardcoded initial positions of the queens
		state[0][0][3] = this.whiteQueen;
		state[0][0][6] = this.whiteQueen;
		state[0][3][0] = this.whiteQueen;
		state[0][3][9] = this.whiteQueen;
		state[0][6][0] = this.blackQueen;
		state[0][6][9] = this.blackQueen;
		state[0][9][3] = this.blackQueen;
		state[0][9][6] = this.blackQueen;

		// Generate the action map for the initial state
		state[1] = Utility.getActionMap(state[0]);

		// Initialize the Monte Carlo Tree Search with the starting state
		this.mc = new MonteCarlo(new TreeNode(state, this.blackQueen), 29000, 1.4);
	}

	/**
	 * @return The username of the player.
	 */
	@Override
	public String userName() {
		return userName;
	}

	/**
	 * @return The game client instance.
	 */
	@Override
	public GameClient getGameClient() {
		return this.gameClient;
	}

	/**
	 * @return The game GUI instance.
	 */
	@Override
	public BaseGameGUI getGameGUI() {
		return this.gamegui;
	}

	/**
	 * Connects the player to the game server.
	 */
	@Override
	public void connect() {
		gameClient = new GameClient(userName, passwd, this);
	}

} // end of class