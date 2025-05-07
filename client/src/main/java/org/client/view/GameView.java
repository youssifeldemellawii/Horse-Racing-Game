package org.client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Die Klasse GameView repräsentiert die Spielansicht des Clients.
 * Sie zeigt das Spielfeld, die Spielerpositionen, den Würfelwurf sowie den Punktestand an.
 * GameView implementiert das Interface {@link ViewIF.GameViewIF} und erweitert {@link JPanel}.
 */
public class GameView extends JPanel implements ViewIF.GameViewIF {

    private JPanel gameViewForm;
    private JPanel leftSidePane;
    private JPanel rightSidePane;
    /** Panel, welches das Spielfeld enthält */
    public JPanel gameBoardPane;
    /** Button zum Auslösen des Würfelwurfs */
    private JButton rollDiceButton;
    /** Textbereich zur Anzeige des Punktestandes */
    private JTextPane scoreArea;
    /** Textbereich, der den Würfelwert als Text anzeigt */
    private JTextPane diceDisplayLabel;
    /** Textbereich zur Anzeige des aktuellen Spielers */
    private JTextPane currentPlayerTextPane;
    /** Label zur Anzeige des Würfelbildes */
    private JLabel diceLabel;
    /** Button zum Verlassen der Spielansicht */
    private JButton ExitButton;

    /** Liste der Zellen, welche das Spielfeld repräsentieren */
    private List<JPanel> cells = new ArrayList<>();

    /** Map für spezielle Feldbezeichnungen (z. B. "Start" oder "End") */
    private Map<Integer, String> specialFieldLabels = null;

    /**
     * Konstruktor für die GameView.
     * Initialisiert das Spielfeld, legt das Layout fest und färbt spezielle Felder.
     */
    public GameView() {
        cells = new ArrayList<>();
        // Initialisiere die Zuordnung spezieller Feldbezeichnungen
        // (wird in createUIComponents() initialisiert, falls noch nicht vorhanden)
        setLayout(new BorderLayout());
        add(gameViewForm, BorderLayout.CENTER);
        settingUpSpecialFields();

    }

    public void settingUpSpecialFields(){
        // Set field colors
        setFieldColorByIndex(6, new Color(205, 141, 122));
        setFieldColorByIndex(19, new Color(195, 226, 194));
        setFieldColorByIndex(31, new Color(205, 141, 122));
        setFieldColorByIndex(42, new Color(205, 141, 122));
        setFieldColorByIndex(52, new Color(195, 226, 194));
        setFieldColorByIndex(58, new Color(205, 141, 122));
        setFieldColorByIndex(63, new Color(255, 242, 242));

        // Set field text
        setFieldText(6, "S");
        setFieldText(19, "H");
        setFieldText(31, "S");
        setFieldText(42, "H");
        setFieldText(52, "S");
        setFieldText(58, "S");
        setFieldText(63, "END");
        return;
    }
    public void setFieldText(int fieldIndex, String text) {
        JLabel script = new JLabel(text);
        JPanel section = getCellSection(fieldIndex, 1);
        section.add(script);

    }

    /**
     * Gibt den Textbereich zurück, der den aktuellen Spieler anzeigt.
     *
     * @return das JTextPane für den aktuellen Spieler
     */
    public JTextPane getCurrentPlayerTextPane() {
        return currentPlayerTextPane;
    }

    /**
     * Fügt dem Exit-Button einen ActionListener hinzu.
     *
     * @param listener der hinzuzufügende ActionListener
     */
    public void setExitButtonListener(ActionListener listener) {
        ExitButton.addActionListener(listener);
    }

