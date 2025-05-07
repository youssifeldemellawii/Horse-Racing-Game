package org.dataTransfer.server.ServerController;

import org.dataTransfer.server.ServerModell.game.Game;
import org.dataTransfer.server.ServerModell.player.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API interface for game and player management.
 */
@RequestMapping("/api/games")
public interface API {

    /**
     * Retrieves all available games.
     * @return A list of all games.
     */
    @GetMapping
    List<Game> getAllGames();

    /**
     * Retrieves a specific game by its ID.
     * @param id The game ID.
     * @return The game with the given ID.
     */
    @GetMapping("/{id}")
    Game getGameById(@PathVariable Long id);

    /**
     * Retrieves all players in a specific game.
     * @param gameID The ID of the game.
     * @return A list of players in the specified game.
     */
    @GetMapping("/{gameID}/players")
    List<Player> getAllPlayersInGame(@PathVariable Long gameID);

    /**
     * Creates a new game lobby with a host player.
     * @param player The player creating the lobby.
     * @return The newly created game.
     */
    @PostMapping
    Game createLobby(@RequestBody Player player);

    /**
     * Allows a player to join an existing game lobby.
     * @param id The game ID.
     * @param player The player joining the lobby.
     * @return The updated game instance.
     */
    @PutMapping("/{id}/join")
    Game joinLobby(@PathVariable Long id, @RequestBody Player player);

    /**
     * Rolls the dice for the current player and advances to the next turn.
     * @param gameID The ID of the game.
     * @return The updated game state.
     */
    @PutMapping("/{gameID}/rollDice")
    ResponseEntity<Game> rollDice(@PathVariable Long gameID);

    /**
     * Updates a player's position in the game.
     * @param gameID The game ID.
     * @param playerID The player ID.
     * @param player The updated player details.
     * @return The updated player entity.
     */
    @PutMapping("/{gameID}/players/{playerID}/position")
    ResponseEntity<Player> updatePosition(@PathVariable Long gameID, @PathVariable Long playerID, @RequestBody Player player);

    /**
     * Updates the game details.
     * @param gameID The game ID.
     * @param game The updated game data.
     * @return The updated game entity.
     */
    @PutMapping("/{gameID}/update")
    ResponseEntity<Game> updatePlayer(@PathVariable Long gameID, @RequestBody Game game);

    /**
     * Updates the readiness status of a player.
     * @param gameId The game ID.
     * @param player The updated player data.
     * @return The updated player entity.
     */
    @PutMapping("/{gameId}/players/{playerIndex}/ready")
    ResponseEntity<Player> updateReadyStatus(@PathVariable Long gameId, @RequestBody Player player);

    /**
     * Starts the game when all conditions are met.
     * @param gameID The ID of the game.
     * @param game The game instance (optional).
     * @return The updated game state.
     */
    @PutMapping("/{gameID}/start")
    ResponseEntity<Game> startGame(@PathVariable Long gameID, @RequestBody Game game);

    /**
     * Removes a player from a game.
     * @param gameId The game ID.
     * @param playerId The player ID to remove.
     * @return A response message indicating success or failure.
     */
    @DeleteMapping("/{gameId}/players/{playerId}")
    ResponseEntity<String> removePlayer(@PathVariable Long gameId, @PathVariable Long playerId);
}
