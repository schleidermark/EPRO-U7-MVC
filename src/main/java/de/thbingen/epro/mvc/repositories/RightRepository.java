package de.thbingen.epro.mvc.repositories;

import de.thbingen.epro.mvc.model.Right;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RightRepository extends CrudRepository<Right, Long> {

    Right findByName(@Param("name") String name);
}
