package factorio.model;

public record ProductionSetting(
    double furnaceProductivity,
    double assemblerProductivity,
    double chemicalPlantProductivity,
    double furnaceProductionSpeed,
    double assemblerProductionSpeed,
    double chemicalPlantProductionSpeed
) {
}
