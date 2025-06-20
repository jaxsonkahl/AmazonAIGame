# AmazonAIGame

## Project Overview  
**AmazonAIGame** implements an intelligent agent to play the Game of Amazons — a two-player abstract strategy game focused on movement and territorial control. The agent leverages **Monte Carlo Tree Search (MCTS)** combined with a **custom heuristic evaluation** to make strategic decisions in real time.

---

## System Architecture  
The AI agent is composed of several core components:

- **`COSC322Test.java`**  
  Main entry point. Extends `GamePlayer`, manages server communication, initialization, and decision-making via the `MonteCarlo` class.

- **`MonteCarlo.java`**  
  Implements the core **MCTS algorithm**, including game tree traversal, expansion, heuristic simulation, and backpropagation.

- **`TreeNode.java`**  
  Represents individual nodes in the game tree. Stores the game state, legal actions, and MCTS statistics (visit count, value, etc.).

- **`Evaluator.java`**  
  Replaces random playouts with a **heuristic scoring function** to improve simulation quality.

- **`ActionFactory.java` / `Actions.java`**  
  Handle **legal move generation**, board transitions, and action validation.

- **`Utility.java`**  
  Offers helper functions such as action map generation and move validation.

---

## Game Tree Search: MCTS Strategy  
The AI uses **Monte Carlo Tree Search** with the following phases:

1. **Selection**  
   Chooses the most promising node using **UCB1 (Upper Confidence Bound)**.

2. **Expansion**  
   Randomly selects and expands one unvisited child node from the current node.

3. **Simulation**  
   Executes a fast, **heuristic-based rollout** instead of a random playout.

4. **Backpropagation**  
   Propagates the simulation result up the tree to update parent nodes.

This process runs within a fixed time limit (`allowedTimeMs`), refining the decision with each iteration.

---

## Heuristic Evaluation Function  
To improve simulation performance and move quality, the agent evaluates board states using a domain-specific heuristic:

- **Territory Control**  
  Measures proximity to regions dominated by each player's queens.

- **Mobility**  
  Scores states based on available legal moves using a precomputed **action map**.

- **Dynamic Weighting**  
  Combines multiple factors (`f1–f5`) that adapt based on the game phase and board control.

This hybrid approach prioritizes both **spatial dominance** and **strategic queen positioning**.

---

## Design Highlights & Performance Insights

- **Random Expansion**  
  Limits branching factor and simplifies expansion during MCTS.

- **Heuristic Rollouts**  
  Speeds up simulations and produces **more consistent, informed results**.

- **Tree Depth Tracking**  
  Logs maximum tree depth and iteration count during gameplay for analysis and debugging.

---

## Limitations & Future Improvements

- **Endgame Tactics**  
  Could benefit from depth-limited search or advanced pruning strategies.

- **Heuristic Speed**  
  Optimizing evaluation functions or introducing caching may reduce rollout time.

- **Parallelism**  
  Current implementation is single-threaded. Parallel rollouts could significantly improve simulation count and move quality.
