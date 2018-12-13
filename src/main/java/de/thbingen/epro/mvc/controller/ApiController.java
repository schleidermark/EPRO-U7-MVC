package de.thbingen.epro.mvc.controller;

import de.thbingen.epro.mvc.model.Group;
import de.thbingen.epro.mvc.model.Right;
import de.thbingen.epro.mvc.model.User;
import de.thbingen.epro.mvc.repositories.GroupRepository;
import de.thbingen.epro.mvc.repositories.RightRepository;
import de.thbingen.epro.mvc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(value = "/users", headers = "Accept=application/json")
public class ApiController {

    private UserRepository userRepository;
    private GroupRepository groupRepository;
    private RightRepository rightRepository;

    @Autowired
    public ApiController(UserRepository userRepository, GroupRepository groupRepository, RightRepository rightRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.rightRepository = rightRepository;
    }


    /**
     * Gibt eine Liste aller Benutzer zurück
     * @return
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {

        Iterable<User> userIt = userRepository.findAll();
        List<User> userList = StreamSupport.stream(userIt.spliterator(), false)
                .collect(Collectors.toList());

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }


    /**
     * Gibt einen einzelnen Benutzer zurück
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable("userId") Long userId) {
        Optional<User> optUser = userRepository.findById(userId);

        if (optUser.isPresent()) {
            return new ResponseEntity<>(optUser.get(), HttpStatus.OK);
        } else {
            throw new UserNotFoundException(userId);
        }
    }


    /**
     * Speichert einen neuen Benutzer in der Datenbank
     * @param user
     * @return
     */
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {

        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new EmailAlreadyInUseException(user.getEmail());
        }

        return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
    }


    /**
     * Führt ein Update eines gesamten Benutzers durch
     * @param userId
     * @param updateUser
     * @return
     */
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUserFull( @PathVariable("userId") Long userId,
                                                @Valid @RequestBody User updateUser) {

        User user = userRepository.findById(userId).get();

        if (user != null) {

            user.setFirstName(updateUser.getFirstName());
            user.setName(updateUser.getName());
            user.setPassword(updateUser.getPassword());
            user.setEmail(updateUser.getEmail());
            user.setGroup(updateUser.getGroup());

            return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);

        } else {
            throw new UserNotFoundException(userId);
        }
    }


    /**
     * Führt ein partielles Update eines Benutzers durch
     * @param userId
     * @param updateUser
     * @return
     */
    @PatchMapping("/{userId}")
    public ResponseEntity<User> updateUserPart( @PathVariable("userId") Long userId,
                                                @RequestBody User updateUser) {

        User user = userRepository.findById(userId).get();

        if (user != null) {

            if (updateUser.getFirstName() != null) user.setFirstName(updateUser.getFirstName());
            if (updateUser.getName() != null) user.setName(updateUser.getName());
            if (updateUser.getPassword() != null) user.setPassword(updateUser.getPassword());
            if (updateUser.getEmail() != null) user.setEmail(updateUser.getEmail());
            if (updateUser.getGroup() != null) user.setGroup(updateUser.getGroup());

            return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);

        } else {
            throw new UserNotFoundException(userId);
        }
    }


    /**
     * Löscht einen Benutzer
     * @param userId
     * @return
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId) {

        try {
            userRepository.deleteById(userId);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("User deleted", HttpStatus.OK);
    }
}
