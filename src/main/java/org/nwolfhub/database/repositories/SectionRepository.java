package org.nwolfhub.database.repositories;

import org.nwolfhub.database.model.Field;
import org.nwolfhub.database.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, String> {
    Optional<Section> findSectionByNameIgnoreCase(String name);
    List<Section> findTop5ByNameLikeIgnoreCase(String name);
}
