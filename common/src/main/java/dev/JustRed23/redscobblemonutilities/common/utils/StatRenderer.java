package dev.JustRed23.redscobblemonutilities.common.utils;

import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.client.CobblemonResources;
import com.cobblemon.mod.common.client.gui.pc.PCGUI;
import com.cobblemon.mod.common.client.render.RenderHelperKt;
import com.cobblemon.mod.common.pokemon.IVs;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Map;
import java.util.Objects;

import static com.cobblemon.mod.common.util.LocalizationUtilsKt.lang;

public final class StatRenderer {

    public static void renderStats(GuiGraphics context, PCGUI gui) {
        PoseStack matrices = context.pose();

        matrices.pushPose();
        matrices.translate(0, 0, 1000);
        int x = (gui.width - PCGUI.BASE_WIDTH) / 2;
        int y = (gui.height - PCGUI.BASE_HEIGHT) / 2;

        // Main box, draws over the existing texture
        context.fill(x + 6, y + 128, x + 72, y + 197, 0xFF676767);

        renderIVs(context, x, y, gui.getPreviewPokemon$common());

        matrices.popPose();
    }

    private static final int IV_MAX = 31;
    private static final int IV_BAR_WIDTH = 41;
    private static final int IV_BAR_HEIGHT = 4;

    private static final Map<Stat, MutableComponent> IV_NAMES = Map.of(
            Stats.HP, lang("ui.stats.hp"),
            Stats.ATTACK, lang("ui.stats.atk"),
            Stats.DEFENCE, lang("ui.stats.def"),
            Stats.SPECIAL_ATTACK, lang("ui.stats.sp_atk"),
            Stats.SPECIAL_DEFENCE, lang("ui.stats.sp_def"),
            Stats.SPEED, lang("ui.stats.speed")
    );

    private static void renderIVs(GuiGraphics context, int x, int y, Pokemon pokemon) {
        if (pokemon == null) return;

        final IVs ivs = pokemon.getIvs();
        int index = 0;

        for (Map.Entry<Stat, MutableComponent> entry : IV_NAMES.entrySet()) {
            Stat stat = entry.getKey();
            MutableComponent name = entry.getValue();
            int iv = Objects.requireNonNull(ivs.get(stat));

            int scaledWidth = (int) ((iv / (float) IV_MAX) * IV_BAR_WIDTH);

            int borderColor = 0xFF4B4B4B;
            int ivColor = iv == IV_MAX ? 0xFF6a8ec3 : 0xFFB05CCC;
            int ivGradient = iv == IV_MAX ? 0xFF8ab9ff : 0xFFE080FF;

            int barX = x + 19;
            int barY = y + 131 + (index * 12);

            context.fill(barX - 1, barY, barX + IV_BAR_WIDTH + 1, barY + IV_BAR_HEIGHT + 1, borderColor);
            context.fillGradient(barX, barY + 1, barX + scaledWidth, barY + IV_BAR_HEIGHT, ivGradient, ivColor);

            RenderHelperKt.drawScaledTextJustifiedRight(context, CobblemonResources.INSTANCE.getDEFAULT_LARGE(),
                    name, barX - 2, barY, 0.5f, 1,
                    100, 0xFFFFFFFF, false);

            RenderHelperKt.drawScaledText(context, CobblemonResources.INSTANCE.getDEFAULT_LARGE(),
                    Component.literal(iv + "/" + IV_MAX).withStyle(iv == IV_MAX ? ChatFormatting.WHITE : ChatFormatting.GRAY).withStyle(ChatFormatting.BOLD),
                    barX + IV_BAR_WIDTH + 2, barY, .5f, 1,
                    100, 0xFFFFFFFF,
                    false, false, null, null);

            index++;
        }
    }

}
