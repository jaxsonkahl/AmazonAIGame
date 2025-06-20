# AmazonAIGame

### 1. Project Overview
This project implements an intelligent AI agent to play the Game of Amazons, a two-player board game requiring strategic movement and spatial control. The AI agent was built using Monte Carlo Tree Search (MCTS) combined with a custom heuristic evaluation function to compete in the game environment effectively.

### 2. System Architecture
The agent is composed of the following components:
COSC322Test.java: Main class extending GamePlayer, manages initialization, server communication, and move decision-making using the MonteCarlo class.
MonteCarlo.java: Core logic for Monte Carlo Tree Search (MCTS), which performs game-tree traversal, expansion, simulation (rollout), and backpropagation.
TreeNode.java: Represents game-tree nodes, holds game state, possible actions, and MCTS stats (visit count, reward, etc.).
Evaluator.java: Implements the heuristic evaluation function used during simulations to replace random rollouts with informed scoring
ActionFactory.java / Actions.java: Handle legal move generation and board state transitions.
Utility.java: Provides helper functions such as generating action maps and validating movements.
### 3. Game Tree Search – MCTS
The AI uses Monte Carlo Tree Search (MCTS) with the following structure:
Selection: UCB1 (Upper Confidence Bound) guides traversal to the most promising child node.
Expansion: One unexplored move is randomly chosen and expanded from the selected node.
Simulation: Instead of random playouts, the agent evaluates the resulting state using a custom heuristic.
Backpropagation: Simulation results are propagated back to update statistics.

The algorithm iterates for a fixed time (allowedTimeMs), improving its decision as more simulations are performed.

### 4. Heuristic Evaluation
To improve rollout quality, the Evaluator uses a domain-specific heuristic:
Territory Control: Measures distance to regions controlled by the player's queens.
Mobility: Evaluates freedom of movement using an action map (via Utility).
Dynamic Weighting: Weight functions (f1-f5) adapt scoring based on game phase and control.
This hybrid approach balances spatial dominance with queen positioning to select effective moves.

### 5. Design Choices and Performance
Random Expansion reduces branching during MCTS.
Custom Rollouts replace randomness with faster, consistent scoring.
Depth Tracking allows debugging and analysis of tree exploration.
The agent prints the number of iterations and tree depth during gameplay for performance insights.
### 6. Limitations and Future Work
Endgame Tactics: Could be improved with better pruning or depth-limited search.
Board Evaluation Speed: More efficient heuristics or caching could reduce simulation time.
Parallelism: Current implementation is single-threaded; parallel rollouts could greatly improve iteration count.
