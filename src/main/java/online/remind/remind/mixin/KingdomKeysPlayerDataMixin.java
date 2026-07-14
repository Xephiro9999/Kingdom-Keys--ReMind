package online.remind.remind.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.remind.remind.dreameater.DreamEaterAbilityLinkHelper;
import online.remind.remind.dreameater.DreamEaterVirtualAbilityBridge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(value = PlayerData.class, remap = false)
public abstract class KingdomKeysPlayerDataMixin {

    private static final boolean KKREMIND_DEBUG = true;

    private static final Set<String> LOGGED_OWNER_NAMES = ConcurrentHashMap.newKeySet();
    private static final Set<String> LOGGED_LEVEL_CHECKS = ConcurrentHashMap.newKeySet();
    private static final Set<String> LOGGED_NUMBER_CHECKS = ConcurrentHashMap.newKeySet();
    private static final Set<String> LOGGED_FAKE_LEVEL_GRANTS = ConcurrentHashMap.newKeySet();
    private static final Set<String> LOGGED_FAKE_NUMBER_GRANTS = ConcurrentHashMap.newKeySet();

    @Inject(
            method = "get(Lnet/minecraft/world/entity/player/Player;)Lonline/kingdomkeys/kingdomkeys/data/PlayerData;",
            at = @At("RETURN"),
            require = 0
    )
    private static void kkremind$rememberOwnerFromGet(
            Player player,
            CallbackInfoReturnable<PlayerData> cir
    ) {
        PlayerData playerData = cir.getReturnValue();
        DreamEaterVirtualAbilityBridge.rememberOwner(playerData, player);

        if (KKREMIND_DEBUG && player != null && playerData != null) {
            String name = player.getGameProfile().getName();

            if (LOGGED_OWNER_NAMES.add("get:" + name)) {
                System.out.println("[KKReMind/DELinks] Remembered PlayerData owner from PlayerData.get(Player): " + name);
            }
        }
    }

    @Inject(
            method = "get(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/world/entity/player/Player;)Lonline/kingdomkeys/kingdomkeys/data/PlayerData;",
            at = @At("RETURN"),
            require = 0
    )
    private static void kkremind$rememberOwnerFromNBTGet(
            CompoundTag nbt,
            Player player,
            CallbackInfoReturnable<PlayerData> cir
    ) {
        PlayerData playerData = cir.getReturnValue();
        DreamEaterVirtualAbilityBridge.rememberOwner(playerData, player);

        if (KKREMIND_DEBUG && player != null && playerData != null) {
            String name = player.getGameProfile().getName();

            if (LOGGED_OWNER_NAMES.add("nbt:" + name)) {
                System.out.println("[KKReMind/DELinks] Remembered PlayerData owner from PlayerData.get(NBT, Player): " + name);
            }
        }
    }

    @Inject(
            method = "getEquippedAbilityLevel",
            at = @At("RETURN"),
            cancellable = true,
            require = 0
    )
    private void kkremind$fakeDreamEaterLinkAsEquippedAbilityLevel(
            ResourceLocation abilityId, CallbackInfoReturnable<int[]> cir
    ) {
        int[] original = cir.getReturnValue();

        if (isEquipped(original)) {
            return;
        }

        PlayerData self = (PlayerData) (Object) this;
        Player player = DreamEaterVirtualAbilityBridge.getOwner(self);

        if (KKREMIND_DEBUG && LOGGED_LEVEL_CHECKS.add(abilityId.toString())) {
            System.out.println(
                    "[KKReMind/DELinks] KK getEquippedAbilityLevel check: "
                            + abilityId
                            + " original="
                            + Arrays.toString(original)
                            + " owner="
                            + getPlayerName(player)
                            + " stored="
                            + getStoredAbilitiesDebug(player)
            );
        }

        if (player == null) {
            return;
        }

        if (!DreamEaterVirtualAbilityBridge.hasDreamEaterLinkAbility(player, abilityId.toString())) {
            return;
        }

        if (KKREMIND_DEBUG && LOGGED_FAKE_LEVEL_GRANTS.add(player.getUUID() + ":" + abilityId)) {
            System.out.println("[KKReMind/DELinks] FAKING getEquippedAbilityLevel as equipped: " + abilityId + " for " + player.getGameProfile().getName());
        }

        cir.setReturnValue(new int[]{1, 1});
    }

    @Inject(
            method = "getNumberOfAbilitiesEquipped(Lnet/minecraft/resources/ResourceLocation;)I",
            at = @At("RETURN"),
            cancellable = true,
            require = 0
    )
    private void kkremind$fakeDreamEaterLinkAsNumberEquipped(
            ResourceLocation abilityId, CallbackInfoReturnable<Integer> cir
    ) {
        Integer original = cir.getReturnValue();

        if (original != null && original > 0) {
            return;
        }

        PlayerData self = (PlayerData) (Object) this;
        Player player = DreamEaterVirtualAbilityBridge.getOwner(self);

        if (KKREMIND_DEBUG && LOGGED_NUMBER_CHECKS.add(abilityId.toString())) {
            System.out.println(
                    "[KKReMind/DELinks] KK getNumberOfAbilitiesEquipped check: "
                            + abilityId
                            + " original="
                            + original
                            + " owner="
                            + getPlayerName(player)
                            + " stored="
                            + getStoredAbilitiesDebug(player)
            );
        }

        if (player == null) {
            return;
        }

        if (!DreamEaterVirtualAbilityBridge.hasDreamEaterLinkAbility(player, abilityId.toString())) {
            return;
        }

        if (KKREMIND_DEBUG && LOGGED_FAKE_NUMBER_GRANTS.add(player.getUUID() + ":" + abilityId)) {
            System.out.println("[KKReMind/DELinks] FAKING getNumberOfAbilitiesEquipped as 1: " + abilityId + " for " + player.getGameProfile().getName());
        }

        cir.setReturnValue(1);
    }

    private static boolean isEquipped(int[] data) {
        return data != null && data.length > 1 && data[1] > 0;
    }

    private static String getPlayerName(Player player) {
        return player == null ? "null" : player.getGameProfile().getName();
    }

    private static String getStoredAbilitiesDebug(Player player) {
        if (player == null) {
            return "[]";
        }

        return DreamEaterAbilityLinkHelper.getAccessoryAbilityIds(player).toString();
    }
}