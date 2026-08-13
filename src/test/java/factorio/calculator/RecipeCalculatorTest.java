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

    @Test
    void productionTreeForRawMaterial(){
        ItemStack target = new ItemStack("iron_ore",20);
        ProductionNode expected = new ProductionNode(target, List.of());

        assertEquals(calculator.calculateProductionTree(target, book, 
            0, 0, 0), expected);
    }

    @Test
    void productionTreeForSingleStepRecipe(){
        ItemStack target = new ItemStack("iron_plate",10);
        ProductionNode result = calculator.calculateProductionTree(target, book,
             0, 0, 0);
        
        ProductionNode expectedChild = new ProductionNode(new ItemStack("iron_ore",10),List.of());
        ProductionNode expected = new ProductionNode(target,List.of(expectedChild));
        
        assertEquals(result, expected);
    }

    @Test
    void productionTreeForMultiStepRecipe(){
        ItemStack target = new ItemStack("gear_wheel",10);
        ProductionNode result = calculator.calculateProductionTree(target, book,
             0, 0, 0);

        ProductionNode expectedChild2 = new ProductionNode(new ItemStack("iron_ore",20),List.of());
        ProductionNode expectedChild1 = new ProductionNode(new ItemStack("iron_plate",20),List.of(expectedChild2));
        ProductionNode expected = new ProductionNode(target,List.of(expectedChild1));
        
        assertEquals(result,expected);
    }

    @Test
    void productionTreeForMultipleIngredients(){
        ItemStack target = new ItemStack("transport_belt",20);
        ProductionNode result = calculator.calculateProductionTree(target, book,
             0, 0, 0);

        ProductionNode expectedChild3_1 = new ProductionNode(new ItemStack("iron_ore",10),List.of());
        ProductionNode expectedChild3_2 = new ProductionNode(new ItemStack("iron_ore",20),List.of());
        ProductionNode expectedChild2_1 = new ProductionNode(new ItemStack("iron_plate",10),List.of(expectedChild3_1));
        ProductionNode expectedChild2_2 = new ProductionNode(new ItemStack("iron_plate",20),List.of(expectedChild3_2));
        ProductionNode expectedChild1 = new ProductionNode(new ItemStack("gear_wheel",10),List.of(expectedChild2_2));
        ProductionNode expected = new ProductionNode(target,List.of(expectedChild2_1,expectedChild1));
        
        assertEquals(result,expected);
    }
}
