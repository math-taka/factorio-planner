package factorio.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.List;

import factorio.model.Recipe;
import factorio.model.ItemStack;

public class JacksonTest {

    private ObjectMapper mapper;

    record Person(
        String name,
        int age
    ){}

    @BeforeEach
    void setUp(){
        mapper=new ObjectMapper();
    }

    @Test
    void readPerson() throws Exception{
        Person person=mapper.readValue(new File("src/test/resources/person.json"),Person.class);

        assertEquals("Alice",person.name());
        assertEquals(20,person.age());
    }

    @Test
    void readRecipe() throws Exception{
        Recipe recipe = mapper.readValue(new File("src/test/resources/recipe.json"),Recipe.class);

        assertEquals(List.of(new ItemStack("iron_ore",1)),recipe.ingredients());
        assertEquals(List.of(new ItemStack("iron_plate",1)),recipe.products());
        assertEquals(3.2,recipe.craftingTime());
    }
}
