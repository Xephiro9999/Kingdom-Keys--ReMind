package online.remind.remind.driveform;

import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.remind.remind.styles.StyleElement;

public abstract class StyleForm extends DriveForm {

    protected int level; // 1, 2, 3...
    protected Set<StyleElement> triggers;
    protected boolean requiresWeapons;
    protected Set<ResourceLocation> requiredWeapons;

    public StyleForm(
            ResourceLocation registryName,
            int order,
            ResourceLocation skinRL,
            boolean hasKeychain,
            boolean baseGrowthAbilities,
            int level,
            Set<StyleElement> triggers,
            boolean requiresWeapons,
            Set<ResourceLocation> requiredWeapons
    ) {
        super(registryName, order, hasKeychain, baseGrowthAbilities);
        this.skinRL = skinRL;

        this.level = level;
        this.triggers = triggers;
        this.requiresWeapons = requiresWeapons;
        this.requiredWeapons = requiredWeapons;

    }

    public void setStyleLevel(int level) {
        this.level = level;
    }

    public void setTriggers(Set<StyleElement> elements) {
        this.triggers = elements;
    }

    public void setWeaponRestrictions(boolean requiresSpecificWeapons, Set<ResourceLocation> requiredWeapons) {
        this.requiresWeapons = requiresSpecificWeapons;
        this.requiredWeapons = requiredWeapons;
    }

    @Override
    public boolean displayInCommandMenu(Player player){
        return false;
    }

    @Override
    public void endDrive(Player player) {
        super.endDrive(player);
        PlayerData playerData = PlayerData.get(player);
    }

}