package bee.post;

import bee.post.block.PackageBlock;
import bee.post.client.networking.ServerboundSaveLetterPacket;
import bee.post.client.networking.ServerboundSignLetterPacket;
import bee.post.entity.PackageEntity;
import bee.post.item.PackageBlockItem;
import bee.post.item.stamp.StampPredicate;
import bee.post.item.stamp.StampRecipe;
import bee.post.networking.ClientboundOpenWritableLetterPacket;
import bee.post.networking.ClientboundOpenWrittenLetterPacket;
import bee.post.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.predicates.DataComponentPredicate;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.network.Filterable;
import net.minecraft.server.network.FilteredText;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WritableBookContent;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Spamton implements ModInitializer {
	public static final String MOD_ID = "spamton";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		MailBlocks.init();
		MailBlockEntities.init();
		MailItemComponents.init();
		MailItems.init();
		MailTags.init();
		MailLootFunctions.init();
		MailLootTables.insertIntoTables();
		MailItemGroups.init();
		MailEntityTypes.init();

		DispenserBlock.registerBehavior(MailBlocks.PACKAGE, (blockSource, itemStack) -> {
			Level level = blockSource.level();
			BlockPos pos = blockSource.pos();
			Direction direction = blockSource.state().getValue(BlockStateProperties.FACING);

			if (!(itemStack.getItem() instanceof PackageBlockItem packageBlockItem)) return itemStack;

			if (level.getBlockState(pos.relative(direction)).isAir()) {
				PackageEntity entity = new PackageEntity(level, itemStack);
				entity.setPos(pos.getCenter().relative(direction, 1));
				level.addFreshEntity(entity);
			}

            return itemStack;
        });


		PayloadTypeRegistry.playC2S().register(ServerboundSignLetterPacket.TYPE, ServerboundSignLetterPacket.STREAM_CODEC);
		PayloadTypeRegistry.playC2S().register(ServerboundSaveLetterPacket.TYPE, ServerboundSaveLetterPacket.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(ClientboundOpenWritableLetterPacket.TYPE, ClientboundOpenWritableLetterPacket.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(ClientboundOpenWrittenLetterPacket.TYPE, ClientboundOpenWrittenLetterPacket.STREAM_CODEC);

		ServerPlayNetworking.registerGlobalReceiver(ServerboundSaveLetterPacket.TYPE, (packet, context) -> {

			ItemStack stack = context.player().getInventory().getItem(packet.slot());

			stack.set(DataComponents.WRITABLE_BOOK_CONTENT, new WritableBookContent(packet.pages().stream().map(Filterable::passThrough).toList()));

			context.player().getInventory().setItem(packet.slot(), stack);
		});

		ServerPlayNetworking.registerGlobalReceiver(ServerboundSignLetterPacket.TYPE, ((packet, context) -> {

			List<MutableComponent> mutableText = packet.pages().stream().map(Component::literal).toList();
			List<Filterable<Component>> text = new ArrayList<>();
			mutableText.forEach(mutableComponent -> text.add(new Filterable<>(mutableComponent, Optional.empty())));

			WrittenBookContent content = new WrittenBookContent(
					Filterable.from(FilteredText.fullyFiltered(packet.title())),
					packet.author(),
					0,
					text,
					true
			);

			context.player().getInventory().getItem(packet.slot()).set(DataComponents.WRITTEN_BOOK_CONTENT, content);

		}));




		Registry.register(BuiltInRegistries.DATA_COMPONENT_PREDICATE_TYPE, id("stamp"), new DataComponentPredicate.ConcreteType<StampPredicate>(StampPredicate.CODEC));

		Registry.register(BuiltInRegistries.RECIPE_TYPE, id("stamp_duplicating"), StampRecipe.StampRecipeType.INSTANCE);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id("stamp_duplicating"), new CustomRecipe.Serializer<StampRecipe>(StampRecipe::new));

	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
