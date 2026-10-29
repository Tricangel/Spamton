package bee.post.client.overlay;

import bee.post.block.ParcelShelfBlock;
import bee.post.block.entity.PackageBlockEntity;
import bee.post.block.entity.ParcelShelfBlockEntity;
import bee.post.registry.MailItemComponents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class PeekPostalInfoOverlay implements HudElement  {

    @Override
    public void render(@NonNull GuiGraphics context, @NonNull DeltaTracker tickCounter) {


        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.screen != null) return;
        if (!(minecraft.hitResult instanceof BlockHitResult hitResult)) return;
        if (minecraft.level == null) return;

        if (minecraft.level.getBlockEntity(hitResult.getBlockPos()) instanceof PackageBlockEntity packageBlockEntity) {
            int x = context.guiWidth() / 2;
            int y = context.guiHeight() / 2;

            x += 15;
            y -= 15;

            List<ClientTooltipComponent> clientTooltipComponents = new ArrayList<>();

            ItemStack stack = packageBlockEntity.getBlockState().getBlock().asItem().getDefaultInstance();
            stack.set(MailItemComponents.POSTAL_INFO, packageBlockEntity.info);

            Component selectedItemName = stack.getStyledHoverName();
            int textWidth = minecraft.font.width(selectedItemName.getVisualOrderText());
            int centerTooltip = x + textWidth / 2 - 12;
            clientTooltipComponents.add(ClientTooltipComponent.create(selectedItemName.getVisualOrderText()));
            stack.getTooltipLines(Item.TooltipContext.of(minecraft.level), minecraft.player, TooltipFlag.NORMAL).forEach(component1 -> {
                if (!component1.equals(selectedItemName)) {
                    clientTooltipComponents.add(ClientTooltipComponent.create(component1.getVisualOrderText()));
                }
            });


            context.renderTooltip(
                    minecraft.font,
                    clientTooltipComponents,
                    centerTooltip - textWidth / 2,
                    y - 15,
                    DefaultTooltipPositioner.INSTANCE,
                    stack.get(DataComponents.TOOLTIP_STYLE)
            );
            return;

        }

        if (!(minecraft.level.getBlockEntity(hitResult.getBlockPos()) instanceof ParcelShelfBlockEntity blockEntity)) return;
        if (!(minecraft.level.getBlockState(hitResult.getBlockPos()).getBlock() instanceof ParcelShelfBlock block)) return;

        renderParcelShelfOverlay(context, hitResult, blockEntity, block);



    }

    public void renderParcelShelfOverlay(GuiGraphics context, BlockHitResult hitResult, ParcelShelfBlockEntity blockEntity, ParcelShelfBlock block) {
        Minecraft minecraft = Minecraft.getInstance();

        OptionalInt optionalInt = block.getHitSlot(hitResult, minecraft.level.getBlockState(hitResult.getBlockPos()).getValue(BlockStateProperties.HORIZONTAL_FACING));

        if (optionalInt.isEmpty()) return;

        ItemStack stack = blockEntity.getItem(optionalInt.getAsInt());
        if (stack.isEmpty()) return;

        int x = context.guiWidth() / 2;
        int y = context.guiHeight() / 2;

        x += 15;
        y -= 30;



        Optional<TooltipComponent> component = stack.getTooltipImage();

        if (component.isEmpty()) return;

        List<ClientTooltipComponent> clientTooltipComponents = new ArrayList<>();

        Component selectedItemName = stack.getStyledHoverName();
        int textWidth = minecraft.font.width(selectedItemName.getVisualOrderText());
        int centerTooltip = x + textWidth / 2 - 12;
        clientTooltipComponents.add(ClientTooltipComponent.create(selectedItemName.getVisualOrderText()));
        clientTooltipComponents.add(ClientTooltipComponent.create(component.get()));
        stack.getTooltipLines(Item.TooltipContext.of(minecraft.level), minecraft.player, TooltipFlag.NORMAL).forEach(component1 -> {
            if (!component1.equals(selectedItemName)) {
                clientTooltipComponents.add(ClientTooltipComponent.create(component1.getVisualOrderText()));
            }
        });


        context.renderTooltip(
                minecraft.font,
                clientTooltipComponents,
                centerTooltip - textWidth / 2,
                y - 15,
                DefaultTooltipPositioner.INSTANCE,
                stack.get(DataComponents.TOOLTIP_STYLE)
        );

    }

}
