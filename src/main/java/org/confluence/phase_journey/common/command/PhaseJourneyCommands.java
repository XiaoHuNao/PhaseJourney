package org.confluence.phase_journey.common.command;

import java.util.Collection;

import org.confluence.phase_journey.PhaseJourney;
import org.confluence.phase_journey.common.attachment.PhaseAttachment;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

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
                                .then(Commands.argument("phase", StringArgumentType.word())
                                        .executes(PhaseJourneyCommands::addPlayerPhase))))
                .then(Commands.literal("remove")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("phase", StringArgumentType.word())
                                        .executes(PhaseJourneyCommands::removePlayerPhase))))
                .then(Commands.literal("list")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .executes(PhaseJourneyCommands::listPlayerPhases)))
                .then(Commands.literal("clear")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .executes(PhaseJourneyCommands::clearPlayerPhases)))
                .build();

        // 世界阶段管理
        var world = Commands.literal("world")
                .then(Commands.literal("add")
                        .then(Commands.argument("phase", StringArgumentType.word())
                                .executes(PhaseJourneyCommands::addWorldPhase)))
                .then(Commands.literal("remove")
                        .then(Commands.argument("phase", StringArgumentType.word())
                                .executes(PhaseJourneyCommands::removeWorldPhase)))
                .then(Commands.literal("list")
                        .executes(PhaseJourneyCommands::listWorldPhases))
                .then(Commands.literal("clear")
                        .executes(PhaseJourneyCommands::clearWorldPhases))
                .build();

        phaseJourney.addChild(player);
        phaseJourney.addChild(world);
    }

    // 玩家阶段管理方法
    private static int addPlayerPhase(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
        String phaseName = StringArgumentType.getString(context, "phase");
        ResourceLocation phase = ResourceLocation.parse(phaseName);

        boolean hasSuccess = false;
        for (ServerPlayer player : targets) {
            PhaseAttachment attachment = PhaseAttachment.of(player);
            if (attachment.addPhaseIfAbsent(phase)) {
                hasSuccess = true;
            }
        }

        if (!hasSuccess) {
            throw PHASE_ALREADY_EXISTS.create();
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

        boolean hasSuccess = false;
        for (ServerPlayer player : targets) {
            PhaseAttachment attachment = PhaseAttachment.of(player);
            if (attachment.removePhaseIfPresent(phase)) {
                hasSuccess = true;
            }
        }

        if (!hasSuccess) {
            throw PHASE_NOT_FOUND.create();
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
            attachment.getPhases().clear();
        }

        final int targetSize = targets.size();
        context.getSource().sendSuccess(() -> Component.translatable("commands.phase_journey.player.clear.success",
                targetSize), true);
        return targetSize;
    }

    // 世界阶段管理方法
    private static int addWorldPhase(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String phaseName = StringArgumentType.getString(context, "phase");
        ResourceLocation phase = ResourceLocation.parse(phaseName);

        Level level = context.getSource().getLevel();
        PhaseAttachment attachment = PhaseAttachment.of(level);

        if (!attachment.addPhaseIfAbsent(phase)) {
            throw PHASE_ALREADY_EXISTS.create();
        }

        context.getSource().sendSuccess(() -> Component.translatable("commands.phase_journey.world.add.success", phase), true);
        return 1;
    }

    private static int removeWorldPhase(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String phaseName = StringArgumentType.getString(context, "phase");
        ResourceLocation phase = ResourceLocation.parse(phaseName);

        Level level = context.getSource().getLevel();
        PhaseAttachment attachment = PhaseAttachment.of(level);

        if (!attachment.removePhaseIfPresent(phase)) {
            throw PHASE_NOT_FOUND.create();
        }

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
        attachment.getPhases().clear();

        context.getSource().sendSuccess(() -> Component.translatable("commands.phase_journey.world.clear.success"), true);
        return 1;
    }

}
