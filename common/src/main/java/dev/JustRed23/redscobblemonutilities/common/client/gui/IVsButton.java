package dev.JustRed23.redscobblemonutilities.common.client.gui;

import com.cobblemon.mod.common.client.gui.CobblemonRenderable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.function.Supplier;

public class IVsButton extends AbstractButton implements CobblemonRenderable {

    private static final int BOX_SIZE = 6;
    private static final int WIDTH = BOX_SIZE + 1, HEIGHT = BOX_SIZE + 1;

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
        //TODO
        guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), this.active ? 0xFF00FF00 : 0xFFFF0000);
    }
}
