package org.nwolfhub.database.repositories;

import org.nwolfhub.database.model.SentMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SentRepository extends JpaRepository<SentMessage, String> {
    public SentMessage findSentMessageById(String messageId);
}
