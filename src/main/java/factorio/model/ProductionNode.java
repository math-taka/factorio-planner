package factorio.model;

import java.util.List;

public record ProductionNode(
    ItemStack itemStack,
    List<ProductionNode> children
) {
    public ProductionNode{
        children = List.copyOf(children);
    }
}