    /**
     * Wird von der GUI-Builder-Umgebung aufgerufen, um benutzerdefinierte Komponenten zu erstellen.
     * Hier wird das Spielfeld (gameBoardPane) als 8x8-Grid initialisiert und die Zellen erstellt.
     */
    private void createUIComponents() {
        // Initialisiere die Map für spezielle Feldbezeichnungen, falls noch nicht vorhanden.
        if (specialFieldLabels == null) {
            specialFieldLabels = new HashMap<>();
            specialFieldLabels.put(0, "Start");
        }

        // Erstelle ein Panel für das Spielfeld mit einem 8x8 Grid und definierten Abständen.
        gameBoardPane = new JPanel(new GridLayout(8, 8, 1, 1));
        gameBoardPane.setBackground(Color.BLACK);

        // Erstelle 64 Zellen (Index 0 bis 63)
        for (int fieldNumber = 0; fieldNumber < 64; fieldNumber++) {
            JPanel cell = new JPanel(new BorderLayout());
            cell.setPreferredSize(new Dimension(50, 50));
            cell.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            // Erstelle ein Panel für gestapelte Bereiche (Sections)
            JPanel stackedSections = new JPanel(new GridLayout(4, 1, 0, 0));
            stackedSections.setPreferredSize(new Dimension(50, 40));

            // Erstelle vier Sektionen mit unterschiedlichen Hintergründen
            JPanel section1 = new JPanel();
            section1.setBackground(new Color(136, 158, 115));
            JPanel section2 = new JPanel();
            section2.setBackground(new Color(169, 74, 74));
            JPanel section3 = new JPanel();
            section3.setBackground(new Color(244, 215, 147));
            JPanel section4 = new JPanel();
            section4.setBackground(new Color(41, 115, 178));

            // Füge die Sektionen zum gestapelten Panel hinzu
            stackedSections.add(section1);
            stackedSections.add(section2);
            stackedSections.add(section3);
            stackedSections.add(section4);

            // Bestimme den anzuzeigenden Text: Verwende spezielle Beschriftung, falls definiert, ansonsten den Feldindex.
            String labelText = specialFieldLabels.containsKey(fieldNumber)
                    ? specialFieldLabels.get(fieldNumber)
                    : String.valueOf(fieldNumber);
            JLabel fieldLabel = new JLabel(labelText, SwingConstants.CENTER);
            fieldLabel.setOpaque(true);
            fieldLabel.setBackground(Color.WHITE);
            fieldLabel.setForeground(Color.BLACK);

            // Füge das gestapelte Panel und das Label zur Zelle hinzu.
            cell.add(stackedSections, BorderLayout.CENTER);
            cell.add(fieldLabel, BorderLayout.SOUTH);

            // Füge die Zelle dem Spielfeld hinzu.
            gameBoardPane.add(cell);
        }
    }

    /**
     * Gibt die Zelle (als JPanel) am angegebenen Index des Spielfelds zurück.
     *
     * @param index der Index der gewünschten Zelle
     * @return das JPanel der Zelle oder null, falls der Index ungültig ist
     */
    public JPanel getCell(int index) {
        if (index >= 0 && index < gameBoardPane.getComponentCount()) {
            Component cell = gameBoardPane.getComponent(index);
            if (cell instanceof JPanel) {
                return (JPanel) cell;
            } else {
                System.out.println("ERROR: Komponente am Index " + index + " ist kein JPanel");
            }
        } else {
            System.out.println("ERROR: Index " + index + " liegt außerhalb des gültigen Bereichs!");
        }
        return null;
    }

    /**
     * Gibt die spezifische Sektion innerhalb einer Zelle zurück.
     *
     * @param cellIndex    der Index der Zelle
     * @param sectionIndex der Index der Sektion innerhalb der Zelle (0 bis 3)
     * @return das JPanel der gewünschten Sektion oder null, falls nicht gefunden
     */
    public JPanel getCellSection(int cellIndex, int sectionIndex) {
        JPanel cell = getCell(cellIndex);
        if (cell != null) {
            Component[] cellComponents = cell.getComponents();
            if (cellComponents.length > 1 && cellComponents[0] instanceof JPanel) {
                JPanel stackedSections = (JPanel) cellComponents[0];
                Component[] stackedSectionsComponents = stackedSections.getComponents();
                if (stackedSectionsComponents.length > sectionIndex && stackedSectionsComponents[sectionIndex] instanceof JPanel) {
                    return (JPanel) stackedSectionsComponents[sectionIndex];
                } else {
                    System.out.println("ERROR: Section-Index " + sectionIndex + " liegt außerhalb des gültigen Bereichs!");
                }
            } else {
                System.out.println("ERROR: Keine gestapelten Sektionen in Zelle " + cellIndex + " gefunden!");
            }
        } else {
            System.out.println("ERROR: Zelle am Index " + cellIndex + " existiert nicht!");
        }
        return null;
    }

    /**
     * Entfernt alle Komponenten aus einer bestimmten Sektion einer Zelle.
     *
     * @param fieldIndex   der Index der Zelle
     * @param sectionIndex der Index der Sektion innerhalb der Zelle
     */
    @Override
    public void clearSection(int fieldIndex, int sectionIndex) {
        JPanel section = getCellSection(fieldIndex, sectionIndex);
        if (section != null) {
            section.removeAll();
            section.revalidate();
            section.repaint();
        } else {
            System.out.println("ERROR: Sektion nicht gefunden für fieldIndex " + fieldIndex + " und sectionIndex " + sectionIndex);
        }
    }

