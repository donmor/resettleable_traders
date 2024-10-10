package com.github.donmor.resettleabletraders.fabric;

import net.fabricmc.api.ModInitializer;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.fml.config.ModConfig.Type;

import org.apache.commons.lang3.tuple.Pair;

import com.github.donmor.resettleabletraders.ResettleableTradersMod;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;

public final class ResettleableTradersModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        class ResettleableTradersModConfig {
        }
        Pair<ResettleableTradersModConfig, ForgeConfigSpec> config = new ForgeConfigSpec.Builder()
                .configure(builder -> builder.comment("Convert traders even if they're still tradeable")
                        .translation("config.resettleable_traders.discard_offers")
                        .define("DiscardOffers", false) != null
                                ? new ResettleableTradersModConfig() {
                                }
                                : null);
        ForgeConfigRegistry.INSTANCE.register(ResettleableTradersMod.MOD_ID, Type.COMMON, config.getRight());
        ResettleableTradersMod.options = () -> config.getRight().getValues()
                .get("DiscardOffers") instanceof BooleanValue value ? value.get() : false;
        ResettleableTradersMod.init();
    }
}
