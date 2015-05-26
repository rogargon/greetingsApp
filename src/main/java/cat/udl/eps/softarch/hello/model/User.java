package cat.udl.eps.softarch.hello.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by http://rhizomik.net/~roberto/
 */
@Entity
@Table(name = "GreetingsUser") //Avoid collision with system table User in Postgres
public class User implements UserDetails{

    @Id
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @Email(message = "E-mail should be valid")
    private String email;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "author")
    @JsonManagedReference
    private List<Greeting> greetings = new ArrayList<>();

    @URL
    private String imageUrl;

    public User() {}

    public User(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() { return username; }

    @Override
    public String getPassword() { return null; } // No password, Twitter authentication

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public List<Greeting> getGreetings() {
        return greetings;
    }

    public void addGreeting(Greeting newGreeting) {
        greetings.add(newGreeting);
    }

    public void removeGreeting(Greeting greeting) {
        greetings.remove(greeting);
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() { return true; }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() { return true; }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    @JsonIgnore
    public boolean isEnabled() { return true; }

    @Override
    public String toString() { return getUsername(); }
}
