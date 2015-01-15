package cat.udl.eps.softarch.hello.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

/**
 * Created by roberto on 14/01/15.
 */
public class SimpleSocialUserDetailsService implements SocialUserDetailsService {

    private UserDetailsService userDetailsService;

    public SimpleSocialUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {

        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        if (userDetails == null)
            throw new UsernameNotFoundException("Username "+userId+" not found");
        return (SocialUserDetails) userDetails;
    }
}
