package factorio.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import factorio.model.*;

public class RecipeCalculator{

    public List<ItemStack> calculateIngredients(ItemStack target,RecipeBook recipeBook,
            double furnaceProductivity,double assemblerProductivity,double chemicalPlantProductivity){
        if(!recipeBook.isContained(target.item()))return List.of(target);

        Recipe recipe = recipeBook.getRecipe(target.item());
        List<ItemStack> ingredients = new ArrayList<>();

        double ratio=calculateRatio(target,recipe,
            furnaceProductivity,assemblerProductivity,chemicalPlantProductivity);

        for(ItemStack ingredient : recipe.ingredients()){
            List<ItemStack> subIngredients = calculateIngredients(
                ingredient.multiplyAmount(ratio), recipeBook,
                furnaceProductivity,assemblerProductivity,chemicalPlantProductivity);
            ingredients=mergeItemStackList(ingredients, subIngredients);
        }

        return ingredients;
    }

     public List<ItemStack> calculateIngredients(ItemStack target,RecipeBook recipeBook){
        return calculateIngredients(target, recipeBook,0,0,0);
     }

    public List<ItemStack> calculateIngredients(List<ItemStack> targets,RecipeBook recipeBook,
            double furnaceProductivity,double assemblerProductivity,double chemicalPlantProductivity){
        Objects.requireNonNull(targets);
        Objects.requireNonNull(recipeBook);
        
        List<ItemStack> ingredients = new ArrayList<>();

        for(ItemStack target:targets){
            ingredients=mergeItemStackList(ingredients, calculateIngredients(target, recipeBook,
                furnaceProductivity,assemblerProductivity,chemicalPlantProductivity));
        }

        return ingredients;
    }

    public List<ItemStack> calculateIngredients(List<ItemStack> targets,RecipeBook recipeBook){
        return calculateIngredients(targets, recipeBook, 0, 0, 0);
    }

    private double calculateRatio(ItemStack target,Recipe recipe,
            double furnaceProductivity,double assemblerProductivity,double chemicalPlantProductivity){
        double ratio=target.amount();

        if(recipe.usableProductivityModule()){
            switch(recipe.factoryType()){
                case FURNACE-> ratio/=(1+furnaceProductivity);
                case ASSEMBLER->ratio/=(1+assemblerProductivity);
                case CHEMICAL_PLANT->ratio/=(1+chemicalPlantProductivity);
            }
        }

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