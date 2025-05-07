package org.client;

import org.client.controller.*;

/**
 * Die Main-Klasse stellt den Einstiegspunkt der Anwendung dar.
 * In der main-Methode wird der ClientController als Singleton-Instanz initialisiert,
 * um die Client-seitige Logik zu starten.
 */
public class Main {
    /**
     * Die main-Methode startet die Anwendung.
     *
     * @param args Kommandozeilenargumente (werden in dieser Anwendung nicht verwendet)
     */
    public static void main(String[] args) {
        ClientController controller = ClientController.getInstance();
    }
}
