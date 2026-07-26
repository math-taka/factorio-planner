package factorio;

import java.util.List;
import java.util.ArrayList;

import java.io.File;
import java.io.IOException;

import factorio.model.ItemStack;
import factorio.model.Recipe;
import factorio.model.RecipeBook;
import factorio.calculator.RecipeCalculator;
import factorio.io.RecipeLoader;

public class Main{
    public static void main(String[] args){

        String[] recipeNames ={
            "iron_plate", "copper_plate", "steel_plate", "copper_cable", "gear_wheel", "electronic_circuit",
            "automation_science_pack", "transport_belt", "inserter", "logistic_science_pack"
        };

        String pathHead = "src/main/resources/recipes/", jsonIdentifier=".json";
        
        ArrayList<Recipe> recipes = new ArrayList<>();

        for(String recipeName:recipeNames){
            File recipeFile = new File(pathHead+recipeName+jsonIdentifier);
            try{
                recipes.add(RecipeLoader.loadRecipe(recipeFile));
            }catch(IOException e){
                System.err.println(e);
            }
        }

        RecipeBook book = new RecipeBook(recipes);

        List<ItemStack> needs = List.of(
            new ItemStack("automation_science_pack",1),
            new ItemStack("logistic_science_pack",1)
        );

        List<ItemStack> ingredients = new RecipeCalculator().calculateIngredients(needs, book);

        for(ItemStack ingredient:ingredients){
            System.out.println(ingredient);
        }
    }
}