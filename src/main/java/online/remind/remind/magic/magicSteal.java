package online.remind.remind.magic;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
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
        PlayerData casterData = PlayerData.get(caster);

        if (lockOnEntity != null){
            if (!caster.level().isClientSide && lockOnEntity instanceof Mob mobTarget) {
                GlobalData mobData = GlobalData.get(mobTarget);
                double chance = ((double) casterData.getMagic(true));
                caster.sendSystemMessage(Component.literal("Chance: "+chance));
                if (chance < 0) chance = 0;
                double roll = Math.random() * 100;

                if (roll > chance){
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.error.get(), SoundSource.PLAYERS, 1F, 1F);
                    caster.sendSystemMessage(Component.literal("Missed!"));
                    return;
                }

                ServerLevel serverLevel = (ServerLevel) mobTarget.level();
                MinecraftServer server = serverLevel.getServer();

                if (server == null) {
                    caster.sendSystemMessage(Component.literal("Steal failed: server unavailable."));
                    return;
                }



                ResourceKey<net.minecraft.world.level.storage.loot.LootTable> lootKey = mobTarget.getLootTable();
                net.minecraft.world.level.storage.loot.LootTable lootTable = server.reloadableRegistries().getLootTable(lootKey);
                if (lootTable == null) {
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.error.get(), SoundSource.PLAYERS, 1F, 1F);
                    caster.sendSystemMessage(Component.literal("Nothing to steal..."));
                    return;
                }

                LootParams.Builder lootBuilder = new LootParams.Builder(serverLevel)
                        .withParameter(LootContextParams.THIS_ENTITY, mobTarget)
                        .withParameter(LootContextParams.ORIGIN, mobTarget.position())
                        .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, caster)
                        .withOptionalParameter(LootContextParams.DAMAGE_SOURCE, caster.damageSources().magic())
                        .withLuck(caster.getLuck());

                caster.sendSystemMessage(Component.literal("Luck: "+ caster.getLuck()));

                net.minecraft.world.level.storage.loot.LootParams params = lootBuilder.create(net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.ENTITY);

                List<ItemStack> generatedLoot = lootTable.getRandomItems(params);

                List<ItemStack> nonEmpty = generatedLoot.stream().filter(s -> !s.isEmpty()).toList();

                if (nonEmpty.isEmpty()) {
                    caster.sendSystemMessage(Component.literal("Nothing to steal..."));
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.error.get(), SoundSource.PLAYERS, 1F, 1F);

                    return;
                }

                // Pick one random item from the generated list
                //ItemStack stolen = nonEmpty.get(caster.getRandom().nextInt(nonEmpty.size())).copy();

                // Try to add to caster inventory; if full, spawn as an ItemEntity at caster
                for (ItemStack stolen : nonEmpty) {
                    ItemStack copy = stolen.copy();
                    boolean added = caster.getInventory().add(copy);
                    if (!added) {
                        // Drop in world if inventory full
                        ItemEntity drop = new ItemEntity(caster.level(), caster.getX(), caster.getY() + 0.5, caster.getZ(), copy);
                        caster.level().addFreshEntity(drop);
                    }

                caster.sendSystemMessage(Component.literal("You stole an item!"));
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.itemget.get(), SoundSource.PLAYERS, 1F, 1F);


                }
            }
        }
    }

    @Override
    protected void playMagicCastSound(Player player, Player caster, int level) {
        //player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundsRM.DEATH_CAST.get(), SoundSource.PLAYERS, 1F, 1F);
    }
}
