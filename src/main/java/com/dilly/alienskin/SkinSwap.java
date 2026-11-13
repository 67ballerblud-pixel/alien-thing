package com.dilly.alienskin;

import net.minecraft.util.Identifier;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;

/**
 * Copies a SkinTextures record but points its body texture at our alien PNG.
 * Done reflectively on purpose: the record's shape (and the type of its first component) changed between
 * Minecraft versions, and this way the same code works for "Identifier texture" and for "TextureAsset body".
 */
final class SkinSwap {
    private SkinSwap() {}

    static Object rebuild(Object orig) throws Exception {
        Class<?> c = orig.getClass();
        RecordComponent[] rc = c.getRecordComponents();
        if (rc == null || rc.length == 0) throw new IllegalStateException(c + " is not a record");
        Object[] vals = new Object[rc.length];
        Class<?>[] types = new Class<?>[rc.length];
        boolean slim = false;
        for (int i = 0; i < rc.length; i++) {
            Method acc = rc[i].getAccessor();
            acc.setAccessible(true);
            vals[i] = acc.invoke(orig);
            types[i] = rc[i].getType();
            if (vals[i] instanceof Enum<?> e && e.name().equalsIgnoreCase("SLIM")) slim = true;
        }
        String name = slim ? "alien_slim" : "alien_classic";
        Identifier shortId = Identifier.of("alienskin", "entity/" + name);
        Identifier fullPath = Identifier.of("alienskin", "textures/entity/" + name + ".png");

        if (types[0] == Identifier.class) {
            vals[0] = fullPath;                                  // older shape: Identifier texture
        } else {
            Class<?> bc = vals[0] != null ? vals[0].getClass() : types[0];
            Object body;
            try {
                Constructor<?> k = bc.getDeclaredConstructor(Identifier.class);          // TextureAssetInfo(Identifier id)
                k.setAccessible(true);
                body = k.newInstance(shortId);
            } catch (NoSuchMethodException e) {
                Constructor<?> k = bc.getDeclaredConstructor(Identifier.class, Identifier.class); // (id, texturePath)
                k.setAccessible(true);
                body = k.newInstance(shortId, fullPath);
            }
            vals[0] = body;
        }
        Constructor<?> canonical = c.getDeclaredConstructor(types);
        canonical.setAccessible(true);
        return canonical.newInstance(vals);
    }
}
