
package online.remind.remind.lib;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import online.remind.remind.KingdomKeysReMind;

public class Tags {

    public static final TagKey<Item>
            MUNNY = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "kkremind/munny"));


}