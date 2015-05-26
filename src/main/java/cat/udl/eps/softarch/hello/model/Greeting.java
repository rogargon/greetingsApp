package cat.udl.eps.softarch.hello.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

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
    private String content;

    @ManyToOne
    @JsonBackReference
    private User author;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date date;

    public Greeting() {}

    public Greeting(String content, User author, Date date) {
        this.content = content;
        this.author = author;
        this.date = date;
    }

    public long getId() { return id; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public User getAuthor() { return author; }

    public void setAuthor(User author) { this.author = author; }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }
}
