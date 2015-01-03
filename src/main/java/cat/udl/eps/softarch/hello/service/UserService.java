package cat.udl.eps.softarch.hello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Service;
import cat.udl.eps.softarch.hello.model.User;
import cat.udl.eps.softarch.hello.repository.UserRepository;

/**
 * Created by roberto on 02/01/15.
 */
@Service
public class UserService implements UserDetailsService, AuthenticationUserDetailsService<OpenIDAuthenticationToken> {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        } else {
            if (!user.isEnabled()) {
                throw new DisabledException("User is disabled");
            }
        }
        return user;
    }

    @Override
    public UserDetails loadUserDetails(OpenIDAuthenticationToken openIDAuthenticationToken) throws UsernameNotFoundException {
        String username = openIDAuthenticationToken.getName();
        String email = openIDAuthenticationToken.getAttributes().get(0).getValues().get(0);

        User user = repository.findUserByUsername(username);
        if (user == null) {
            user = new User(username, email, AuthorityUtils.createAuthorityList("ROLE_USER"));
            repository.save(user);
        }

        return user;
    }
}
