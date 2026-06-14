package online.remind.remind.datagen;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.datagen.builder.MagicBuilder;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.lib.StringsRM;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MagicDataProviderRM implements DataProvider {

	private final PackOutput.PathProvider pathProvider;

	public MagicDataProviderRM(PackOutput output) {
		this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "magics");
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		Map<String, JsonObject> magics = new LinkedHashMap<>();

		//TODO rest of magics
		magics.put(ResourceLocation.parse(StringsRM.Magic_Haste).getPath(), new MagicBuilder().cost(8).castTime(10).cooldown(40).damageMultiplier(2F, 6F).lockOn(false).maxExp(1800).maxExpLevel(3).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Hastera).getPath(), new MagicBuilder().cost(8).castTime(10).cooldown(40).damageMultiplier(2F, 6F).lockOn(false).maxExp(1800).maxExpLevel(3).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Hastega).getPath(), new MagicBuilder().cost(8).castTime(10).cooldown(40).damageMultiplier(2F, 6F).lockOn(false).maxExp(1800).maxExpLevel(3).build());

		CompletableFuture<?>[] futures = magics.entrySet().stream().map(entry -> {
			Path path = pathProvider.json(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, entry.getKey()));
			return DataProvider.saveStable(cache, entry.getValue(), path);
		}).toArray(CompletableFuture[]::new);
		return CompletableFuture.allOf(futures);
	}

	@Override
	public String getName() {
		return "Kingdom Keys Re:Mind Magic Data";
	}
}