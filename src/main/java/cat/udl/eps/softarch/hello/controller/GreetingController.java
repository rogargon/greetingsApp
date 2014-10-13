package cat.udl.eps.softarch.hello.controller;

import cat.udl.eps.softarch.hello.model.Greeting;
import cat.udl.eps.softarch.hello.repository.GreetingRepository;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Iterable<Greeting> list(@RequestParam(required=false, defaultValue="0") int page,
                                   @RequestParam(required=false, defaultValue="10") int size) {
        PageRequest request = new PageRequest(page, size);
        return greetingRepository.findAll(request).getContent();
    }
    @RequestMapping(method=RequestMethod.GET, produces="text/html")
    public ModelAndView listHTML(@RequestParam(required=false, defaultValue="0") int page,
                                 @RequestParam(required=false, defaultValue="10") int size) {
        return new ModelAndView("greetings", "greetings", list(page, size));
    }

// RETRIEVE
    @RequestMapping(value = "/{id}", method = RequestMethod.GET )
    @ResponseBody
    public Greeting retrieve(@PathVariable( "id" ) Long id) {
        logger.info("Retrieving greeting number {}", id);
        Preconditions.checkNotNull(greetingRepository.findOne(id), "Greeting with id %s not found", id);
        return greetingRepository.findOne(id);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/html")
    public ModelAndView retrieveHTML(@PathVariable( "id" ) Long id) {
        return new ModelAndView("greeting", "greeting", retrieve(id));
    }

// CREATE
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Greeting create(@Valid @RequestBody Greeting greeting, HttpServletResponse response) {
        logger.info("Creating greeting with content'{}'", greeting.getContent());
        response.setHeader("Location", "/greetings/" + greetingRepository.save(greeting).getId());
        return greeting;
    }
    @RequestMapping(method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded", produces="text/html")
    public String createHTML(@Valid @ModelAttribute("greeting") Greeting greeting, BindingResult binding, HttpServletResponse response) {
        if (binding.hasErrors()) {
            logger.info("Validation error: {}", binding);
            return "form";
        }
        return "redirect:/greetings/"+create(greeting, response).getId();
    }
    // Create form
    @RequestMapping(value = "/form", method = RequestMethod.GET, produces = "text/html")
    public ModelAndView createForm() {
        logger.info("Generating form for greeting creation");
        return new ModelAndView("form", "greeting", new Greeting());
    }

// UPDATE
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Greeting update(@PathVariable("id") Long id, @Valid @RequestBody Greeting greeting) {
        logger.info("Updating greeting {}, new content is '{}'", id, greeting.getContent());
        Preconditions.checkNotNull(greetingRepository.findOne(id), "Greeting with id %s not found", id);
        Greeting updateGreeting = greetingRepository.findOne(id);
        updateGreeting.setContent(greeting.getContent());
        return greetingRepository.save(updateGreeting);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/x-www-form-urlencoded")
    @ResponseStatus(HttpStatus.OK)
    public String updateHTML(@PathVariable("id") Long id, @Valid @ModelAttribute("greeting") Greeting greeting,
                         BindingResult binding) {
        if (binding.hasErrors()) {
            logger.info("Validation error: {}", binding);
            return "form";
        }
        return "redirect:/greetings/"+update(id, greeting).getId();
    }
    // Update form
    @RequestMapping(value = "/{id}/form", method = RequestMethod.GET, produces = "text/html")
    public ModelAndView updateForm(@PathVariable("id") Long id) {
        logger.info("Generating form for updating greeting number {}", id);
        Preconditions.checkNotNull(greetingRepository.findOne(id), "Greeting with id %s not found", id);
        return new ModelAndView("form", "greeting", greetingRepository.findOne(id));
    }

// DELETE
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Long id) {
        logger.info("Deleting greeting number {}", id);
        Preconditions.checkNotNull(greetingRepository.findOne(id), "Greeting with id %s not found", id);
        greetingRepository.delete(id);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    @ResponseStatus(HttpStatus.OK)
    public String deleteHTML(@PathVariable("id") Long id) {
        delete(id);
        return "redirect:/greetings";
    }
}
