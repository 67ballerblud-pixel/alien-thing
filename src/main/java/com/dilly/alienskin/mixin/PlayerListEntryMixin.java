package com.dilly.alienskin.mixin;

import com.dilly.alienskin.AlienSkin;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.SkinTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Every place that asks a player for its skin (world model, first-person hands, tab list) goes through here. */
@Mixin(PlayerListEntry.class)
public abstract class PlayerListEntryMixin {
    @Inject(method = "getSkinTextures", at = @At("RETURN"), cancellable = true)
    private void alienskin$swap(CallbackInfoReturnable<SkinTextures> cir) {
        SkinTextures alien = AlienSkin.swap((PlayerListEntry) (Object) this, cir.getReturnValue());
        if (alien != null) cir.setReturnValue(alien);
    }
}
