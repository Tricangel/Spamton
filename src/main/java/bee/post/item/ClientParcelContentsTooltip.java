package bee.post.item;

import bee.post.Spamton;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ClientParcelContentsTooltip implements ClientTooltipComponent {
    private static final Identifier SLOT_HIGHLIGHT_BACK_SPRITE = Identifier.withDefaultNamespace("container/bundle/slot_background");
    private static final Identifier SLOT_GIFT_SPRITE = Spamton.id("slot/gift");
    private static final Identifier SLOT_LETTER_SPRITE = Spamton.id("slot/letter");
    private final ParcelContents contents;

    public ClientParcelContentsTooltip(ParcelContents contents) {
        this.contents = contents;
    }


    @Override
    public int getHeight(Font font) {
        return 18;
    }

    @Override
    public int getWidth(Font font) {
        return 18 * 2;
    }

    @Override
    public void renderImage(Font font, int x, int y, int w, int h, GuiGraphics guiGraphics) {
        ParcelContents actualContents = this.loadContents(contents);

        y -= 5;

        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_HIGHLIGHT_BACK_SPRITE, x, y, 24, 24);


        if (!actualContents.letter().isEmpty()) {
            guiGraphics.renderItem(actualContents.letter(), x + 4, y + 4);
        } else guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_LETTER_SPRITE, x + 4, y + 4, 16, 16);

        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_HIGHLIGHT_BACK_SPRITE, x + 24, y, 24, 24);


        if (!actualContents.gift().isEmpty()) {
            guiGraphics.renderItem(actualContents.gift(), x + 28, y + 4);
        } else guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_GIFT_SPRITE, x + 28, y + 4, 16, 16);

    }

    @Override
    public boolean showTooltipWithItemInHand() {
        return true;
    }

    public ParcelContents loadContents(ParcelContents contents) {
        if (contents.gift().is(Items.BEDROCK)) contents = new ParcelContents(contents.letter(), ItemStack.EMPTY, contents.ownerName(), contents.targetName());
        if (contents.letter().is(Items.BEDROCK)) contents = new ParcelContents(ItemStack.EMPTY, ItemStack.EMPTY, contents.ownerName(), contents.targetName());
        return contents;
    }
}
