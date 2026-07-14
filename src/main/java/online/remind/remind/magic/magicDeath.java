package online.remind.remind.magic;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.remind.remind.client.sound.ModSoundsRM;

public class magicDeath extends Magic {

    public magicDeath(ResourceLocation registryName, boolean hasToSelect, int tier, ResourceLocation gmAbility) {
        super(registryName, hasToSelect, gmAbility);
setTier(tier);
    }
    @Override
    public void magicUse(LivingEntity player, Player caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
        PlayerData casterData = PlayerData.get(caster);
        int crisisLv = 0;

        float casterHPPercent = ((caster.getMaxHealth() - caster.getHealth()) / caster.getMaxHealth()) *100f;


        if (casterHPPercent >= 25 && casterHPPercent <= 50){
            crisisLv = 1;
        }
        if (casterHPPercent >= 50 && casterHPPercent <= 75){
            crisisLv = 2;
        }
        if (casterHPPercent >= 75){
            crisisLv = 3;
        }

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

                if (target.isAbilityEquipped(ModAbilities.SECOND_CHANCE)){
                    target.unequipAbility(ModAbilities.SECOND_CHANCE.location(), 0);
                    //System.out.println("Unequipped pesky ability");
                    PacketHandler.syncToAllAround((Player) lockOnEntity, target);
                }
                double roll = Math.random() * 100;
                switch(crisisLv){
                    case 0: // Death Lv4
                        caster.sendSystemMessage(Component.literal("<Death> You hardly need my help you know..."));
                        if (targetLevel % 4 == 0){
                            chance += chanceBoost;
                            if (roll <= chance){
                                lockOnEntity.sendSystemMessage(Component.literal("Death Awaits You..."));
                                lockOnEntity.level().playSound(null, lockOnEntity.getX(), lockOnEntity.getY(), lockOnEntity.getZ(), ModSoundsRM.DEATH_HIT.get(), SoundSource.MASTER, 1f, 1f);
                                lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),99999);
                            }
                        } else {
                            if (roll <= chance){
                                lockOnEntity.sendSystemMessage(Component.literal("Death Awaits You..."));
                                lockOnEntity.level().playSound(null, lockOnEntity.getX(), lockOnEntity.getY(), lockOnEntity.getZ(), ModSoundsRM.DEATH_HIT.get(), SoundSource.MASTER, 1f, 1f);
                                lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),99999);
                            }
                        }
                        break;
                    case 1: // Death Lv3
                        caster.sendSystemMessage(Component.literal("<Death> You've need of my strength..? As you wish..."));
                        if (targetLevel % 3 == 0){
                            chance += chanceBoost;
                            if (roll <= chance){
                                lockOnEntity.sendSystemMessage(Component.literal("Death Awaits You..."));
                                lockOnEntity.level().playSound(null, lockOnEntity.getX(), lockOnEntity.getY(), lockOnEntity.getZ(), ModSoundsRM.DEATH_HIT.get(), SoundSource.MASTER, 1f, 1f);
                                lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),99999);
                            }
                        } else {
                            if (roll <= chance){
                                lockOnEntity.sendSystemMessage(Component.literal("Death Awaits You..."));
                                lockOnEntity.level().playSound(null, lockOnEntity.getX(), lockOnEntity.getY(), lockOnEntity.getZ(), ModSoundsRM.DEATH_HIT.get(), SoundSource.MASTER, 1f, 1f);
                                lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),99999);
                            }
                        }
                        break;
                    case 2: // Death Lv2
                        caster.sendSystemMessage(Component.literal("<Death> My approach draws near on you and your foe... but first... THEM."));
                        if (targetLevel % 2 == 0){
                            chance += chanceBoost;
                            if (roll <= chance){
                                lockOnEntity.sendSystemMessage(Component.literal("Death Awaits You..."));
                                lockOnEntity.level().playSound(null, lockOnEntity.getX(), lockOnEntity.getY(), lockOnEntity.getZ(), ModSoundsRM.DEATH_HIT.get(), SoundSource.MASTER, 1f, 1f);
                                lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),99999);
                            }
                        } else {
                            if (roll <= chance){
                                lockOnEntity.sendSystemMessage(Component.literal("Death Awaits You..."));
                                lockOnEntity.level().playSound(null, lockOnEntity.getX(), lockOnEntity.getY(), lockOnEntity.getZ(), ModSoundsRM.DEATH_HIT.get(), SoundSource.MASTER, 1f, 1f);
                                lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),99999);
                            }
                        }
                        break;
                    case 3: // Death Lv1
                        caster.sendSystemMessage(Component.literal("<Death> On my doorstep you beg for my aid? So be it."));
                        chance += chanceBoost;
                        if (roll <= chance){
                            lockOnEntity.sendSystemMessage(Component.literal("Death Awaits You..."));
                            lockOnEntity.level().playSound(null, lockOnEntity.getX(), lockOnEntity.getY(), lockOnEntity.getZ(), ModSoundsRM.DEATH_HIT.get(), SoundSource.MASTER, 1f, 1f);
                            lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),99999);
                        }
                        break;
                }
            } else {
                GlobalData mobData = GlobalData.get(lockOnEntity);
                int mobLvl = mobData.getLevel();
                double chance = (casterData.getMagicStat().getStat() / 4);
                double roll = Math.random() * 100;
                switch(crisisLv){
                    case 0: // Death Lv4
                        caster.sendSystemMessage(Component.literal("<Death> You hardly need my help you know..."));
                        if (mobLvl % 4 == 0){
                            chance += 20;
                            if (roll <= chance){
                                lockOnEntity.level().playSound(null, lockOnEntity.getX(), lockOnEntity.getY(), lockOnEntity.getZ(), ModSoundsRM.DEATH_HIT.get(), SoundSource.MASTER, 1f, 1f);
                                lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),99999);
                            }
                        } else {
                            if (roll <= chance){
                                lockOnEntity.level().playSound(null, lockOnEntity.getX(), lockOnEntity.getY(), lockOnEntity.getZ(), ModSoundsRM.DEATH_HIT.get(), SoundSource.MASTER, 1f, 1f);
                                lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),99999);
                            }
                        }
                        break;
                    case 1: // Death Lv3
                        caster.sendSystemMessage(Component.literal("<Death> You've need of my strength..? As you wish..."));
                        if (mobLvl % 3 == 0){
                            chance += 30;
                            if (roll <= chance){
                                lockOnEntity.level().playSound(null, lockOnEntity.getX(), lockOnEntity.getY(), lockOnEntity.getZ(), ModSoundsRM.DEATH_HIT.get(), SoundSource.MASTER, 1f, 1f);
                                lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),9999);
                            }
                        } else {
                            if (roll <= chance){
                                lockOnEntity.level().playSound(null, lockOnEntity.getX(), lockOnEntity.getY(), lockOnEntity.getZ(), ModSoundsRM.DEATH_HIT.get(), SoundSource.MASTER, 1f, 1f);
                                lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),9999);
                            }
                        }
                        break;
                    case 2: // Death Lv2
                        caster.sendSystemMessage(Component.literal("<Death> My approach draws near on you and your foe... but first... THEM."));
                        if (mobLvl % 2 == 0){
                            chance += 40;
                            if (roll <= chance){
                                lockOnEntity.level().playSound(null, lockOnEntity.getX(), lockOnEntity.getY(), lockOnEntity.getZ(), ModSoundsRM.DEATH_HIT.get(), SoundSource.MASTER, 1f, 1f);
                                lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),9999);
                            }
                        } else {
                            if (roll <= chance){
                                lockOnEntity.level().playSound(null, lockOnEntity.getX(), lockOnEntity.getY(), lockOnEntity.getZ(), ModSoundsRM.DEATH_HIT.get(), SoundSource.MASTER, 1f, 1f);
                                lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),9999);
                            }
                        }
                        break;
                    case 3: // Death Lv1
                        caster.sendSystemMessage(Component.literal("<Death> On my doorstep you beg for my aid? So be it."));
                        chance += 50;
                        if (roll <= chance){
                            lockOnEntity.level().playSound(null, lockOnEntity.getX(), lockOnEntity.getY(), lockOnEntity.getZ(), ModSoundsRM.DEATH_HIT.get(), SoundSource.MASTER, 1f, 1f);
                            lockOnEntity.hurt(lockOnEntity.damageSources().indirectMagic(caster, null),9999);
                        }
                        break;
                }


            }
        }

    }

    @Override
    public void playMagicCastSound(LivingEntity player, Player caster) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundsRM.DEATH_CAST.get(), SoundSource.PLAYERS, 1F, 1F);
    }
}
