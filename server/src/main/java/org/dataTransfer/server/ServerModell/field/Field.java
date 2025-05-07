package org.dataTransfer.server.ServerModell.field;

import org.dataTransfer.server.ServerModell.player.Player;

/**
 * Die Klasse Field stellt Hilfsmethoden zur Verfügung, um Effekte basierend auf dem Spielfeld
 * auf einen Spieler anzuwenden.
 *
 * <p>
 * Die Methode {@link #applyFieldEffect(int, Player)} überprüft den übergebenen Feldindex und
 * ändert die Position des Spielers gemäß den festgelegten Regeln (z. B. Hindernisse oder einzigartige Felder).
 * </p>
 */
public class Field {

	/**
	 * Wendet einen Feldeffekt auf einen Spieler an, basierend auf dem angegebenen Feldindex.
	 *
	 * <p>
	 * Abhängig vom Feldindex wird die Position des Spielers angepasst:
	 * <ul>
	 *     <li>Bei Feld 6 (Hindernis): Zurück auf Start (Position 0).</li>
	 *     <li>Bei Feld 19 (einzigartiges Feld): Setzt die Position auf 27.</li>
	 *     <li>Bei Feld 31 (Hindernis): Setzt die Position auf 20.</li>
	 *     <li>Bei Feld 42 (Hindernis): Setzt die Position auf 32.</li>
	 *     <li>Bei Feld 52 (einzigartiges Feld): Setzt die Position auf 57.</li>
	 *     <li>Bei Feld 58 (Hindernis): Setzt die Position auf 53.</li>
	 * </ul>
	 * Falls kein spezifischer Effekt definiert ist, bleibt die Position unverändert.
	 * </p>
	 *
	 * @param fieldIndex der Index des Spielfeldes, auf dem der Effekt angewendet wird
	 * @param player     der Spieler, auf den der Effekt angewendet werden soll
	 */
	public static void applyFieldEffect(int fieldIndex, Player player) {
		System.out.println("applyFieldEffect called with fieldIndex: " + fieldIndex);

		switch (fieldIndex) {
			case 6:  // Feld 6 ist ein Hindernis
				player.setPostion(0);
				break;
			case 19: // Feld 19 ist ein einzigartiges Feld
				player.setPostion(27);
				break;
			case 31: // Feld 31 ist ein Hindernis
				player.setPostion(20);
				break;
			case 42: // Feld 42 ist ein Hindernis
				player.setPostion(32);
				break;
			case 52: // Feld 52 ist ein einzigartiges Feld
				player.setPostion(57);
				break;
			case 58: // Feld 58 ist ein Hindernis
				player.setPostion(53);
				break;
			default:
				// Kein Effekt: Position bleibt unverändert
				break;
		}
		System.out.println("After effect, player's position is: " + player.getPostion());
	}
}
