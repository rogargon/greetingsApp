package cat.udl.eps.softarch.hello.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by http://rhizomik.net/~roberto/
 */
@Entity
public class User {
    @Id
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "E-mail cannot be blank")
    @Email(message = "E-mail should be valid")
    private String email;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Greeting> greetings = new ArrayList<>();

    public User() { }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() { return username; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public List<Greeting> getGreetings() {
        return greetings;
    }

    public void addGreeting(Greeting newGreeting) {
        greetings.add(newGreeting);
    }

    public void removeGreeting(Greeting greeting) {
        greetings.remove(greeting);
    }
}
