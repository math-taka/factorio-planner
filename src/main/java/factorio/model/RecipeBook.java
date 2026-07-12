package factorio.model;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class RecipeBook{

    private final Map<String,Recipe> book;

    public RecipeBook(Map<String,Recipe> book){
        this.book = Map.copyOf(book);
    }

    public RecipeBook(List<Recipe> recipes){
        Map<String,Recipe> map=new HashMap<String,Recipe>();

        for(Recipe recipe:recipes){
            for(ItemStack product:recipe.products()){
                map.put(product.item(),recipe);
            }
        }

        this.book = Map.copyOf(map);
    }

    public Recipe getRecipe(String item){
        return book.get(item);
    }

    public boolean isContained(String item){
        return book.containsKey(item);
    }
}