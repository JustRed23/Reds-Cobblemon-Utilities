package dev.JustRed23.redscobblemonutilities.common;

import net.minecraft.resources.ResourceLocation;

public final class RedsCobblemonUtilities {
    public static final String MOD_ID = "redscobblemonutilities";

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
