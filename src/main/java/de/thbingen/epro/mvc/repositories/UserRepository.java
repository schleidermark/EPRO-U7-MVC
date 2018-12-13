package de.thbingen.epro.mvc.repositories;

import de.thbingen.epro.mvc.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmail(@Param("email") String email);
}
