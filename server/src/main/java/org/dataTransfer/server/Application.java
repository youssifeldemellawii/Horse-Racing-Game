package org.dataTransfer.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Die Hauptklasse der Server-Anwendung.
 *
 * <p>
 * Diese Klasse dient als Einstiegspunkt für die Spring Boot-Anwendung. Durch die Annotation
 * {@code @SpringBootApplication} wird die automatische Konfiguration, Komponenten-Scan und
 * weitere Spring Boot-Funktionalitäten aktiviert.
 * </p>
 *
 * <p>
 * Um die Anwendung zu starten, wird die {@link #main(String[])} Methode aufgerufen.
 * </p>
 */
@SpringBootApplication
public class Application {

	/**
	 * Der Einstiegspunkt der Anwendung.
	 *
	 * @param args Kommandozeilenargumente
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
