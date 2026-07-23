package factorio.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

import factorio.model.*;

public final class RecipeLoader {
    
    public static RecipeBook load(File file) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        List<Recipe> recipes = mapper.readValue(file,new TypeReference<List<Recipe>>(){});

        return new RecipeBook(recipes);
    }
}