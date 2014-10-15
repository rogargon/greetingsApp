package cat.udl.eps.softarch.hello;

import cat.udl.eps.softarch.hello.config.GreetingsAppTestContext;
import cat.udl.eps.softarch.hello.model.Greeting;
import cat.udl.eps.softarch.hello.repository.GreetingRepository;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        Long index = 1L;
        for (String c : greetingContents) {
            if (!greetingRepository.exists(index))
                greetingRepository.save(new Greeting(c));
            else if (!greetingRepository.findOne(index).getContent().equals(c)) {
                Greeting toBeUpdated = greetingRepository.findOne(index);
                toBeUpdated.setContent(c);
                greetingRepository.save(toBeUpdated);
            }
            index++;
        }
    }

    @When("^the client request the list of greetings$")
    public void the_client_request_the_list_of_greetings() throws Throwable {
        result = mockMvc.perform(get("/greetings")
                            .accept(MediaType.APPLICATION_JSON));
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
        result = mockMvc.perform(get("/greetings/{id}", id)
                            .accept(MediaType.APPLICATION_JSON));
    }

    @Then("^the response is a greeting with id (\\d+) and content \"([^\"]*)\"$")
    public void the_response_is_a_greeting_with_id_and_content(int id, String content) throws Throwable {
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.content", is(content)));
    }

    @Given("^greeting with id (\\d+) doesn't exist$")
    public void greeting_with_id_doesn_t_exist(Long id) throws Throwable {
        assertFalse(greetingRepository.exists(id));
    }

    @Then("^the response is status code (\\d+)$")
    public void the_response_is_status_code(int statusCode) throws Throwable {
        result.andExpect(status().is(statusCode));
    }

    @And("^error message contains \"([^\"]*)\"$")
    public void error_message_contains(String errorMessage) throws Throwable {
        result.andExpect(jsonPath("$.errorInfo.message", containsString(errorMessage)));
    }

    @And("^error url is \"([^\"]*)\"$")
    public void error_url_is(String errorURL) throws Throwable {
        result.andExpect(jsonPath("$.errorInfo.url", is(errorURL)));
    }

    @When("^the client creates a greeting with content \"([^\"]*)\"$")
    public void the_client_creates_a_greeting_with_content(String content) throws Throwable {
        result = mockMvc.perform(post("/greetings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"content\" : \"" + content + "\" }")
                            .accept(MediaType.APPLICATION_JSON));
    }

    @And("^header \"([^\"]*)\" points to a greeting with content \"([^\"]*)\"$")
    public void header_points_to_a_greeting_with_content(String header, String content) throws Throwable {
        String location = result.andReturn().getResponse().getHeader(header);
        result = mockMvc.perform(get(location)
                            .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", is(content)));
    }

    @When("^the client updates greeting with id (\\d+) with content \"([^\"]*)\"$")
    public void the_client_updates_greeting_with_id_with_content(int id, String content) throws Throwable {
        result = mockMvc.perform(put("/greetings/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"content\" : \"" + content + "\" }")
                            .accept(MediaType.APPLICATION_JSON));
    }

    @When("^the client deletes greeting with id (\\d+)$")
    public void the_client_deletes_greeting_with_id(int id) throws Throwable {
        result = mockMvc.perform(delete("/greetings/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON));
    }

    @And("^the response is empty$")
    public void the_response_is_empty() throws Throwable {
        result.andExpect(content().string(""));
    }
}
