package bee.post.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.*;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WritableBookContent;
import net.minecraft.world.item.component.WrittenBookContent;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class LetterViewScreen extends Screen {
    private static final Component TITLE = Component.translatable("book.view.title");
    private static final Style PAGE_TEXT_STYLE = Style.EMPTY.withoutShadow().withColor(-16777216);
    public static final LetterAccess EMPTY_ACCESS = new LetterAccess(List.of());
    public static final Identifier BOOK_LOCATION = Identifier.withDefaultNamespace("textures/gui/book.png");
    private LetterAccess letterAccess;
    private int currentPage;
    private List<FormattedCharSequence> cachedPageComponents;
    private int cachedPage;
    private Component pageMsg;
    private PageButton forwardButton;
    private PageButton backButton;

    public LetterViewScreen() {
        this(EMPTY_ACCESS);
    }

    public LetterViewScreen(LetterAccess letterAccess) {
        super(TITLE);
        this.cachedPageComponents = Collections.emptyList();
        this.cachedPage = -1;
        this.pageMsg = CommonComponents.EMPTY;
        this.letterAccess = letterAccess;

    }

    @Override
    protected void init() {
        this.createMenuControls();
        this.createPageControlButtons();
    }

    @Override
    public @NonNull Component getNarrationMessage() {
        return CommonComponents.joinLines(super.getNarrationMessage(), this.getPageNumberMessage(), this.letterAccess.getPage(this.currentPage));
    }

    private Component getPageNumberMessage() {
        return Component.translatable("book.pageIndicator", this.currentPage + 1, Math.max(this.getNumPages(), 1)).withStyle(PAGE_TEXT_STYLE);
    }

    protected void createMenuControls() {
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> this.onClose()).pos((this.width - 200) / 2, this.menuControlsTop()).width(200).build());
    }

    protected void createPageControlButtons() {
        int i = this.backgroundLeft();
        int j = this.backgroundTop();
        this.forwardButton = this.addRenderableWidget(new PageButton(i + 116, j + 157, true, (button) -> this.pageForward(), true));
        this.backButton = this.addRenderableWidget(new PageButton(i + 43, j + 157, false, (button) -> this.pageBack(), true));
        this.updateButtonVisibility();
    }

    private int getNumPages() {
        return this.letterAccess.getPageCount();
    }

    protected void pageBack() {
        if (this.currentPage > 0) {
            --this.currentPage;
        }

        this.updateButtonVisibility();
    }

    protected void pageForward() {
        if (this.currentPage < this.getNumPages() - 1) {
            ++this.currentPage;
        }

        this.updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        this.forwardButton.visible = this.currentPage < this.getNumPages() - 1;
        this.backButton.visible = this.currentPage > 0;
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        if (super.keyPressed(keyEvent)) {
            return true;
        } else {
            boolean var10000 = switch (keyEvent.key()) {
                case 266 -> {
                    this.backButton.onPress(keyEvent);
                    yield true;
                }
                case 267 -> {
                    this.forwardButton.onPress(keyEvent);
                    yield true;
                }
                default -> false;
            };

            return var10000;
        }
    }

    @Override
    public void render(@NonNull GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);
        this.visitText(guiGraphics.textRenderer(GuiGraphics.HoveredTextEffects.TOOLTIP_AND_CURSOR), false);
    }

    private void visitText(ActiveTextCollector activeTextCollector, boolean bl) {
        if (this.cachedPage != this.currentPage) {
            FormattedText formattedText = ComponentUtils.mergeStyles(this.letterAccess.getPage(this.currentPage), PAGE_TEXT_STYLE);
            this.cachedPageComponents = this.font.split(formattedText, 114);
            this.pageMsg = this.getPageNumberMessage();
            this.cachedPage = this.currentPage;
        }

        int i = this.backgroundLeft();
        int j = this.backgroundTop();
        if (!bl) {
            activeTextCollector.accept(TextAlignment.RIGHT, i + 148, j + 16, this.pageMsg);
        }

        Objects.requireNonNull(this.font);
        int k = Math.min(128 / 9, this.cachedPageComponents.size());

        for(int l = 0; l < k; ++l) {
            FormattedCharSequence formattedCharSequence = this.cachedPageComponents.get(l);
            int var10001 = i + 36;
            int var10002 = j + 30;
            Objects.requireNonNull(this.font);
            activeTextCollector.accept(var10001, var10002 + l * 9, formattedCharSequence);
        }

    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int i, int j, float f) {
        super.renderBackground(guiGraphics, i, j, f);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BOOK_LOCATION, this.backgroundLeft(), this.backgroundTop(), 0.0F, 0.0F, 192, 192, 256, 256);
    }

    private int backgroundLeft() {
        return (this.width - 192) / 2;
    }

    private int backgroundTop() {
        return 2;
    }

    protected int menuControlsTop() {
        return this.backgroundTop() + 192 + 2;
    }

    @Environment(EnvType.CLIENT)
    public record LetterAccess(List<Component> pages) {
        public int getPageCount() {
            return this.pages.size();
        }

        public Component getPage(int i) {
            return i >= 0 && i < this.getPageCount() ? this.pages.get(i) : CommonComponents.EMPTY;
        }

        public static @Nullable LetterAccess fromItem(ItemStack itemStack) {
            boolean bl = Minecraft.getInstance().isTextFilteringEnabled();
            WrittenBookContent writtenBookContent = itemStack.get(DataComponents.WRITTEN_BOOK_CONTENT);
            if (writtenBookContent != null) {
                return new LetterAccess(writtenBookContent.getPages(bl));
            } else if ( itemStack.get(DataComponents.WRITABLE_BOOK_CONTENT) != null){
                WritableBookContent writableBookContent = itemStack.get(DataComponents.WRITABLE_BOOK_CONTENT);
                List<Component> pages = new ArrayList<>(writableBookContent.getPages(bl).map(Component::literal).toList());
                return new LetterAccess(pages);
            } else return null;
        }
    }
}
