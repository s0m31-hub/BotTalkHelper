package org.nwolfhub.database.repositories;

import org.nwolfhub.database.model.Field;
import org.nwolfhub.database.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<Unit, String> {

}
