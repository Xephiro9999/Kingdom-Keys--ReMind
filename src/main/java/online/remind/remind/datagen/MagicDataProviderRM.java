package online.remind.remind.datagen;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.datagen.builder.MagicBuilder;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.MagicData;
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
		magics.put(ResourceLocation.parse(StringsRM.Magic_Haste).getPath(), new MagicBuilder().cost(8).castTime(10).cooldown(40).damageMultiplier(2F, 6F).lockOn(false).maxExp(1800).maxExpLevel(3).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Hastera).getPath(), new MagicBuilder().cost(16).castTime(10).cooldown(40).damageMultiplier(2F, 6F).lockOn(false).maxExp(1800).maxExpLevel(3).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Hastega).getPath(), new MagicBuilder().cost(24).castTime(10).cooldown(40).damageMultiplier(2F, 6F).lockOn(false).maxExp(1800).maxExpLevel(4).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Slow).getPath(), new MagicBuilder().cost(8).castTime(10).cooldown(40).damageMultiplier(2F, 6F).lockOn(false).maxExp(1800).maxExpLevel(3).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Slowra).getPath(), new MagicBuilder().cost(16).castTime(10).cooldown(40).damageMultiplier(2F, 6F).lockOn(false).maxExp(1800).maxExpLevel(3).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Slowga).getPath(), new MagicBuilder().cost(24).castTime(10).cooldown(40).damageMultiplier(2F, 6F).lockOn(false).maxExp(1800).maxExpLevel(4).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Holy).getPath(), new MagicBuilder().cost(8).castTime(10).cooldown(40).damageMultiplier(2F, 2.5F).lockOn(false).maxExp(1800).maxExpLevel(3).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Holyra).getPath(), new MagicBuilder().cost(12).castTime(10).cooldown(40).damageMultiplier(2.5F, 3F).lockOn(false).maxExp(1800).maxExpLevel(3).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Holyga).getPath(), new MagicBuilder().cost(16).castTime(10).cooldown(40).damageMultiplier(3F, 4F).lockOn(false).maxExp(2400).maxExpLevel(4).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Ruin).getPath(), new MagicBuilder().cost(8).castTime(10).cooldown(40).damageMultiplier(2F, 2.5F).lockOn(false).maxExp(1800).maxExpLevel(3).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Ruinra).getPath(), new MagicBuilder().cost(12).castTime(10).cooldown(40).damageMultiplier(2.5F, 3F).lockOn(false).maxExp(1800).maxExpLevel(3).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Ruinga).getPath(), new MagicBuilder().cost(16).castTime(10).cooldown(40).damageMultiplier(3F, 4F).lockOn(false).maxExp(2400).maxExpLevel(4).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Esuna).getPath(), new MagicBuilder().cost(16).castTime(10).cooldown(40).damageMultiplier(2F, 6F).lockOn(false).maxExp(1800).maxExpLevel(3).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Group_Esuna).getPath(), new MagicBuilder().cost(32).castTime(10).cooldown(40).damageMultiplier(2F, 6F).lockOn(false).maxExp(2400).maxExpLevel(4).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Dispel).getPath(), new MagicBuilder().cost(16).castTime(10).cooldown(40).damageMultiplier(2F, 6F).lockOn(false).maxExp(2400).maxExpLevel(4).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Berserk).getPath(), new MagicBuilder().cost(8).castTime(10).cooldown(40).damageMultiplier(2F, 1F).lockOn(false).maxExp(1800).maxExpLevel(3).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Berserkra).getPath(), new MagicBuilder().cost(16).castTime(20).cooldown(40).damageMultiplier(1F, 1F).lockOn(false).maxExp(2400).maxExpLevel(3).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Berserkga).getPath(), new MagicBuilder().cost(24).castTime(40).cooldown(40).damageMultiplier(1F, 1F).lockOn(false).maxExp(3200).maxExpLevel(4).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Drain).getPath(), new MagicBuilder().cost(8).castTime(10).cooldown(40).damageMultiplier(1F, 2F).lockOn(false).maxExp(1800).maxExpLevel(3).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Drainra).getPath(), new MagicBuilder().cost(10).castTime(30).cooldown(40).damageMultiplier(2F, 3F).lockOn(false).maxExp(2400).maxExpLevel(3).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Drainga).getPath(), new MagicBuilder().cost(10).castTime(30).cooldown(40).damageMultiplier(3F, 4F).lockOn(false).maxExp(3200).maxExpLevel(4).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Osmose).getPath(), new MagicBuilder().cost(1).castTime(10).cooldown(40).damageMultiplier(1F, 2F).lockOn(false).maxExp(1800).maxExpLevel(3).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Osmosera).getPath(), new MagicBuilder().cost(1).castTime(15).cooldown(40).damageMultiplier(2F, 3F).lockOn(false).maxExp(2400).maxExpLevel(3).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Osmosega).getPath(), new MagicBuilder().cost(1).castTime(20).cooldown(40).damageMultiplier(3F, 4F).lockOn(false).maxExp(3200).maxExpLevel(4).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Regen).getPath(), new MagicBuilder().cost(20).castTime(10).cooldown(40).damageMultiplier(1F, 2F).lockOn(false).maxExp(1800).maxExpLevel(3).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Regenra).getPath(), new MagicBuilder().cost(30).castTime(15).cooldown(40).damageMultiplier(2F, 3F).lockOn(false).maxExp(2400).maxExpLevel(3).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Regenga).getPath(), new MagicBuilder().cost(60).castTime(20).cooldown(40).damageMultiplier(3F, 4F).lockOn(false).maxExp(3200).maxExpLevel(4).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Auto_Life).getPath(), new MagicBuilder().cost(50).castTime(60).cooldown(60).damageMultiplier(1.5F, 3F).lockOn(false).maxExp(4200).maxExpLevel(5).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Silence).getPath(), new MagicBuilder().cost(20).castTime(10).cooldown(40).damageMultiplier(1F, 2F).lockOn(false).maxExp(1800).maxExpLevel(3).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Silencera).getPath(), new MagicBuilder().cost(30).castTime(15).cooldown(40).damageMultiplier(2F, 3F).lockOn(false).maxExp(2400).maxExpLevel(3).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Silencega).getPath(), new MagicBuilder().cost(60).castTime(20).cooldown(40).damageMultiplier(3F, 4F).lockOn(false).maxExp(3200).maxExpLevel(4).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Confuse).getPath(), new MagicBuilder().cost(20).castTime(15).cooldown(40).damageMultiplier(1F, 2F).lockOn(false).maxExp(1800).maxExpLevel(3).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Confusera).getPath(), new MagicBuilder().cost(25).castTime(20).cooldown(40).damageMultiplier(2F, 3F).lockOn(false).maxExp(2400).maxExpLevel(3).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Confusega).getPath(), new MagicBuilder().cost(30).castTime(25).cooldown(40).damageMultiplier(3F, 4F).lockOn(false).maxExp(3200).maxExpLevel(4).spellType(MagicData.SpellType.MAGIC).build());

		magics.put(ResourceLocation.parse(StringsRM.Magic_Steal).getPath(), new MagicBuilder().cost(10).castTime(20).cooldown(40).damageMultiplier(1.5F, 3F).lockOn(false).maxExp(1800).maxExpLevel(3).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Comet).getPath(), new MagicBuilder().cost(25).castTime(20).cooldown(40).damageMultiplier(1.5F, 3F).lockOn(false).maxExp(1800).maxExpLevel(3).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Meteor).getPath(), new MagicBuilder().cost(50).castTime(30).cooldown(60).damageMultiplier(2.5F, 6F).lockOn(false).maxExp(3200).maxExpLevel(4).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Faith).getPath(), new MagicBuilder().cost(50).castTime(30).cooldown(60).damageMultiplier(2.5F, 6F).lockOn(false).maxExp(3200).maxExpLevel(4).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Death).getPath(), new MagicBuilder().cost(50).castTime(40).cooldown(60).damageMultiplier(2.5F, 6F).lockOn(true).maxExp(3200).maxExpLevel(4).spellType(MagicData.SpellType.MAGIC).build());
		magics.put(ResourceLocation.parse(StringsRM.Magic_Ultima).getPath(), new MagicBuilder().cost(300).castTime(40).cooldown(300).damageMultiplier(3F, 6F).lockOn(false).maxExp(9999).maxExpLevel(5).spellType(MagicData.SpellType.MAGIC).build());

		// Attacks
		magics.put(ResourceLocation.parse(StringsRM.Attack_Quick_Blitz).getPath(), new MagicBuilder().cost(5).castTime(5).cooldown(20).damageMultiplier(2F, 2.2F).lockOn(false).maxExp(200).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Sliding_Dash).getPath(), new MagicBuilder().cost(5).castTime(5).cooldown(20).damageMultiplier(1F, 1.2F).lockOn(false).maxExp(200).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Blitz).getPath(), new MagicBuilder().cost(10).castTime(5).cooldown(60).damageMultiplier(2F, 4f).lockOn(false).maxExp(700).maxExpLevel(4).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Slot_Edge).getPath(), new MagicBuilder().cost(10).castTime(5).cooldown(60).damageMultiplier(2F, 2.3f).lockOn(false).maxExp(530).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Zantetsuken).getPath(), new MagicBuilder().cost(40).castTime(5).cooldown(60).damageMultiplier(4.2F, 4.6f).lockOn(false).maxExp(5400).maxExpLevel(5).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Swift_Strike).getPath(), new MagicBuilder().cost(55).castTime(5).cooldown(60).damageMultiplier(4.2F, 5.2f).lockOn(false).maxExp(9999).maxExpLevel(5).spellType(MagicData.SpellType.PHYSICAL).build());

		magics.put(ResourceLocation.parse(StringsRM.Attack_Fire_Strike).getPath(), new MagicBuilder().cost(5).castTime(5).cooldown(20).damageMultiplier(2.5F, 2.7F).lockOn(false).maxExp(420).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Blizzard_Strike).getPath(), new MagicBuilder().cost(5).castTime(5).cooldown(20).damageMultiplier(2.5F, 2.7F).lockOn(false).maxExp(420).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Thunder_Strike).getPath(), new MagicBuilder().cost(5).castTime(5).cooldown(20).damageMultiplier(2.5F, 2.7F).lockOn(false).maxExp(420).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Aero_Strike).getPath(), new MagicBuilder().cost(5).castTime(5).cooldown(20).damageMultiplier(2.5F, 2.7F).lockOn(false).maxExp(420).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Water_Strike).getPath(), new MagicBuilder().cost(5).castTime(5).cooldown(20).damageMultiplier(2.5F, 2.7F).lockOn(false).maxExp(420).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Light_Strike).getPath(), new MagicBuilder().cost(5).castTime(5).cooldown(20).damageMultiplier(2.5F, 2.7F).lockOn(false).maxExp(420).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Dark_Strike).getPath(), new MagicBuilder().cost(5).castTime(5).cooldown(20).damageMultiplier(2.5F, 2.7F).lockOn(false).maxExp(420).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Confusion_Strike).getPath(), new MagicBuilder().cost(5).castTime(5).cooldown(20).damageMultiplier(2.2F, 2.4F).lockOn(false).maxExp(420).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Binding_Strike).getPath(), new MagicBuilder().cost(5).castTime(5).cooldown(20).damageMultiplier(2.0f, 2.2F).lockOn(false).maxExp(420).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Fire_Surge).getPath(), new MagicBuilder().cost(10).castTime(5).cooldown(20).damageMultiplier(1.5F, 1.6F).lockOn(false).maxExp(500).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Fira_Surge).getPath(), new MagicBuilder().cost(10).castTime(5).cooldown(20).damageMultiplier(1.6F, 1.7F).lockOn(false).maxExp(600).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Firaga_Surge).getPath(), new MagicBuilder().cost(10).castTime(5).cooldown(20).damageMultiplier(1.7F, 1.8F).lockOn(false).maxExp(700).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Thunder_Surge).getPath(), new MagicBuilder().cost(10).castTime(5).cooldown(20).damageMultiplier(1.6F, 1.75F).lockOn(false).maxExp(500).maxExpLevel(4).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Thundara_Surge).getPath(), new MagicBuilder().cost(10).castTime(5).cooldown(20).damageMultiplier(1.75F, 1.9F).lockOn(false).maxExp(600).maxExpLevel(4).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Thundaga_Surge).getPath(), new MagicBuilder().cost(10).castTime(5).cooldown(20).damageMultiplier(1.9F, 2.05F).lockOn(false).maxExp(700).maxExpLevel(4).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Blizzard_Surge).getPath(), new MagicBuilder().cost(10).castTime(5).cooldown(20).damageMultiplier(1.5F, 1.6F).lockOn(false).maxExp(500).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Blizzara_Surge).getPath(), new MagicBuilder().cost(10).castTime(5).cooldown(20).damageMultiplier(1.6F, 1.7F).lockOn(false).maxExp(600).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Blizzaga_Surge).getPath(), new MagicBuilder().cost(10).castTime(5).cooldown(20).damageMultiplier(1.7F, 1.8F).lockOn(false).maxExp(700).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Water_Surge).getPath(), new MagicBuilder().cost(10).castTime(5).cooldown(20).damageMultiplier(1.5F, 1.6F).lockOn(false).maxExp(500).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Watera_Surge).getPath(), new MagicBuilder().cost(10).castTime(5).cooldown(20).damageMultiplier(1.6F, 1.7F).lockOn(false).maxExp(600).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Waterga_Surge).getPath(), new MagicBuilder().cost(10).castTime(5).cooldown(20).damageMultiplier(1.7F, 1.8F).lockOn(false).maxExp(700).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Aero_Surge).getPath(), new MagicBuilder().cost(10).castTime(5).cooldown(20).damageMultiplier(1.5F, 1.6F).lockOn(false).maxExp(500).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Aerora_Surge).getPath(), new MagicBuilder().cost(10).castTime(5).cooldown(20).damageMultiplier(1.6F, 1.7F).lockOn(false).maxExp(600).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Aeroga_Surge).getPath(), new MagicBuilder().cost(10).castTime(5).cooldown(20).damageMultiplier(1.7F, 1.8F).lockOn(false).maxExp(700).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Light_Surge).getPath(), new MagicBuilder().cost(10).castTime(5).cooldown(20).damageMultiplier(1.5F, 1.6F).lockOn(false).maxExp(500).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Lightra_Surge).getPath(), new MagicBuilder().cost(10).castTime(5).cooldown(20).damageMultiplier(1.6F, 1.7F).lockOn(false).maxExp(600).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Lightga_Surge).getPath(), new MagicBuilder().cost(10).castTime(5).cooldown(20).damageMultiplier(1.7F, 1.8F).lockOn(false).maxExp(700).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Dark_Surge).getPath(), new MagicBuilder().cost(10).castTime(5).cooldown(20).damageMultiplier(1.5F, 1.6F).lockOn(false).maxExp(500).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Darkra_Surge).getPath(), new MagicBuilder().cost(10).castTime(5).cooldown(20).damageMultiplier(1.6F, 1.7F).lockOn(false).maxExp(600).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());
		magics.put(ResourceLocation.parse(StringsRM.Attack_Darkga_Surge).getPath(), new MagicBuilder().cost(10).castTime(5).cooldown(20).damageMultiplier(1.7F, 1.8F).lockOn(false).maxExp(700).maxExpLevel(3).spellType(MagicData.SpellType.PHYSICAL).build());

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