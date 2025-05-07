package org.client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Die Klasse LobbyView repräsentiert die Lobby-Ansicht, in der sich Spieler vor Spielbeginn befinden.
 * Sie implementiert das Interface {@link ViewIF.LobbyViewIF} und stellt Methoden zur Verfügung, um
 * Aktionen wie "Bereit", "Start" und "Exit" zu verarbeiten, sowie die Anzeige beigetretener Spieler und
 * der Spiel-ID zu aktualisieren.
 */
public class LobbyView extends JPanel implements ViewIF.LobbyViewIF {
    private JPanel LobbyViewForm;
    private JTextPane joinedPlayersArea;
    private JButton readyButton;
    private JLabel GameID;
    private JButton startButton;
    private JButton ExitButton;

    /**
     * Konstruktor für die LobbyView.
     * Setzt das Layout auf BorderLayout und fügt das Formularpanel in die Mitte ein.
     */
    public LobbyView() {
        setLayout(new BorderLayout());
        add(LobbyViewForm, BorderLayout.CENTER);
    }

    /**
     * Gibt das Label zurück, das die Spiel-ID anzeigt.
     *
     * @return das JLabel mit der Spiel-ID
     */
    public JLabel getGameID() {
        return GameID;
    }

    /**
     * Setzt das Label, das die Spiel-ID anzeigt.
     *
     * @param gameID das neue JLabel, das die Spiel-ID enthalten soll
     */
    public void setGameID(JLabel gameID) {
        GameID = gameID;
    }

    /**
     * Gibt den "Bereit"-Button zurück.
     *
     * @return der JButton, der den Bereitschaftsstatus darstellt
     */
    public JButton getReadyButton() {
        return readyButton;
    }

    /**
     * Fügt dem "Bereit"-Button einen ActionListener hinzu.
     *
     * @param listener der hinzuzufügende ActionListener
     */
    public void setReadyButtonListener(ActionListener listener) {
        readyButton.addActionListener(listener);
    }

    /**
     * Fügt dem "Start"-Button einen ActionListener hinzu.
     *
     * @param listener der hinzuzufügende ActionListener
     */
    public void setStartButtonListener(ActionListener listener) {
        startButton.addActionListener(listener);
    }

    /**
     * Gibt den Textbereich zurück, in dem die Namen der beigetretenen Spieler angezeigt werden.
     *
     * @return das JTextPane mit den beigetretenen Spielern
     */
    public JTextPane getJoinedPlayersArea() {
        return joinedPlayersArea;
    }

    /**
     * Gibt den "Start"-Button zurück.
     *
     * @return der JButton, der zum Starten des Spiels dient
     */
    public JButton getStartButton() {
        return startButton;
    }

    /**
     * Fügt dem "Exit"-Button einen ActionListener hinzu.
     *
     * @param listener der hinzuzufügende ActionListener
     */
    public void setExitButtonListener(ActionListener listener) {
        ExitButton.addActionListener(listener);
    }

    /**
     * Gibt den "Exit"-Button zurück.
     *
     * @return der JButton, der zum Verlassen der Lobby verwendet wird
     */
    public JButton getExitButton() {
        return ExitButton;
    }
}
