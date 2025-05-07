package org.client.view;

import javax.swing.*;
import java.awt.*;

/**
 * Die Klasse WinnerView stellt die Gewinneransicht dar, die angezeigt wird,
 * wenn ein Spieler das Spiel gewonnen hat.
 * Sie zeigt eine große Nachricht im Zentrum des Bildschirms an.
 */
public class WinnerView extends JPanel {

    /**
     * Konstruktor für die WinnerView.
     *
     * @param playerName   der Name des Spielers, der gewonnen hat
     * @param gameViewSize die Größe der Spielansicht, um die Gewinneransicht entsprechend anzupassen
     */
    public WinnerView(String playerName, Dimension gameViewSize) {
        setPreferredSize(gameViewSize);
        setBackground(new Color(136, 158, 115));
        setLayout(new BorderLayout());

        JLabel winnerLabel = new JLabel(playerName + " YOU WIN!", SwingConstants.CENTER);
        winnerLabel.setFont(new Font("SansSerif", Font.BOLD, 50));
        winnerLabel.setForeground(Color.WHITE);
        add(winnerLabel, BorderLayout.CENTER);
    }
}
