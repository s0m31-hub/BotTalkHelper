package org.nwolfhub.database.repositories;

import org.nwolfhub.database.model.Field;
import org.nwolfhub.database.model.PreparedMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessagesRepository extends JpaRepository<PreparedMessage, String> {
    List<PreparedMessage> getPreparedMessagesByGlobal(boolean global);
    List<PreparedMessage> getPreparedMessagesByOwner(Long owner);
    List<PreparedMessage> getPreparedMessagesByGlobalOrOwner(boolean global, Long owner);
    Optional<PreparedMessage> findPreparedMessageById(Long id);
    Integer countByOwner(Long owner);
}