    /**
     * Löscht den Inhalt aller Zellen des Spielfelds, indem alle Sektionen in jeder Zelle geleert werden.
     */
    public void clearAllCells() {
        int totalCells = gameBoardPane.getComponentCount();
        for (int i = 0; i < totalCells; i++) {
            for (int j = 0; j < 4; j++) {
                clearSection(i, j);
            }
        }
    }

    /**
     * Setzt die Hintergrundfarbe einer Zelle.
     *
     * der Index der Zelle
     *   die zu setzende Farbe
     */
    public void setFieldColorByIndex(int fieldIndex, Color color) {
        for (int i = 0; i < 4; i++){
            JPanel section = getCellSection(fieldIndex, i);
            section.setBackground(color);
        }
        JPanel section1 = getCellSection(fieldIndex, 1);
        JLabel text = new JLabel(String.valueOf(color.getRed()), SwingConstants.CENTER);
        section1.add(text);
    }


    /**
     * Verschiebt die Spielfigur eines Spielers auf das angegebene Feld und in die entsprechende Sektion.
     * Es wird ein Label mit dem Spielernamen und einem Symbol hinzugefügt.
     *
     * @param fieldIndex  der Index des Zielfeldes
     * @param playerIndex der Index des Spielers (Sektion im Feld, 0-basiert)
     * @param playerName  der Name des Spielers
     */
    @Override
    public void movePlayerPosition(int fieldIndex, int playerIndex, String playerName) {
        if (gameBoardPane == null) {
            System.out.println("ERROR: gameBoardPane ist nicht initialisiert!");
            return;
        }
        JPanel section = getCellSection(fieldIndex, playerIndex);
        if (section != null) {
            // Remove existing components
            section.removeAll();

            // Create a label representing the game figure
            JLabel figure = new JLabel();
            figure.setText(playerName);

            // Load the icon from the classpath
            URL iconUrl = getClass().getResource("/DicePics/chess-knight.png");
            if (iconUrl == null) {
                System.err.println("Resource not found: /DicePics/chess-knight.png");
            } else {
                ImageIcon icon = new ImageIcon(iconUrl);
                figure.setIcon(icon);
            }

            // Add the figure to the section and refresh
            section.add(figure);
            section.revalidate();
            section.repaint();
            gameBoardPane.revalidate();
            gameBoardPane.repaint();

        } else {
            System.out.println("ERROR: Section at index " + playerIndex + " in field " + fieldIndex + " not found!");
        }
    }

    /**
     * Aktualisiert das Scoreboard anhand der übergebenen Spieldaten.
     * Die Spieler werden nach ihrem Punktestand absteigend sortiert und in einem Textbereich angezeigt.
     *
     * @param playersData eine Liste von Maps, die den Namen und den Punktestand der Spieler enthalten
     */
    public void updateScoreBoard(List<Map<String, Object>> playersData) {
        playersData.sort((p1, p2) -> {
            Integer score1 = (Integer) p1.get("score");
            Integer score2 = (Integer) p2.get("score");
            return score2.compareTo(score1);
        });

        StringBuilder sb = new StringBuilder();
        int rank = 1;
        for (Map<String, Object> player : playersData) {
            String name = (String) player.get("name");
            Integer score = (Integer) player.get("score");
            sb.append(rank++).append(". ").append(name)
                    .append(" - ").append(score).append("\n");
        }
        scoreArea.setText(sb.toString());
    }

    /**
     * Gibt den Button zurück, der den Würfelwurf auslöst.
     *
     * @return der JButton für den Würfelwurf
     */
    @Override
    public JButton getRollDiceButton() {
        return rollDiceButton;
    }

    /**
     * Gibt den Textbereich zurück, der den Würfelwert anzeigt.
     *
     * @return das JTextPane für die Anzeige des Würfelwerts
     */
    @Override
    public JTextPane getDiceDisplayLabel() {
        return diceDisplayLabel;
    }

    /**
     * Gibt das Label zurück, das das Würfelbild anzeigt.
     *
     * @return das JLabel für die Anzeige des Würfelbildes
     */
    @Override
    public JLabel getDiceDisplay() {
        return diceLabel;
    }

    /**
     * Gibt den Textbereich zurück, der den Punktestand anzeigt.
     *
     * @return das JTextPane für die Anzeige des Punktestandes
     */
    @Override
    public JTextPane getScoreArea() {
        return scoreArea;
    }
}
