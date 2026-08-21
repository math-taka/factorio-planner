package factorio.translation;

import java.util.Map;
import java.util.stream.Collectors;

public class JapaneseItemNameTranslator implements ItemNameTranslator {
    private static final Map<String,String> TRANSLATIONS = Map.of(
        "鉄板", "iron_plate",
        "銅板", "copper_plate",
        "歯車", "gear_wheel"
    );

    private static final Map<String,String> REVERSE_TRANSLATIONS =
        TRANSLATIONS.entrySet().stream().collect(
            Collectors.toUnmodifiableMap(Map.Entry::getValue, Map.Entry::getKey)
        );

    @Override
    public String toInternalName(String displayName) {
        return TRANSLATIONS.get(displayName);
    }

    @Override
    public String toDisplayName(String internalName) {
        return REVERSE_TRANSLATIONS.get(internalName);
    }
    
}
