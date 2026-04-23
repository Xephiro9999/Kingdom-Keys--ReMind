package online.remind.remind.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.ability.ModAbilitiesRM;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class AbilityOrbRegistry {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.createItems(KingdomKeysReMind.MODID);

    public static final Map<ResourceLocation, Supplier<Item>> GENERATED_ORBS = new HashMap<>();

    public static void generateAbilityOrbs() {

        ModAbilities.ABILITIES.getEntries().forEach(entry -> {
            ResourceLocation idKK = entry.getKey().location();
            registerOrb(idKK);
        });

        ModAbilitiesRM.ABILITIES.getEntries().forEach(entry -> {
            ResourceLocation id = entry.getKey().location();
            registerOrb(id);
        });
    }

    private static void registerOrb(ResourceLocation abilityId) {
        
        if (GENERATED_ORBS.containsKey(abilityId)) return;

        String itemName = "ability_orb_" + abilityId.getNamespace() + "_" + abilityId.getPath();

        Supplier<Item> item = ITEMS.register(itemName, () ->
                new AbilityOrbItem(new Item.Properties(), abilityId.toString())
        );

        GENERATED_ORBS.put(abilityId, item);
    }
}