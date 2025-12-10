package online.remind.remind.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.config.ModConfigs;
import online.remind.remind.driveform.ModDriveFormsRM;
import online.remind.remind.lib.StringsRM;

import java.util.Random;
import java.util.WeakHashMap;

public class RageFormRC extends ReactionCommand {
    //private static final double RAGE_PERCENT = ModConfigs.rageFormPercent;
    private static final WeakHashMap<Player, RageFormChance> playerStates = new WeakHashMap<>();

    public RageFormRC(ResourceLocation registryName, boolean constantCheck) {
        super(registryName, constantCheck);
    }

    @Override
    public void onUse(Player player, LivingEntity livingEntity, LivingEntity livingEntity1) {
        if (conditionsToAppear(player, player)) {
            PlayerData playerData = PlayerData.get(player);
            DriveForm rageForm = ModDriveForms.registry.get(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, StringsRM.rageForm));
            if (playerData.getDriveFormLevel(ModDriveFormsRM.RAGE.get().getRegistryName().toString()) == 0) {
                playerData.setDriveFormLevel(ModDriveFormsRM.RAGE.get().getRegistryName().toString(), 1);
            }
            rageForm.initDrive(player);
        }
    }

    @Override
    public boolean conditionsToAppear(Player player, LivingEntity livingEntity) {
        PlayerData playerData = PlayerData.get(player);
        float playerMaxHP = player.getMaxHealth();
        float currentHP = player.getHealth();
        float missingHP = (currentHP / playerMaxHP) * 100;
        double baseChance = ModConfigs.rageFormPercent;

        if (playerData != null) {
            RageFormChance state = playerStates.computeIfAbsent(player, p -> new RageFormChance());
            if (!playerData.getActiveDriveForm().equals(StringsRM.rageForm)) {
                if (player.getHealth() <= (playerMaxHP * 0.25f)) {
                    if (!state.hasRolled) {
                        double chance = calculateDynamicChance(missingHP);
                        state.shouldAppear = rollChance(chance);
                        state.hasRolled = true;
                    }
                    return state.shouldAppear;

                } else {
                    state.hasRolled = false;
                    state.shouldAppear = false;
                }
            }
        }
        return false;
    }

    private boolean rollChance(double percent){
        Random rand = new Random();
        return rand.nextDouble() * 100 < percent;
    }

    private static class RageFormChance {
        boolean hasRolled = false;
        boolean shouldAppear = false;
    }

    private double calculateDynamicChance(float hpPercent) {
        // Cap HP percent at 25, and floor at 1 to avoid going above 34%
        hpPercent = Math.max(1.0f, Math.min(25.0f, hpPercent));
        double baseChance = ModConfigs.rageFormPercent;
        float missingPercent = 0;
        // Percent below 25, unless config is set to 0
        if (baseChance != 0.0) {
            missingPercent = 25.0f - hpPercent;

        }

        // Base chance is 10% at 25% HP, +1% for each % below
        return baseChance + missingPercent;
    }
}