package org.confluence.phase_journey.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;

public class PhaseJourneyCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection commandSelection) {
        LiteralCommandNode<CommandSourceStack> phaseJourney = Commands.literal("phase_journey")
                .requires(sourceStack -> sourceStack.hasPermission(4)).build();

        dispatcher.getRoot().addChild(phaseJourney);

        LiteralCommandNode<CommandSourceStack> player = Commands.literal("player").build();
        LiteralCommandNode<CommandSourceStack> world = Commands.literal("world").build();
        phaseJourney.addChild(player);
        phaseJourney.addChild(world);

        Commands.literal("add")
                .then(Commands.argument("phase", StringArgumentType.word()))
                .executes(sourceStack -> {
                    addPhase(sourceStack.getSource(), StringArgumentType.getString(sourceStack, "phase"));
                    return 1;
                }).build();


        LiteralCommandNode<CommandSourceStack> build = Commands.literal("list")
                .then(Commands.argument("targets", EntityArgument.entities())).build();
        player.addChild(build);
        world.addChild(build);
    }

    private static void addPhase(CommandSourceStack sourceStack, String phase) {

    }

}
