package org.dataTransfer.server.ServerModell;

/**
 * Interface representing a Dice.
 */
public interface DiceIF {

    /**
     * Rolls a six-sided dice and returns the result.
     *
     * @return A random number between 1 and 6.
     */
    static int rollDice() {
        return (int) (Math.random() * 6 + 1);
    }
}
