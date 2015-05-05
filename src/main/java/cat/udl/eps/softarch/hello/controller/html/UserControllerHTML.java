package cat.udl.eps.softarch.hello.controller.html;

import cat.udl.eps.softarch.hello.controller.UserController;
import cat.udl.eps.softarch.hello.repository.UserRepository;
import cat.udl.eps.softarch.hello.service.UserGreetingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by http://rhizomik.net/~roberto/
 */

@Controller
@RequestMapping(value = "/users")
public class UserControllerHTML {
    final Logger logger = LoggerFactory.getLogger(UserControllerHTML.class);

    @Autowired UserRepository       userRepository;
    @Autowired UserGreetingsService userGreetingsService;
    @Autowired UserController       userController;

    // LIST
    @RequestMapping(method = RequestMethod.GET, produces = "text/html")
    public ModelAndView listHTML(@RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return new ModelAndView("users", "users", userController.list(page, size));
    }

    // RETRIEVE
    @RequestMapping(value = "/{username}", method = RequestMethod.GET, produces = "text/html")
    public ModelAndView retrieveHTML(@PathVariable("username") String username) {
        return new ModelAndView("user", "user", userController.retrieve(username));
    }
}
