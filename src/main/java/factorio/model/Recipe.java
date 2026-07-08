package factorio.model;

import java.util.List;

public record Recipe(
    List<ItemStack> ingredients,
    List<ItemStack> products,
    double craftingTime
){
    
}