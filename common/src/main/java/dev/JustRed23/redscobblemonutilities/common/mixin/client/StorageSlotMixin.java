package dev.JustRed23.redscobblemonutilities.common.mixin.client;

import com.cobblemon.mod.common.client.gui.pc.PCGUI;
import com.cobblemon.mod.common.client.gui.pc.StorageSlot;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.JustRed23.redscobblemonutilities.common.utils.StatComparer;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static dev.JustRed23.redscobblemonutilities.common.RedsCobblemonUtilities.*;

@Mixin(com.cobblemon.mod.common.client.gui.pc.StorageSlot.class)
public class StorageSlotMixin {

    private static final @Unique float ICON_SIZE = 8;

    @Inject(
            method = "renderSlot",
            at = @At(value = "INVOKE", target = "Lcom/cobblemon/mod/common/pokemon/Pokemon;heldItemNoCopy$common()Lnet/minecraft/world/item/ItemStack;")
    )
    private void redscobblemonutilities$renderStatIconsOnSlot(GuiGraphics context, int posX, int posY, float partialTicks, CallbackInfo ci, @Local Pokemon pokemon, @Local PoseStack matrices) {
        final boolean shiny = pokemon.getShiny();
        final boolean maxIV = StatComparer.Companion.isMax(pokemon.getIvs());
        final boolean minIV = StatComparer.Companion.isMin(pokemon.getIvs());
        if (!shiny && !maxIV && !minIV) return;

        float yPos = (posY - 1 + (StorageSlot.SIZE - (ICON_SIZE / 2))) / PCGUI.SCALE;

        if (shiny)
            blitk(
                    matrices, ICON_SHINY,
                    (posX + 1) / PCGUI.SCALE, yPos, ICON_SIZE, ICON_SIZE, //pos
                    0,0,ICON_SIZE,ICON_SIZE,0, //offsets
                    1,1,1,1,true, //colors
                    PCGUI.SCALE
            );

        if (!maxIV && !minIV) return;

        blitk(
                matrices, maxIV ? ICON_MAX_IV : ICON_MIN_IV,
                (posX + 1) / PCGUI.SCALE, shiny ? (yPos - 1) - ICON_SIZE : yPos, ICON_SIZE, ICON_SIZE, //pos
                0,0,ICON_SIZE,ICON_SIZE,0, //offsets
                1,1,1,1,true, //colors
                PCGUI.SCALE
        );
    }
}
