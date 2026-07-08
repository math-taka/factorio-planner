package factorio;

import java.util.List;

import factorio.model.ItemStack;
import factorio.model.Recipe;

public class Main{
    public static void main(String[] args){

        ItemStack iron = new ItemStack("Iron Plate", 10);

        System.out.println(iron);

        ItemStack ironOre = new ItemStack("Iron Ore",1);
        ItemStack ironPlate = new ItemStack("Iron Plate",1);

        Recipe recipe = new Recipe(List.of(ironOre),List.of(ironPlate),3.2);

        System.out.println(recipe);
        
    }
}