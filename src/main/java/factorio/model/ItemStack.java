package factorio.model;

public record ItemStack(
    String item,
    double amount
){
    public boolean sameItem(ItemStack is){
        return item.equals(is.item());
    }

    public ItemStack addAmount(double value){
        return new ItemStack(item,amount+value);
    }
}