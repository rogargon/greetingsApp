package cat.udl.eps.softarch.hello.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    public User getUserAndGreetings(Long userId) {
        User u = userRepository.findOne(userId);
        logger.info("User {} has {} greetings", u.getUsername(), u.getGreetings().size());
        return u;
    }

    @Transactional
    @Override
    public Greeting addGreetingToUser(Greeting g) {
        User u = userRepository.findUserByEmail(g.getEmail());
        if (u == null) {
            String email = g.getEmail();
            String username = email.substring(0, email.indexOf('@'));
            u = new User(username, email);
        }
        greetingRepository.save(g);
        u.addGreeting(g);
        userRepository.save(u);
        return g;
    }

    @Transactional
    @Override
    public Greeting updateGreetingFromUser(Greeting updateGreeting, Long greetingId) {
        Greeting oldGreeting = greetingRepository.findOne(greetingId);
        oldGreeting.setContent(updateGreeting.getContent());
        oldGreeting.setDate(updateGreeting.getDate());
        if (!updateGreeting.getEmail().equals(oldGreeting.getEmail())) {
            throw new GreetingEmailUpdateException("Greeting e-mail cannot be updated");
        }
        return greetingRepository.save(oldGreeting);
    }

    @Transactional
    @Override
    public void removeGreetingFromUser(Long greetingId) {
        Greeting g = greetingRepository.findOne(greetingId);
        User u = userRepository.findUserByEmail(g.getEmail());
        if (u != null) {
            u.removeGreeting(g);
            userRepository.save(u);
        }
        greetingRepository.delete(g);
    }
}
