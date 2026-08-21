package factorio.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import factorio.model.*;

public final class RecipeLoader {
    
    public static RecipeBook loadBook(File file) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        List<Recipe> recipes = mapper.readValue(file,new TypeReference<List<Recipe>>(){});

        return new RecipeBook(recipes);
    }
    

    public static Recipe loadRecipe(File file) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        Recipe recipe = mapper.readValue(file,Recipe.class);

        return recipe;
    }

    public static RecipeBook load(File directory) throws IOException{
        ArrayList<Recipe> recipes = new ArrayList<>();
        File[] files = directory.listFiles();

        for(File file:files){
            recipes.add(loadRecipe(file));
        }

        return new RecipeBook(recipes);
    }

    public static RecipeBook load(File directory,List<String> recipeNames) throws IOException{
        ArrayList<Recipe> recipes = new ArrayList<>();

        for(String recipeName:recipeNames){
            File recipeFile = new File(directory,recipeName+".json");

            recipes.add(loadRecipe(recipeFile));
        }

        return new RecipeBook(recipes);
    }
}