package factorio;

import java.util.List;

import factorio.model.ItemStack;
import factorio.model.Recipe;
import factorio.model.RecipeBook;
import factorio.calculator.RecipeCalculator;

public class Main{
    public static void main(String[] args){

        ItemStack needs = new ItemStack("Transport Belt", 10);

        ItemStack ironOre = new ItemStack("Iron Ore",1);
        ItemStack ironPlate = new ItemStack("Iron Plate",1);

        Recipe ironPlateRecipe = new Recipe(List.of(ironOre),List.of(ironPlate),3.2);

        ItemStack ironPlateForGear = new ItemStack("Iron Plate",2);
        ItemStack GearWheel = new ItemStack("Gear Wheel",1);

        Recipe gearWheelRecipe = new Recipe(List.of(ironPlateForGear),List.of(GearWheel),0.5);

        ItemStack gearForBelt = new ItemStack("Gear Wheel",1);
        ItemStack ironPlateForBelt = new ItemStack("Iron Plate",1);
        ItemStack TransportBelt = new ItemStack("Transport Belt",2);
        
        Recipe transportBeltRecipe = new Recipe(
            List.of(gearForBelt,ironPlateForBelt),
            List.of(TransportBelt),0.5
        );

        RecipeBook selected=new RecipeBook(
            List.of(ironPlateRecipe,gearWheelRecipe,transportBeltRecipe)
        );
        
        RecipeCalculator calculator = new RecipeCalculator();

        List<ItemStack> result=calculator.calculateIngredients(needs, selected);

        System.out.println(result);
        
    }
}