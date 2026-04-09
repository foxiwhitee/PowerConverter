package foxiwhitee.PowerConverter.config;

import foxiwhitee.FoxLib.config.Config;
import foxiwhitee.FoxLib.config.ConfigValue;

@Config(folder = "Fox-Mods", name = "PowerConverter")
public class ModConfig {
    @ConfigValue(desc = "Enable tooltips?")
    public static boolean enableTooltips = true;

    @ConfigValue(desc = "How many RF are equal to 1 EU?")
    public static int rfInEu = 4;


    // Upgrades Power Converter
    @ConfigValue(category = "Upgrades.PowerConverter", name = "storageMultiplierEU", desc = "How many times will the mechanism's EU storage be increased with each upgrade?")
    public static double powerConverterUpgradeEUStorageMultiplier = 2;

    @ConfigValue(category = "Upgrades.PowerConverter", name = "storageMultiplierRF", desc = "How many times will the mechanism's RF storage be increased with each upgrade?")
    public static double powerConverterUpgradeRFStorageMultiplier = 2;

    @ConfigValue(category = "Upgrades.PowerConverter", name = "outputMultiplierEU", desc = "How many times will the rate of EU energy output increase?")
    public static double powerConverterUpgradeEUOutputMultiplier = 8;

    @ConfigValue(category = "Upgrades.PowerConverter", name = "outputMultiplierRF", desc = "How many times will the rate of RF energy output increase?")
    public static double powerConverterUpgradeRFOutputMultiplier = 8;


    // Power Converter
    @ConfigValue(category = "PowerConverter", name = "storageEU", desc = "The maximum amount of EU energy that the block can hold")
    public static double powerConverterEUStorage = 1_000_000;

    @ConfigValue(category = "PowerConverter", name = "storageRF", desc = "The maximum amount of RF energy that the block can hold")
    public static double powerConverterRFStorage = 4_000_000;

    @ConfigValue(category = "PowerConverter", name = "outputEU", desc = "How much EU energy does it transfer at a time?")
    public static double powerConverterEUPerTick = 1024;

    @ConfigValue(category = "PowerConverter", name = "outputRF", desc = "How much RF energy does it transfer at a time?")
    public static double powerConverterRFPerTick = 4096;
}
