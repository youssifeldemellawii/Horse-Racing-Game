package org.client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Die Klasse StartView repräsentiert die Startansicht des Clients, in der der Benutzer seinen Namen eingeben,
 * einem bestehenden Spiel beitreten oder ein neues Spiel erstellen kann.
 * StartView implementiert das Interface {@link ViewIF.StartViewIF} und erweitert {@link JPanel}.
 */
public class StartView extends JPanel implements ViewIF.StartViewIF {

    private JPanel StartViewForm;
    private JTextField joinField;
    private JButton joinButton;
    private JButton createButton;
    private JTextField playerNameField;
    private JLabel gameID;

    /**
     * Konstruktor für die StartView.
     * Setzt das Layout auf BorderLayout und fügt das Formularpanel zur Ansicht hinzu.
     */
    public StartView() {
        setLayout(new BorderLayout());
        add(StartViewForm);
    }

    /**
     * Gibt den eingegebenen Spielernamen zurück.
     *
     * @return der Spielername als String
     */
    @Override
    public String getPlayerName() {
        return playerNameField.getText();
    }

    /**
     * Fügt dem "Erstellen"-Button einen ActionListener hinzu.
     *
     * @param Listener der hinzuzufügende ActionListener
     */
    @Override
    public void setCreateButtonListener(ActionListener Listener) {
        createButton.addActionListener(Listener);
    }

    /**
     * Fügt dem "Beitreten"-Button einen ActionListener hinzu.
     *
     * @param Listener der hinzuzufügende ActionListener
     */
    @Override
    public void setJoinButtonListener(ActionListener Listener) {
        joinButton.addActionListener(Listener);
    }

    /**
     * Gibt das Label zurück, das die Spiel-ID anzeigt.
     * (Momentan wird hier null zurückgegeben, da keine konkrete Implementierung erfolgt.)
     *
     * @return das JLabel mit der Spiel-ID oder null
     */
    @Override
    public JLabel getGameID() {
        return null;
    }

    /**
     * Gibt das Textfeld zurück, in das die Spiel-ID eingegeben wird.
     *
     * @return das JTextField für die Spiel-ID
     */
    @Override
    public JTextField getJoinField() {
        return joinField;
    }

    /**
     * Gibt den "Beitreten"-Button zurück.
     *
     * @return der JButton zum Beitreten eines Spiels
     */
    @Override
    public JButton getJoinButton() {
        return joinButton;
    }

    /**
     * Gibt den "Erstellen"-Button zurück.
     *
     * @return der JButton zum Erstellen eines Spiels
     */
    @Override
    public JButton getCreateButton() {
        return createButton;
    }

    /**
     * Gibt das Textfeld zurück, in dem der Spielername eingegeben wird.
     *
     * @return das JTextField für den Spielernamen
     */
    @Override
    public JTextField getPlayerNameField() {
        return playerNameField;
    }
}
