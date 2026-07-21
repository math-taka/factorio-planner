package factorio.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

public class JacksonTest {
    record Person(
        String name,
        int age
    ){}

    @Test
    void readPerson() throws Exception{
        ObjectMapper mapper=new ObjectMapper();
        Person person=mapper.readValue(new File("src/test/resources/person.json"),Person.class);

        assertEquals("Alice",person.name());
        assertEquals(20,person.age());
    }

    @Test
    void readRecipe() throws Exception{
        ObjectMapper mapper=new ObjectMapper();
        
    }
}
