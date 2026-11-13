package com.dilly.alienskin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.SkinTextures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

public final class AlienSkin {
    private AlienSkin() {}

    public enum Mode { OFF, SELF, ALL }

    static final Logger LOG = LoggerFactory.getLogger("alienskin");
    private static volatile Mode mode = Mode.SELF;
    private static boolean warned;
    private static final Map<SkinTextures, SkinTextures> CACHE = Collections.synchronizedMap(new WeakHashMap<>());

    public static Mode mode() { return mode; }

    public static void set(Mode m) {
        mode = m;
        try { Files.writeString(file(), m.name()); } catch (Exception ignored) {}
    }

    static void load() {
        try { mode = Mode.valueOf(Files.readString(file()).trim().toUpperCase(Locale.ROOT)); } catch (Exception ignored) {}
    }

    private static Path file() { return FabricLoader.getInstance().getConfigDir().resolve("alienskin.txt"); }

    /** Returns the alien version of this player's skin, or null to leave it alone. */
    public static SkinTextures swap(PlayerListEntry entry, SkinTextures orig) {
        if (orig == null || mode == Mode.OFF) return null;
        if (mode == Mode.SELF) {
            MinecraftClient mc = MinecraftClient.getInstance();
            ClientPlayNetworkHandler nh = mc.getNetworkHandler();
            if (nh == null || mc.getSession() == null) return null;
            if (nh.getPlayerListEntry(mc.getSession().getUsername()) != entry) return null;
        }
        SkinTextures cached = CACHE.get(orig);
        if (cached != null) return cached;
        try {
            SkinTextures alien = (SkinTextures) SkinSwap.rebuild(orig);
            CACHE.put(orig, alien);
            return alien;
        } catch (Throwable t) {
            if (!warned) { warned = true; LOG.error("Could not build the alien skin - leaving skins untouched", t); }
            return null;
        }
    }
}
