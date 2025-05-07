package org.client.controller;

import com.google.gson.Gson;
import org.client.view.ViewIF;
import org.client.view.ErrorView;
import org.client.view.MainView;
import org.client.view.WinnerView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Die Klasse ClientController verwaltet die Client-seitige Logik des Spiels.
 * Sie steuert die Kommunikation zwischen der Benutzeroberfläche und dem Server,
 * einschließlich des Erstellens und Beitretens zu einem Spiel, der Aktualisierung
 * von Spieleraktionen (wie Würfeln oder Bereitschaftsstatus) sowie des periodischen
 * Abrufs von Spieldaten.
 * <p>
 * Diese Klasse folgt dem Singleton-Pattern, sodass stets nur eine Instanz verwendet wird.
 * Zudem werden die verschiedenen Ansichten (Start-, Lobby- und Spielansicht) initialisiert und verwaltet.
 * </p>
 *
 * @author
 */
public class ClientController {

    /** Singleton-Instanz von ClientController */
    private static final ClientController instance = new ClientController();
    /** Hauptansicht zur Steuerung der UI-Elemente */
    private final ViewIF mainView;
    /** Scheduler für das periodische Abrufen von Spieldaten */
    private final ScheduledExecutorService scheduler;
    /** Aktuelle Spieldaten, die vom Server abgerufen wurden */
    private static GameData gameData;
    /** Daten des aktuellen Spielers */
    private static PlayerData playerData;
    /** Basis-URL für die API-Endpunkte des Servers */
    private final String baseUrl = "http://localhost:8080/api";
    // Dummy-Daten
    private int cntr = 0;

    /**
     * Privater Konstruktor für das Singleton-Pattern.
     * Initialisiert die Hauptansicht und richtet die Start-, Lobby- und Spielansichten ein.
     */
    private ClientController() {
        this.mainView = new MainView();
        this.scheduler = Executors.newScheduledThreadPool(1); // Scheduler für periodische Updates

        setUpStartView();
        setUpLobbyView();
        setUpGameView();
    }

    /**
     * Gibt die Singleton-Instanz des ClientControllers zurück.
     *
     * @return die Instanz von ClientController
     */
    public static ClientController getInstance() {
        return instance;
    }

    // ===================================================================================
    //                                 < START VIEW >
    // ===================================================================================

    /**
     * Initialisiert die Startansicht, indem Event Listener für die Buttons "Erstellen" und "Beitreten"
     * gesetzt werden. Es werden Eingaben validiert und die entsprechenden Methoden zum Erstellen bzw.
     * Beitreten eines Spiels aufgerufen.
     */
    public void setUpStartView() {
        ViewIF.StartViewIF startView = mainView.getStartView();

        // -------------------------------------------------------------------------------
        // EVENT LISTENER: Erstellen-Button
        // -------------------------------------------------------------------------------
        startView.setCreateButtonListener(e -> {
            String playerName = startView.getPlayerName();

            if (playerName.isEmpty()) {
                ErrorView.showError("Spielername darf nicht leer sein!");
                return;
            }

            try {
                createGame(playerName);
            } catch (URISyntaxException | IOException | InterruptedException ex) {
                ErrorView.showError("Spiel konnte nicht erstellt werden: " + ex.getMessage());
            }
        });

        // -------------------------------------------------------------------------------
        // EVENT LISTENER: Beitreten-Button
        // -------------------------------------------------------------------------------
        startView.setJoinButtonListener(e -> {
            String playerName = startView.getPlayerName();
            String gameId = startView.getJoinField().getText();

            if (playerName.isEmpty()) {
                ErrorView.showError("Spielername darf nicht leer sein!");
                return;
            }

            if (gameId.isEmpty()) {
                ErrorView.showError("Spiel-ID darf nicht leer sein!");
                return;
            }

            try {
                joinGame(playerName, gameId);
            } catch (Exception ex) {
                ErrorView.showError("Spielbeitritt fehlgeschlagen: " + ex.getMessage());
            }
        });
    }

