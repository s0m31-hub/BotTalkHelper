package org.nwolfhub.database.repositories;

import org.nwolfhub.database.model.Field;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FieldRepository extends JpaRepository<Field, String> {
    Optional<Field> findFieldByNameIgnoreCase(String name);
    List<Field> findTop5ByNameLikeIgnoreCase(String name);
}
