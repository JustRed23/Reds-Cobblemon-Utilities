package dev.JustRed23.redscobblemonutilities.common.mixin.client;

import com.cobblemon.mod.common.client.gui.pc.PCGUI;
import com.cobblemon.mod.common.client.gui.pc.StorageSlot;
import com.cobblemon.mod.common.client.gui.summary.Summary;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;

@Mixin(com.cobblemon.mod.common.client.gui.pc.StorageSlot.class)
public class StorageSlotMixin {

    /**
     * Draw the shiny star on the Pokémon's slot
     */
    @Inject(
            method = "renderSlot",
            at = @At(value = "INVOKE", target = "Lcom/cobblemon/mod/common/pokemon/Pokemon;heldItemNoCopy$common()Lnet/minecraft/world/item/ItemStack;")
    )
    private void redscobblemonutilities$renderShinyOnSlot(GuiGraphics context, int posX, int posY, float partialTicks, CallbackInfo ci, @Local Pokemon pokemon, @Local PoseStack matrices) {
        if (!pokemon.getShiny())
            return;

        float wh = 8;

        blitk(
                matrices, Summary.Companion.getIconShinyResource(),
                (posX + 1) / PCGUI.SCALE, (posY - 1 + (StorageSlot.SIZE - (wh / 2))) / PCGUI.SCALE, wh, wh, //pos
                0,0,wh,wh,0, //offsets
                1,1,1,1,true, //colors
                PCGUI.SCALE
        );
    }
}
