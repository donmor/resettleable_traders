package com.github.donmor.resettleabletraders;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.InteractionEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;;

public final class ResettleableTradersMod {
    public static ResettleableTradersModOptionIf options;

    public static final String MOD_ID = "resettleable_traders";

    public static void init() {
        InteractionEvent.INTERACT_ENTITY.register((player, entity, hand) -> {
            if (hand != InteractionHand.MAIN_HAND)
                return EventResult.pass();
            if (!(player.getMainHandItem().getItem() instanceof BedItem))
                return EventResult.pass();
            if (!(entity instanceof WanderingTrader))
                return EventResult.pass();
            WanderingTrader trader = (WanderingTrader) entity;
            if (options instanceof ResettleableTradersModOptionIf && !options.DiscardOffers())
                for (MerchantOffer offer : trader.getOffers())
                    if (!offer.isOutOfStock())
                        return EventResult.pass();
            Level world = player.level();
            Villager newVillager = trader.convertTo(EntityType.VILLAGER, false);
            newVillager.setVariant(VillagerType.byBiome(
                    world.getBiome(BlockPos.containing(newVillager.getX(), newVillager.getY(), newVillager.getZ()))));
            if (world instanceof ServerLevel _level) {
                trader.remove(Entity.RemovalReason.DISCARDED);
                _level.sendParticles(ParticleTypes.HAPPY_VILLAGER, newVillager.getX(), newVillager.getY(), newVillager.getZ(), 8, 1, 1, 1, 1);
            }
            world.addFreshEntity(newVillager);
            return EventResult.interruptTrue();
        });
    }

    public interface ResettleableTradersModOptionIf {

        boolean DiscardOffers();
    }
}
