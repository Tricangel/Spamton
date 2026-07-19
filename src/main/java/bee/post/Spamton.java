package bee.post;

import bee.post.client.model.ParcelModel;
import bee.post.client.model.PlayerStampModel;
import bee.post.client.networking.ServerboundSignLetterPacket;
import bee.post.item.StampPredicate;
import bee.post.item.StampRecipe;
import bee.post.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.predicates.DataComponentPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.network.Filterable;
import net.minecraft.server.network.FilteredText;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.item.crafting.CustomRecipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Spamton implements ModInitializer {
	public static final String MOD_ID = "spamton";
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		MailBlocks.init();
		MailBlockEntities.init();
		MailItemComponents.init();
		MailItems.init();
		MailTags.init();

		PayloadTypeRegistry.playC2S().register(ServerboundSignLetterPacket.TYPE, ServerboundSignLetterPacket.STREAM_CODEC);

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
		EntityModelLayerRegistry.registerModelLayer(PlayerStampModel.STAMP, PlayerStampModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(PlayerStampModel.STAMP_ITEM, PlayerStampModel::createItemLayer);
		EntityModelLayerRegistry.registerModelLayer(ParcelModel.PARCEL, ParcelModel::createBodyLayer);

		Registry.register(BuiltInRegistries.RECIPE_TYPE, id("stamp_duplicating"), StampRecipe.StampRecipeType.INSTANCE);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id("stamp_duplicating"), new CustomRecipe.Serializer<StampRecipe>(StampRecipe::new));

	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
