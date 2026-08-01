package online.remind.remind.datagen;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.datagen.builder.ShotlockBuilder;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.lib.StringsRM;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ShotlockDataProviderRM implements DataProvider {

	private final PackOutput.PathProvider pathProvider;

	public ShotlockDataProviderRM(PackOutput output) {
		this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "shotlocks");
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		Map<String, JsonObject> shotlocks = new LinkedHashMap<>();

		shotlocks.put(StringsRM.darkDivide, new ShotlockBuilder().cooldown(2).cooldownMax(1).maxLocks(28).damageMultiplier(0.55F).damageMultiplierMax(0.66F).maxExp(8600).maxLevel(5).element("darkness").minigame("mash").build());
		shotlocks.put(StringsRM.heartlessAngel, new ShotlockBuilder().cooldown(110).cooldownMax(80).maxLocks(1).damageMultiplier(1.0F).damageMultiplierMax(1.0F).maxExp(12000).maxLevel(6).element("darkness").minigame("none").build());

		CompletableFuture<?>[] futures = shotlocks.entrySet().stream().map(entry -> {
			Path path = pathProvider.json(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, entry.getKey()));
			return DataProvider.saveStable(cache, entry.getValue(), path);
		}).toArray(CompletableFuture[]::new);
		return CompletableFuture.allOf(futures);
	}

	@Override
	public String getName() {
		return "Kingdom Keys Re:Mind Shotlock Data";
	}
}
