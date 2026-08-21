package factorio;

import java.util.List;
import java.util.ArrayList;

import java.io.File;
import java.io.IOException;

import factorio.model.ItemStack;
import factorio.model.ProductionNode;
import factorio.model.ProductionSetting;
import factorio.model.Recipe;
import factorio.model.RecipeBook;
import factorio.calculator.RecipeCalculator;
import factorio.io.ProductionTreePrinter;
import factorio.io.RecipeLoader;

public class Main{
    public static void main(String[] args){

        List<String> recipeNames =List.of(
            "iron_plate", "copper_plate", "steel_plate", "copper_cable", "gear_wheel", "electronic_circuit",
            "automation_science_pack", "transport_belt", "inserter", "logistic_science_pack"
        );
        File directory = new File("src/main/resources/recipes");
        RecipeBook book = null;
        try {
            book = RecipeLoader.load(directory, recipeNames);
        } catch (Exception e) {
            System.err.println(e);
        }

        List<ItemStack> needs = List.of(
            new ItemStack("automation_science_pack",1),
            new ItemStack("logistic_science_pack",1)
        );

        List<ItemStack> ingredients = new RecipeCalculator().calculateIngredients(needs, book);

        for(ItemStack ingredient:ingredients){
            System.out.println(ingredient);
        }

        ProductionSetting setting = new ProductionSetting(
            0.08, 0.12, 0.16,
            2.0, 1.25, 1);
        List<ProductionNode> nodes = new RecipeCalculator().calculateProductionTree(needs, book, setting);
        for(ProductionNode node:nodes){
            System.out.println(ProductionTreePrinter.print(node));
        }
    }
}