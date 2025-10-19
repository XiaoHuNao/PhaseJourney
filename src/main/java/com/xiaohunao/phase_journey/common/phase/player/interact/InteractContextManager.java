package com.xiaohunao.phase_journey.common.phase.player.interact;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.xiaohunao.phase_journey.common.phase.InteractType;
import com.xiaohunao.phase_journey.common.phase.PhaseManager;
import com.xiaohunao.phase_journey.common.phase.PhaseType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import net.neoforged.neoforge.event.entity.living.AnimalTameEvent;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.function.Function;

public class InteractContextManager extends PhaseManager<InteractContext> {
        public static final InteractContextManager MANAGER = new InteractContextManager();

        public final Multimap<EntityType<?>, InteractType> entityTypeInteracts = ArrayListMultimap.create();

        @Override
        public void register(PhaseType type, ResourceLocation phase, InteractContext phaseContext) {
            super.register(type, phase, phaseContext);

            phaseContext.interactTypes.forEach(interactType -> {
                entityTypeInteracts.put(phaseContext.getTargetType(), interactType);
            });
        }

        public boolean isRestricted(Level level, BlockPos pos, Player player, EntityType<?> entityType, InteractType interactType) {
            return isRestricted(level,pos,player,cxt -> {
                return cxt.getTargetType() == entityType && cxt.interactTypes.contains(interactType);
            });
        }

        public boolean isRestricted(Level level, BlockPos pos, Player player, EntityType<?> entityType, InteractType interactType, Function<InteractContext,Boolean> action) {
            return isRestricted(level,pos,player,cxt -> {
                return cxt.getTargetType() == entityType && cxt.interactTypes.contains(interactType) && action.apply(cxt);
            });
        }

        @SubscribeEvent
        public void onAnimalTame(AnimalTameEvent event) {
            Animal animal = event.getAnimal();
            if (entityTypeInteracts.get(animal.getType()).contains(InteractType.TAME)) {
                if (isRestricted(animal.level(), animal.blockPosition(), event.getTamer(), animal.getType(), InteractType.TAME)){
                    event.setCanceled(true);
                }
            }
        }

        @SubscribeEvent
        public void onBreed(BabyEntitySpawnEvent event) {
            Player causedByPlayer = event.getCausedByPlayer() == null ? null : event.getCausedByPlayer();
            Mob parentA = event.getParentA();
            if (entityTypeInteracts.get(parentA.getType()).contains(InteractType.BREED)) {
                if (isRestricted(parentA.level(), parentA.blockPosition(), causedByPlayer, parentA.getType(), InteractType.BREED)){
                    event.setCanceled(true);
                }
            }

        }

        @SubscribeEvent
        public void onMount(EntityMountEvent event) {
            Entity entityBeingMounted = event.getEntityBeingMounted();
            Player player = event.getEntityMounting() instanceof Player ? (Player) event.getEntityMounting() : null;
            if (entityTypeInteracts.get(entityBeingMounted.getType()).contains(InteractType.RIDE)){
                if (isRestricted(entityBeingMounted.level(), entityBeingMounted.blockPosition(), player, entityBeingMounted.getType(), InteractType.RIDE)){
                    event.setCanceled(true);
                }
            }
        }

        @SubscribeEvent
        public void onAttackEntity(AttackEntityEvent event) {
            Player player = event.getEntity();
            Entity target = event.getTarget();
            if (entityTypeInteracts.get(target.getType()).contains(InteractType.ATTACK)){
                if (isRestricted(target.level(), target.blockPosition(), player, target.getType(), InteractType.ATTACK)){
                    event.setCanceled(true);
                }
            }
        }

        @SubscribeEvent
        public void onBlockBreak(BlockEvent.BreakEvent event) {
            Player player = event.getPlayer();
            if (isRestricted(player.level(), event.getPos(), player, player.getType(), InteractType.BLOCK_BREAK, ctx ->{
                if (ctx instanceof BlockInteractContext blockInteractContext){
                    return blockInteractContext.getBlock() == event.getState().getBlock();
                }
                return false;
            })) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
            Player player = event.getEntity() instanceof Player ? (Player) event.getEntity() : null;
            EntityType<?> entityType = event.getEntity() == null ? null : event.getEntity().getType();
            if (isRestricted((Level) event.getLevel(), event.getPos(), player, entityType, InteractType.BLOCK_PLACE, ctx ->{
                if (ctx instanceof BlockInteractContext blockInteractContext){
                    return blockInteractContext.getBlock() == event.getState().getBlock();
                }
                return false;
            })) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public void onPlayerInteractRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
            Player player = event.getEntity();
            if (isRestricted(player.level(), event.getPos(), player, player.getType(), InteractType.BLOCK_INTERACT, ctx ->{
                if (ctx instanceof BlockInteractContext blockInteractContext){
                    return blockInteractContext.getBlock() == event.getLevel().getBlockState(event.getPos()).getBlock();
                }
                return false;
            })) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public void onPlayerInteractEntity(PlayerInteractEvent.EntityInteract event) {
            Player player = event.getEntity();
            Entity target = event.getTarget();
            if (isRestricted(player.level(), player.blockPosition(), player, target.getType(), InteractType.ENTITY_INTERACT)) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public void onPlayerInteractEntityInteractSpecific(PlayerInteractEvent.EntityInteractSpecific event) {
            Player player = event.getEntity();
            Entity target = event.getTarget();
            if (isRestricted(player.level(), player.blockPosition(), player, target.getType(), InteractType.ENTITY_INTERACT)) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public void onPlayerInteractItem(PlayerInteractEvent.RightClickItem event) {
            Player player = event.getEntity();
            if (isRestricted(player.level(), player.blockPosition(), player, player.getType(), InteractType.USE_ITEM)) {
                event.setCanceled(true);
            }
        }


    }