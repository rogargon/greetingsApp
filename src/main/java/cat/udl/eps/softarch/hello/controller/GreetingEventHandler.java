package cat.udl.eps.softarch.hello.controller;

import cat.udl.eps.softarch.hello.model.Greeting;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Created by roberto on 20/09/14.
 */
@Component
@RepositoryEventHandler(Greeting.class)
public class GreetingEventHandler {

    private final static Logger log = Logger.getLogger(GreetingEventHandler.class.getName());

    @HandleBeforeCreate
    public void handleBeforeCreate(Greeting greeting) {
        log.info("Handling before create for greeting #" + greeting.getId() + "with content:'" + greeting.getContent() + "'");
    }

    @HandleAfterCreate
    public void handleAfterCreate(Greeting greeting) {
        log.info("Handling after create for greeting #" + greeting.getId() + "with content:'" + greeting.getContent() + "'");
    }

    @HandleBeforeSave
    public void handleBeforeSave(Greeting greeting) {
        log.info("Handling before save for greeting #" + greeting.getId() + "with content:'" + greeting.getContent() + "'");
    }

    @HandleAfterSave
    public void handleAfterSave(Greeting greeting) {
        log.info("Handling after save for greeting #" + greeting.getId() + "with content:'" + greeting.getContent() + "'");
    }

    @HandleBeforeDelete
    public void handleBeforeDelete(Greeting greeting) {
        log.info("Handling before delete for greeting #" + greeting.getId() + "with content:'" + greeting.getContent() + "'");
    }

    @HandleAfterDelete
    public void handleAfterDelete(Greeting greeting) {
        log.info("Handling after delete for greeting #" + greeting.getId() + "with content:'" + greeting.getContent() + "'");
    }
}
