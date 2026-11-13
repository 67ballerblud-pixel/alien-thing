package com.dilly.alienskin;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;

public class AlienSkinClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AlienSkin.load();
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, access) ->
            dispatcher.register(ClientCommandManager.literal("alienskin")
                .executes(c -> {
                    c.getSource().sendFeedback(Text.literal("[AlienSkin] mode: " + AlienSkin.mode() + "  (use /alienskin self|all|off)"));
                    return 1;
                })
                .then(ClientCommandManager.literal("self").executes(c -> setMode(c.getSource()::sendFeedback, AlienSkin.Mode.SELF)))
                .then(ClientCommandManager.literal("all").executes(c -> setMode(c.getSource()::sendFeedback, AlienSkin.Mode.ALL)))
                .then(ClientCommandManager.literal("off").executes(c -> setMode(c.getSource()::sendFeedback, AlienSkin.Mode.OFF)))));
    }

    private static int setMode(java.util.function.Consumer<Text> say, AlienSkin.Mode m) {
        AlienSkin.set(m);
        say.accept(Text.literal(switch (m) {
            case SELF -> "[AlienSkin] Only YOU are an alien now.";
            case ALL -> "[AlienSkin] Everyone is an alien (client-side only).";
            case OFF -> "[AlienSkin] Alien skin off.";
        }));
        return 1;
    }
}
