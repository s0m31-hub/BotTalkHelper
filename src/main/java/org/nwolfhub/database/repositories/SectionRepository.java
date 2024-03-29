package org.nwolfhub.database.repositories;

import org.nwolfhub.database.model.Field;
import org.nwolfhub.database.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, String> {
}
