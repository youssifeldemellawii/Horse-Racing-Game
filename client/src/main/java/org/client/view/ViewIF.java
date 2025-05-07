package org.client.view;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Das Interface ViewIF definiert die Methoden, die von den verschiedenen Ansichten (Game, Lobby und Start) genutzt werden.
 * Es dient als zentrale Schnittstelle für die Interaktion zwischen Controller und UI.
 */
public interface ViewIF {

    /**
     * Gibt die Spielansicht zurück.
     *
     * @return die GameView
     */
    GameView getGameView();

    /**
     * Wechselt zur Ansicht, die durch den übergebenen Namen spezifiziert wird.
     *
     * @param name der Name der anzuzeigenden Ansicht
     */
    void toggleView(String name);

    /**
     * Gibt die Lobby-Ansicht zurück.
     *
     * @return die LobbyView
     */
    LobbyView getLobbyView();

    /**
     * Gibt die Start-Ansicht zurück.
     *
     * @return die StartView
     */
    StartView getStartView();

    /**
     * Gibt den Namen des Spielers zurück.
     *
     * @return der Spielername als String
     */
    String getPlayerName();

    /**
     * Setzt einen ActionListener für den "Erstellen"-Button.
     *
     * @param Listener der zu setzende ActionListener
     */
    void setCreateButtonListener(ActionListener Listener);

    /**
     * Fügt einen allgemeinen ActionListener hinzu.
     *
     * @param listener der hinzuzufügende ActionListener
     */
    void addActionListener(ActionListener listener);

    /**
     * Entfernt den gesetzten ActionListener.
     */
    void removeActionListener();

    /**
     * Setzt den Benutzernamen.
     *
     * @param username der Benutzername
     */
    void setUsername(String username);

    /**
     * Zeigt den Spielernamen an.
     *
     * @return der angezeigte Spielername
     */
    String showPlayerName();

    /**
     * Aktualisiert den angezeigten Würfelwert.
     */
    void updateDiceValue();

    /**
     * Aktualisiert die Positionen der Spielfiguren.
     */
    void updateFiguresPositions();

    /**
     * Aktualisiert den Zustand des Spielzugs.
     */
    void updateTurnState();

    /**
     * Schnittstelle für die Lobby-Ansicht.
     */
    interface LobbyViewIF {
        /**
         * Setzt einen ActionListener für den "Bereit"-Button.
         *
         * @param listener der zu setzende ActionListener
         */
        void setReadyButtonListener(ActionListener listener);

        /**
         * Setzt einen ActionListener für den "Start"-Button.
         *
         * @param listener der zu setzende ActionListener
         */
        void setStartButtonListener(ActionListener listener);

        /**
         * Setzt einen ActionListener für den "Exit"-Button in der Lobby.
         *
         * @param listener der zu setzende ActionListener
         */
        void setExitButtonListener(ActionListener listener);

        /**
         * Gibt den Textbereich zurück, der die Liste der beigetretenen Spieler anzeigt.
         *
         * @return der JTextPane mit den Spielernamen
         */
        JTextPane getJoinedPlayersArea();

        /**
         * Gibt das Label zurück, das die Spiel-ID anzeigt.
         *
         * @return das JLabel mit der Spiel-ID
         */
        JLabel getGameID();

        /**
         * Setzt das Label, das die Spiel-ID anzeigt.
         *
         * @param gameID das JLabel, das die Spiel-ID enthalten soll
         */
        void setGameID(JLabel gameID);

        /**
         * Gibt den "Bereit"-Button zurück.
         *
         * @return der JButton für den Bereitschaftsstatus
         */
        JButton getReadyButton();

        /**
         * Gibt den "Start"-Button zurück.
         *
         * @return der JButton zum Starten des Spiels
         */
        JButton getStartButton();
    }

    /**
     * Schnittstelle für die Start-Ansicht.
     */
    interface StartViewIF {
        /**
         * Gibt den eingegebenen Spielernamen zurück.
         *
         * @return der Spielername als String
         */
        String getPlayerName();

        /**
         * Setzt einen ActionListener für den "Erstellen"-Button in der Startansicht.
         *
         * @param Listener der zu setzende ActionListener
         */
        void setCreateButtonListener(ActionListener Listener);

        /**
         * Setzt einen ActionListener für den "Beitreten"-Button in der Startansicht.
         *
         * @param listener der zu setzende ActionListener
         */
        void setJoinButtonListener(ActionListener listener);

        /**
         * Gibt das Label zurück, das die Spiel-ID anzeigt.
         *
         * @return das JLabel mit der Spiel-ID
         */
        JLabel getGameID();

        /**
         * Gibt das Textfeld zurück, in das die Spiel-ID eingegeben wird.
         *
         * @return das JTextField für die Spiel-ID
         */
        JTextField getJoinField();

        /**
         * Gibt den "Beitreten"-Button zurück.
         *
         * @return der JButton zum Beitreten eines Spiels
         */
        JButton getJoinButton();

        /**
         * Gibt den "Erstellen"-Button zurück.
         *
         * @return der JButton zum Erstellen eines Spiels
         */
        JButton getCreateButton();

        /**
         * Gibt das Textfeld zurück, in dem der Spielername eingegeben wird.
         *
         * @return das JTextField für den Spielernamen
         */
        JTextField getPlayerNameField();
    }

    /**
     * Schnittstelle für die Spielansicht.
     */
    interface GameViewIF {
        /**
         * Setzt einen ActionListener für den "Exit"-Button in der Spielansicht.
         *
         * @param listener der zu setzende ActionListener
         */
        void setExitButtonListener(ActionListener listener);

        /**
         * Löscht einen bestimmten Abschnitt im Spielfeld.
         *
         * @param fieldIndex   der Index des Feldes, das gelöscht werden soll
         * @param sectionIndex der Index des Bereichs innerhalb des Feldes
         */
        void clearSection(int fieldIndex, int sectionIndex);

        /**
         * Verschiebt die Spielfigur eines Spielers an die angegebene Position.
         *
         * @param fieldIndex  der Ziel-Feldindex
         * @param playerIndex der Index des Spielers
         * @param playerName  der Name des Spielers
         */
        void movePlayerPosition(int fieldIndex, int playerIndex, String playerName);

        /**
         * Gibt den "Würfeln"-Button zurück.
         *
         * @return der JButton zum Auslösen eines Würfelwurfs
         */
        JButton getRollDiceButton();

        /**
         * Gibt den Textbereich zurück, der den Würfelwert anzeigt.
         *
         * @return der JTextPane für die Anzeige des Würfelwerts
         */
        JTextPane getDiceDisplayLabel();

        /**
         * Gibt das Label zurück, das den Würfelwert anzeigt.
         *
         * @return das JLabel für die Anzeige des Würfelwerts
         */
        JLabel getDiceDisplay();

        /**
         * Gibt den Bereich zurück, in dem der Punktestand angezeigt wird.
         *
         * @return der JTextPane für die Anzeige des Punktestandes
         */
        JTextPane getScoreArea();
    }
}
