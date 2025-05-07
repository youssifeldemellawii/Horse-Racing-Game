package org.dataTransfer.server.ServerModell;

/**
 * Interface representing a player in the game.
 */
public interface PlayerIF {

    /**
     * Gets the index of the player in the game.
     * @return The player index.
     */
    int getPlayerIndex();

    /**
     * Sets the index of the player in the game.
     * @param playerIndex The player index.
     */
    void setPlayerIndex(int playerIndex);

    /**
     * Gets the name of the player.
     * @return The player's name.
     */
    String getName();

    /**
     * Checks if the player is ready.
     * @return True if the player is ready, otherwise false.
     */
    boolean isReady();

    /**
     * Sets the player's readiness status.
     * @param ready True if the player is ready, otherwise false.
     */
    void setReady(boolean ready);

    /**
     * Gets the player's current position in the game.
     * @return The player's position.
     */
    int getPostion();

    /**
     * Sets the player's current position in the game.
     * @param postion The new position of the player.
     */
    void setPostion(int postion);

}
