package cat.udl.eps.softarch.hello.controller;

import cat.udl.eps.softarch.hello.model.User;
import cat.udl.eps.softarch.hello.repository.UserRepository;
import cat.udl.eps.softarch.hello.service.UserGreetingsService;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by http://rhizomik.net/~roberto/
 */

@Controller
@RequestMapping(value = "/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired UserRepository       userRepository;
    @Autowired UserGreetingsService userGreetingsService;

    // LIST
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Iterable<User> list(@RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        PageRequest request = new PageRequest(page, size);

        return userRepository.findAll(request).getContent();
    }

    // RETRIEVE
    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    @ResponseBody
    public User retrieve(@PathVariable("username") String username) {
        logger.info("Retrieving user {}", username);
        Preconditions.checkNotNull(userRepository.findOne(username), "User with id %s not found", username);
        return userGreetingsService.getUserAndGreetings(username);
    }
}
