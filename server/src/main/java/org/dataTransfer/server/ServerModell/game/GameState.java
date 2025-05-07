package org.dataTransfer.server.ServerModell.game;

/**
 * Das Enum {@code GameState} definiert die möglichen Zustände eines Spiels.
 * Es wird verwendet, um den aktuellen Zustand der Spielansicht anzugeben.
 *
 * <ul>
 *     <li>{@code GAMEVIEW} – Der Zustand, in dem das eigentliche Spiel angezeigt wird.</li>
 *     <li>{@code LOBBYVIEW} – Der Zustand, in dem sich die Spieler in der Lobby befinden, bevor das Spiel startet.</li>
 * </ul>
 */
public enum GameState {
    GAMEVIEW,
    LOBBYVIEW
}
