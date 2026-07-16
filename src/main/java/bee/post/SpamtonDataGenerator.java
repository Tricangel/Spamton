package bee.post;

import bee.post.datagen.MailLangGen;
import bee.post.datagen.MailLootGen;
import bee.post.datagen.MailModelGen;
import bee.post.datagen.MailTagGen;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class SpamtonDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(MailModelGen::new);
		pack.addProvider(MailLangGen::new);
		pack.addProvider(MailTagGen::new);
		pack.addProvider(MailLootGen::new);


	}
}
