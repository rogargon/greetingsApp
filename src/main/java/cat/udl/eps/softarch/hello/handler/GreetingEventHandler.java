package cat.udl.eps.softarch.hello.handler;

import cat.udl.eps.softarch.hello.model.Greeting;
import cat.udl.eps.softarch.hello.model.User;
import cat.udl.eps.softarch.hello.repository.UserRepository;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by http://rhizomik.net/~roberto/
 */

@Component
@RepositoryEventHandler(Greeting.class)
public class GreetingEventHandler {
    @Autowired
    UserRepository userRepository;

    @HandleBeforeCreate
    @Transactional
    public void handleGreetingCreate(Greeting greeting) {
        User author = userRepository.findUserByEmail(greeting.getEmail());
        Preconditions.checkNotNull(author, "Greeting author %s not found", greeting.getEmail());
        greeting.setAuthor(author);
    }

    @HandleBeforeSave
    @Transactional
    public void handleGreetingSave(Greeting greeting) {
        User author = userRepository.findUserByEmail(greeting.getEmail());
        Preconditions.checkNotNull(author, "Greeting author %s not found", greeting.getEmail());
        greeting.setAuthor(author);
    }
}
