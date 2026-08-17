package factorio.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import factorio.io.RecipeLoader;
import factorio.model.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeCalculatorTest {

    private RecipeBook book;
    private RecipeCalculator calculator;
    private ProductionSetting defaultSetting;

    @BeforeEach
    void setUp() throws IOException{
        book = RecipeLoader.load(new File("src/test/resources/recipeCalculatorTest.json"));
        calculator = new RecipeCalculator();
        defaultSetting = new ProductionSetting(
            0,0,0,
            2,1.25,1
        );
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
            calculator.calculateIngredients(target, book,defaultSetting),
            calculator.calculateIngredients(target, book));
    }

    @Test
    void assemblingWithPositiveProductivity(){
        ItemStack target = new ItemStack("gear_wheel",11);
        ProductionSetting positiveProductivity = new ProductionSetting(
            0,0.1,0,
            1,1,1
        );
        List<ItemStack> expect = List.of(new ItemStack("iron_ore", 20));

        assertEquals(calculator.calculateIngredients(target, book,positiveProductivity), expect);
    }

    @Test
    void ignoresProductivityForNonProductivityRecipe(){
        ItemStack target = new ItemStack("transport_belt",22);
        ProductionSetting positiveProductivity = new ProductionSetting(
            0,0.1,0,
            1,1,1
        );
        List<ItemStack> expect = List.of(new ItemStack("iron_ore", 31));

        assertEquals(calculator.calculateIngredients(target, book,positiveProductivity), expect);
    }

    @Test
    void productionTreeForRawMaterial(){
        ItemStack target = new ItemStack("iron_ore",20);
        ProductionNode expected = new ProductionNode(target, List.of());

        assertEquals(calculator.calculateProductionTree(target, book, 
            defaultSetting), expected);
    }

    @Test
    void productionTreeForSingleStepRecipe(){
        ItemStack target = new ItemStack("iron_plate",10);
        ProductionNode result = calculator.calculateProductionTree(target, book,
             defaultSetting);
        
        ProductionNode expectedChild = new ProductionNode(new ItemStack("iron_ore",10),List.of());
        ProductionNode expected = new ProductionNode(target,List.of(expectedChild));
        
        assertEquals(result, expected);
    }

    @Test
    void productionTreeForMultiStepRecipe(){
        ItemStack target = new ItemStack("gear_wheel",10);
        ProductionNode result = calculator.calculateProductionTree(target, book,defaultSetting);

        ProductionNode expectedChild2 = new ProductionNode(new ItemStack("iron_ore",20),List.of());
        ProductionNode expectedChild1 = new ProductionNode(new ItemStack("iron_plate",20),List.of(expectedChild2));
        ProductionNode expected = new ProductionNode(target,List.of(expectedChild1));
        
        assertEquals(result,expected);
    }

    @Test
    void productionTreeForMultipleIngredients(){
        ItemStack target = new ItemStack("transport_belt",20);
        ProductionNode result = calculator.calculateProductionTree(target, book,defaultSetting);

        ProductionNode expectedChild3_1 = new ProductionNode(new ItemStack("iron_ore",10),List.of());
        ProductionNode expectedChild3_2 = new ProductionNode(new ItemStack("iron_ore",20),List.of());
        ProductionNode expectedChild2_1 = new ProductionNode(new ItemStack("iron_plate",10),List.of(expectedChild3_1));
        ProductionNode expectedChild2_2 = new ProductionNode(new ItemStack("iron_plate",20),List.of(expectedChild3_2));
        ProductionNode expectedChild1 = new ProductionNode(new ItemStack("gear_wheel",10),List.of(expectedChild2_2));
        ProductionNode expected = new ProductionNode(target,List.of(expectedChild2_1,expectedChild1));
        
        assertEquals(result,expected);
    }

    @Test
    void productionTreeForMultipleTargets(){
        List<ItemStack> targets = List.of(
            new ItemStack("iron_plate",10), new ItemStack("gear_wheel",10)
        );
        List<ProductionNode> result = calculator.calculateProductionTree(
            targets, book, defaultSetting
        );

        ProductionNode expected1Child = new ProductionNode(
            new ItemStack("iron_ore",10),List.of()
        );
        ProductionNode expected1 = new ProductionNode(
            targets.get(0),List.of(expected1Child)
        );

        ProductionNode expected2Child1_1 = new ProductionNode(
            new ItemStack("iron_ore",20),List.of()
        );
        ProductionNode expected2Child1=new ProductionNode(
            new ItemStack("iron_plate",20),List.of(expected2Child1_1)
        );
        ProductionNode expected2 = new ProductionNode(
            targets.get(1),List.of(expected2Child1)
        );

        assertEquals(2,result.size());
        assertEquals(expected1,result.get(0));
        assertEquals(expected2,result.get(1));
    }

    @Test
    void productionTreesForEmptyTargets(){
        List<ProductionNode> result = calculator.calculateProductionTree(
            List.of(), book, defaultSetting
        );

        assertEquals(true,result.isEmpty());
    }

    @Test
    void calculatesFacilityRequirementForSingleStepNode(){
        ProductionNode singleStep = calculator.calculateProductionTree(
            new ItemStack("iron_plate",10), book, defaultSetting);
        HashMap<FactoryType,Double> expected = new HashMap<>();
        expected.put(FactoryType.FURNACE,16.0);

        assertEquals(expected,calculator.calculateFacilityRequirements(
            singleStep, book,defaultSetting));
    }

    @Test
    void calculatesFacilityRequirementForMultiStepNode(){
        ProductionNode multiStepNode = calculator.calculateProductionTree(
            new ItemStack("gear_wheel",10), book, defaultSetting);
        Map<FactoryType,Double> result = calculator.calculateFacilityRequirements(
            multiStepNode, book, defaultSetting);
        
        assertEquals(32,result.get(FactoryType.FURNACE));
        assertEquals(4,result.get(FactoryType.ASSEMBLER));
        assertFalse(result.containsKey(FactoryType.CHEMICAL_PLANT));
    }

    @Test
    void calculatesFacilityRequirementWithProductionSpeed(){
        ProductionNode node = calculator.calculateProductionTree(
            new ItemStack("iron_plate",10), book, defaultSetting);
        ProductionSetting setting = new ProductionSetting(
            0,0,0,1,1,1
        );
        Map<FactoryType,Double> result = calculator.calculateFacilityRequirements(
            node, book, setting);
        
        assertEquals(32,result.get(FactoryType.FURNACE));
    }

    @Test
    void calculatesFacilityRequirementWithProductivity(){
        ProductionNode node = calculator.calculateProductionTree(
            new ItemStack("iron_plate",10), book, defaultSetting);
        ProductionSetting setting = new ProductionSetting(
            0.6,0,0,
            2,1,1
        );
        Map<FactoryType,Double> result = calculator.calculateFacilityRequirements(
            node, book, setting);
        
        assertEquals(10,result.get(FactoryType.FURNACE));
    }

    @Test
    void ignoresRawMaterialNode(){
        ProductionNode ironOre = new ProductionNode(
            new ItemStack("ironOre",100),List.of()
        );
        
        assertTrue(
            calculator.calculateFacilityRequirements(
                ironOre, book, defaultSetting).isEmpty()
        );
    }

    @Test
    void aggregatesSameFactoryTypeRequirements(){
        ProductionNode node = calculator.calculateProductionTree(
            new ItemStack("transport_belt",20),book,defaultSetting
        );

        Map<FactoryType,Double> result =calculator.calculateFacilityRequirements(
            node, book, defaultSetting);

        assertEquals(48,result.get(FactoryType.FURNACE));
        assertEquals(8,result.get(FactoryType.ASSEMBLER));
    }

    @Test
    void calculatesFacilityRequirementForNoTree(){
        Map<FactoryType,Double> result = calculator.calculateFacilityRequirements(
            List.of(), book, defaultSetting);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void calculatesFacilityRequirementForSingleTree(){
        ProductionNode node = calculator.calculateProductionTree(
            new ItemStack("iron_plate",10), book, defaultSetting);
        assertEquals(
            calculator.calculateFacilityRequirements(node, book, defaultSetting),
            calculator.calculateFacilityRequirements(List.of(node), book, defaultSetting)
        );
    }

    @Test
    void calculatesFacilityRequirementsForMultiTree(){
        ProductionNode node1 = calculator.calculateProductionTree(
            new ItemStack("iron_plate",10),book,defaultSetting
        );
        ProductionNode node2 = calculator.calculateProductionTree(
            new ItemStack("gear_wheel",10), book, defaultSetting
        );

        Map<FactoryType,Double> result = calculator.calculateFacilityRequirements(
            List.of(node1,node2),book,defaultSetting
        );

        assertEquals(48, result.get(FactoryType.FURNACE));
        assertEquals(4,result.get(FactoryType.ASSEMBLER));
    }
}
