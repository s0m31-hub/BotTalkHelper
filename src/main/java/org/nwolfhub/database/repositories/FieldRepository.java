package org.nwolfhub.database.repositories;

import org.nwolfhub.database.model.Field;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FieldRepository extends JpaRepository<Field, String> {
}
