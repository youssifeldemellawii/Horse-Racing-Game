package org.dataTransfer.server.ServerModell;

import org.dataTransfer.server.ServerModell.player.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Das Interface PlayerRepository dient als Repository für die Entität {@link Player}.
 * Es erweitert {@link JpaRepository} und ermöglicht damit grundlegende CRUD-Operationen
 * auf Player-Objekten.
 *
 * <p>
 * Weitere benutzerdefinierte Abfragemethoden können bei Bedarf implementiert werden.
 * </p>
 *
 * @see JpaRepository
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
}
