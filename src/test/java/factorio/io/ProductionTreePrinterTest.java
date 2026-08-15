package factorio.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import factorio.model.ItemStack;
import factorio.model.ProductionNode;

public class ProductionTreePrinterTest {
    @Test
    void printsSingleNode(){
        ProductionNode ironOre = new ProductionNode(
            new ItemStack("iron_ore",10),
            List.of()
        );

        String expected = "└─iron_ore × 10.0\n";

        assertEquals(expected,ProductionTreePrinter.print(ironOre));
    }

    @Test
    void printsNodeWithSingleChild(){
        ProductionNode child = new ProductionNode(
            new ItemStack("iron_ore",10),List.of()
        );
        ProductionNode parent = new ProductionNode(
            new ItemStack("iron_plate",10),
            List.of(child)
        );

        String expected = "└─iron_plate × 10.0\n  └─iron_ore × 10.0\n";

        assertEquals(expected,ProductionTreePrinter.print(parent));
    }

    @Test
    void printsNodeWithMultipleChildren(){
        ProductionNode child1 = new ProductionNode(
            new ItemStack("iron_plate",10), List.of()
        );
        ProductionNode child2 = new ProductionNode(
            new ItemStack("gear_wheel",10),
            List.of()
        );
        ProductionNode parent = new ProductionNode(
            new ItemStack("transport_belt",20),
            List.of(child1,child2)
        );

        String expected = "└─transport_belt × 20.0\n  ├─iron_plate × 10.0\n  └─gear_wheel × 10.0\n";

        assertEquals(expected, ProductionTreePrinter.print(parent));
    }

    @Test
    void printsDeepProductionTree(){
        ProductionNode child1_1 = new ProductionNode(
            new ItemStack("iron_ore",10), List.of()
        );
        ProductionNode child1 = new ProductionNode(
            new ItemStack("iron_plate",10),List.of(child1_1)
        );
        ProductionNode child2_1_1 = new ProductionNode(
            new ItemStack("iron_ore",20), List.of()
        );
        ProductionNode child2_1 = new ProductionNode(
            new ItemStack("iron_plate",20), List.of(child2_1_1)
        );
        ProductionNode child2 = new ProductionNode(
            new ItemStack("gear_wheel",10),List.of(child2_1)
        );
        ProductionNode parent = new ProductionNode(
            new ItemStack("transport_belt",20),List.of(child1,child2)
        );

        String expected = """
                └─transport_belt × 20.0
                  ├─iron_plate × 10.0
                  │ └─iron_ore × 10.0
                  └─gear_wheel × 10.0
                    └─iron_plate × 20.0
                      └─iron_ore × 20.0
                """;
        
        assertEquals(expected,ProductionTreePrinter.print(parent));
    }
}
