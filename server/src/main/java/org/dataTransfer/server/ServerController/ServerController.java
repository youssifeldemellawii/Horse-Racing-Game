package org.dataTransfer.server.ServerController;

import org.dataTransfer.server.ServerModell.game.Game;
import org.dataTransfer.server.ServerModell.player.Player;
import org.dataTransfer.server.ServerModell.GameRepository;
import org.dataTransfer.server.ServerModell.PlayerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Der {@code ServerController} stellt REST-Endpunkte zur Verwaltung von Spielen und Spielern bereit.
 * Er ermöglicht das Abrufen, Erstellen, Aktualisieren und Löschen von Spielen sowie das Verwalten von
 * Spieleraktionen wie dem Würfeln, Beitreten oder Verlassen eines Spiels.
 *
 * <p>
 * Die Endpunkte werden unter dem Pfad <code>api/games</code> bereitgestellt.
 * </p>
 */
@RestController
@RequestMapping("api/games")
public class ServerController implements API{

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    /**
     * Konstruktor des ServerControllers.
     *
     * @param gameRepository   das Repository für Spiele
     * @param playerRepository das Repository für Spieler
     */
    public ServerController(GameRepository gameRepository, PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }

    /**
     * Liefert eine Liste aller existierenden Spiele.
     *
     * @return Liste der Spiele
     */
    @GetMapping
    public List<Game> getAllGames(){
        System.out.println("Fetching all games...");
        return gameRepository.findAll();
    }

    /**
     * Liefert das Spiel mit der angegebenen ID.
     *
     * @param id die ID des gesuchten Spiels
     * @return das Spiel, falls gefunden
     * @throws RuntimeException falls kein Spiel mit der angegebenen ID existiert
     */
    @GetMapping("/{id}")
    public Game getGameById(@PathVariable Long id){
        return gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found with id " + id));
    }

    /**
     * Liefert alle Spieler, die einem bestimmten Spiel beigetreten sind.
     *
     * @param gameID die ID des Spiels
     * @return Liste der Spieler im Spiel
     * @throws RuntimeException falls das Spiel nicht gefunden wird
     */
    @GetMapping("/{gameID}/players")
    public List<Player> getAllPlayersInGame(@PathVariable Long gameID){
        System.out.println("Fetching all players...");
        Game savedGame = gameRepository.findById(gameID)
                .orElseThrow(() -> new RuntimeException("Game not found with id " + gameID));
        return savedGame.getPlayers();
    }

    /**
     * Erstellt eine neue Lobby (Spiel) und fügt den übergebenen Spieler als Host hinzu.
     *
     * @param player der Spieler, der das Spiel erstellt (wird als Host gesetzt)
     * @return das neu erstellte Spiel
     */
    @PostMapping
    public Game createLobby (@RequestBody Player player){
        Player hostPlayer = playerRepository.save(player);
        Game newGame = new Game();
        newGame.setGameHostName(hostPlayer.getName());
        newGame.addPlayer(hostPlayer);
        gameRepository.save(newGame);
        hostPlayer = playerRepository.save(hostPlayer);
        System.out.println("Creating new game by player " + hostPlayer.getName() + " ...");
        return newGame;
    }

    /**
     * Ermöglicht es einem Spieler, einer bestehenden Lobby beizutreten.
     *
     * @param id     die ID des Spiels, dem beigetreten werden soll
     * @param player der Spieler, der beitreten möchte
     * @return das aktualisierte Spiel, nachdem der Spieler hinzugefügt wurde
     * @throws RuntimeException falls das Spiel nicht existiert oder voll ist
     */
    @PutMapping("/{id}/join")
    public Game joinLobby (@PathVariable Long id, @RequestBody Player player){
        System.out.println("Joining game with id: " + id);
        Game savedGame = gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found with id" + id));
        savedGame.setAllPlayersAreReady(false);
        System.out.println("players count: " + savedGame.getCurrentPlayersCount());

        if(savedGame.getCurrentPlayersCount() < savedGame.getMaxPlayersNumber()){
            savedGame.addPlayer(player);
            gameRepository.save(savedGame);
            return savedGame;
        } else {
            throw new RuntimeException("Game with ID " + id + " is full");
        }
    }

