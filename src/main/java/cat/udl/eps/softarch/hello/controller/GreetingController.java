package cat.udl.eps.softarch.hello.controller;

import cat.udl.eps.softarch.hello.model.Greeting;
import cat.udl.eps.softarch.hello.repository.GreetingRepository;
import cat.udl.eps.softarch.hello.service.UserGreetingsService;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by http://rhizomik.net/~roberto/
 */

@Controller
@RequestMapping(value = "/greetings")
public class GreetingController {
    final Logger logger = LoggerFactory.getLogger(GreetingController.class);

    @Autowired GreetingRepository   greetingRepository;
    @Autowired UserGreetingsService userGreetingsService;

// LIST
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Iterable<Greeting> list(@RequestParam(required=false, defaultValue="0") int page,
                                   @RequestParam(required=false, defaultValue="10") int size) {
        PageRequest request = new PageRequest(page, size);
        return greetingRepository.findAll(request).getContent();
    }

// RETRIEVE
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Greeting retrieve(@PathVariable("id") Long id) {
        logger.info("Retrieving greeting number {}", id);
        Preconditions.checkNotNull(greetingRepository.findOne(id), "Greeting with id %s not found", id);
        return greetingRepository.findOne(id);
    }

// CREATE
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Greeting create(@Valid @RequestBody Greeting greeting, HttpServletResponse response) {
        logger.info("Creating greeting with content'{}'", greeting.getContent());
        Greeting newGreeting = userGreetingsService.addGreetingToUser(greeting);
        response.setHeader("Location", "greetings/" + newGreeting.getId());
        return newGreeting;
    }

// UPDATE
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Greeting update(@PathVariable("id") Long id, @Valid @RequestBody Greeting greeting) {
        logger.info("Updating greeting {}, new content is '{}'", id, greeting.getContent());
        Preconditions.checkNotNull(greetingRepository.findOne(id), "Greeting with id %s not found", id);
        return userGreetingsService.updateGreetingFromUser(greeting, id);
    }

// DELETE
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Long id) {
        logger.info("Deleting greeting number {}", id);
        Preconditions.checkNotNull(greetingRepository.findOne(id), "Greeting with id %s not found", id);
        userGreetingsService.removeGreetingFromUser(id);
    }
}
