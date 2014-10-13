package cat.udl.eps.softarch.hello;

import cat.udl.eps.softarch.hello.config.GreetingsAppContext;
import cat.udl.eps.softarch.hello.config.GreetingsAppTestContext;
import cat.udl.eps.softarch.hello.model.Greeting;
import cat.udl.eps.softarch.hello.repository.GreetingRepository;
import com.google.common.primitives.Ints;
import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by http://rhizomik.net/~roberto/
 */

@WebAppConfiguration
@ContextConfiguration(classes = GreetingsAppTestContext.class)
public class GreetingsStepdefs {

    @Autowired
    GreetingRepository greetingRepository;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    private ResultActions result;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @After
    public void tearDown() throws Exception {}

    @Given("^the greetings repository has the following greetings:$")
    public void the_greetings_repository_has_the_following_greetings(List<String> greetingContents) throws Throwable {
        for (String c : greetingContents)
            greetingRepository.save(new Greeting(c));
    }

    @When("^the client request the list of greetings$")
    public void the_client_request_the_list_of_greetings() throws Throwable {
        result = mockMvc.perform(get("/greetings").accept(MediaType.APPLICATION_JSON));
    }

    @Then("^the response is a list containing (\\d+) greetings$")
    public void the_response_is_a_list_containing_greetings(int lenght) throws Throwable {
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(lenght)));
    }

    @And("^one greeting has id (\\d+) and content \"([^\"]*)\"$")
    public void one_greeting_has_id_and_content(int id, String content) throws Throwable {
        result.andExpect(jsonPath("$[?(@.id =='"+id+"')].content").value(hasItem(content)));
    }

    @When("^the client requests greeting with id (\\d+)$")
    public void the_client_requests_greeting_with_id(int id) throws Throwable {
        result = mockMvc.perform(get("/greetings/{id}", id).accept(MediaType.APPLICATION_JSON));
    }

    @Then("^the response is a greeting with id (\\d+) and content \"([^\"]*)\"$")
    public void the_response_is_a_greeting_with_id_and_content(int id, String content) throws Throwable {
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.content", is(content)));
    }
}
