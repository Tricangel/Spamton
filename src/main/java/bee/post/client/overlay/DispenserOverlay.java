package bee.post.client.overlay;

import bee.post.registry.MailItemComponents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.DispenserScreen;
import net.minecraft.util.ARGB;
import net.minecraft.world.inventory.Slot;

public class DispenserOverlay implements HudElement {
    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen instanceof DispenserScreen screen) {
            boolean hasPackage = false;

            for (Slot slot : screen.getMenu().slots) {
                if (slot.hasItem() && slot.getItem().getOrDefault(MailItemComponents.TAPED, false)) hasPackage = true;
            }

            if (hasPackage) {
                guiGraphics.fill(300, 300, 400, 400, ARGB.white(150));
            }
        }
    }
}