    /**
     * Führt einen Würfelwurf im Spiel durch und aktualisiert den Zugstatus.
     *
     * @param gameID die ID des Spiels, in dem gewürfelt wird
     * @return ResponseEntity, die das aktualisierte Spiel enthält, oder einen Bad Request, falls der aktuelle Spieler nicht an der Reihe ist
     * @throws RuntimeException falls das Spiel nicht gefunden wird
     */
    @PutMapping("/{gameID}/rollDice")
    public ResponseEntity<Game> rollDice(@PathVariable Long gameID) {
        Game game = gameRepository.findById(gameID)
                .orElseThrow(() -> new RuntimeException("Game not found with ID " + gameID));

        // Überprüfe, ob es der korrekte Zug des aktuellen Spielers ist
        if (!game.getCurrentPlayerUsername().equals(
                game.getPlayers().get(game.getCurrentPlayerIndex()).getName())) {
            return ResponseEntity.badRequest().body(null);
        }

        int rolledValue = game.rollDiceAndNextTurn();
        gameRepository.save(game);

        System.out.println("Dice rolled: " + rolledValue + " by " + game.getCurrentPlayerUsername());
        return ResponseEntity.ok(game);
    }

    /**
     * Aktualisiert die Position eines Spielers im Spiel.
     * Es wird der Serverwert für die Position beibehalten, anstatt den übergebenen Wert zu überschreiben.
     *
     * @param gameID   die ID des Spiels
     * @param playerID die ID des Spielers, dessen Position aktualisiert werden soll
     * @param player   der Spieler, dessen Position übermittelt wird (wird hier nicht direkt verwendet)
     * @return ResponseEntity, die den aktualisierten Spieler enthält
     * @throws RuntimeException falls der Spieler nicht gefunden wird
     */
    @PutMapping("/{gameID}/players/{playerID}/postion")
    public ResponseEntity<Player> updatePosition(@PathVariable Long gameID,
                                                @PathVariable Long playerID,
                                                @RequestBody Player player){
        Player currentPlayer = playerRepository.findById(playerID)
                .orElseThrow(() -> new RuntimeException("Player not found with ID: " + playerID));
        System.out.println("Server updatePostion called. Current computed position: "
                + currentPlayer.getPostion());

        // Statt den übergebenen Wert zu übernehmen, wird der Serverwert verwendet.
        playerRepository.save(currentPlayer);
        return ResponseEntity.ok(currentPlayer);
    }

    /**
     * Aktualisiert bestimmte Attribute eines Spiels, wie den Hostnamen, die maximale Spielerzahl
     * und den Ready-Status der Spieler.
     *
     * @param gameID die ID des Spiels, das aktualisiert werden soll
     * @param game   das Spielobjekt mit den neuen Werten
     * @return ResponseEntity, die das aktualisierte Spiel enthält
     * @throws RuntimeException falls das Spiel nicht gefunden wird
     */
    @PutMapping("/{gameID}/update")
    public ResponseEntity<Game> updatePlayer(@PathVariable Long gameID, @RequestBody Game game) {
        // Hole das bestehende Spiel aus der Datenbank
        Game savedGame = gameRepository.findById(gameID)
                .orElseThrow(() -> new RuntimeException("Game not found with ID " + gameID));

        if (game.getGameHostName() != null) {
            savedGame.setGameHostName(game.getGameHostName());
        }
        if (game.getMaxPlayersNumber() > 0) {
            savedGame.setMaxPlayersNumber(game.getMaxPlayersNumber());
        }

        if (game.getPlayers() != null) {
            for (int i = 0; i < savedGame.getPlayers().size(); i++) {
                if (i < game.getPlayers().size() && game.getPlayers().get(i) != null) {
                    savedGame.getPlayers().get(i).setReady(game.getPlayers().get(i).isReady());
                }
            }
        }

        gameRepository.save(savedGame);
        System.out.println("Updated Game: " + savedGame);
        return ResponseEntity.ok(savedGame);
    }

