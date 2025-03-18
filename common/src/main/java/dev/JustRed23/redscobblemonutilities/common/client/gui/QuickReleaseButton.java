package dev.JustRed23.redscobblemonutilities.common.client.gui;

import com.cobblemon.mod.common.client.CobblemonResources;
import com.cobblemon.mod.common.client.gui.CobblemonRenderable;
import com.cobblemon.mod.common.client.render.RenderHelperKt;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.function.Supplier;

public class QuickReleaseButton extends AbstractButton implements CobblemonRenderable {

    private static final int BOX_SIZE = 8;
    private static final int WIDTH = 60, HEIGHT = BOX_SIZE + 1;

    private final Button.CreateNarration narration = Supplier::get;
    private boolean active;

    public QuickReleaseButton(int x, int y) {
        super(x, y, WIDTH, HEIGHT, Component.translatable("redscobblemonutilities.gui.quickrelease"));
    }

    public void onPress() {
        active = !active;
    }

    public boolean isActive() {
        return active;
    }

    protected boolean clicked(double mouseX, double mouseY) {
        return this.getX() <= mouseX && mouseX <= this.getX() + BOX_SIZE && this.getY() <= mouseY && mouseY <= this.getY() + this.getHeight();
    }

    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        this.narration.createNarrationMessage(() -> wrapDefaultNarrationMessage(this.getMessage()));
    }

    protected void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
        int color = clicked(x, y) ? 0xFFAAAAAA : 0xFF8D8D8D;

        guiGraphics.hLine(this.getX(), this.getX() + BOX_SIZE, this.getY(), color); // Top line
        guiGraphics.hLine(this.getX(), this.getX() + BOX_SIZE, this.getY() + BOX_SIZE, color); // Bottom line
        guiGraphics.vLine(this.getX(), this.getY(), this.getY() + BOX_SIZE, color); // Left line
        guiGraphics.vLine(this.getX() + BOX_SIZE, this.getY(), this.getY() + BOX_SIZE, color); // Right line

        if (active) {
            guiGraphics.hLine(this.getX() + 1, this.getX() + BOX_SIZE - 1, this.getY() + 1, 0xFF009CAE); // Top line
            guiGraphics.vLine(this.getX() + 1, this.getY() + 1, this.getY() + BOX_SIZE, 0xFF009CAE); // Left line

            guiGraphics.fill(this.getX() + 2, this.getY() + 2, this.getX() + BOX_SIZE - 1, this.getY() + BOX_SIZE - 1, 0xFF6AC3C0); // Main box

            guiGraphics.hLine(this.getX() + 1, this.getX() + BOX_SIZE - 1, this.getY() + BOX_SIZE - 1, 0xFF005761); // Bottom line
            guiGraphics.vLine(this.getX() + BOX_SIZE - 1, this.getY() + 1, this.getY() + BOX_SIZE - 1, 0xFF005761); // Right line
        }

        RenderHelperKt.drawScaledText(
                guiGraphics,
                CobblemonResources.INSTANCE.getDEFAULT_LARGE(),
                getMessage().copy().withStyle(ChatFormatting.BOLD),
                getX() + BOX_SIZE + 3, getY(),
                1, 1,
                getWidth() - BOX_SIZE - 3,
                0xFF8D8D8D,
                false, false,
                x, y
        );
    }
}
