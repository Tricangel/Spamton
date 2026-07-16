package bee.post;

import bee.post.client.model.PlayerStampModel;
import bee.post.item.StampPredicate;
import bee.post.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.component.predicates.DataComponentPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

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



		Registry.register(BuiltInRegistries.DATA_COMPONENT_PREDICATE_TYPE, id("stamp"), new DataComponentPredicate.ConcreteType<StampPredicate>(StampPredicate.CODEC));
		EntityModelLayerRegistry.registerModelLayer(PlayerStampModel.STAMP, PlayerStampModel::createBodyLayer);

	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
