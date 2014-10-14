package cat.udl.eps.softarch.hello.repository;

import cat.udl.eps.softarch.hello.model.Greeting;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
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
        log.info("handling before create for greeting #" + greeting.getId() + "with content:'" + greeting.getContent() + "'");
    }

    @HandleAfterSave
    public void handleAfterSave(Greeting greeting) {
        log.info("saved greeting #" + greeting.getId() + "with content:'" + greeting.getContent() + "'");
    }

    @HandleAfterDelete
    public void handleAfterDelete(Greeting greeting) {
        log.info("deleted greeting #" + greeting.getId() + "with content:'" + greeting.getContent() + "'");
    }
}