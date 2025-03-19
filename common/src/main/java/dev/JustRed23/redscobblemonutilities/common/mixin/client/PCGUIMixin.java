package dev.JustRed23.redscobblemonutilities.common.mixin.client;

import com.cobblemon.mod.common.client.gui.pc.PCGUI;
import dev.JustRed23.redscobblemonutilities.common.client.gui.IVsButton;
import dev.JustRed23.redscobblemonutilities.common.utils.StatRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(com.cobblemon.mod.common.client.gui.pc.PCGUI.class)
public class PCGUIMixin extends Screen {

    protected PCGUIMixin(Component component) {
        super(component);
    }

    @Unique private IVsButton redscobblemonutilities$ivsButton;

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
        this.redscobblemonutilities$ivsButton = new IVsButton(x + 6, y + 118);
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
}
