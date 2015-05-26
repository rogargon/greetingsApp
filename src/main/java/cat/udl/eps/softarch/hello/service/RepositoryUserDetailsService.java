package cat.udl.eps.softarch.hello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import cat.udl.eps.softarch.hello.model.User;
import cat.udl.eps.softarch.hello.repository.UserRepository;

/**
 * Created by http://rhizomik.net/~roberto/
 */
public class RepositoryUserDetailsService implements UserDetailsService {

    private UserRepository repository;

    @Autowired
    public RepositoryUserDetailsService(UserRepository repository) {
        //this.repository = repository;
        //User u = new User("user", "pass", "user@test.org");
        //this.repository.save(u);
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findOne(username);
        if (user == null)
            throw new UsernameNotFoundException("No user found with username: " + username);
        return user;
    }
}
