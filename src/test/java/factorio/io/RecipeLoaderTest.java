package factorio.io;

import org.junit.jupiter.api.Test;

import factorio.model.ItemStack;
import factorio.model.Recipe;
import factorio.model.RecipeBook;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.List;

public class RecipeLoaderTest{

    @Test
    void loadRecipeBookFromJSON() throws Exception{
        File bookFile = new File("src/test/resources/recipelist.json");
        RecipeBook recipeBook = RecipeLoader.load(bookFile);
        
        Recipe recipe0=new Recipe(
            List.of(new ItemStack("iron_ore",1)),
            List.of(new ItemStack("iron_plate",1)),
            3.2
        );

        Recipe recipe1=new Recipe(
            List.of(new ItemStack("iron_plate",2)),
            List.of(new ItemStack("gear_wheel",1)),
            0.5
        );

        assertEquals(recipe0,recipeBook.getRecipe("iron_plate"));
        assertEquals(recipe1,recipeBook.getRecipe("gear_wheel"));
        assertEquals(false,recipeBook.isContained("iron_ore"));
    }
}