    /**
     * Erstellt ein neues Spiel auf dem Server mit dem angegebenen Spielernamen.
     * Es wird eine POST-Anfrage an den Server gesendet, um das Spiel zu erstellen.
     *
     * @param playerName der Name des Spielers, der das Spiel erstellt
     * @throws URISyntaxException wenn die URL fehlerhaft ist
     * @throws IOException bei Netzwerkfehlern
     * @throws InterruptedException wenn der Request unterbrochen wird
     */
    public void createGame(String playerName) throws URISyntaxException, IOException, InterruptedException {
        gameData = new GameData();
        playerData = new PlayerData();
        playerData.setName(playerName);
        playerData.setReady(true);

        Gson gson = new Gson();
        String playerJsonBody = gson.toJson(playerData);

        System.out.println("Sende Anfrage: " + playerJsonBody);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI(baseUrl + "/games"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(playerJsonBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        // Aktualisiere gameData, falls die Antwort erfolgreich war
        if (response.statusCode() >= 199 && response.statusCode() < 300) {
            // Parse der Antwort
            gameData = gson.fromJson(response.body(), GameData.class);

            // Aktualisiere playerData anhand der Spielerdaten
            playerData = gameData.getPlayers().isEmpty() ? null : gameData.getPlayers().get(0);

            System.out.println("Serverantwort: " + response.body());
            System.out.println("--------------------------------------------------------------------------------------------------------------------");
            gson = new Gson();
            String gameJsonBody = gson.toJson(gameData);
            System.out.println("Clientdaten: " + gameJsonBody);
            System.out.println("--------------------------------------------------------------------------------------------------------------------");

            // Aktualisiere die UI
            mainView.toggleView("Lobby");
            mainView.getLobbyView().getGameID().setText("GameID: " + gameData.getId());
            startPolling();

        } else {
            ErrorView.showError("Spielerstellung fehlgeschlagen. HTTP-Code: " + response.statusCode());
        }
    }

    /**
     * Tritt einem bestehenden Spiel bei, indem eine PUT-Anfrage an den Server gesendet wird.
     * Es werden der Spielername und die Spiel-ID verwendet, um dem Spiel beizutreten.
     *
     * @param playerName der Name des Spielers, der beitreten möchte
     * @param gameId die ID des Spiels, dem beigetreten werden soll
     * @throws URISyntaxException wenn die URL fehlerhaft ist
     * @throws IOException bei Netzwerkfehlern
     * @throws InterruptedException wenn der Request unterbrochen wird
     */
    public void joinGame(String playerName, String gameId) throws URISyntaxException, IOException, InterruptedException {
        playerData = new PlayerData();
        gameData = new GameData();
        playerData.setName(playerName);
        playerData.setReady(true);

        Gson gson = new Gson();
        String playerJsonBody = gson.toJson(playerData);

        System.out.println("Spielername: " + playerData.getName());

        HttpRequest putRequest = HttpRequest.newBuilder()
                .uri(new URI(baseUrl + "/games/" + gameId + "/join"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(playerJsonBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(putRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println("Antwort: " + playerData.getId());
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            gameData = gson.fromJson(response.body(), GameData.class);

            // Aktualisiere playerData anhand der Spielerdaten
            playerData = gameData.getPlayers().isEmpty() ? null : gameData.getPlayers().get(gameData.getPlayers().size() - 1);

            mainView.toggleView("Lobby");

            mainView.getLobbyView().getGameID().setText("GameID: " + gameData.getId());
            startPolling();

        } else {
            ErrorView.showError("Spielbeitritt fehlgeschlagen. HTTP-Code: " + response.statusCode());
        }
        System.out.println("Spielername: " + playerData.getName());
    }

    // ===================================================================================
    //                                 < LOBBY VIEW >
    // ===================================================================================

    /**
     * Initialisiert die Lobby-Ansicht, indem Event Listener für den "Exit", "Ready" und "Start" Button gesetzt werden.
     * Hier wird unter anderem der Status des Spielers (bereit/nicht bereit) an den Server gesendet.
     */
    public void setUpLobbyView() {
        ViewIF.LobbyViewIF lobbyView = mainView.getLobbyView();

        // -------------------------------------------------------------------------------
        // EVENT LISTENER: Exit-Button
        // -------------------------------------------------------------------------------
        lobbyView.setExitButtonListener(e -> {
            playerExitLobby();
        });

        // -------------------------------------------------------------------------------
        // EVENT LISTENER: Ready-Button
        // -------------------------------------------------------------------------------
        lobbyView.setReadyButtonListener(e -> {
            playerData.setReady(!playerData.isReady());
            sendReadyStatusToServer(playerData.isReady());

            // Ändere die Button-Farbe basierend auf dem Bereitschaftsstatus
            Color notReadyColor = Color.decode("#780C28"); // Benutzerdefiniertes Rot

            if (playerData.isReady()) {
                lobbyView.getReadyButton().setBackground(new Color(37, 50, 33)); // Bereit: Grün
                lobbyView.getReadyButton().setText("Ready");
            } else {
                lobbyView.getReadyButton().setBackground(notReadyColor);   // Nicht bereit: Rot
                lobbyView.getReadyButton().setText("Not Ready");
            }
        });

        // -------------------------------------------------------------------------------
        // EVENT LISTENER: Start-Button
        // -------------------------------------------------------------------------------
        lobbyView.setStartButtonListener(e -> {
            try {
                sendGameIsStarted();
                mainView.toggleView("Game");
            } catch (IOException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            // TODO: Funktion implementieren, die den Spielbeitritt bestätigt, falls alle Spieler bereit sind
        });
    }

    /**
     * Sendet den Bereitschaftsstatus des Spielers an den Server.
     *
     * @param ready der neue Bereitschaftsstatus des Spielers
     */
    private void sendReadyStatusToServer(boolean ready) {
        try {
            // Setzt den Bereitschaftsstatus
            playerData.setReady(ready);

            // Initialisiert den JSON-Body mit den Spieler-Daten
            Gson gson = new Gson();
            String jsonRequest = gson.toJson(playerData);

            System.out.println("Sende Anfrage: " + playerData.getId());
            System.out.println("Sende Anfrage: " + playerData.getName());

            // Bereitet die PUT-Anfrage vor
            HttpRequest putRequest = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + "/games/" + gameData.getId() + "/players/" + playerData.getId() + "/ready"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonRequest))
                    .build();

            // Sendet die Anfrage und empfängt die Antwort
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(putRequest, HttpResponse.BodyHandlers.ofString());

            System.out.println("Antwort: " + response.body());

        } catch (URISyntaxException e) {
            System.err.println("Ungültige URL: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Netzwerkfehler: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Anfrage unterbrochen: " + e.getMessage());
            Thread.currentThread().interrupt(); // Wiederherstellen des Interrupt-Status
        }
    }

    /**
     * Informiert den Server, dass das Spiel gestartet wurde.
     * Es wird eine PUT-Anfrage gesendet, die den Spielstatus aktualisiert.
     *
     * @throws IOException bei Netzwerkfehlern
     * @throws InterruptedException wenn der Request unterbrochen wird
     */
    public void sendGameIsStarted() throws IOException, InterruptedException {
        gameData.setGameStarted(true);
        gameData.setGameState("Game");
        Gson gson = new Gson();
        String gameJsonBody = gson.toJson(gameData);

        HttpRequest putRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/games/" + gameData.getId() + "/start"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(gameJsonBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(putRequest, HttpResponse.BodyHandlers.ofString());
    }

    // ===================================================================================
    //                                 < GAME VIEW >
    // ===================================================================================

    /**
     * Initialisiert die Spielansicht und setzt Event Listener für den "Exit"-Button
     * sowie den "Würfeln"-Button. Beim Würfeln werden die Spielfeldpositionen aktualisiert.
     */
    public void setUpGameView() {
        ViewIF.GameViewIF gameView = mainView.getGameView();

        // -------------------------------------------------------------------------------
        // EVENT LISTENER: Exit-Button
        // -------------------------------------------------------------------------------
        gameView.setExitButtonListener(e -> {
            playerExitGame();
        });

        // -------------------------------------------------------------------------------
        // EVENT LISTENER: Würfeln-Button
        // -------------------------------------------------------------------------------
        gameView.getRollDiceButton().addActionListener(e -> {
            mainView.getGameView().clearSection(playerData.getPostion(), playerData.playerIndex - 1);
            requestDiceRoll();
            try {
                updatePostion();
            } catch (IOException | InterruptedException | URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Fordert einen Würfelwurf vom Server an, sofern der Spieler an der Reihe ist.
     * Aktualisiert danach den Würfelwert in der Spielansicht.
     */
    public void requestDiceRoll() {
        if (!isMyTurn()) {
            return;
        }
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + "/games/" + gameData.getId() + "/rollDice"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                Gson gson = new Gson();
                gameData = gson.fromJson(response.body(), GameData.class);

                System.out.println("Empfangener Würfelwert: " + gameData.getLastDiceRoll());
                SwingUtilities.invokeLater(() -> {
                    try {
                        updateDiceDisplay(gameData.getLastDiceRoll());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else {
                ErrorView.showError("Würfeln fehlgeschlagen. HTTP-Code: " + response.statusCode());
            }
        } catch (Exception ex) {
            ErrorView.showError("Fehler beim Anfordern des Würfelwurfs: " + ex.getMessage());
        }
    }

    /**
     * Aktualisiert die Position des Spielers basierend auf dem letzten Würfelwurf
     * und sendet die neuen Daten an den Server.
     *
     * @throws IOException bei Netzwerkfehlern
     * @throws InterruptedException wenn der Request unterbrochen wird
     * @throws URISyntaxException wenn die URL fehlerhaft ist
     */
    public void updatePostion() throws IOException, InterruptedException, URISyntaxException {
        playerData.increasePostion(gameData.getLastDiceRoll());
        Gson gson = new Gson();
        String jsonRequest = gson.toJson(playerData);
        System.out.println("Lokale Spielerposition: " + jsonRequest);

        HttpRequest putRequest = HttpRequest.newBuilder()
                .uri(new URI(baseUrl + "/games/" + gameData.getId() + "/players/" + playerData.getId() + "/postion"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(putRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println("Antwort des Spielers: " + response.body());
    }

    /**
     * Aktualisiert die Anzeige des Würfelwerts in der Spielansicht.
     * Es wird ein entsprechendes Bild für den aktuellen Würfelwert geladen.
     *
     * @param diceValue der aktuelle Würfelwert
     * @throws IOException falls das Bild nicht geladen werden kann
     */
    public void updateDiceDisplay(int diceValue) throws IOException {
        JLabel displayLabel = mainView.getGameView().getDiceDisplay();
        if (displayLabel != null) {

            // Show dice value in text
            String statusMessage = "Dice: " + diceValue;
            displayLabel.setText(statusMessage);

            // Decide which resource path to use based on diceValue
            String imagePath;
            switch (diceValue) {
                case 1:
                    imagePath = "/DicePics/dice-six-faces-one.png";
                    break;
                case 2:
                    imagePath = "/DicePics/dice-six-faces-two.png";
                    break;
                case 3:
                    imagePath = "/DicePics/dice-six-faces-three.png";
                    break;
                case 4:
                    imagePath = "/DicePics/dice-six-faces-four.png";
                    break;
                case 5:
                    imagePath = "/DicePics/dice-six-faces-five.png";
                    break;
                case 6:
                    imagePath = "/DicePics/dice-six-faces-six.png";
                    break;
                default:
                    imagePath = "/DicePics/dice-idle.png";
                    break;
            }

            // Load the resource from the classpath
            try (InputStream is = getClass().getResourceAsStream(imagePath)) {
                if (is == null) {
                    // If this prints, your path might be wrong or the file isn't on the classpath
                    System.err.println("Resource not found: " + imagePath);
                    return;
                }
                // Read the image
                BufferedImage diceImage = ImageIO.read(is);

                // Set it as the label’s icon
                displayLabel.setIcon(new ImageIcon(diceImage));
            }
        }
    }


    /**
     * Prüft, ob der aktuelle Spieler an der Reihe ist.
     *
     * @return true, wenn der aktuelle Spieler an der Reihe ist, ansonsten false
     */
    private boolean isMyTurn() {
        if (playerData == null || gameData == null) {
            return false;
        }
        return playerData.getName().trim().equalsIgnoreCase(gameData.getCurrentPlayerUsername().trim());
    }

    // ===================================================================================
    //                             < Score und Aktualisierung >
    // ===================================================================================

    /**
     * Bereitet die Daten für das Scoreboard vor.
     *
     * @return eine Liste von Maps, die den Namen und Punktestand jedes Spielers enthalten
     */
    private List<Map<String, Object>> prepareScoreBoardData() {
        List<Map<String, Object>> scoreBoardData = new ArrayList<>();
        for (PlayerData player : gameData.getPlayers()) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", player.getName());
            map.put("score", player.getScore());
            scoreBoardData.add(map);
        }
        return scoreBoardData;
    }

    /**
     * Aktualisiert den Punktestand der Spieler anhand ihrer aktuellen Position.
     */
    private void updatePlayerScores() {
        for (PlayerData player : gameData.getPlayers()) {
            // Hier wird die aktuelle Spielfeldposition als Punktestand verwendet.
            player.setScore(player.getPostion());
        }
    }

    // ===================================================================================
    //                        < Start des Pollings und Updates >
    // ===================================================================================

    /**
     * Startet das periodische Abrufen von Spieldaten vom Server.
     * Abhängig vom Spielstatus werden die entsprechenden Ansichten (Lobby oder Spiel) aktualisiert.
     */
    public void startPolling() {
        System.out.println("Polling wird gestartet");
        scheduler.scheduleAtFixedRate(() -> {
            try {
                if (Objects.equals(gameData.getGameState(), "Game")) {
                    updateGameView();
                } else if (Objects.equals(gameData.getGameState(), "Lobby")) {
                    updateLobbyView();
                }
                receiveUpdate();
            } catch (IOException e) {
                System.err.println("Netzwerkfehler: " + e.getMessage());
            } catch (InterruptedException e) {
                System.err.println("Anfrage unterbrochen: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }, 0, 100, TimeUnit.MILLISECONDS); // Polling alle 100ms
    }

    /**
     * Stoppt das periodische Abrufen von Spieldaten.
     */
    public void stopPolling() {
        System.out.println("Polling wird gestoppt...");
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow(); // Stoppt alle geplanten Aufgaben sofort
        }
    }

    /**
     * Aktualisiert die Lobby-Ansicht basierend auf den aktuellen Spieldaten.
     * Zeigt die Liste der beigetretenen Spieler an und prüft, ob alle Spieler bereit sind.
     *
     * @throws IOException bei Netzwerkfehlern
     * @throws InterruptedException wenn der Request unterbrochen wird
     */
    public void updateLobbyView() throws IOException, InterruptedException {
        ViewIF.LobbyViewIF lobbyView = mainView.getLobbyView();

        StringBuilder playersText = new StringBuilder();
        boolean playerStillInGame = false; // Überprüft, ob der lokale Spieler noch im Spiel ist

        for (PlayerData player : gameData.getPlayers()) {
            playersText.append("Player ")
                    .append(player.getPlayerIndex())
                    .append(": ")
                    .append(player.getName())
                    .append(player.isReady() ? " <----------ready---------> " : " <----------not ready---------> ")
                    .append("\n");

            // Prüfen, ob der aktuelle Spieler noch im Spiel ist
            if (playerData != null && player.getId().equals(playerData.getId())) {
                playerStillInGame = true;
            }
        }

        lobbyView.getJoinedPlayersArea().setText(playersText.toString());

        // Start-Button aktivieren, wenn alle Spieler bereit sind
        if (gameData.isAllPlayersReady() && gameData.players.size ()> 1) {
            lobbyView.getStartButton().setEnabled(true);
            lobbyView.getStartButton().setBackground(new Color(223, 197, 197));
            lobbyView.getStartButton().setForeground(Color.BLACK);
        } else {
            lobbyView.getStartButton().setEnabled(false);
            lobbyView.getStartButton().setBackground(new Color(38, 48, 35));
        }

        // Überprüfen: Falls der Spieler nicht mehr im Spiel ist, zur Startansicht zurückkehren
        if (!playerStillInGame) {
            System.out.println("Spieler ist nicht mehr im Spiel. Zurück zur Startansicht.");
            playerData = null;
            gameData = null;
            mainView.toggleView("Start");
            return;
        }

        // Wechsel zur Spielansicht, falls das Spiel gestartet wurde
        if (gameData.isGameStarted()) {
            System.out.println("Spiel gestartet, Wechsel zur Spielansicht");
            mainView.toggleView("Game");
        }
    }

    /**
     * Aktualisiert die Spielansicht mit den neuesten Spieldaten.
     * Überprüft zudem, ob ein Spieler die Gewinnbedingung erfüllt hat und zeigt gegebenenfalls die Gewinneransicht an.
     *
     * @throws IOException bei Netzwerkfehlern
     * @throws InterruptedException wenn der Request unterbrochen wird
     */
    public void updateGameView() throws IOException, InterruptedException {
        // Überprüfe Gewinnbedingung: Erreicht oder überschreitet ein Spieler Feld 63, wird dieser als Gewinner erklärt.
        for (PlayerData p : gameData.getPlayers()) {
            if (p.getPostion() >= 63) {  // Annahme: 63 ist das Zielfeld
                SwingUtilities.invokeLater(() -> {
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainView.getGameView());
                    WinnerView winnerView = new WinnerView(p.getName(), frame.getSize());
                    frame.getContentPane().removeAll();
                    frame.add(winnerView);
                    frame.revalidate();
                    frame.repaint();
                });
                stopPolling();  // Beende das Polling, da ein Gewinner feststeht
                return;
            }
        }

        // Now update the rest of the UI inside a single EDT block
        SwingUtilities.invokeLater(() -> {
            // Switch to the Game view.
            mainView.toggleView("Game");

            // Update static UI elements.
            mainView.getGameView().getScoreArea().setText(playerData.getName());
            try {
                updateDiceDisplay(gameData.getLastDiceRoll());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Update the roll button state.
            if (!gameData.getCurrentPlayerUsername().trim().equalsIgnoreCase(playerData.getName().trim())) {
                mainView.getGameView().getRollDiceButton().setText("Wait");
                mainView.getGameView().getRollDiceButton().setEnabled(false);
            } else {
                mainView.getGameView().getRollDiceButton().setText("Roll");
                mainView.getGameView().getRollDiceButton().setEnabled(true);
            }

            // Update current player text.
            mainView.getGameView().getCurrentPlayerTextPane()
                    .setText("Current Player: " + gameData.getCurrentPlayerUsername());

            // Update scoreboard.
            updatePlayerScores();
            List<Map<String, Object>> playerDataList = prepareScoreBoardData();
            mainView.getGameView().updateScoreBoard(playerDataList);

            // Clear the entire board.
            mainView.getGameView().clearAllCells();

            // Place players on the board.
            for (PlayerData p : gameData.getPlayers()) {
                System.out.println("Placing " + p.getName() + " at field " + p.getPostion());
                int boardIndex = p.getPostion();          // Use the position from the server or local data
                int sectionIndex = p.getPlayerIndex() - 1;  // Adjust index if needed
                mainView.getGameView().movePlayerPosition(boardIndex, sectionIndex, p.getName());
            }
        });
    }

    /**
     * Ruft aktuelle Spieldaten vom Server ab und aktualisiert das Spiel.
     * Bei bestimmten Bedingungen (z. B. Spiel nicht gefunden) wird die Ansicht zurückgesetzt.
     *
     * @throws IOException bei Netzwerkfehlern
     * @throws InterruptedException wenn der Request unterbrochen wird
     */
    public void receiveUpdate() throws IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/games/" + gameData.getId()))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println("Antwort: " + response.body());

        // Falls das Spiel nicht gefunden wurde, zur Startansicht wechseln
        if (response.statusCode() == 404 || response.body().isEmpty()) {
            System.out.println("Spiel nicht gefunden. Zurück zur Startansicht.");
            playerData = null;
            gameData = null;
            mainView.toggleView("Start");
            return;
        }

        Gson gson = new Gson();
        gameData = gson.fromJson(response.body(), GameData.class);

        // Falls nur ein Spieler in der Lobby ist und das Spiel noch nicht gestartet wurde, ignoriere das Update
        if (gameData.getPlayers().size() == 1 && !gameData.isGameStarted()) {
            System.out.println("Spiel noch in der Lobby mit einem Spieler. Update wird ignoriert.");
            return;
        }

        // Falls nach Spielstart nur noch ein Spieler übrig ist, beende das Spiel
        if (gameData.getPlayers().size() == 1 && gameData.isGameStarted()) {
            System.out.println("Spiel beendet, da nach dem Start nur noch ein Spieler übrig ist. Zurück zur Startansicht.");
            playerData = null;
            gameData = null;
            mainView.toggleView("Start");
            stopPolling();
        }
    }

    //========================================================================================
    //                      < Exit-Button in der Spielansicht >
    //========================================================================================

    /**
     * Beendet den Spielbeitritt, indem eine DELETE-Anfrage an den Server gesendet wird,
     * und wechselt zurück zur Startansicht.
     */
    public void playerExitGame() {
        try {
            if (playerData != null && gameData != null) {
                HttpRequest deleteRequest = HttpRequest.newBuilder()
                        .uri(new URI(baseUrl + "/games/" + gameData.getId() + "/players/" + playerData.getId()))
                        .header("Content-Type", "application/json")
                        .DELETE()
                        .build();

                HttpClient client = HttpClient.newHttpClient();
                HttpResponse<String> response = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());

                System.out.println("Antwort beim Verlassen des Spiels: " + response.body());

                // Lösche Spieler- und Spieldaten
                playerData = null;
                gameData = null;

                // Wechsel zur Startansicht
                mainView.toggleView("Start");
            }
        } catch (Exception e) {
            ErrorView.showError("Spielverlassen fehlgeschlagen: " + e.getMessage());
        }
    }

    //========================================================================================
    //                      < Exit-Button in der Lobbyansicht >
    //========================================================================================

    /**
     * Beendet den Lobbybeitritt, indem eine DELETE-Anfrage an den Server gesendet wird,
     * und wechselt zurück zur Startansicht.
     */
    public void playerExitLobby() {
        try {
            if (playerData != null && gameData != null) {
                HttpRequest deleteRequest = HttpRequest.newBuilder()
                        .uri(new URI(baseUrl + "/games/" + gameData.getId() + "/players/" + playerData.getId()))
                        .header("Content-Type", "application/json")
                        .DELETE()
                        .build();

                HttpClient client = HttpClient.newHttpClient();
                HttpResponse<String> response = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());

                System.out.println("Antwort beim Verlassen der Lobby: " + response.body());
                playerData = null;
                gameData = null;
                mainView.toggleView("Start");
            }
        } catch (Exception e) {
            ErrorView.showError("Lobby-Verlassen fehlgeschlagen: " + e.getMessage());
        }
    }

    // ===================================================================================
    //                      < Innere Klassen: GameData und PlayerData >
    // ===================================================================================

    /**
     * Die Klasse GameData repräsentiert die Spieldaten, die vom Server abgerufen werden.
     * Sie enthält Informationen wie die Spiel-ID, den Hostnamen, den Status des Spiels und die Liste der Spieler.
     */
    public static class GameData {
        /** Eindeutige Spiel-ID */
        private long id;
        /** Name des Spiel-Hosts */
        private String gameHostName;
        /** Maximale Anzahl der Spieler im Spiel */
        private int maxPlayersNumber = 4;
        /** Gibt an, ob alle Spieler bereit sind */
        private boolean allPlayersAreReady = false;
        /** Der zuletzt gewürfelte Wert */
        private int lastDiceRoll;
        /** Benutzername des Spielers, der an der Reihe ist */
        private String currentPlayerUsername;
        /** Der Spieler, dessen Zug aktuell ist */
        private PlayerData currentPlayerTurn;
        /** Gibt an, ob das Spiel gestartet wurde */
        private boolean gameStarted = true;
        /** Der aktuelle Spielstatus (z. B. "Lobby" oder "Game") */
        private String gameState;
        /** Liste der Spieler im Spiel */
        private java.util.List<PlayerData> players = new ArrayList<>();
        /** Aktuelle Anzahl der Spieler */
        private int currentPlayersCount = 0;

        /**
         * Gibt den aktuellen Spielstatus zurück.
         *
         * @return der Spielstatus
         */
        public String getGameState(){
            return gameState;
        }

        /**
         * Setzt den Spielstatus.
         *
         * @param gameState der neue Spielstatus
         */
        public void setGameState(String gameState){
            this.gameState = gameState;
        }

        /**
         * Setzt, ob das Spiel gestartet wurde.
         *
         * @param gameStarted true, wenn das Spiel gestartet wurde, sonst false
         */
        public void setGameStarted(boolean gameStarted) {
            this.gameStarted = gameStarted;
        }

        /**
         * Prüft, ob das Spiel gestartet wurde.
         *
         * @return true, wenn das Spiel gestartet wurde, sonst false
         */
        public boolean isGameStarted() {
            return gameStarted;
        }

        /**
         * Gibt die Spiel-ID zurück.
         *
         * @return die Spiel-ID
         */
        public long getId() {
            return id;
        }

        /**
         * Setzt die Spiel-ID.
         *
         * @param id die neue Spiel-ID
         */
        public void setId(long id) {
            this.id = id;
        }

        /**
         * Gibt den Namen des Spiel-Hosts zurück.
         *
         * @return der Name des Spiel-Hosts
         */
        public String getGameHostName() {
            return gameHostName;
        }

        /**
         * Setzt den Namen des Spiel-Hosts.
         *
         * @param gameHostName der neue Name des Spiel-Hosts
         */
        public void setGameHostName(String gameHostName) {
            this.gameHostName = gameHostName;
        }

        /**
         * Gibt die maximale Anzahl der Spieler zurück.
         *
         * @return die maximale Anzahl der Spieler
         */
        public int getMaxPlayersNumber() {
            return maxPlayersNumber;
        }

        /**
         * Setzt die maximale Anzahl der Spieler.
         *
         * @param maxPlayersNumber die maximale Anzahl der Spieler
         */
        public void setMaxPlayersNumber(int maxPlayersNumber) {
            this.maxPlayersNumber = maxPlayersNumber;
        }

        /**
         * Prüft, ob alle Spieler bereit sind.
         *
         * @return true, wenn alle Spieler bereit sind, sonst false
         */
        public boolean isAllPlayersReady() {
            return allPlayersAreReady;
        }

        /**
         * Setzt, ob alle Spieler bereit sind.
         *
         * @param allPlayersAreReady true, wenn alle Spieler bereit sind, sonst false
         */
        public void setAllPlayersReady(boolean allPlayersAreReady) {
            this.allPlayersAreReady = allPlayersAreReady;
        }

        /**
         * Gibt den zuletzt gewürfelten Wert zurück.
         *
         * @return der zuletzt gewürfelte Wert
         */
        public int getLastDiceRoll() {
            return lastDiceRoll;
        }

        /**
         * Gibt den Benutzernamen des aktuellen Spielers zurück.
         *
         * @return der Benutzername des aktuellen Spielers
         */
        public String getCurrentPlayerUsername() {
            return currentPlayerUsername;
        }

        /**
         * Gibt die Liste der Spieler zurück.
         *
         * @return die Liste der Spieler
         */
        public java.util.List<PlayerData> getPlayers() {
            return players;
        }

        /**
         * Setzt die Liste der Spieler.
         *
         * @param players die neue Liste der Spieler
         */
        public void setPlayers(List<PlayerData> players) {
            this.players = players;
        }

        /**
         * Gibt die aktuelle Anzahl der Spieler zurück.
         *
         * @return die Anzahl der Spieler
         */
        public int getCurrentPlayersCount() {
            return currentPlayersCount;
        }

        /**
         * Setzt die aktuelle Anzahl der Spieler.
         *
         * @param currentPlayersCount die neue Anzahl der Spieler
         */
        public void setCurrentPlayersCount(int currentPlayersCount) {
            this.currentPlayersCount = currentPlayersCount;
        }
    }

    /**
     * Die Klasse PlayerData repräsentiert die Daten eines Spielers.
     * Sie enthält Informationen wie die Spieler-ID, den Namen, den Bereitschaftsstatus, die Position und den Punktestand.
     */
    public static class PlayerData {
        /** Eindeutige Spieler-ID */
        private Long id;
        /** Index des Spielers (beginnend bei 1) */
        private int playerIndex = 1;
        /** Name des Spielers */
        private String name;
        /** Bereitschaftsstatus des Spielers */
        private boolean ready = false;
        /** Aktuelle Position des Spielers auf dem Spielfeld */
        private int postion = 0;
        /** Aktueller Punktestand des Spielers */
        private int score = 0;

        /**
         * Gibt die aktuelle Position zurück.
         *
         * @return die Position des Spielers
         */
        public int getPostion() {
            return postion;
        }

        /**
         * Erhöht die Position des Spielers um den angegebenen Wert.
         *
         * @param postion der Wert, um den die Position erhöht werden soll
         */
        public void increasePostion(int postion) {
            this.postion += postion;
        }

        /**
         * Gibt die Spieler-ID zurück.
         *
         * @return die Spieler-ID
         */
        public Long getId() {
            return id;
        }

        /**
         * Setzt die Spieler-ID.
         *
         * @param id die neue Spieler-ID
         */
        public void setId(Long id) {
            this.id = id;
        }

        /**
         * Gibt den Spielerindex zurück.
         *
         * @return der Spielerindex
         */
        public int getPlayerIndex() {
            return playerIndex;
        }

        /**
         * Setzt den Spielerindex.
         *
         * @param playerIndex der neue Spielerindex
         */
        public void setPlayerIndex(int playerIndex) {
            this.playerIndex = playerIndex;
        }

        /**
         * Gibt den Namen des Spielers zurück.
         *
         * @return der Name des Spielers
         */
        public String getName() {
            return name;
        }

        /**
         * Setzt den Namen des Spielers.
         *
         * @param name der neue Name des Spielers
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Prüft, ob der Spieler bereit ist.
         *
         * @return true, wenn der Spieler bereit ist, sonst false
         */
        public boolean isReady() {
            return ready;
        }

        /**
         * Setzt den Bereitschaftsstatus des Spielers.
         *
         * @param ready true, wenn der Spieler bereit ist, sonst false
         */
        public void setReady(boolean ready) {
            this.ready = ready;
        }

        /**
         * Gibt den Punktestand des Spielers zurück.
         *
         * @return der Punktestand des Spielers
         */
        public int getScore() {
            return score;
        }

        /**
         * Setzt den Punktestand des Spielers.
         *
         * @param score der neue Punktestand des Spielers
         */
        public void setScore(int score) {
            this.score = score;
        }
    }
}
