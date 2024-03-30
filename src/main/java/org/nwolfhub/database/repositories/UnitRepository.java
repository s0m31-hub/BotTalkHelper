package org.nwolfhub.database.repositories;

import org.nwolfhub.database.model.Field;
import org.nwolfhub.database.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnitRepository extends JpaRepository<Unit, String> {
    public List<Unit> getAllByDescriptionNot(String description); //aka getall
}
