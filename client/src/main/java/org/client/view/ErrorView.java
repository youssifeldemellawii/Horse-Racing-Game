package org.client.view;

import javax.swing.*;
import java.awt.*;

/**
 * Die Klasse ErrorView stellt ein modales Dialogfenster dar, das Fehlernachrichten anzeigt.
 * Das Fenster wird automatisch in der Mitte des Bildschirms positioniert und bietet einen "Close"-Button zum Schließen des Dialogs.
 */
public class ErrorView extends JDialog {
    private JPanel errorPanel;
    private JLabel errorMessageLabel;
    private JButton closeButton;
    private JPanel errorViewForm;

    /**
     * Konstruktor für die ErrorView.
     * Erzeugt ein modales Dialogfenster mit der übergebenen Fehlernachricht.
     *
     * @param errorMessage die anzuzeigende Fehlernachricht
     */
    public ErrorView(String errorMessage) {
        setTitle("Error");
        setModal(true);
        setSize(300, 150);
        setLocationRelativeTo(null);

        // Schließt den Dialog, wenn der Close-Button betätigt wird
        closeButton.addActionListener(e -> dispose());

        // Fügt das Fehlerformular dem Dialog hinzu
        add(errorViewForm, BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Zeigt einen Fehlerdialog mit der angegebenen Nachricht an.
     *
     * @param message die Fehlernachricht, die angezeigt werden soll
     */
    public static void showError(String message) {
        new ErrorView(message);
    }
}
