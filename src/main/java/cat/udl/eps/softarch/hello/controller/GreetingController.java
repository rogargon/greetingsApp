package cat.udl.eps.softarch.hello.controller;

import cat.udl.eps.softarch.hello.model.Greeting;
import cat.udl.eps.softarch.hello.repository.GreetingRepository;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by http://rhizomik.net/~roberto/
 */

@Controller
@RequestMapping(value = "/greetings")
public class GreetingController {
    final Logger logger = LoggerFactory.getLogger(GreetingController.class);

    @Autowired GreetingRepository greetingRepository;

// LIST
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView list() {
        logger.info("Listing {} greetings", greetingRepository.count());
        return new ModelAndView("greetings", "greetings", greetingRepository.findAll());
    }

// RETRIEVE
    @RequestMapping(value = "/{id}", method = RequestMethod.GET )
    public ModelAndView retrieve(@PathVariable( "id" ) Long id) {
        logger.info("Retrieving greeting number {}", id);
        Preconditions.checkNotNull(greetingRepository.findOne(id), "Greeting with id %s not found", id);
        return new ModelAndView("greeting", "greeting", greetingRepository.findOne(id));
    }

// CREATE
    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("greeting") Greeting greeting, BindingResult binding) {
        logger.info("Creating greeting with content'{}'", greeting.getContent());
        if (binding.hasErrors()) {
            logger.info("Validation error: {}", binding);
            return "form";
        }
        return "redirect:/greetings/"+greetingRepository.save(greeting).getId();
    }
    // Create form
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public ModelAndView createForm() {
        logger.info("Generating form for greeting creation");
        return new ModelAndView("form", "greeting", new Greeting());
    }

// UPDATE
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String update(@PathVariable("id") Long id, @Valid @ModelAttribute("greeting") Greeting greeting,
                         BindingResult binding) {
        logger.info("Updating greeting {}, new content is '{}'", id, greeting.getContent());
        Preconditions.checkNotNull(greetingRepository.findOne(id), "Greeting with id %s not found", id);
        if (binding.hasErrors()) {
            logger.info("Validation error: {}", binding);
            return "form";
        }
        Greeting updateGreeting = greetingRepository.findOne(id);
        updateGreeting.setContent(greeting.getContent());
        return "redirect:/greetings/"+greetingRepository.save(updateGreeting).getId();
    }
    // Update form
    @RequestMapping(value = "/{id}/form", method = RequestMethod.GET)
    public ModelAndView updateForm(@PathVariable("id") Long id) {
        logger.info("Generating form for updating greeting number {}", id);
        Preconditions.checkNotNull(greetingRepository.findOne(id), "Greeting with id %s not found", id);
        return new ModelAndView("form", "greeting", greetingRepository.findOne(id));
    }

// DELETE
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Long id) {
        logger.info("Deleting greeting number {}", id);
        Preconditions.checkNotNull(greetingRepository.findOne(id), "Greeting with id %s not found", id);
        greetingRepository.delete(id);
        return "redirect:/greetings";
    }
}
