package cat.udl.eps.softarch.hello.repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cat.udl.eps.softarch.hello.config.GreetingsAppTestContext;
import cat.udl.eps.softarch.hello.model.Greeting;
import cat.udl.eps.softarch.hello.model.User;
import cat.udl.eps.softarch.hello.repository.GreetingRepository;
import com.google.common.primitives.Ints;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GreetingsAppTestContext.class)
@WebAppConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class GreetingRepositoryTest {
    static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    GreetingRepository greetingRepository;
    UserRepository userRepository;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        if (greetingRepository.count() == 0) {
            User u = new User("test", "test@example.org", AuthorityUtils.createAuthorityList("ROLE_USER"));
            userRepository.save(u);
            Greeting g = new Greeting("test1", u, new Date());
            greetingRepository.save(g);
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    //TODO: Add tests for email and date greeting fields on retrieve/create/update, validation errors...

    @Test
    public void testList() throws Exception {
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(get("/greetings").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$._embedded.greetings", hasSize(startSize)))
                .andExpect(jsonPath("$._embedded.greetings[0].content", is("test1")))
                .andExpect(jsonPath("$._embedded.greetings[0].email", is("test@example.org")));
    }

    @Test
    public void testRetrieveExisting() throws Exception {
        mockMvc.perform(get("/greetings/{id}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", is("test1")))
                .andExpect(jsonPath("$.email", is("test@example.org")));
    }

    @Test
    public void testRetrieveNonExisting() throws Exception {
        mockMvc.perform(get("/greetings/{id}", 999L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreate() throws Exception {
        Greeting last = new Greeting("last", userRepository.findUserByUsername("test"), new Date());
        int nextGreetingId = Ints.checkedCast(greetingRepository.save(last).getId())+1;
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(post("/greetings")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"content\":\"newtest\", \"email\":\"newtest@example.org\"," +
                            " \"date\":\"" + df.format(new Date()) + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/greetings/" + nextGreetingId));

        assertEquals(startSize + 1, greetingRepository.count());
    }

    @Test
    public void testCreateEmptyContent() throws Exception {
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(post("/greetings")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"content\":\"\", \"email\":\"newtest@example.org\"," +
                            " \"date\":\"" + df.format(new Date()) + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorInfo.url", is("/greetings")))
                .andExpect(jsonPath("$.errorInfo.message", containsString("Content cannot be blank")));


        assertEquals(startSize, greetingRepository.count());
    }

    @Test
    public void testUpdate() throws Exception {
        Greeting tobeupdated = greetingRepository.save(new Greeting("tobeupdated", userRepository.findUserByUsername("test"), new Date()));
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(put("/greetings/{id}", tobeupdated.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"content\":\"updated\", \"email\":\"newtest@example.org\"," +
                            " \"date\":\"" + df.format(new Date()) + "\"}"))
                .andExpect(status().isNoContent())
                .andExpect(header().string("Location", "http://localhost/greetings/" + tobeupdated.getId()));

        assertEquals("updated", greetingRepository.findOne(tobeupdated.getId()).getContent());
        assertEquals("newtest@example.org", greetingRepository.findOne(tobeupdated.getId()).getUser().getEmail());
        assertEquals(startSize, greetingRepository.count());
    }

    @Test
    public void testUpdateEmptyEmail() throws Exception {
        Greeting tobeupdated = greetingRepository.save(
                new Greeting("tobeupdated", userRepository.findUserByUsername("test"), new Date()));
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(put("/greetings/{id}", tobeupdated.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"content\":\"updated\", \"email\":\"\"," +
                                  " \"date\":\"" + df.format(new Date()) + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorInfo.url", is("/greetings/" + tobeupdated.getId())))
                .andExpect(jsonPath("$.errorInfo.message", containsString("E-mail cannot be blank")));

        assertEquals(startSize, greetingRepository.count());
    }

    @Test
    public void testUpdateWrongEmail() throws Exception {
        Greeting tobeupdated = greetingRepository.save(
                new Greeting("tobeupdated", userRepository.findUserByUsername("test"), new Date()));
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(put("/greetings/{id}", tobeupdated.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"content\":\"updated\", \"email\":\"wrong\"," +
                        " \"date\":\"" + df.format(new Date()) + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorInfo.url", is("/greetings/" + tobeupdated.getId())))
                .andExpect(jsonPath("$.errorInfo.message", containsString("E-mail should be valid")));

        assertEquals(startSize, greetingRepository.count());
    }

    @Test
    public void testUpdateNonExisting() throws Exception {
        Greeting last = new Greeting("last", userRepository.findUserByUsername("test"), new Date());
        int nextGreetingId = Ints.checkedCast(greetingRepository.save(last).getId())+1;
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(put("/greetings/{id}", 999L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"content\":\"updated\", \"email\":\"newtest@example.org\"," +
                        " \"date\":\"" + df.format(new Date()) + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/greetings/" + nextGreetingId));

        assertEquals(startSize + 1, greetingRepository.count());
    }

    @Test
    public void testDeleteExisting() throws Exception {
        Greeting toBeRemoved = greetingRepository.save(
                new Greeting("toberemoved", userRepository.findUserByUsername("test"), new Date()));
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(delete("/greetings/{id}", toBeRemoved.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertEquals(startSize - 1, greetingRepository.count());
        assertThat(greetingRepository.findAll(), not(hasItem(toBeRemoved)));
    }

    @Test
    public void testDeleteNonExisting() throws Exception {
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(delete("/greetings/{id}", 999L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        assertEquals(startSize, greetingRepository.count());
    }
}
