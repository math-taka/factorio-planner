package factorio.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import factorio.model.*;
import java.util.List;

public class RecipeCalculatorTest {
    
    @Test
    void sampleTest(){
        assertEquals(2,1+1);
    }

    @Test
    void calculatesIronPlateIngredients(){
        ItemStack need = new ItemStack("Iron Plate",10);
        
        ItemStack ironPlateForRecipe = new ItemStack("Iron Plate",1);
        ItemStack ironOreForRecipe = new ItemStack("Iron Ore",1);

        Recipe ironPlateRecipe = new Recipe(List.of(ironOreForRecipe),List.of(ironPlateForRecipe),3.2);
        RecipeBook book =new RecipeBook(List.of(ironPlateRecipe));

        RecipeCalculator calculator= new RecipeCalculator();

        List<ItemStack> expected = List.of(new ItemStack("Iron Ore",10));
        assertEquals(expected,calculator.calculateIngredients(need, book));

    }
}
