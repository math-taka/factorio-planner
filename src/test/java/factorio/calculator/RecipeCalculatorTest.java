package factorio.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import factorio.model.*;
import java.util.List;

public class RecipeCalculatorTest {

    private RecipeBook book;
    private RecipeCalculator calculator;

    @BeforeEach
    void setUp(){
        ItemStack ironPlateForRecipe = new ItemStack("Iron Plate",1);
        ItemStack ironOreForIronPlate = new ItemStack("Iron Ore",1);

        Recipe ironPlateRecipe = new Recipe(
            List.of(ironOreForIronPlate),
            List.of(ironPlateForRecipe),
            3.2
        );

        ItemStack GearWheelForRecipe = new ItemStack("Gear Wheel",1);
        ItemStack ironPlateForGear = new ItemStack("Iron Plate",2);

        Recipe GearWheelRecipe = new Recipe(
            List.of(ironPlateForGear),
            List.of(GearWheelForRecipe),
            0.5
        );

        ItemStack transportBeltForRecipe = new ItemStack("Transport Belt",2);
        ItemStack ironPlateForBelt = new ItemStack("Iron Plate",1);
        ItemStack GearWheelForBelt = new ItemStack("Gear Wheel",1);

        Recipe transportBeltRecipe = new Recipe(
            List.of(ironPlateForBelt,GearWheelForBelt),
            List.of(transportBeltForRecipe),
            0.5
        );

        book = new RecipeBook(List.of(ironPlateRecipe,GearWheelRecipe,transportBeltRecipe));
        calculator = new RecipeCalculator();
    }
    
    @Test
    void sampleTest(){
        assertEquals(2,1+1);
    }

    @Test
    void calculatesIronPlateIngredients(){
        ItemStack need = new ItemStack("Iron Plate",10);
        List<ItemStack> expected = List.of(new ItemStack("Iron Ore",10));
        
        assertEquals(expected,calculator.calculateIngredients(need, book));

    }

    @Test
    void recursivelyCalculatesGearWheel(){
        ItemStack need = new ItemStack("Gear Wheel",2);
        List<ItemStack> expected = List.of(new ItemStack("Iron Ore",4));

        assertEquals(expected,calculator.calculateIngredients(need, book));
    }

    @Test
    void recursivelyCalculatesTransportBelt(){
        ItemStack need = new ItemStack("Transport Belt",4);
        List<ItemStack> expected = List.of(new ItemStack("Iron Ore",6));

        assertEquals(expected,calculator.calculateIngredients(need, book));
    }

    
}
