package online.remind.remind.magic;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.remind.remind.client.sound.ModSoundsRM;

public class magicDeath extends Magic {

    public magicDeath(ResourceLocation registryName, boolean hasToSelect, int maxLevel) {
        super(registryName, hasToSelect, maxLevel, null);
    }
    @Override
    public void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {
        PlayerData casterData = PlayerData.get(caster);

        if (lockOnEntity != null){

            if (lockOnEntity instanceof Player){

                PlayerData target = PlayerData.get((Player) lockOnEntity);
                int targetLevel = target.getLevel();
                double chance = ((double) casterData.getMagic(true) / 4) - ((double) target.getDefense(true) / 4);
                float remaningHP = ((lockOnEntity.getMaxHealth() - lockOnEntity.getHealth()) / lockOnEntity.getMaxHealth()) * 100F;
                double chanceBoost = remaningHP;

                if (chance < 0){
                    chance *= -1;
                }

                if (target.isAbilityEquipped(Strings.secondChance)){
                    target.unequipAbility(Strings.secondChance, 0);
                    System.out.println("Unequipped pesky ability");
                    PacketHandler.syncToAllAround((Player) lockOnEntity, target);
                }

                // Chance Breakdown
                /*
                System.out.println("Caster's MAG: " + casterData.getMagic(true));
                System.out.println("Target's DEF: " + target.getDefense(true));
                System.out.println("Increased Chance based on Target's Missing HP %: " + chanceBoost);
                 */

                double roll = Math.random() * 100;
                switch(level){
                    case 0: // Death Lv4
                        if (targetLevel % 4 == 0){
                            chance += chanceBoost;
                            System.out.println("Target meets Level Req! Chance: " + chance);
                            if (roll <= chance){
                                lockOnEntity.sendSystemMessage(Component.literal("Death Awaits You..."));
                                lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),99999);
                            }
                        } else {
                            if (roll <= chance){
                                lockOnEntity.sendSystemMessage(Component.literal("Death Awaits You..."));
                                lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),99999);
                            }
                        }
                        break;
                    case 1: // Death Lv3
                        if (targetLevel % 3 == 0){
                            chance += chanceBoost;
                            System.out.println("Target meets Level Req! Chance: " + chance);
                            if (roll <= chance){
                                lockOnEntity.sendSystemMessage(Component.literal("Death Awaits You..."));

                                lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),99999);
                            }
                        } else {
                            if (roll <= chance){
                                lockOnEntity.sendSystemMessage(Component.literal("Death Awaits You..."));

                                lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),99999);
                            }
                        }
                        break;
                    case 2: // Death Lv2
                        if (targetLevel % 2 == 0){
                            chance += chanceBoost;
                            System.out.println("Target meets Level Req! Chance: " + chance);
                            if (roll <= chance){
                                lockOnEntity.sendSystemMessage(Component.literal("Death Awaits You..."));
                                lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),99999);
                            }
                        } else {
                            if (roll <= chance){
                                lockOnEntity.sendSystemMessage(Component.literal("Death Awaits You..."));
                                lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),99999);
                            }
                        }
                        break;
                    case 3: // Death Lv1
                        chance += chanceBoost;
                        System.out.println("Target meets Level Req! Chance: " + chance);
                        if (roll <= chance){
                            lockOnEntity.sendSystemMessage(Component.literal("Death Awaits You..."));
                            lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),99999);
                        }
                        break;
                }
            } else {
                GlobalData mobData = GlobalData.get(lockOnEntity);
                int mobLvl = mobData.getLevel();
                double chance = (casterData.getMagicStat().getStat() / 4);
                System.out.println(chance);
                double roll = Math.random() * 100;
                System.out.println(roll);
                switch(level){
                    case 0: // Death Lv4
                        if (mobLvl % 4 == 0){
                            chance += 10;
                            if (roll <= chance){
                                lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),99999);
                            }
                        } else {
                            if (roll <= chance){
                                lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),99999);
                            }
                        }
                        break;
                    case 1: // Death Lv3
                        if (mobLvl % 3 == 0){
                            chance += 10;
                            if (roll <= chance){
                                lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),9999);
                            }
                        } else {
                            if (roll <= chance){
                                lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),9999);
                            }
                        }
                        break;
                    case 2: // Death Lv2
                        if (mobLvl % 2 == 0){
                            chance += 10;
                            if (roll <= chance){
                                lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),9999);
                            }
                        } else {
                            if (roll <= chance){
                                lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),9999);
                            }
                        }
                        break;
                    case 3: // Death Lv1
                        chance += 10;
                        if (roll <= chance){
                            lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),9999);
                        }
                        break;
                }


            }
        }

    }

    @Override
    protected void playMagicCastSound(Player player, Player caster, int level) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundsRM.PLAYER_CAST.get(), SoundSource.PLAYERS, 1F, 1F);
    }
}
