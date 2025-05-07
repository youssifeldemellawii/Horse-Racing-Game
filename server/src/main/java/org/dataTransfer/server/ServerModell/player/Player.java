package org.dataTransfer.server.ServerModell.player;

import org.dataTransfer.server.ServerModell.PlayerIF;
import org.dataTransfer.server.ServerModell.field.Field;
import org.dataTransfer.server.ServerModell.field.FieldTypes;
import org.dataTransfer.server.ServerModell.figur.Figur;
import org.dataTransfer.server.ServerModell.figur.FigurColor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.dataTransfer.server.ServerModell.game.Game;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Die Klasse {@code Player} repräsentiert einen Spieler im Spiel und wird als JPA-Entität in der Datenbank gespeichert.
 * Ein Spieler besitzt einen eindeutigen Identifikator, einen Spielernamen, einen Spielerindex, einen Ready-Status sowie
 * eine aktuelle Position auf dem Spielfeld. Zudem wird über die Many-to-One-Beziehung das zugehörige {@link Game} referenziert.
 *
 * <p>
 * Hinweis: Einige Funktionen zur Bewegung und Feldwirkung sind aus Gründen der Verantwortlichkeit (Controller-Logik)
 * auskommentiert und werden nicht direkt in dieser Klasse implementiert.
 * </p>
 *
 * @see Figur
 * @see FigurColor
 * @see Field
 * @see Game
 */
@Entity
@Table(name = "players")
@Getter
@Setter
public class Player implements PlayerIF {
	/**
	 * Eindeutige Identifikation des Spielers.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;  // Every entity must have an ID

	/**
	 * Der Index des Spielers innerhalb eines Spiels.
	 */
	private int playerIndex;

	/**
	 * Der Name des Spielers.
	 */
	private String name;

	/**
	 * Der Bereitschaftsstatus des Spielers.
	 */
	private boolean isReady = false;

	/**
	 * Die aktuelle Position des Spielers auf dem Spielfeld.
	 */
	private int postion = 0;

	/**
	 * Das Spiel, dem der Spieler zugeordnet ist.
	 * Die Beziehung wird über @ManyToOne realisiert und in der Datenbank über die Spalte "game_id" verknüpft.
	 * Die Annotation @JsonIgnore verhindert eine rekursive Serialisierung.
	 */
	@ManyToOne
	@JoinColumn(name = "game_id")
	@JsonIgnore
	private Game game;

	/**
	 * Standardkonstruktor.
	 * JPA benötigt einen parameterlosen Konstruktor.
	 */
	public Player() {
	}

	/**
	 * Konstruktor zur Erstellung eines neuen Spielers mit Namen, Farbe und Startfeld.
	 *
	 * <p>
	 * Hinweis: Die zugehörige Spielfigur wird aktuell nicht gesetzt, da der Code für {@code Figur} auskommentiert ist.
	 * </p>
	 *
	 * @param name       der Name des Spielers
	 * @param color      die Farbe der Spielfigur
	 * @param startField das Startfeld, auf dem die Figur platziert werden soll
	 */
	public Player(String name, FigurColor color, Field startField) {
		this.name = name;
		// this.figur = new Figur(color, startField);
	}

	/**
	 * Gibt den Bereitschaftsstatus des Spielers zurück.
	 *
	 * @return {@code true}, falls der Spieler bereit ist, ansonsten {@code false}
	 */
	public boolean isReady() {
		return isReady;
	}

	/**
	 * Setzt den Bereitschaftsstatus des Spielers.
	 *
	 * @param ready {@code true}, wenn der Spieler bereit ist, ansonsten {@code false}
	 */
	public void setReady(boolean ready) {
		isReady = ready;
	}

	// Die folgenden Methoden zur Bewegung der Spielfigur und zum Überprüfen des Gewinns
	// wurden aus Verantwortlichkeitsgründen in den Controller verlagert und sind hier auskommentiert.
	//
	// public void move(int steps, List<Field> board) {
	//     int currentIndex = board.indexOf(figur.getField());
	//     int newIndex = currentIndex + steps;
	//     if (newIndex >= board.size()) {
	//         System.out.println("Das Pferd hat das Ziel erreicht!");
	//         figur.setField(board.get(board.size() - 1));
	//     } else {
	//         Field newField = board.get(newIndex);
	//         handleFieldEffect(newField);
	//         figur.setField(newField);
	//     }
	// }
	//
	// private void handleFieldEffect(Field field) {
	//     if (field.fieldTypes == FieldTypes.OBSTACLE) {
	//         System.out.println("Hindernis! Zurück zum Startfeld.");
	//         figur.setField(field);
	//     } else if (field.fieldTypes == FieldTypes.UNIQUE) {
	//         System.out.println("Spezialfeld erreicht! Zusätzliche Aktionen erforderlich.");
	//     }
	// }
	//
	// public boolean hasWon() {
	//     return figur.getField().fieldTypes == FieldTypes.END;
	// }
}
