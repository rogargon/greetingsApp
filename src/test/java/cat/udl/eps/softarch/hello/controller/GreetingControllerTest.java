package cat.udl.eps.softarch.hello.controller;

import cat.udl.eps.softarch.hello.config.ApplicationConfig;
import cat.udl.eps.softarch.hello.model.Greeting;
import cat.udl.eps.softarch.hello.model.User;
import cat.udl.eps.softarch.hello.repository.GreetingRepository;
import cat.udl.eps.softarch.hello.repository.UserRepository;
import com.google.common.primitives.Ints;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
@WebAppConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class GreetingControllerTest {
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
            Greeting g = new Greeting("test1", "test@example.org", greetingDate);
            greetingRepository.save(g);
            User u = new User("testuser", "test@example.org");
            u.addGreeting(g);
            userRepository.save(u);
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    //TODO: Add tests for email and date greeting fields on retrieve/create/update, validation errors...

    @Test
    public void testList() throws Exception {
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(get("/greetings").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("greetings"))
                .andExpect(forwardedUrl("/WEB-INF/views/greetings.jsp"))
                .andExpect(model().attributeExists("greetings"))
                .andExpect(model().attribute("greetings", hasSize(startSize)))
                .andExpect(model().attribute("greetings", hasItem(allOf(
                        hasProperty("id", is(1L)),
                        hasProperty("content", is("test1")),
                        hasProperty("email", is("test@example.org"))))));
    }

    @Test
    public void testRetrieveExisting() throws Exception {
        mockMvc.perform(get("/greetings/{id}", 1L).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("greeting"))
                .andExpect(forwardedUrl("/WEB-INF/views/greeting.jsp"))
                .andExpect(model().attributeExists("greeting"))
                .andExpect(model().attribute("greeting", allOf(
                        hasProperty("id", is(1L)),
                        hasProperty("content", is("test1")))));
    }

    @Test
    public void testRetrieveNonExisting() throws Exception {
        mockMvc.perform(get("/greetings/{id}", 999L).accept(MediaType.TEXT_HTML))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(forwardedUrl("/WEB-INF/views/error.jsp"));
    }

    @Test
    public void testCreate() throws Exception {
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(post("/greetings")
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("content", "newtest")
                        .param("email", "newtest@example.org")
                        .param("date", df.format(new Date())))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:greetings/" + (startSize + 1)))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("greeting", hasProperty("content", is("newtest"))));

        assertEquals(startSize+1, greetingRepository.count());
    }

    @Test
    public void testCreateEmpty() throws Exception {
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(post("/greetings")
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("content", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(forwardedUrl("/WEB-INF/views/form.jsp"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("greeting", "content"))
                .andExpect(model().attribute("greeting", hasProperty("content", isEmptyOrNullString())));

        assertEquals(startSize, greetingRepository.count());
    }

    @Test
    public void testCreateForm() throws Exception {
        mockMvc.perform(get("/greetings/form"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(forwardedUrl("/WEB-INF/views/form.jsp"))
                .andExpect(model().attribute("greeting", hasProperty("content", isEmptyOrNullString())));
    }

    @Test
    public void testUpdate() throws Exception {
        Greeting tobeupdated = greetingRepository.save(new Greeting("tobeupdated", "test@example.org", new Date()));
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(put("/greetings/{id}", tobeupdated.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("content", "updated")
                        .param("email", "test@example.org")
                        .param("date", df.format(new Date())))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:"+tobeupdated.getId()))
                .andExpect(model().hasNoErrors());

        assertEquals("updated", greetingRepository.findOne(tobeupdated.getId()).getContent());
        assertEquals(startSize, greetingRepository.count());
    }

    @Test
    public void testUpdateEmpty() throws Exception {
        Greeting tobeupdated = greetingRepository.save(new Greeting("tobeupdated", "test@example.org", new Date()));
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(put("/greetings/{id}", tobeupdated.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("content", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(forwardedUrl("/WEB-INF/views/form.jsp"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("greeting", "content"))
                .andExpect(model().attribute("greeting", hasProperty("content", isEmptyOrNullString())));

        assertEquals(startSize, greetingRepository.count());
    }

    @Test
    public void testUpdateNonExisting() throws Exception {
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(put("/greetings/{id}", 999L)
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("content", "updated")
                        .param("email", "newtest@example.org")
                        .param("date", df.format(new Date())))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(forwardedUrl("/WEB-INF/views/error.jsp"));

        assertEquals(startSize, greetingRepository.count());
    }

    @Test
    public void testUpdateForm() throws Exception {
        mockMvc.perform(get("/greetings/{id}/form", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(forwardedUrl("/WEB-INF/views/form.jsp"))
                .andExpect(model().attribute("greeting", hasProperty("content", is("test1"))));
    }

    @Test
    public void testUpdateFormNonExisting() throws Exception {
        mockMvc.perform(get("/greetings/{id}/form", 999L).accept(MediaType.TEXT_HTML))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(forwardedUrl("/WEB-INF/views/error.jsp"));
    }

    @Test
    public void testDeleteExisting() throws Exception {
        Greeting toBeRemoved = greetingRepository.save(new Greeting("toberemoved", "test@example.org", new Date()));
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(delete("/greetings/{id}", toBeRemoved.getId()).accept(MediaType.TEXT_HTML))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:"));

        assertEquals(startSize-1, greetingRepository.count());
        assertThat(greetingRepository.findAll(), not(hasItem(toBeRemoved)));
    }

    @Test
    public void testDeleteNonExisting() throws Exception {
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(delete("/greetings/{id}", 999L).accept(MediaType.TEXT_HTML))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(forwardedUrl("/WEB-INF/views/error.jsp"));

        assertEquals(startSize, greetingRepository.count());
    }
}
