package bee.post.client.screen;

import bee.post.client.networking.ServerboundSignLetterPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class LetterSignScreen extends Screen {
    private static final Component EDIT_TITLE_LABEL = Component.translatable("letter.editTitle");
    private static final Component FINALIZE_WARNING_LABEL = Component.translatable("letter.finalizeWarning");
    private static final Component TITLE = Component.translatable("letter.sign.title");
    private static final Component TITLE_EDIT_BOX = Component.translatable("book.sign.titlebox");
    private final LetterEditScreen letterEditScreen;
    private final Player owner;
    private final List<String> pages;
    private final InteractionHand hand;
    private EditBox titleBox;
    private String titleValue = "";

    public LetterSignScreen(LetterEditScreen letterEditScreen, Player player, InteractionHand interactionHand, List<String> list) {
        super(TITLE);
        this.letterEditScreen = letterEditScreen;
        this.owner = player;
        this.hand = interactionHand;
        this.pages = list;
    }

    @Override
    protected void init() {
        Button button = Button.builder(Component.translatable("book.finalizeButton"), (buttonx) -> {
            this.saveChanges();
            this.minecraft.setScreen((Screen)null);
        }).bounds(this.width / 2 - 100, 196, 98, 20).build();
        button.active = false;
        this.titleBox = this.addRenderableWidget(new EditBox(this.minecraft.font, (this.width - 114) / 2 - 3, 50, 114, 20, TITLE_EDIT_BOX));
        this.titleBox.setMaxLength(15);
        this.titleBox.setBordered(false);
        this.titleBox.setCentered(true);
        this.titleBox.setTextColor(-16777216);
        this.titleBox.setTextShadow(false);
        this.titleBox.setResponder((string) -> button.active = !StringUtil.isBlank(string));
        this.titleBox.setValue(this.titleValue);
        this.addRenderableWidget(button);
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, (buttonx) -> {
            this.titleValue = this.titleBox.getValue();
            this.minecraft.setScreen(this.letterEditScreen);
        }).bounds(this.width / 2 + 2, 196, 98, 20).build());
    }

    @Override
    protected void setInitialFocus() {
        this.setInitialFocus(this.titleBox);
    }

    private void saveChanges() {
        int i = this.hand == InteractionHand.MAIN_HAND ? this.owner.getInventory().getSelectedSlot() : 40;
        ServerboundSignLetterPacket signLetterPacket = new ServerboundSignLetterPacket(i, pages, titleBox.getValue().trim(), this.owner.getPlainTextName());

        ClientPlayNetworking.send(signLetterPacket);

    }

    @Override
    public boolean isInGameUi() {
        return true;
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        if (this.titleBox.isFocused() && !this.titleBox.getValue().isEmpty() && keyEvent.isConfirmation()) {
            this.saveChanges();
            this.minecraft.setScreen((Screen)null);
            return true;
        } else {
            return super.keyPressed(keyEvent);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);
        int xo = (this.width - 192) / 2;
        int yo = 2;
        int tw = this.font.width(EDIT_TITLE_LABEL);
        guiGraphics.drawString(this.font, EDIT_TITLE_LABEL, xo + 36 + (114 - tw) / 2, 34, -16777216, false);
        guiGraphics.drawWordWrap(this.font, FINALIZE_WARNING_LABEL, xo + 36, 82, 114, -16777216, false);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int i, int j, float f) {
        super.renderBackground(guiGraphics, i, j, f);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, LetterViewScreen.BOOK_LOCATION, (this.width - 192) / 2, 2, 0.0F, 0.0F, 192, 192, 256, 256);
    }
}
