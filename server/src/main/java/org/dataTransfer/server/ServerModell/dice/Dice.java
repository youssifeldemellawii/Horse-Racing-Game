package org.dataTransfer.server.ServerModell.dice;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.dataTransfer.server.ServerModell.DiceIF;

import java.io.Serializable;
import java.util.Random;

/**
 * Diese Klasse repräsentiert einen Würfel im Server-Modell.
 *
 * <p>
 * Mithilfe von Lombok (@Getter, @Setter) werden automatisch Getter- und Setter-Methoden für die Attribute generiert.
 * </p>
 *
 * <p>
 * Die statische Methode {@link #rollDice()} simuliert einen Würfelwurf und gibt einen Wert zwischen 1 und 6 zurück.
 * </p>
 */
public class Dice implements DiceIF {
    /**
     * Simuliert einen Würfelwurf und gibt einen zufälligen Wert zwischen 1 und 6 zurück.
     *
     * @return ein int-Wert zwischen 1 und 6, der den Würfelwurf repräsentiert
     */
    public static int rollDice() {
        Random random = new Random();
        return random.nextInt(6) + 1;
    }
}
