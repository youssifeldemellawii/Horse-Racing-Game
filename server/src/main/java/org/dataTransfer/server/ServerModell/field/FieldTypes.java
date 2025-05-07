package org.dataTransfer.server.ServerModell.field;

/**
 * Das Enum {@code FieldTypes} definiert die möglichen Typen von Spielfeldern.
 * Diese Typen geben an, welche besonderen Eigenschaften oder Effekte
 * mit einem Feld im Spiel verbunden sind.
 *
 * <ul>
 *     <li>{@code OBSTACLE} – Ein Hindernisfeld, das den Spieler beispielsweise zurücksetzen oder auf einen anderen Bereich verschieben kann.</li>
 *     <li>{@code UNIQUE} – Ein einzigartiges Feld, das einen speziellen Effekt besitzt, wie das Setzen des Spielers auf eine bestimmte Position.</li>
 *     <li>{@code START} – Das Startfeld des Spiels, an dem die Spieler beginnen.</li>
 *     <li>{@code END} – Das Zielfeld, das den Abschluss des Spiels markiert.</li>
 * </ul>
 */
public enum FieldTypes {
	OBSTACLE,
	UNIQUE,
	START,
	END
}
