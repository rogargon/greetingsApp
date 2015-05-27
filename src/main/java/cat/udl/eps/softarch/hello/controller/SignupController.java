package cat.udl.eps.softarch.hello.controller;

import cat.udl.eps.softarch.hello.model.User;
import cat.udl.eps.softarch.hello.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by http://rhizomik.net/~roberto/
 */
@Controller
public class SignupController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ConnectionFactoryLocator connectionFactoryLocator;
    @Autowired
    UsersConnectionRepository usersConnectionRepository;
    @Autowired
    ConnectionRepository connectionRepository;

    @RequestMapping(value="/login")
    public String login() { return "login"; }

    @RequestMapping(value="/signup", method=RequestMethod.GET)
    public String signup() {
        Connection<Twitter> twitter = connectionRepository.getPrimaryConnection(Twitter.class);
        if (twitter != null) {
            User user;
            if (userRepository.exists(twitter.getDisplayName()))
                user = userRepository.findOne(twitter.getDisplayName());
            else {
                user = new User(twitter.getDisplayName());
                user.setImageUrl(twitter.getImageUrl());
                userRepository.save(user);
            }
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return "redirect:/api/users/"+twitter.getDisplayName();
        }
        return null;
    }
}
