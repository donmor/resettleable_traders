package com.github.donmor.resettleabletraders.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.commons.lang3.tuple.Pair;

import com.github.donmor.resettleabletraders.ResettleableTradersMod;

@Mod(ResettleableTradersMod.MOD_ID)
public final class ResettleableTradersModForge {
    public ResettleableTradersModForge() {
        // Submit our event bus to let Architectury API register our content on the
        // right time.
        EventBuses.registerModEventBus(ResettleableTradersMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

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
        ModLoadingContext.get().registerConfig(Type.COMMON, config.getRight());
        ResettleableTradersMod.options = () -> config.getRight().getValues()
                .get("DiscardOffers") instanceof BooleanValue value ? value.get() : false;
        ResettleableTradersMod.init();
    }
}
