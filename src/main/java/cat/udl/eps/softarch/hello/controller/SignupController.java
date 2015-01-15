package cat.udl.eps.softarch.hello.controller;

import cat.udl.eps.softarch.hello.model.User;
import cat.udl.eps.softarch.hello.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

/**
 * Created by http://rhizomik.net/~roberto/
 */
@Controller
public class SignupController {

    @Autowired
    UserRepository userRepository;

    private final ProviderSignInUtils providerSignInUtils = new ProviderSignInUtils();

    @RequestMapping(value="/login")
    public String login() { return "login"; }

    @RequestMapping(value="/signup", method=RequestMethod.GET)
    public String signup(WebRequest request) {
        Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
        if (connection != null) {
            User user = new User(connection.getDisplayName(), "pass", "invalid@noname.org");
            user.setImageUrl(connection.getImageUrl());
            userRepository.save(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            providerSignInUtils.doPostSignUp(user.getUsername(), request);
            return "redirect:/users/"+connection.getDisplayName();
        }
        return null;
    }
}