    /**
     * Aktualisiert den Bereitschaftsstatus eines Spielers und prüft, ob alle Spieler bereit sind.
     *
     * @param gameId die ID des Spiels
     * @param player der Spieler, dessen Ready-Status aktualisiert werden soll
     * @return ResponseEntity, die den aktualisierten Spieler enthält
     * @throws RuntimeException falls das Spiel nicht gefunden wird
     */
    @PutMapping("/{gameId}/players/{playerIndex}/ready")
    public ResponseEntity<Player> updateReadyStatus(@PathVariable Long gameId, @RequestBody Player player) {
        // Hole das Spiel anhand der gameID
        Game savedGame = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found with ID " + gameId));

        // Überprüfe, ob der Spielerindex im gültigen Bereich liegt
        if (0 > player.getPlayerIndex() || player.getPlayerIndex() > savedGame.getCurrentPlayersCount()){
            return ResponseEntity.badRequest().body(player);
        }

        // Aktualisiere den Ready-Status des entsprechenden Spielers
        Player updatedPlayer = savedGame.getPlayers().get(player.getPlayerIndex() - 1);
        updatedPlayer.setReady(player.isReady());
        playerRepository.save(updatedPlayer);

        // Prüfe, ob alle Spieler bereit sind
        boolean allPlayersReady = savedGame.getPlayers().stream().allMatch(Player::isReady);
        savedGame.setAllPlayersAreReady(allPlayersReady);
        gameRepository.save(savedGame);

        System.out.println("Updated Game: " + savedGame.isAllPlayersAreReady());
        return ResponseEntity.ok(updatedPlayer);
    }

    /**
     * Startet ein Spiel, indem der Status auf "Game" gesetzt und das Spiel als gestartet markiert wird.
     *
     * @param gameID die ID des zu startenden Spiels
     * @param game   das Spielobjekt mit den neuen Start-Parametern (z. B. {@code gameStarted} und {@code gameState})
     * @return ResponseEntity, die das aktualisierte Spiel enthält
     * @throws RuntimeException falls das Spiel nicht gefunden wird
     */
    @PutMapping("/{gameID}/start")
    public ResponseEntity<Game> startGame(@PathVariable Long gameID, @RequestBody Game game) {
        Game savedGame = gameRepository.findById(gameID)
                .orElseThrow(() -> new RuntimeException("Game not found with ID " + gameID));
        savedGame.setGameStarted(game.isGameStarted());
        savedGame.setGameState(game.getGameState());
        gameRepository.save(savedGame);
        return ResponseEntity.ok(savedGame);
    }

    /**
     * Entfernt einen Spieler aus einem Spiel. Falls nach dem Entfernen keine Spieler mehr übrig sind,
     * wird das Spiel ebenfalls gelöscht.
     *
     * @param gameId   die ID des Spiels
     * @param playerId die ID des zu entfernenden Spielers
     * @return ResponseEntity mit einer Bestätigungsmeldung
     * @throws RuntimeException falls das Spiel oder der Spieler nicht gefunden wird
     */
    @DeleteMapping("/{gameId}/players/{playerId}")
    public ResponseEntity<String> removePlayer(@PathVariable Long gameId, @PathVariable Long playerId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found with ID " + gameId));

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with ID " + playerId));

        System.out.println("🔹 Entferne Spieler " + player.getName() + " aus Spiel " + gameId);
        game.getPlayers().remove(player);
        playerRepository.delete(player);

        // Falls das Spiel nach Entfernen leer ist, wird es gelöscht
        if (game.getPlayers().isEmpty()) {
            System.out.println("⚠️ Keine Spieler mehr in der Lobby. Lösche Spiel " + gameId);
            gameRepository.delete(game);
            return ResponseEntity.ok("Game deleted because it had no players left.");
        }

        gameRepository.save(game);
        return ResponseEntity.ok("Player removed successfully.");
    }
}
