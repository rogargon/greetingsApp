package cat.udl.eps.softarch.hello.controller.html;

import cat.udl.eps.softarch.hello.controller.GreetingController;
import cat.udl.eps.softarch.hello.model.Greeting;
import cat.udl.eps.softarch.hello.repository.GreetingRepository;
import cat.udl.eps.softarch.hello.service.UserGreetingsService;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;

/**
 * Version of the GreetingController producing HTML
 * (GreetingController implements the JSON API)
 *
 * Created by http://rhizomik.net/~roberto/
 */

@Controller
@RequestMapping(value = "/greetings")
public class GreetingControllerHTML {
    final Logger logger = LoggerFactory.getLogger(GreetingControllerHTML.class);

    @Autowired GreetingRepository   greetingRepository;
    @Autowired UserGreetingsService userGreetingsService;
    @Autowired GreetingController   greetingController;

    // LIST
    @RequestMapping(method = RequestMethod.GET, produces = "text/html")
    public ModelAndView listHTML(@RequestParam(required=false, defaultValue="0") int page,
                                 @RequestParam(required=false, defaultValue="10") int size) {
        return new ModelAndView("greetings", "greetings", greetingController.list(page, size));
    }

    // RETRIEVE
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/html")
    public ModelAndView retrieveHTML(@PathVariable( "id" ) Long id) {
        return new ModelAndView("greeting", "greeting", greetingController.retrieve(id));
    }

    // CREATE
    @RequestMapping(method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded", produces="text/html")
    public String createHTML(@Valid @ModelAttribute("greeting") Greeting greeting, BindingResult binding, HttpServletResponse response) {
        if (binding.hasErrors()) {
            logger.info("Validation error: {}", binding);
            return "form";
        }
        return "redirect:greetings/"+greetingController.create(greeting, response).getId();
    }
    // Create form
    @RequestMapping(value = "/form", method = RequestMethod.GET, produces = "text/html")
    public ModelAndView createForm() {
        logger.info("Generating form for greeting creation");
        Greeting emptyGreeting = new Greeting();
        emptyGreeting.setDate(new Date());
        return new ModelAndView("form", "greeting", emptyGreeting);
    }

    // UPDATE
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/x-www-form-urlencoded")
    @ResponseStatus(HttpStatus.OK)
    public String updateHTML(@PathVariable("id") Long id, @Valid @ModelAttribute("greeting") Greeting greeting, BindingResult binding) {
        if (binding.hasErrors()) {
            logger.info("Validation error: {}", binding);
            return "form";
        }
        return "redirect:"+greetingController.update(id, greeting).getId();
    }
    // Update form
    @RequestMapping(value = "/{id}/form", method = RequestMethod.GET, produces = "text/html")
    public ModelAndView updateForm(@PathVariable("id") Long id) {
        logger.info("Generating form for updating greeting number {}", id);
        Preconditions.checkNotNull(greetingRepository.findOne(id), "Greeting with id %s not found", id);
        return new ModelAndView("form", "greeting", greetingRepository.findOne(id));
    }

    // DELETE
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    @ResponseStatus(HttpStatus.OK)
    public String deleteHTML(@PathVariable("id") Long id) {
        greetingController.delete(id);
        return "redirect:";
    }
}
