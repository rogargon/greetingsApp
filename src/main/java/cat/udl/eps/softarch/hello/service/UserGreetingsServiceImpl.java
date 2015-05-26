package cat.udl.eps.softarch.hello.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cat.udl.eps.softarch.hello.model.Greeting;
import cat.udl.eps.softarch.hello.model.User;
import cat.udl.eps.softarch.hello.repository.GreetingRepository;
import cat.udl.eps.softarch.hello.repository.UserRepository;

/**
 * Created by http://rhizomik.net/~roberto/
 */
@Service
public class UserGreetingsServiceImpl implements UserGreetingsService {
    final Logger logger = LoggerFactory.getLogger(UserGreetingsServiceImpl.class);

    @Autowired
    GreetingRepository greetingRepository;
    @Autowired
    UserRepository     userRepository;

    @Transactional(readOnly = true)
    @Override
    public User getUserAndGreetings(String username) {
        User u = userRepository.findOne(username);
        logger.info("User {} has {} greetings", u.getUsername(), u.getGreetings().size());
        return u;
    }

    @Transactional
    @Override
    public Greeting addGreetingToUser(Greeting g) {
        User u = userRepository.findOne(g.getAuthor().getUsername());
        logger.info("Adding greeting with content '{}' to user {}", g.getContent(), u.getUsername());
        u.addGreeting(g);
        userRepository.save(u);
        greetingRepository.save(g);
        return g;
    }

    @Transactional
    @Override
    public Greeting updateGreetingFromUser(Greeting updateGreeting, Long greetingId) {
        Greeting oldGreeting = greetingRepository.findOne(greetingId);
        oldGreeting.setContent(updateGreeting.getContent());
        oldGreeting.setDate(updateGreeting.getDate());
        if (!updateGreeting.getAuthor().getUsername().equals(oldGreeting.getAuthor().getUsername())) {
            throw new GreetingUsernameUpdateException("Greeting author cannot be changed");
        }
        return greetingRepository.save(oldGreeting);
    }

    @Transactional
    @Override
    public void removeGreetingFromUser(Long greetingId) {
        Greeting g = greetingRepository.findOne(greetingId);
        User u = g.getAuthor();
        if (u != null) {
            u.removeGreeting(g);
            userRepository.save(u);
        }
        greetingRepository.delete(g);
    }
}
