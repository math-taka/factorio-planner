package factorio.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ProductionNodeTest {
    
    @Test
    void storesItemStack(){
        ProductionNode ironOre = new ProductionNode(
            new ItemStack("iron_ore",10),List.of()
        );

        ItemStack ironPlateStack = new ItemStack("iron_plate",10);
        ProductionNode ironPlate = new ProductionNode(
            ironPlateStack, List.of(ironOre) 
        );

        assertEquals(ironPlateStack,ironPlate.itemStack());
    }

    @Test
    void storesChildren(){
        ProductionNode ironOre = new ProductionNode(
            new ItemStack("iron_ore",10),List.of()
        );

        ItemStack ironPlateStack = new ItemStack("iron_plate",10);
        ProductionNode ironPlate = new ProductionNode(
            ironPlateStack, List.of(ironOre) 
        );

        assertEquals(ironOre,ironPlate.children().get(0));
    }

    @Test
    void storesEmptyChildren(){
        ProductionNode ironOre = new ProductionNode(
            new ItemStack("iron_ore",10),List.of()
        );

        assertTrue(ironOre.children().isEmpty());
    }

    @Test
    void childrenIsImmutable(){
        ProductionNode ironOre = new ProductionNode(
            new ItemStack("iron_ore",10),new ArrayList<ProductionNode>()
        );

        ProductionNode phantom = new ProductionNode(
            new ItemStack("phantom_item",1),
            List.of()
        );

        assertThrows(
            UnsupportedOperationException.class,
            () -> ironOre.children().add(phantom)
        );
    }
}
