package org.dataTransfer.server.ServerModell.figur;

import org.dataTransfer.server.ServerModell.field.Field;

/**
 * Die Klasse {@code Figur} repräsentiert eine Spielfigur im Spiel.
 * Jede Figur besitzt eine bestimmte Farbe und befindet sich auf einem Spielfeld.
 *
 * <p>
 * Mithilfe dieser Klasse können Spielfiguren erstellt und ihre Positionen auf dem Spielfeld verwaltet werden.
 * </p>
 */
public class Figur {
	/**
	 * Die Farbe der Figur.
	 */
	private FigurColor color;

	/**
	 * Das Spielfeld, auf dem sich die Figur aktuell befindet.
	 */
	private Field field;

	/**
	 * Konstruktor zur Erstellung einer neuen Figur.
	 *
	 * @param color      die Farbe der Figur
	 * @param startField das Spielfeld, auf dem die Figur initial platziert wird
	 */
	public Figur(FigurColor color, Field startField) {
		this.color = color;
		this.field = startField;
	}

	/**
	 * Gibt das Spielfeld zurück, auf dem sich die Figur befindet.
	 *
	 * @return das aktuelle {@link Field} der Figur
	 */
	public Field getField() {
		return field;
	}

	/**
	 * Setzt das Spielfeld, auf dem sich die Figur befindet.
	 *
	 * @param field das neue {@link Field}, auf dem die Figur positioniert werden soll
	 */
	public void setField(Field field) {
		this.field = field;
	}
}
