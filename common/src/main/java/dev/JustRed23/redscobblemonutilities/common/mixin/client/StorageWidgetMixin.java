package dev.JustRed23.redscobblemonutilities.common.mixin.client;

import com.cobblemon.mod.common.CobblemonNetwork;
import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.api.storage.StorePosition;
import com.cobblemon.mod.common.api.storage.party.PartyPosition;
import com.cobblemon.mod.common.api.storage.pc.PCPosition;
import com.cobblemon.mod.common.client.gui.pc.BoxStorageSlot;
import com.cobblemon.mod.common.client.gui.pc.PartyStorageSlot;
import com.cobblemon.mod.common.client.gui.pc.StorageSlot;
import com.cobblemon.mod.common.client.gui.pc.StorageWidget;
import com.cobblemon.mod.common.client.storage.ClientParty;
import com.cobblemon.mod.common.net.messages.server.storage.party.ReleasePartyPokemonPacket;
import com.cobblemon.mod.common.net.messages.server.storage.pc.ReleasePCPokemonPacket;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.llamalad7.mixinextras.sugar.Local;
import dev.JustRed23.redscobblemonutilities.common.client.gui.QuickReleaseButton;
import dev.JustRed23.redscobblemonutilities.common.utils.Stats;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.sounds.SoundEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(com.cobblemon.mod.common.client.gui.pc.StorageWidget.class)
public abstract class StorageWidgetMixin {

    private final StorageWidget redscobblemonutilities$instance = (StorageWidget) (Object) this;

    @Unique private QuickReleaseButton redscobblemonutilities$quickReleaseButton;
    @Shadow @Final private ClientParty party;
    @Shadow protected abstract void playSound(SoundEvent sound);

    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/cobblemon/mod/common/client/gui/pc/StorageWidget;setupStorageSlots()V",
                    shift = At.Shift.AFTER
            )
    )
    private void redscobblemonutilities$initQuickReleaseButton(CallbackInfo ci) {
        redscobblemonutilities$quickReleaseButton = new QuickReleaseButton(redscobblemonutilities$instance.getX() + 194, redscobblemonutilities$instance.getY() + 128);
    }

    @Inject(
            method = "renderWidget",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/cobblemon/mod/common/client/gui/pc/ReleaseConfirmButton;render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
                    ordinal = 1,
                    shift = At.Shift.AFTER
            )
    )
    private void redscobblemonutilities$renderQuickReleaseButton(GuiGraphics guiGraphics, int posX, int posY, float partialTicks, CallbackInfo ci) {
        if (redscobblemonutilities$instance.getSelectedPosition() == null)
            redscobblemonutilities$quickReleaseButton.render(guiGraphics, posX, posY, partialTicks);
    }

    @Inject(
            method = "mouseClicked",
            at = @At("HEAD")
    )
    private void redscobblemonutilities$onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (redscobblemonutilities$instance.getSelectedPosition() == null)
            redscobblemonutilities$quickReleaseButton.mouseClicked(mouseX, mouseY, button);
    }

    @Inject(
            method = "onStorageSlotClicked",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/Screen;hasShiftDown()Z"
            ),
            cancellable = true
    )
    private void redscobblemonutilities$quickRelease(CallbackInfo ci, @Local StorePosition position, @Local Pokemon pokemon) {
        if (!redscobblemonutilities$quickReleaseButton.isActive())
            return;

        if (position instanceof PartyPosition && party.getSlots().stream().filter(Objects::nonNull).count() <= 1) return;
        if (position == null || pokemon == null) return;

        var packetToSend = position instanceof PartyPosition
                ? new ReleasePartyPokemonPacket(pokemon.getUuid(), (PartyPosition) position)
                : position instanceof PCPosition
                ? new ReleasePCPokemonPacket(pokemon.getUuid(), (PCPosition) position)
                : null;

        if (packetToSend != null) {
            CobblemonNetwork.INSTANCE.sendToServer(packetToSend);
            playSound(CobblemonSounds.PC_RELEASE);
            ci.cancel();
        }
    }

    @Inject(
            method = "renderWidget",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/cobblemon/mod/common/client/gui/pc/BoxStorageSlot;isHovered(II)Z"
            )
    )
    private void redscobblemonutilities$renderBoxSlotIVs(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci, @Local BoxStorageSlot slot, @Local Pokemon pokemon) {
        this.renderIVs(context, mouseX, mouseY, slot, pokemon);
    }

    @Inject(
            method = "renderWidget",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/cobblemon/mod/common/client/gui/pc/PartyStorageSlot;isHovered(II)Z"
            )
    )
    private void redscobblemonutilities$renderPartySlotIVs(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci, @Local PartyStorageSlot slot, @Local Pokemon pokemon) {
        this.renderIVs(context, mouseX, mouseY, slot, pokemon);
    }

    private void renderIVs(GuiGraphics context, int mouseX, int mouseY, StorageSlot slot, Pokemon pokemon) {
        if (pokemon != null  && slot.isHovered(mouseX, mouseY)) {
            context.fill(mouseX, mouseY, mouseX + 134, mouseY + 148, 0x7F000000);
            Stats.Companion.drawIVs(context.pose(), pokemon, mouseX, mouseY, 134, 148);
        }
    }
}
