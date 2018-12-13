package de.thbingen.epro.mvc.repositories;

import de.thbingen.epro.mvc.model.Group;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends CrudRepository<Group, Long> {

    Group findByName(@Param("name") String name);
}
