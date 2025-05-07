package org.dataTransfer.server.ServerModell;

import org.dataTransfer.server.ServerModell.player.Player;
import java.util.List;


/**
 * Interface representing a game session.
 */
public interface GameIF {

    /**
     * Gets the hostname of the game.
     * @return The game host name.
     */
    String getGameHostName();

    /**
     * Sets the hostname of the game.
     * @param gameHostName The game host name.
     */
    void setGameHostName(String gameHostName);

    /**
     * Gets the maximum number of players allowed in the game.
     * @return The maximum number of players.
     */
    int getMaxPlayersNumber();

    /**
     * Sets the maximum number of players allowed in the game.
     * @param maxPlayersNumber The maximum number of players.
     */
    void setMaxPlayersNumber(int maxPlayersNumber);

    /**
     * Checks if all players are ready to start the game.
     * @return True if all players are ready, otherwise false.
     */
    boolean isAllPlayersAreReady();

    /**
     * Sets the readiness status of all players.
     * @param allPlayersAreReady True if all players are ready, otherwise false.
     */
    void setAllPlayersAreReady(boolean allPlayersAreReady);

    /**
     * Gets the username of the current player whose turn it is.
     * @return The username of the current player.
     */
    String getCurrentPlayerUsername();

    /**
     * Checks if the game has started.
     * @return True if the game has started, otherwise false.
     */
    boolean isGameStarted();

    /**
     * Sets whether the game has started.
     * @param gameStarted True if the game has started, otherwise false.
     */
    void setGameStarted(boolean gameStarted);

    /**
     * Gets the current game state as a string.
     * @return The game state.
     */
    String getGameState();

    /**
     * Sets the current game state.
     * @param gameState The new game state.
     */
    void setGameState(String gameState);

    /**
     * Gets the list of players in the game.
     * @return The list of players.
     */
    List<Player> getPlayers();

    /**
     * Gets the current number of players in the game.
     * @return The current player count.
     */
    int getCurrentPlayersCount();

    /**
     * Gets the index of the current player in the turn order.
     * @return The index of the current player.
     */
    int getCurrentPlayerIndex();

    /**
     * Rolls the dice and proceeds to the next player's turn.
     * @return The result of the dice roll.
     */
    int rollDiceAndNextTurn();
}
