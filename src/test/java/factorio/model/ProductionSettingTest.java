package factorio.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ProductionSettingTest {
    @Test
    void storesAllProductionSetting(){
        ProductionSetting setting = new ProductionSetting(0.08,0.16,0.12,2,1.25,1);

        assertEquals(0.08,setting.furnaceProductivity());
        assertEquals(0.16,setting.assemblerProductivity());
        assertEquals(0.12,setting.chemicalPlantProductivity());
        assertEquals(2,setting.furnaceProductionSpeed());
        assertEquals(1.25,setting.assemblerProductionSpeed());
        assertEquals(1,setting.chemicalPlantProductionSpeed());
    }
}
