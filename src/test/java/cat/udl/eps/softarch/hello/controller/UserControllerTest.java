package cat.udl.eps.softarch.hello.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.google.common.primitives.Ints;
import cat.udl.eps.softarch.hello.config.GreetingsAppTestContext;
import cat.udl.eps.softarch.hello.model.Greeting;
import cat.udl.eps.softarch.hello.model.User;
import cat.udl.eps.softarch.hello.repository.GreetingRepository;
import cat.udl.eps.softarch.hello.repository.UserRepository;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GreetingsAppTestContext.class)
@WebAppConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest {
    static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired GreetingRepository greetingRepository;
    @Autowired UserRepository     userRepository;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private Date greetingDate;

    @Before
    public void setup() throws ParseException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        this.greetingDate = df.parse("2015-01-01");

        if (greetingRepository.count() == 0) {
            Greeting g = new Greeting("test-content", "test@example.org", greetingDate);
            greetingRepository.save(g);
            User u = new User("test-user", "test@example.org");
            u.addGreeting(g);
            userRepository.save(u);
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testList() throws Exception {
        int startSize = Ints.checkedCast(userRepository.count());

        mockMvc.perform(get("/users").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/views/users.jsp"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", hasSize(startSize)))
                .andExpect(model().attribute("users", hasItem(allOf(
                        hasProperty("id", is(1L)),
                        hasProperty("username", is("test-user")),
                        hasProperty("email", is("test@example.org"))))));
    }

    @Test
    public void testRetrieveExisting() throws Exception {
        mockMvc.perform(get("/users/{id}", 1L).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("user"))
                .andExpect(forwardedUrl("/WEB-INF/views/user.jsp"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("id", is(1L)),
                        hasProperty("username", is("test-user")),
                        hasProperty("email", is("test@example.org")),
                        hasProperty("greetings", contains( allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("content", is("test-content")),
                                hasProperty("email", is("test@example.org")),
                                hasProperty("date", comparesEqualTo(greetingDate)))
                        )))));
    }

    @Test
    public void testRetrieveNonExisting() throws Exception {
        mockMvc.perform(get("/users/{id}", 999L).accept(MediaType.TEXT_HTML))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(forwardedUrl("/WEB-INF/views/error.jsp"));
    }

    @Test
    public void testAddGreetingToExistingUser() throws Exception {
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(post("/greetings")
                .accept(MediaType.TEXT_HTML)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("content", "newtest")
                .param("email", "test@example.org")
                .param("date", df.format(greetingDate)))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/greetings/" + (startSize + 1)))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("greeting", hasProperty("content", is("newtest"))));

        assertEquals(startSize + 1, greetingRepository.count());

        mockMvc.perform(get("/users/{id}", 1L).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("user"))
                .andExpect(forwardedUrl("/WEB-INF/views/user.jsp"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("id", is(1L)),
                        hasProperty("username", is("test-user")),
                        hasProperty("email", is("test@example.org")),
                        hasProperty("greetings", hasSize(startSize+1)),
                        hasProperty("greetings", contains(
                                allOf(
                                        hasProperty("id", is(1L)),
                                        hasProperty("content", is("test-content")),
                                        hasProperty("email", is("test@example.org"))),
                                allOf(
                                        hasProperty("id", is(2L)),
                                        hasProperty("content", is("newtest")),
                                        hasProperty("email", is("test@example.org")))
                        )))));
    }

    @Test
    public void testAddGreetingToNewUser() throws Exception {
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(post("/greetings")
                .accept(MediaType.TEXT_HTML)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("content", "newtest")
                .param("email", "newuser@example.org")
                .param("date", df.format(greetingDate)))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/greetings/" + (startSize + 1)))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("greeting", hasProperty("content", is("newtest"))));

        assertEquals(startSize + 1, greetingRepository.count());

        mockMvc.perform(get("/users/{id}", 2L).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("user"))
                .andExpect(forwardedUrl("/WEB-INF/views/user.jsp"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("id", is(2L)),
                        hasProperty("username", is("newuser")),
                        hasProperty("email", is("newuser@example.org")),
                        hasProperty("greetings", hasSize(1)),
                        hasProperty("greetings", contains(
                                allOf(
                                        hasProperty("id", is(2L)),
                                        hasProperty("content", is("newtest")),
                                        hasProperty("email", is("newuser@example.org")))
                        )))));
    }

    @Test
    public void testRemoveGreetingFromExistingUser() throws Exception {
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(delete("/greetings/{id}", 1L).accept(MediaType.TEXT_HTML))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/greetings"));

        assertEquals(startSize-1, greetingRepository.count());
        assertThat(greetingRepository.findAll(), not(contains(hasProperty("id", is(1L)))));

        mockMvc.perform(get("/users/{id}", 1L).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("user"))
                .andExpect(forwardedUrl("/WEB-INF/views/user.jsp"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("id", is(1L)),
                        hasProperty("username", is("test-user")),
                        hasProperty("email", is("test@example.org")),
                        hasProperty("greetings", hasSize(0)))));
    }

    @Test
    public void testUpdateGreetingFromExistingUser() throws Exception {
        int startSize = Ints.checkedCast(greetingRepository.count());
        Date greetingDate = new Date();

        mockMvc.perform(put("/greetings/{id}", 1L)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("content", "updated-content")
                .param("email", "test@example.org")
                .param("date", df.format(greetingDate)))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/greetings/"+1L))
                .andExpect(model().hasNoErrors());

        assertEquals("updated-content", greetingRepository.findOne(1L).getContent());
        assertEquals(startSize, greetingRepository.count());

        mockMvc.perform(get("/users/{id}", 1L).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("user"))
                .andExpect(forwardedUrl("/WEB-INF/views/user.jsp"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("id", is(1L)),
                        hasProperty("username", is("test-user")),
                        hasProperty("email", is("test@example.org")),
                        hasProperty("greetings", hasSize(1)),
                        hasProperty("greetings", contains(
                                allOf(
                                        hasProperty("id", is(1L)),
                                        hasProperty("content", is("updated-content")),
                                        hasProperty("email", is("test@example.org")))
                        )))));
    }

    @Test
    public void testGreetingEmailCannotBeUpdated() throws Exception {
        int startSize = Ints.checkedCast(greetingRepository.count());
        Date greetingDate = new Date();

        mockMvc.perform(put("/greetings/{id}", 1L)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .accept(MediaType.TEXT_HTML)
                    .param("content", "updated-content")
                    .param("email", "newuser@example.org")
                    .param("date", df.format(greetingDate)))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(forwardedUrl("/WEB-INF/views/error.jsp"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", allOf(
                        hasProperty("url", is("/greetings/1")),
                        hasProperty("message", is("Greeting e-mail cannot be updated")))));

        assertEquals("test-content", greetingRepository.findOne(1L).getContent());
        assertEquals(startSize, greetingRepository.count());
    }
}
