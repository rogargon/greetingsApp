package cat.udl.eps.softarch.hello.config;

import cat.udl.eps.softarch.hello.repository.UserRepository;
import cat.udl.eps.softarch.hello.service.RepositoryUserDetailsService;
import cat.udl.eps.softarch.hello.service.SimpleSocialUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * Created by http://rhizomik.net/~roberto/
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/api/login").and()
                .authorizeRequests()
                    .antMatchers(
                        "/api/*/form",
                        "/api/users/**").authenticated()
                    .antMatchers(
                        "/api/connect/**",
                        "/api/login",
                        "/api/signup/**",
                        "/api/greetings/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/**").authenticated()
                    .antMatchers(HttpMethod.PUT, "/api/**").authenticated()
                    .antMatchers(HttpMethod.DELETE, "/api/**").authenticated().and()
                .apply(new SpringSocialConfigurer());

        http.csrf().disable();
                //.and()
                //.addFilterAfter(new CSRFCookieFilter(), CsrfFilter.class)
                //.csrf().csrfTokenRepository(csrfTokenRepository());
    }

    @Autowired
    private UserRepository userRepository;

    @Bean
    public SocialUserDetailsService socialUserDetailsService() {
        return new SimpleSocialUserDetailsService(userDetailsService());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new RepositoryUserDetailsService(userRepository);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }
}
