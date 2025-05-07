package org.client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Die Klasse MainView ist das Hauptfenster der Anwendung und implementiert das {@link ViewIF}-Interface.
 * Sie verwaltet die verschiedenen Ansichten (Start-, Lobby- und Spielansicht) mittels eines CardLayouts,
 * sodass zwischen diesen Ansichten dynamisch umgeschaltet werden kann.
 */
public class MainView extends JFrame implements ViewIF {
    /** Layout-Manager zur Verwaltung der verschiedenen Ansichten */
    private final CardLayout cardLayout;
    /** Panel, welches die einzelnen Karten (Ansichten) enthält */
    private final JPanel cardPanel;
    /** Die Startansicht, die beim Programmstart angezeigt wird */
    private final StartView startView;
    /** Die Lobbyansicht, in der sich Spieler vor Spielbeginn befinden */
    private final LobbyView lobbyView;
    /** Die Spielansicht, in der das eigentliche Spiel dargestellt wird */
    private final GameView gameView;

    /**
     * Konstruktor für die MainView.
     * Initialisiert das Hauptfenster, passt die Größe an den Bildschirm an, erstellt die einzelnen
     * Ansichten und fügt diese dem CardPanel hinzu. Anschließend wird das Fenster sichtbar gemacht.
     */
    public MainView() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        setSize(screenWidth, screenHeight);
        // setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        startView = new StartView();
        lobbyView = new LobbyView();
        gameView = new GameView();

        cardPanel.add(startView, "Start");
        cardPanel.add(lobbyView, "Lobby");
        cardPanel.add(gameView, "Game");

        add(cardPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    /**
     * Gibt die Lobbyansicht zurück.
     *
     * @return die aktuelle LobbyView
     */
    @Override
    public LobbyView getLobbyView() {
        return lobbyView;
    }

    /**
     * Gibt die Startansicht zurück.
     *
     * @return die aktuelle StartView
     */
    @Override
    public StartView getStartView() {
        return startView;
    }

    /**
     * Gibt die Spielansicht zurück.
     *
     * @return die aktuelle GameView
     */
    @Override
    public GameView getGameView() {
        return gameView;
    }

    /**
     * Wechselt zu einer Ansicht, die durch den übergebenen Namen identifiziert wird.
     * Hierbei wird das CardLayout genutzt, um die entsprechende Karte anzuzeigen.
     *
     * @param name der Name der anzuzeigenden Ansicht (z.B. "Start", "Lobby" oder "Game")
     */
    @Override
    public void toggleView(String name) {
        cardLayout.show(cardPanel, name);
    }

    /**
     * Gibt den Spielernamen zurück.
     * (Aktuell wird hier ein leerer String zurückgegeben, da die Implementierung noch aussteht.)
     *
     * @return einen leeren String
     */
    @Override
    public String getPlayerName() {
        return "";
    }

    /**
     * Setzt einen ActionListener für den Erstellen-Button.
     * (Momentan ist diese Methode nicht implementiert.)
     *
     * @param Listener der hinzuzufügende ActionListener
     */
    @Override
    public void setCreateButtonListener(ActionListener Listener) {

    }

    /**
     * Fügt einen ActionListener hinzu.
     * (Momentan ist diese Methode nicht implementiert.)
     *
     * @param listener der hinzuzufügende ActionListener
     */
    @Override
    public void addActionListener(ActionListener listener) {

    }

    /**
     * Entfernt einen ActionListener.
     * (Momentan ist diese Methode nicht implementiert.)
     */
    @Override
    public void removeActionListener() {

    }

    /**
     * Setzt den Benutzernamen.
     * (Momentan ist diese Methode nicht implementiert.)
     *
     * @param username der zu setzende Benutzername
     */
    @Override
    public void setUsername(String username) {

    }

    /**
     * Zeigt den Spielernamen an.
     * (Momentan wird hier ein leerer String zurückgegeben, da die Implementierung noch aussteht.)
     *
     * @return einen leeren String
     */
    @Override
    public String showPlayerName() {
        return "";
    }

    /**
     * Aktualisiert den angezeigten Würfelwert.
     * (Momentan ist diese Methode nicht implementiert.)
     */
    @Override
    public void updateDiceValue() {

    }

    /**
     * Aktualisiert die Positionen der Spielfiguren.
     * (Momentan ist diese Methode nicht implementiert.)
     */
    @Override
    public void updateFiguresPositions() {

    }

    /**
     * Aktualisiert den Zustand des Spielzugs.
     * (Momentan ist diese Methode nicht implementiert.)
     */
    @Override
    public void updateTurnState() {

    }
}
