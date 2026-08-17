package factorio.calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import factorio.model.*;

public class RecipeCalculator{

    private List<ItemStack> calculateIngredients(ItemStack target,RecipeBook recipeBook,
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

    public List<ItemStack> calculateIngredients(ItemStack target,RecipeBook recipeBook,ProductionSetting setting){
        return calculateIngredients(target, recipeBook,
            setting.furnaceProductivity(),
            setting.assemblerProductivity(),
            setting.chemicalPlantProductivity()
        );
    }

    private List<ItemStack> calculateIngredients(List<ItemStack> targets,RecipeBook recipeBook,
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

    public List<ItemStack> calculateIngredients(List<ItemStack> targets,RecipeBook recipeBook,ProductionSetting setting){
        return calculateIngredients(targets, recipeBook,
            setting.furnaceProductivity(),
            setting.assemblerProductivity(),
            setting.chemicalPlantProductivity()
        );
    }

    private ProductionNode calculateProductionTree(ItemStack target, RecipeBook recipeBook,
        double furnaceProductivity, double assemblerProductivity, double chemicalPlantProductivity
    ){
        if(!recipeBook.isContained(target.item()))return new ProductionNode(target, List.of());

        Recipe recipe = recipeBook.getRecipe(target.item());
        List<ProductionNode> children = new ArrayList<>();

        double ratio = calculateRatio(target,recipe,
            furnaceProductivity,assemblerProductivity,chemicalPlantProductivity);

        for(ItemStack ingredient: recipe.ingredients()){
            children.add(
                calculateProductionTree(ingredient.multiplyAmount(ratio), recipeBook,
                    furnaceProductivity, assemblerProductivity, chemicalPlantProductivity)
            );
        }

        return new ProductionNode(target,children);
    }

    public ProductionNode calculateProductionTree(
        ItemStack target,RecipeBook recipeBook, ProductionSetting setting
    ){
        return calculateProductionTree(
            target, recipeBook,
            setting.furnaceProductivity(),
            setting.assemblerProductivity(),
            setting.chemicalPlantProductivity()
        );
    }

    private List<ProductionNode> calculateProductionTree(List<ItemStack> targets, RecipeBook recipeBook,
        double furnaceProductivity, double assemblerProductivity, double chemicalPlantProductivity
    ){
        List<ProductionNode> nodes = new ArrayList<>();

        for(ItemStack target:targets){
            nodes.add(calculateProductionTree(target, recipeBook, 
                furnaceProductivity, assemblerProductivity, chemicalPlantProductivity));
        }

        return nodes;
    }

    public List<ProductionNode> calculateProductionTree(
        List<ItemStack> targets, RecipeBook recipeBook,ProductionSetting setting
    ){
        return calculateProductionTree(
            targets, recipeBook,
            setting.furnaceProductivity(),
            setting.assemblerProductivity(),
            setting.chemicalPlantProductivity()
        );
    }

    public Map<FactoryType,Double> calculateFacilityRequirements(
        ProductionNode node, RecipeBook recipeBook, ProductionSetting setting
    ){
        Map<FactoryType,Double> requirements= new HashMap<>();
        
        if(node.children().isEmpty()){
            return requirements;
        }

        Recipe recipe = recipeBook.getRecipe(node.itemStack().item());
        double numberOfProductionRun = calculateRatio(node.itemStack(), recipe, 
            setting.furnaceProductivity(), setting.assemblerProductivity(), setting.chemicalPlantProductivity());
        double productionSpeed = 1/recipe.craftingTime();

        switch (recipe.factoryType()) {
            case FURNACE:
                productionSpeed*=setting.furnaceProductionSpeed();
                break;
            case ASSEMBLER:
                productionSpeed*=setting.assemblerProductionSpeed();
                break;
            case CHEMICAL_PLANT:
                productionSpeed*=setting.chemicalPlantProductionSpeed();
            default:
                break;
        }

        requirements.put(recipe.factoryType(),numberOfProductionRun/productionSpeed);
        for(ProductionNode child:node.children()){
            Map<FactoryType,Double> childMap = calculateFacilityRequirements(
                child, recipeBook, setting
            );

            for(FactoryType key:childMap.keySet()){
                if(requirements.containsKey(key)){
                    requirements.replace(key, requirements.get(key)+childMap.get(key));
                }else{
                    requirements.put(key,childMap.get(key));
                }
            }
        }

        return requirements;
    }

    public Map<FactoryType,Double> calculateFacilityRequirements(
        List<ProductionNode> nodes, RecipeBook book, ProductionSetting setting
    ){
        Map<FactoryType,Double> requirements = new HashMap<>();

        for(ProductionNode node:nodes){
            Map<FactoryType,Double> subRequirements = calculateFacilityRequirements(
                node, book, setting);
            requirements = mergeFacilityRequirements(requirements, subRequirements);
        }
        return requirements;
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

    private Map<FactoryType,Double> mergeFacilityRequirements(
        Map<FactoryType,Double> req1, Map<FactoryType,Double> req2
    ){
        Map<FactoryType,Double> mergedRequirement = new HashMap<>(req1);
    for(FactoryType key:req2.keySet()){
                if(mergedRequirement.containsKey(key)){
                    mergedRequirement.replace(key, mergedRequirement.get(key)+req2.get(key));
                }else{
                    mergedRequirement.put(key,req2.get(key));
                }
            }
        return mergedRequirement;
    }
}