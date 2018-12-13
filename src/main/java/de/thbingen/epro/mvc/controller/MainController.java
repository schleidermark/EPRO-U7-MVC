package de.thbingen.epro.mvc.controller;

import de.thbingen.epro.mvc.model.User;
import de.thbingen.epro.mvc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class MainController {

    @Value("${spring.application.name}")
    private String appName;

    private UserRepository userRepository;

    public MainController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Bereitet das Model auf die Ausgabe eines einzelnen Users vor
     * @param model
     * @param userId
     * @return
     */
    @GetMapping({"/", "/home", "/home/{userId}"})
    public String homePage(Model model, @PathVariable("userId") Optional<Long> userId) {

        if (!userId.isPresent()) {
            model.addAttribute("status", "empty");
            return "home";
        }

        Optional<User> optUser = userRepository.findById(userId.get());

        if (optUser.isPresent()) {

            User user = optUser.get();

            model.addAttribute("firstName", user.getFirstName());
            model.addAttribute("lastName", user.getName());
            model.addAttribute("email", user.getEmail());

            if (user.getGroup() != null) {
                model.addAttribute("group", user.getGroup().getName());
            } else {
                model.addAttribute("group", "n/a");
            }

            model.addAttribute("status", "ok");

        } else {
            model.addAttribute("status", "error");
            model.addAttribute("error", "User not found");
        }

        return "home";
    }


    /**
     * Bereitet das Model auf die Ausgabe der gesamten Benutzerliste vor
     * @param model
     * @return
     */
    @GetMapping("/sample")
    public String samplePage(Model model) {

        Iterable<User> userIt = userRepository.findAll();
        List<User> userList = StreamSupport.stream(userIt.spliterator(), false)
                .collect(Collectors.toList());

        model.addAttribute("users", userList);
        return "sample";
    }

}
