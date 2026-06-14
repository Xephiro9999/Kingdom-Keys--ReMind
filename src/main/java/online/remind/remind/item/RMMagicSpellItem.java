package online.remind.remind.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.item.MagicSpellItem;
import online.kingdomkeys.kingdomkeys.item.ModItems;

import java.util.Map;
import java.util.function.Supplier;

public class RMMagicSpellItem extends MagicSpellItem implements ICreativeTabRM {
    private static final Map<Supplier<Item>, Supplier<Item>> CONVERSIONS = Map.ofEntries(
            Map.entry(ModItemsRM.sparkSpell, ModItems.sparkSpell),
            Map.entry(ModItemsRM.sparkraSpell, ModItems.sparkraSpell),
            Map.entry(ModItemsRM.sparkgaSpell, ModItems.sparkgaSpell),
            Map.entry(ModItemsRM.balloonSpell, ModItems.balloonSpell),
            Map.entry(ModItemsRM.balloonraSpell, ModItems.balloonraSpell),
            Map.entry(ModItemsRM.balloongaSpell, ModItems.balloongaSpell),
            Map.entry(ModItemsRM.warpSpell, ModItems.warpSpell),
            Map.entry(ModItemsRM.esunaSpell, ModItems.esunaSpell),
            Map.entry(ModItemsRM.mineShieldSpell, ModItems.mineShieldSpell),
            Map.entry(ModItemsRM.mineShield1Spell, ModItems.mineShieldSpell),
            Map.entry(ModItemsRM.mineShield2Spell, ModItems.mineShieldSpell),
            Map.entry(ModItemsRM.mineShield3Spell, ModItems.mineSeekerSpell),
            Map.entry(ModItemsRM.mineSquareSpell, ModItems.mineSquareSpell),
            Map.entry(ModItemsRM.mineSquare1Spell, ModItems.mineSquareSpell),
            Map.entry(ModItemsRM.mineSquare2Spell, ModItems.mineSquareSpell),
            Map.entry(ModItemsRM.mineSquare3Spell, ModItems.mineSeekerSpell)
    );

    public RMMagicSpellItem(Properties properties, String name) {
        super(properties, name);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (level.isClientSide())
            return;

        for (Map.Entry<Supplier<Item>, Supplier<Item>> entry : CONVERSIONS.entrySet()) {
            if (stack.is(entry.getKey().get())) {
                Item newItem = entry.getValue().get();
                ItemStack newStack = new ItemStack(newItem, stack.getCount());

                newStack.applyComponents(stack.getComponents());

                if (entity instanceof Player player) {
                    player.getInventory().setItem(slotId, newStack);
                }
            }
        }
    }

    @Override
    public Tab getTab() {
        return Tab.SPELLS;
    }
}
