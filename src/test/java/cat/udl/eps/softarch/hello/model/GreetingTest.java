package cat.udl.eps.softarch.hello.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GreetingTest {

    private Greeting greeting;

    @Before
    public void setUp() throws Exception {
        greeting = new Greeting();
    }

    @Test
    public void testSetContent() throws Exception {
        greeting.setContent("test");
        assertEquals("test", greeting.getContent());
    }
}