package factorio.translation;

public interface ItemNameTranslator {
    String toInternalName(String displayName);
    String toDisplayName(String internalName);
}
