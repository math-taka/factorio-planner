package factorio.calculator;

import java.util.ArrayList;
import java.util.List;
import factorio.model.*;

public class RecipeCalculator{

    public List<ItemStack> calculateIngredients(ItemStack target,RecipeBook recipeBook){
        if(!recipeBook.isContained(target.item()))return List.of(target);

        Recipe recipe = recipeBook.getRecipe(target.item());
        List<ItemStack> ingredients = new ArrayList<>();

        double ratio=calculateRatio(target,recipe);

        for(ItemStack ingredient : recipe.ingredients()){
            List<ItemStack> subIngredients = calculateIngredients(
                ingredient.multiplyAmount(ratio), recipeBook);
            ingredients=mergeItemStackList(ingredients, subIngredients);
        }

        return ingredients;
    }

    private double calculateRatio(ItemStack target,Recipe recipe){
        double ratio=target.amount();
        for(ItemStack product:recipe.products()){
            if(product.sameItem(target)){
                ratio/=product.amount();
                break;
            }
        }

        return ratio;
    }

    private List<ItemStack> mergeItemStackList(List<ItemStack> list1,List<ItemStack> list2){
        List<ItemStack> mergedList=new ArrayList<>(list1);

        listloop:for(ItemStack adding:list2){
            for(int j=0;j<mergedList.size();j++){
                ItemStack existing=mergedList.get(j);
                if(existing.sameItem(adding)){
                    mergedList.set(j,existing.addAmount(adding.amount()));
                    continue listloop;
                }
            }

            mergedList.add(adding);
        }

        return mergedList;
    }
}