package cat.udl.eps.softarch.hello.controller;

import cat.udl.eps.softarch.hello.model.Greeting;
import cat.udl.eps.softarch.hello.repository.GreetingRepository;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by http://rhizomik.net/~roberto/
 */

@Controller
@RequestMapping(value = "/greetings")
public class GreetingController {

    @Autowired GreetingRepository greetingRepository;

// LIST
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView list() {
        return new ModelAndView("greetings", "greetings", greetingRepository.findAll());
    }

// RETRIEVE
    @RequestMapping(value = "/{id}", method = RequestMethod.GET )
    public ModelAndView retrieve(@PathVariable( "id" ) Long id) {
        Preconditions.checkNotNull(greetingRepository.findOne(id), "Greeting with id %s not found", id);
        return new ModelAndView("greeting", "greeting", greetingRepository.findOne(id));
    }

// CREATE
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@ModelAttribute("greeting") Greeting greeting) {
        return "redirect:/greetings/"+greetingRepository.save(greeting).getId();
    }
    // Create form
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public ModelAndView createForm() {
        return new ModelAndView("form", "greeting", new Greeting());
    }

// UPDATE
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public String update(@PathVariable("id") Long id, @ModelAttribute("greeting") Greeting greeting) {
        Preconditions.checkNotNull(greetingRepository.findOne(id), "Greeting with id %s not found", id);
        Greeting updateGreeting = greetingRepository.findOne(id);
        updateGreeting.setContent(greeting.getContent());
        return "redirect:/greetings/"+greetingRepository.save(updateGreeting).getId();
    }
    // Update form
    @RequestMapping(value = "/{id}/form", method = RequestMethod.GET)
    public ModelAndView updateForm(@PathVariable("id") Long id) {
        Preconditions.checkNotNull(greetingRepository.findOne(id), "Greeting with id %s not found", id);
        return new ModelAndView("form", "greeting", greetingRepository.findOne(id));
    }

// DELETE
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public String delete(@PathVariable("id") Long id) {
        Preconditions.checkNotNull(greetingRepository.findOne(id), "Greeting with id %s not found", id);
        greetingRepository.delete(id);
        return "redirect:/greetings";
    }
}
