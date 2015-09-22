package cat.udl.eps.softarch.hello.repository;

import cat.udl.eps.softarch.hello.model.Greeting;
import cat.udl.eps.softarch.hello.model.User;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

/**
 * Created by http://rhizomik.net/~roberto/
 */

@Component
@RepositoryEventHandler
public class GreetingEventHandler {
    @Autowired
    UserRepository userRepository;

    @HandleBeforeCreate
    public void handleGreetingCreate(Greeting greeting) {
        User author = userRepository.findUserByEmail(greeting.getEmail());
        Preconditions.checkNotNull(author, "Greeting author %s not found", greeting.getEmail());
        greeting.setAuthor(author);
    }
}
