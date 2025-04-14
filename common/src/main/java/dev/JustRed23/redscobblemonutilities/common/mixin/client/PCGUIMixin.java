package dev.JustRed23.redscobblemonutilities.common.mixin.client;

import com.cobblemon.mod.common.client.gui.pc.PCGUI;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.llamalad7.mixinextras.sugar.Local;
import dev.JustRed23.redscobblemonutilities.common.client.gui.IVsButton;
import dev.JustRed23.redscobblemonutilities.common.utils.StatComparer;
import dev.JustRed23.redscobblemonutilities.common.utils.StatRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static dev.JustRed23.redscobblemonutilities.common.RedsCobblemonUtilities.ICON_MAX_IV;
import static dev.JustRed23.redscobblemonutilities.common.RedsCobblemonUtilities.ICON_MIN_IV;

@Mixin(com.cobblemon.mod.common.client.gui.pc.PCGUI.class)
public class PCGUIMixin extends Screen {

    protected PCGUIMixin(Component component) {
        super(component);
    }

    private static final @Unique float ICON_SIZE = 16;
    private @Unique IVsButton redscobblemonutilities$ivsButton;

    @Inject(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/cobblemon/mod/common/client/gui/pc/PCGUI;setPreviewPokemon(Lcom/cobblemon/mod/common/pokemon/Pokemon;)V"
            )
    )
    private void redscobblemonutilities$initIVsButton(CallbackInfo ci) {
        int x = (this.width - PCGUI.BASE_WIDTH) / 2;
        int y = (this.height - PCGUI.BASE_HEIGHT) / 2;
        this.redscobblemonutilities$ivsButton = new IVsButton(x + 6, y + 117);
        this.addRenderableWidget(redscobblemonutilities$ivsButton);
    }

    @Inject(
            method = "render",
            at = @At("RETURN")
    )
    private void redscobblemonutilities$renderStats(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (redscobblemonutilities$ivsButton.isActive())
            StatRenderer.renderStats(context, ((PCGUI) (Object) this));
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/cobblemon/mod/common/pokemon/Pokemon;getShiny()Z"
            )
    )
    private void redscobblemonutilities$renderStatIconsOnPC(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci, @Local Pokemon pokemon) {
        final boolean maxIV = StatComparer.Companion.isMax(pokemon.getIvs());
        final boolean minIV = StatComparer.Companion.isMin(pokemon.getIvs());
        if (!maxIV && !minIV) return;

        int x = (this.width - PCGUI.BASE_WIDTH) / 2;
        int y = (this.height - PCGUI.BASE_HEIGHT) / 2;

        blitk(
                context.pose(), maxIV ? ICON_MAX_IV : ICON_MIN_IV,
                (x + 62.5 - (pokemon.getShiny() ? (ICON_SIZE / 2) + 1 : 1)) / PCGUI.SCALE, (y + 28.5) / PCGUI.SCALE, ICON_SIZE, ICON_SIZE, //pos
                0,0,ICON_SIZE,ICON_SIZE,0, //offsets
                1,1,1,1,true, //colors
                PCGUI.SCALE
        );
    }
}
