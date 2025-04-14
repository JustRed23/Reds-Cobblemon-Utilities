package dev.JustRed23.redscobblemonutilities.common;

import com.cobblemon.mod.common.client.gui.summary.Summary;
import net.minecraft.resources.ResourceLocation;

public final class RedsCobblemonUtilities {
    public static final String MOD_ID = "redscobblemonutilities";

    public static final ResourceLocation ICON_SHINY = Summary.Companion.getIconShinyResource();
    public static final ResourceLocation ICON_MAX_IV = asResource("textures/gui/summary/icon_max_ivs.png");
    public static final ResourceLocation ICON_MIN_IV = asResource("textures/gui/summary/icon_min_ivs.png");

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
