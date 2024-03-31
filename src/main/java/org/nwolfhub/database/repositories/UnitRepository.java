package org.nwolfhub.database.repositories;

import org.nwolfhub.database.model.Field;
import org.nwolfhub.database.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit, String> {
    List<Unit> getAllByDescriptionNot(String description); //aka getall
    Optional<Unit> findUnitByNameIgnoreCase(String name);
    List<Unit> findTop5ByNameLikeIgnoreCase(String name);
}
