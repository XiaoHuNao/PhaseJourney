package com.xiaohunao.phase_journey.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.xiaohunao.phase_journey.common.attachment.PhaseAttachment;
import com.xiaohunao.phase_journey.common.phase.PhaseType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.Collection;

public class PhaseJourneyCommands {
    private static final SimpleCommandExceptionType PHASE_ALREADY_EXISTS = new SimpleCommandExceptionType(
            Component.translatable("commands.phase_journey.phase_already_exists")
    );
    private static final SimpleCommandExceptionType PHASE_NOT_FOUND = new SimpleCommandExceptionType(
            Component.translatable("commands.phase_journey.phase_not_found")
    );

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection commandSelection) {
        var phaseJourney = Commands.literal("phase_journey")
                .requires(sourceStack -> sourceStack.hasPermission(2))
                .build();

        dispatcher.getRoot().addChild(phaseJourney);

        // 玩家阶段管理
        var player = Commands.literal("player")
                .then(Commands.literal("add")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("phase", StringArgumentType.greedyString())
                                        .executes(PhaseJourneyCommands::addPlayerPhase))))
                .then(Commands.literal("remove")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("phase", StringArgumentType.greedyString())
                                        .executes(PhaseJourneyCommands::removePlayerPhase))))
                .then(Commands.literal("list")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .executes(PhaseJourneyCommands::listPlayerPhases)))
                .then(Commands.literal("clear")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .executes(PhaseJourneyCommands::clearPlayerPhases)))
                .build();

        // 世界阶段管理
        var level = Commands.literal("level")
                .then(Commands.literal("add")
                        .then(Commands.argument("phase", StringArgumentType.greedyString())
                                .executes(PhaseJourneyCommands::addLevelPhase)))
                .then(Commands.literal("remove")
                        .then(Commands.argument("phase", StringArgumentType.greedyString())
                                .executes(PhaseJourneyCommands::removeLevelPhase)))
                .then(Commands.literal("list")
                        .executes(PhaseJourneyCommands::listWorldPhases))
                .then(Commands.literal("clear")
                        .executes(PhaseJourneyCommands::clearWorldPhases))
                .build();

        phaseJourney.addChild(player);
        phaseJourney.addChild(level);
    }

    // 玩家阶段管理方法
    private static int addPlayerPhase(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
        String phaseName = StringArgumentType.getString(context, "phase");
        ResourceLocation phase = ResourceLocation.parse(phaseName);

        for (ServerPlayer player : targets) {
            PhaseType.PLAYER.applyOrRevokePhase(player, phase, true);
        }

        final ResourceLocation finalPhase = phase;
        final int targetSize = targets.size();
        context.getSource().sendSuccess(() -> Component.translatable("commands.phase_journey.player.add.success",
                finalPhase, targetSize), true);
        return targetSize;
    }

    private static int removePlayerPhase(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
        String phaseName = StringArgumentType.getString(context, "phase");
        ResourceLocation phase = ResourceLocation.parse(phaseName);

        for (ServerPlayer player : targets) {
            PhaseType.PLAYER.applyOrRevokePhase(player, phase, false);
        }

        final ResourceLocation finalPhase = phase;
        final int targetSize = targets.size();
        context.getSource().sendSuccess(() -> Component.translatable("commands.phase_journey.player.remove.success",
                finalPhase, targetSize), true);
        return targetSize;
    }

    private static int listPlayerPhases(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");

        for (ServerPlayer player : targets) {
            PhaseAttachment attachment = PhaseAttachment.of(player);
            var phases = attachment.getPhases();

            if (phases.isEmpty()) {
                context.getSource().sendSuccess(() -> Component.translatable("commands.phase_journey.player.list.empty",
                        player.getDisplayName()), false);
            } else {
                context.getSource().sendSuccess(() -> Component.translatable("commands.phase_journey.player.list.header",
                        player.getDisplayName(), phases.size()), false);
                for (ResourceLocation phase : phases) {
                    context.getSource().sendSuccess(() -> Component.literal("  - " + phase), false);
                }
            }
        }

        return targets.size();
    }

    private static int clearPlayerPhases(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");

        for (ServerPlayer player : targets) {
            PhaseAttachment attachment = PhaseAttachment.of(player);
            for (ResourceLocation phase : attachment.getPhases()) {
                PhaseType.PLAYER.applyOrRevokePhase(player,phase,false);
            }

        }

        final int targetSize = targets.size();
        context.getSource().sendSuccess(() -> Component.translatable("commands.phase_journey.player.clear.success",
                targetSize), true);
        return targetSize;
    }

    // 世界阶段管理方法
    private static int addLevelPhase(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String phaseName = StringArgumentType.getString(context, "phase");
        ResourceLocation phase = ResourceLocation.parse(phaseName);

        Level level = context.getSource().getLevel();
        PhaseType.LEVEL.applyOrRevokePhase(level, phase, true);

        return 1;
    }

    private static int removeLevelPhase(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String phaseName = StringArgumentType.getString(context, "phase");
        ResourceLocation phase = ResourceLocation.parse(phaseName);

        Level level = context.getSource().getLevel();
        PhaseType.LEVEL.applyOrRevokePhase(level, phase, false);

        context.getSource().sendSuccess(() -> Component.translatable("commands.phase_journey.world.remove.success", phase), true);
        return 1;
    }

    private static int listWorldPhases(CommandContext<CommandSourceStack> context) {
        Level level = context.getSource().getLevel();
        PhaseAttachment attachment = PhaseAttachment.of(level);
        var phases = attachment.getPhases();

        if (phases.isEmpty()) {
            context.getSource().sendSuccess(() -> Component.translatable("commands.phase_journey.world.list.empty"), false);
        } else {
            context.getSource().sendSuccess(() -> Component.translatable("commands.phase_journey.world.list.header",
                    phases.size()), false);
            for (ResourceLocation phase : phases) {
                context.getSource().sendSuccess(() -> Component.literal("  - " + phase), false);
            }
        }

        return phases.size();
    }

    private static int clearWorldPhases(CommandContext<CommandSourceStack> context) {
        Level level = context.getSource().getLevel();
        PhaseAttachment attachment = PhaseAttachment.of(level);
        for (ResourceLocation phase : attachment.getPhases()) {
            PhaseType.LEVEL.applyOrRevokePhase(level,phase,false);
        }

        context.getSource().sendSuccess(() -> Component.translatable("commands.phase_journey.world.clear.success"), true);
        return 1;
    }

}
