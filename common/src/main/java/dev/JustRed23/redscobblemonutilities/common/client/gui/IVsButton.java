package dev.JustRed23.redscobblemonutilities.common.client.gui;

import com.cobblemon.mod.common.api.gui.GuiUtilsKt;
import com.cobblemon.mod.common.client.gui.CobblemonRenderable;
import dev.JustRed23.redscobblemonutilities.common.RedsCobblemonUtilities;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class IVsButton extends AbstractButton implements CobblemonRenderable {

    private static final ResourceLocation TEXTURE = RedsCobblemonUtilities.asResource("textures/gui/pc/iv_button.png");
    private static final ResourceLocation TEXTURE_ACTIVE = RedsCobblemonUtilities.asResource("textures/gui/pc/iv_button_active.png");

    private static final int WIDTH = 20, HEIGHT = 9;

    private final Button.CreateNarration narration = Supplier::get;
    private boolean active;

    public IVsButton(int x, int y) {
        super(x, y, WIDTH, HEIGHT, Component.literal("IV"));
    }

    public void onPress() {
        active = !active;
    }

    public boolean isActive() {
        return active;
    }

    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        this.narration.createNarrationMessage(() -> wrapDefaultNarrationMessage(this.getMessage()));
    }

    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        GuiUtilsKt.blitk(
                guiGraphics.pose(),
                active ? TEXTURE_ACTIVE : TEXTURE,
                getX(),
                getY(),
                getHeight(),
                getWidth(),
                0, 0,
                getWidth(), getHeight(),
                0,
                1,1,1, 1,
                true,
                1
        );
    }
}
