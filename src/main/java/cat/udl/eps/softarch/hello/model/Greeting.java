package cat.udl.eps.softarch.hello.model;

import java.util.Date;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by http://rhizomik.net/~roberto/
 */
@Entity
public class Greeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Content cannot be blank")
    @Size(max = 256, message = "Content maximum length is {max} characters long")
    private String message;

    @NotBlank(message = "E-mail cannot be blank")
    @Email(message = "E-mail should be valid")
    private String email;

    @NotNull
    private Date date;

    public Greeting() {}

<<<<<<< Updated upstream
    public Greeting(String content, String email, Date date) {
        this.content = content;
=======
    public Greeting(String message, String email, User author, Date date) {
        this.message = message;
        this.email = email;
        this.author = author;
        this.date = date;
    }

    public Greeting(String message, String email, Date date) {
        this.message = message;
>>>>>>> Stashed changes
        this.email = email;
        this.date = date;
    }

    public long getId() { return id; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }
}
