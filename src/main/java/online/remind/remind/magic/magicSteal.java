package online.remind.remind.magic;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowOverlayPacket;
import online.remind.remind.client.sound.ModSoundsRM;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.List;
import java.util.Random;

public class magicSteal extends Magic {

    public magicSteal(ResourceLocation registryName, boolean hasToSelect, int maxLevel) {
        super(registryName, hasToSelect, maxLevel, null);
    }

    @Override
    public void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {
        if (lockOnEntity == null || caster.level().isClientSide || !(lockOnEntity instanceof Mob mobTarget)) return;

        PlayerData casterData = PlayerData.get(caster);

        // Chance roll based on caster's magic stat
        double chance = (double) (casterData.getMagic(true) + casterData.getStrength(true)) /2;
        caster.sendSystemMessage(Component.literal("Chance: " + chance));
        if (chance < 0) chance = 0;
        double roll = Math.random() * 100;
        if (roll > chance) {
            caster.sendSystemMessage(Component.literal("Missed!"));
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.error.get(), SoundSource.PLAYERS, 1F, 1F);
            return;
        }

        ServerLevel serverLevel = (ServerLevel) mobTarget.level();
        MinecraftServer server = serverLevel.getServer();
        if (server == null) return;

        // Get mob's loot table
        ResourceKey<net.minecraft.world.level.storage.loot.LootTable> lootKey = mobTarget.getLootTable();
        net.minecraft.world.level.storage.loot.LootTable lootTable = server.reloadableRegistries().getLootTable(lootKey);
        if (lootTable == null) return;

        // Build loot parameters
        LootParams.Builder lootBuilder = new LootParams.Builder(serverLevel)
                .withParameter(LootContextParams.THIS_ENTITY, mobTarget)
                .withParameter(LootContextParams.ORIGIN, mobTarget.position())
                .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, caster)
                .withOptionalParameter(LootContextParams.DAMAGE_SOURCE, player.damageSources().playerAttack(caster))
                .withLuck(caster.getLuck());

        LootParams lootParams = lootBuilder.create(LootContextParamSets.ENTITY);

        // Generate all loot items
        List<ItemStack> generatedLoot = lootTable.getRandomItems(lootParams);

        // Filter out empty stacks
        List<ItemStack> nonEmpty = generatedLoot.stream().filter(s -> !s.isEmpty()).toList();
        if (nonEmpty.isEmpty()) {
            caster.sendSystemMessage(Component.literal("Nothing to steal!"));
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.error.get(), SoundSource.PLAYERS, 1F, 1F);
            return;
        }

        // Give all items to caster or drop if inventory full
        for (ItemStack stolen : nonEmpty) {
            stolen = stolen.copy(); // copy to prevent modifying original
            if (!caster.getInventory().add(stolen)) {
                caster.level().addFreshEntity(new ItemEntity(caster.level(), caster.getX(), caster.getY() + 0.5, caster.getZ(), stolen));
            }
            caster.sendSystemMessage(Component.literal("You stole an item!"));

            int randMunny = (int) ((Math.random() * 50) * (1 + casterData.getNumberOfAbilitiesEquipped(Strings.luckyLucky)));

            caster.sendSystemMessage(Component.literal("You stole " + randMunny + " munny!"));
            casterData.setMunny(casterData.getMunny() + randMunny);
            PacketHandler.sendTo(new SCShowOverlayPacket("munny", randMunny), (ServerPlayer) player);

        }

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.itemget.get(), SoundSource.PLAYERS, 1F, 1F);
    }

    @Override
    protected void playMagicCastSound(Player player, Player caster, int level) {
        //player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundsRM.DEATH_CAST.get(), SoundSource.PLAYERS, 1F, 1F);
    }
}
