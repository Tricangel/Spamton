package bee.post.item;

import bee.post.Spamton;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

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
    public void extractImage(Font font, int x, int y, int w, int h, GuiGraphicsExtractor guiGraphics) {

        y -= 5;

        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_HIGHLIGHT_BACK_SPRITE, x, y, 24, 24);


        if (contents.letter().isPresent()) {
            guiGraphics.item(contents.letter().get(), x + 4, y + 4);
        } else guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_LETTER_SPRITE, x + 4, y + 4, 16, 16);

        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_HIGHLIGHT_BACK_SPRITE, x + 24, y, 24, 24);


        if (contents.gift().isPresent()) {
            guiGraphics.item(contents.gift().get(), x + 28, y + 4);
        } else guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_GIFT_SPRITE, x + 28, y + 4, 16, 16);

    }

    @Override
    public boolean showTooltipWithItemInHand() {
        return true;
    }

}
