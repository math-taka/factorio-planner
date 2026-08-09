package factorio.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import factorio.io.RecipeLoader;
import factorio.model.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class RecipeCalculatorTest {

    private RecipeBook book;
    private RecipeCalculator calculator;

    @BeforeEach
    void setUp() throws IOException{
        book = RecipeLoader.load(new File("src/test/resources/recipeCalculatorTest.json"));
        calculator = new RecipeCalculator();
    }
    
    @Test
    void calculatesIronPlateIngredients(){
        ItemStack need = new ItemStack("iron_plate",10);
        List<ItemStack> expected = List.of(new ItemStack("iron_ore",10));
        
        assertEquals(expected,calculator.calculateIngredients(need, book));

    }

    @Test
    void recursivelyCalculatesGearWheel(){
        ItemStack need = new ItemStack("gear_wheel",2);
        List<ItemStack> expected = List.of(new ItemStack("iron_ore",4));

        assertEquals(expected,calculator.calculateIngredients(need, book));
    }

    @Test
    void recursivelyCalculatesTransportBelt(){
        ItemStack need = new ItemStack("transport_belt",4);
        List<ItemStack> expected = List.of(new ItemStack("iron_ore",6));

        assertEquals(expected,calculator.calculateIngredients(need, book));
    }

    @Test
    void calculatesNoRecipeItem(){
        ItemStack need = new ItemStack("Iron Ore",10);
        List<ItemStack> expected = List.of(need);

        assertEquals(expected,calculator.calculateIngredients(need,book));
    }

    @Test
    void targetsIsEmpty(){
        List<ItemStack> expected = List.of();
        assertEquals(expected,calculator.calculateIngredients(List.of(),book));
    }

    @Test
    void targetsHasATarget(){
        List<ItemStack> targets = List.of(
            new ItemStack("iron_plate",10)
        );
        List<ItemStack> expected = List.of(
            new ItemStack("iron_ore",10)
        );

        assertEquals(expected,calculator.calculateIngredients(targets, book));
    }

    @Test
    void targetsHasTwoTargets(){
        List<ItemStack> targets = List.of(
            new ItemStack("iron_plate",10),
            new ItemStack("gear_wheel",5)
        );
        List<ItemStack> expected = List.of(
            new ItemStack("iron_ore",20)
        );

        assertEquals(expected,calculator.calculateIngredients(targets, book));
    }

    @Test
    void noProductivityCalculation(){
        ItemStack target = new ItemStack("iron_plate",10);
        assertEquals(
            calculator.calculateIngredients(target, book,0,0,0),
            calculator.calculateIngredients(target, book));
    }

    @Test
    void assemblingWithPositiveProductivity(){
        ItemStack target = new ItemStack("gear_wheel",11);
        List<ItemStack> expect = List.of(new ItemStack("iron_ore", 20));

        assertEquals(calculator.calculateIngredients(target, book,0,0.1,0), expect);
    }

    @Test
    void CalculateRecipebeingNotProductivityUsable(){
        ItemStack target = new ItemStack("transport_belt",22);
        List<ItemStack> expect = List.of(new ItemStack("iron_ore", 31));

        assertEquals(calculator.calculateIngredients(target, book,0,0.1,0), expect);
    }
}
