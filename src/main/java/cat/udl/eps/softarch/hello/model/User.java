package cat.udl.eps.softarch.hello.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by http://rhizomik.net/~roberto/
 */
@Entity
@Table(name = "GreetingsUser") //Avoid collision with system table User in Postgres
public class User {
    @Id
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @Email(message = "E-mail should be valid")
    private String email;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "author")
    @JsonManagedReference
    private List<Greeting> greetings = new ArrayList<>();

    public User() { }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() { return username; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public List<Greeting> getGreetings() { return greetings; }

    public void addGreeting(Greeting newGreeting) { greetings.add(newGreeting); }

    public void removeGreeting(Greeting greeting) { greetings.remove(greeting); }
}
