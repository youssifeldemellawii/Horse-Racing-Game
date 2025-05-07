package org.dataTransfer.server.ServerModell;

import org.dataTransfer.server.ServerModell.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Das Interface GameRepository dient als Repository für die Entität {@link Game}.
 * Es erweitert {@link JpaRepository} und ermöglicht somit grundlegende CRUD-Operationen
 * auf Game-Objekten.
 *
 * <p>
 * Weitere benutzerdefinierte Abfragemethoden können bei Bedarf hinzugefügt werden.
 * </p>
 *
 * @see JpaRepository
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

}
