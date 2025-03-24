package dev.JustRed23.redscobblemonutilities.common.mixin;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(com.cobblemon.mod.common.api.storage.party.PlayerPartyStore.class)
public class PlayerPartyStoreMixin {

    @ModifyArgs(
            method = "add",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/cobblemon/mod/common/util/LocalizationUtilsKt;lang(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;"
            )
    )
    private void redscobblemonutilities$typeColorsInChat(Args args, @Local(argsOnly = true) Pokemon pokemon) {
        Object[] array = args.get(1);
        if (!(array[0] instanceof MutableComponent component))
            return;

        MutableComponent newComponent = component.copy().withStyle(ChatFormatting.BOLD);
        if (pokemon.getShiny())
            newComponent = newComponent.withStyle(ChatFormatting.GOLD);
        else if (pokemon.isLegendary())
            newComponent = newComponent.withStyle(ChatFormatting.DARK_PURPLE);
        else if (pokemon.isMythical())
            newComponent = newComponent.withStyle(ChatFormatting.AQUA);
        else return;

        array[0] = newComponent;
        args.set(1, array);
    }
}
