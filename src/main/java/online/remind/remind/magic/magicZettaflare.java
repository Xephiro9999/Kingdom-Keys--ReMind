package online.remind.remind.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.entity.magic.ZettaflareBeamEntity;

public class magicZettaflare extends Magic {

    public magicZettaflare(
            ResourceLocation registryName,
            boolean hasToSelect,
            int tier,
            ResourceLocation gmAbility
    ) {
        super(registryName, hasToSelect, gmAbility);
        setTier(tier);
    }

    @Override
    public void magicUse(
            LivingEntity player,
            Player caster,
            float fullMPBlastMult,
            LivingEntity lockOnTarget
    ) {
        float dmgMult = getDamageMult();
        dmgMult *= fullMPBlastMult;

        switch (getTier()) {
            case 0:
                ZettaflareBeamEntity beam =
                        new ZettaflareBeamEntity(
                                player.level(),
                                player,
                                caster,
                                dmgMult
                        );

                player.level().addFreshEntity(beam);
                break;
        }
    }

    @Override
    public void playMagicCastSound(
            LivingEntity player,
            Player caster
    ) {
        player.level().playSound(
                null,
                player.blockPosition(),
                ModSoundsRM.PLAYER_CAST.get(),
                SoundSource.PLAYERS,
                1.5F,
                0.75F
        );
    }
}