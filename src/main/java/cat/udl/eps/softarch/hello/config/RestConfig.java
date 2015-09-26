package cat.udl.eps.softarch.hello.config;

import cat.udl.eps.softarch.hello.model.Greeting;
import cat.udl.eps.softarch.hello.model.User;
import cat.udl.eps.softarch.hello.repository.GreetingRepository;
import cat.udl.eps.softarch.hello.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.http.MediaType;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * Created by http://rhizomik.net/~roberto/
 */
@Configuration
public class RestConfig extends RepositoryRestMvcConfiguration {
    @Autowired
    UserRepository userRepository;
    @Autowired
    GreetingRepository greetingRepository;

    @PostConstruct
    public void init() {
        User user = new User("tester", "tester@test.org");
        userRepository.save(user);
        Greeting greeting1 = new Greeting("Hello!", "tester@test.org", user, new Date());
        Greeting greeting2 = new Greeting("Bye!", "tester@test.org", user, new Date());
        greetingRepository.save(greeting1);
        greetingRepository.save(greeting2);
    }
}