package org.dataTransfer.server.ServerModell.game;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.dataTransfer.server.ServerModell.GameIF;
import org.dataTransfer.server.ServerModell.dice.Dice;
import org.dataTransfer.server.ServerModell.field.Field;
import org.dataTransfer.server.ServerModell.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse {@code Game} repräsentiert ein Spiel und wird als JPA-Entität in der Datenbank gespeichert.
 * Ein Spiel besitzt einen Host, eine maximale Spieleranzahl, einen Status (z. B. "Lobby" oder "Game") und
 * eine Liste von Spielern. Zudem wird der aktuelle Spielerzug sowie der Wert des letzten Würfelwurfs verwaltet.
 *
 * <p>
 * Mit der Methode {@link #rollDiceAndNextTurn()} wird ein Würfelwurf simuliert, der aktuelle Spieler
 * bewegt und gegebenenfalls ein spezieller Feldeffekt (über {@link Field#applyFieldEffect(int, Player)}) angewendet.
 * </p>
 *
 * <p>
 * Die Klasse nutzt Lombok (@Getter, @Setter) zur automatischen Generierung von Getter- und Setter-Methoden.
 * </p>
 */
@Entity
@Table(name = "games")
@Setter
@Getter
public class Game implements GameIF {
    /**
     * Die eindeutige Identifikation des Spiels.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Der Name des Spiel-Hosts.
     */
    private String gameHostName;

    /**
     * Die maximale Anzahl der Spieler, die an diesem Spiel teilnehmen können.
     */
    private int maxPlayersNumber = 4;

    /**
     * Gibt an, ob alle Spieler bereit sind.
     */
    private boolean allPlayersAreReady = false;

    /**
     * Der Wert des letzten Würfelwurfs.
     */
    private int lastDiceRoll = 0;

    /**
     * Der Benutzername des Spielers, der aktuell am Zug ist.
     */
    private String currentPlayerUsername;

    /**
     * Gibt an, ob das Spiel gestartet wurde.
     */
    private boolean gameStarted = false;

    /**
     * Der aktuelle Spielstatus, z. B. "Lobby" oder "Game".
     */
    private String gameState = "Lobby";

    /**
     * Die Liste der Spieler, die an diesem Spiel teilnehmen.
     * Die Beziehung wird per OneToMany mit CascadeType.ALL und Lazy-Fetching verwaltet.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private List<Player> players = new ArrayList<>();

    /**
     * Gibt die aktuelle Anzahl der Spieler im Spiel zurück.
     *
     * @return die Anzahl der Spieler
     */
    // Costumed D
//    private static long lastId = 1000;
//
//    public Game() {
//        this.id = generateNextId();
//    }
//
//    private static synchronized long generateNextId() {
//        Random random = new Random();
//        long randomIncrement = random.nextInt(2000) + 1;
//        lastId += randomIncrement;
//        return lastId;
//    }

    public int getCurrentPlayersCount(){
        return players.size();
    }

    /**
     * Fügt einen neuen Spieler dem Spiel hinzu.
     * Dabei wird dem Spieler ein Spielerindex zugewiesen, er wird als nicht bereit markiert
     * und falls noch kein Spieler am Zug ist, wird der Spieler als aktueller Spieler gesetzt.
     *
     * @param player der hinzuzufügende Spieler
     */
    public void addPlayer(Player player) {
        int newIndex = getCurrentPlayersCount() + 1;
        player.setPlayerIndex(newIndex);
        player.setReady(false);
        player.setGame(this);
        players.add(player);
        if (currentPlayerUsername == null) {
            currentPlayerUsername = player.getName();
        }
    }

    /**
     * Wechselt den Zug an den nächsten Spieler.
     * Falls das Spiel nicht leer ist, wird der aktuelle Spielerindex ermittelt und der nächste Spieler als aktuell gesetzt.
     */
    public void nextTurn() {
        if (!players.isEmpty()) {
            int currentIndex = getCurrentPlayerIndex();
            int nextIndex = (currentIndex + 1) % players.size();
            currentPlayerUsername = players.get(nextIndex).getName();
        }
    }

    /**
     * Ermittelt den Index des aktuellen Spielers anhand des Benutzernamens.
     *
     * @return der Index des aktuellen Spielers oder 0, falls kein passender Spieler gefunden wird
     */
    public int getCurrentPlayerIndex() {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getName().equals(currentPlayerUsername)) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Simuliert einen Würfelwurf, aktualisiert die Position des aktuellen Spielers und wendet gegebenenfalls einen Feldeffekt an.
     * Anschließend wird der Zug an den nächsten Spieler weitergegeben.
     *
     * <p>
     * Der Würfelwurf erfolgt über {@link Dice#rollDice()}. Die neue Position des Spielers wird berechnet und auf maximal 64 begrenzt.
     * Falls das neue Feld einen speziellen Effekt besitzt, wird dieser über {@link Field#applyFieldEffect(int, Player)} angewendet.
     * </p>
     *
     * @return der Wert des gewürfelten Ergebnisses
     */
    public int rollDiceAndNextTurn() {
        int rolledValue = Dice.rollDice();
        this.lastDiceRoll = rolledValue;

        int currentIndex = getCurrentPlayerIndex();
        Player currentPlayer = players.get(currentIndex);

        // Berechne die neue Position: Falls sie 64 überschreitet, wird sie auf 64 begrenzt
        int newPos = currentPlayer.getPostion() + rolledValue;
        if (newPos > 64) {
            newPos = 64;
        }
        currentPlayer.setPostion(newPos);
        System.out.println("Player landed on field index: " + newPos);

        // Wende den Feldeffekt an, falls das Feld einen speziellen Effekt besitzt
        Field.applyFieldEffect(newPos, currentPlayer);

        // Wechsle zum nächsten Spieler
        nextTurn();
        return rolledValue;
    }
}
