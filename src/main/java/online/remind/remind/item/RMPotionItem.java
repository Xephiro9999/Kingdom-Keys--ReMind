
package online.remind.remind.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.item.KKPotionItem;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RMPotionItem extends KKPotionItem {

    private final Consumer<ServerPlayer> customEffect;
    private final Supplier<SoundEvent> useSound;
    private final boolean partyWide;


    // Default: single target, standard Potion sound.
    public RMPotionItem(
            Item.Properties properties,
            Consumer<ServerPlayer> customEffect
    ) {
        this(
                properties,
                customEffect,
                false,
                ModSounds.potion::get
        );
    }

    // Optional party-wide support, standard Potion sound.
    public RMPotionItem(
            Item.Properties properties,
            Consumer<ServerPlayer> customEffect,
            boolean partyWide
    ) {
        this(
                properties,
                customEffect,
                partyWide,
                ModSounds.potion::get
        );
    }

    // Full customization: effect, party-wide behavior, sound.
    public RMPotionItem(
            Item.Properties properties,
            Consumer<ServerPlayer> customEffect,
            boolean partyWide,
            Supplier<SoundEvent> useSound
    ) {
        super(
                properties,
                PotionType.PANACEA,
                0,
                false,
                partyWide
        );

        this.customEffect = customEffect;
        this.partyWide = partyWide;
        this.useSound = useSound;
    }

    @Override
    public void potionEffect(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        // Apply the effect to the selected player.
        applyEffectTo(serverPlayer);

        // Apply to eligible party members, if enabled.
        if (partyWide) {
            applyPartyEffects(serverPlayer);
        }


        SoundEvent sound = useSound != null
                ? useSound.get()
                : ModSounds.potion.get();

        if (sound != null) {
            serverPlayer.level().playSound(
                    null,
                    serverPlayer.blockPosition(),
                    sound,
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F
            );
        }

    }

    private void applyPartyEffects(ServerPlayer user) {
        if (user.getServer() == null) {
            return;
        }

        WorldData worldData = WorldData.get(user.getServer());

        if (worldData == null) {
            return;
        }

        Party party = worldData.getPartyFromMember(
                user.getUUID()
        );

        if (party == null) {
            return;
        }

        double maxRange =
                ModConfigs.SERVER.partyRangeLimit.get();

        double maxRangeSqr = maxRange * maxRange;

        for (Party.Member member : party.getMembers()) {

            // The user has already received the effect.
            if (member.getUUID().equals(user.getUUID())) {
                continue;
            }

            // This base handles player-targeted effects.
            if (!member.isPlayer()) {
                continue;
            }

            ServerPlayer target = user.getServer()
                    .getPlayerList()
                    .getPlayer(member.getUUID());

            if (target == null || !target.isAlive()) {
                continue;
            }

            // Must be in the same dimension.
            if (target.level() != user.level()) {
                continue;
            }

            // Must be within Kingdom Keys' party range.
            if (user.distanceToSqr(target) > maxRangeSqr) {
                continue;
            }

            applyEffectTo(target);
        }
    }

    private void applyEffectTo(ServerPlayer target) {
        if (customEffect == null) {
            return;
        }

        customEffect.accept(target);

        // Sync Kingdom Keys data in case the effect
        // also modifies MP, DP, Focus, or other KK stats.
        PlayerData data = PlayerData.get(target);

        if (data != null) {
            PacketHandler.syncToAllAround(target, data);
        }
    }

    @Override
    public List<Component> getRestoreStats() {
        List<Component> stats = new ArrayList<>();

        stats.add(Component.translatable(
                getDescriptionId() + ".effect"
        ));

        if (partyWide) {
            stats.add(Component.translatable(
                    "potion.stat.party"
            ));
        }

        return stats;
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
    ) {
        tooltip.add(Component.translatable(
                getDescriptionId() + ".effect"
        ));

        if (partyWide) {
            tooltip.add(Component.translatable(
                    "potion.stat.party"
            ));
        }
    }
}
