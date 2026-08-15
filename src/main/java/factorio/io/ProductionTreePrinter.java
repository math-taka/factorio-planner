package factorio.io;

import factorio.model.ItemStack;
import factorio.model.ProductionNode;

public class ProductionTreePrinter {
    public static String print(ProductionNode node){
        return print(node,"",true);
    }

    private static String print(ProductionNode node, String prefix,boolean isLast){
        String nodeString , childPrefix;

        if(isLast){
            nodeString = prefix+"└─"+itemStackToString(node.itemStack())+"\n";
            childPrefix = prefix+"  ";
        }else{
            nodeString = prefix+"├─"+itemStackToString(node.itemStack())+"\n";
            childPrefix = prefix+"│ ";
        }
        for(int i=0;i<node.children().size();i++){
            ProductionNode child=node.children().get(i);
            boolean isLastChild = (i==node.children().size()-1);
            
            nodeString+=print(child, childPrefix, isLastChild);
        }
        
        return nodeString;
    }

    private static String itemStackToString(ItemStack itemStack){
        return itemStack.item()+" × "+itemStack.amount();
    }
}

