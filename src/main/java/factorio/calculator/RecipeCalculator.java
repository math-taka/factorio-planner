package factorio.calculator;

import java.util.ArrayList;
import java.util.List;
import factorio.model.*;

public class RecipeCalculator{

    public List<ItemStack> calculateIngredients(ItemStack target,RecipeBook recipeBook){
        Recipe recipe = recipeBook.getRecipe(target.item());
        List<ItemStack> ingredients = new ArrayList<>();

        double ratio=target.amount();
        for(ItemStack itm:recipe.products()){
            if(itm.sameItem(target)){
                ratio/=itm.amount();
                break;
            }
        }

        for(ItemStack ingredient : recipe.ingredients()){
            double amount= ratio*ingredient.amount();
            if(recipeBook.isContained(ingredient.item())){
                List<ItemStack> subIngredients=calculateIngredients(
                    new ItemStack(ingredient.item(),amount), recipeBook);
                ingredients=mergeItemStackList(ingredients, subIngredients);
            }else{
                ingredients.add(new ItemStack(ingredient.item(),amount));
            }
        }

        return ingredients;
    }

    private List<ItemStack> mergeItemStackList(List<ItemStack> list1,List<ItemStack> list2){
        List<ItemStack> mergedList=new ArrayList<>(list1);

        //list1 と list2 を合わせたリスト margedList を作る
        listloop:for(int i=0;i<list2.size();i++){
            ItemStack itst=list2.get(i);
            for(int j=0;j<mergedList.size();j++){
                ItemStack listed=mergedList.get(j);
                if(listed.sameItem(itst)){
                    mergedList.set(j,listed.addAmount(itst.amount()));
                    continue listloop;
                }
            }

            mergedList.add(itst);
        }

        return mergedList;
    }
}