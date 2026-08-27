package online.remind.remind.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.entity.magic.ZettaflareBeamEntity;

public class magicZettaflare extends Magic {

    // ============================================================
    // EASTER EGG DATA
    // ============================================================

    private static final String VARIANT_TAG =
            "kkremind_zettaflare_variant";


    public magicZettaflare(
            ResourceLocation registryName,
            boolean hasToSelect,
            int tier,
            ResourceLocation gmAbility
    ) {
        super(registryName, hasToSelect, gmAbility);
        setTier(tier);
    }


    // ============================================================
    // MAGIC USE
    // ============================================================

    @Override
    public void magicUse(
            LivingEntity player,
            Player caster,
            float fullMPBlastMult,
            LivingEntity lockOnTarget
    ) {

        float dmgMult =
                getDamageMult();

        dmgMult *=
                fullMPBlastMult;


        switch (getTier()) {

            case 0:

                // ====================================================
                // GET VARIANT THAT WAS CHOSEN AT CAST START
                // ====================================================

                int variant;


                if (
                        caster.getPersistentData()
                                .contains(VARIANT_TAG)
                ) {

                    variant =
                            caster.getPersistentData()
                                    .getInt(VARIANT_TAG);

                } else {

                    /*
                     * Failsafe.
                     *
                     * Normally playMagicCastSound() will already
                     * have chosen the variant.
                     */
                    variant =
                            rollVariant(player);
                }


                // ====================================================
                // CREATE BEAM
                // ====================================================

                ZettaflareBeamEntity beam =
                        new ZettaflareBeamEntity(
                                player.level(),
                                player,
                                caster,
                                dmgMult
                        );


                beam.setBeamVariant(
                        variant
                );


                // ====================================================
                // DEBUG
                // ====================================================

                switch (variant) {

                    case ZettaflareBeamEntity.VARIANT_FINAL_FLASH ->

                            System.out.println(
                                    "Zettaflare fired: FINAL FLASH"
                            );


                    case ZettaflareBeamEntity.VARIANT_KAMEHAMEHA ->

                            System.out.println(
                                    "Zettaflare fired: KAMEHAMEHA"
                            );


                    default ->

                            System.out.println(
                                    "Zettaflare fired: ZETTAFLARE"
                            );
                }


                // ====================================================
                // SPAWN
                // ====================================================

                player.level().addFreshEntity(
                        beam
                );


                // ====================================================
                // CLEAN UP
                // ====================================================

                /*
                 * This cast is finished.
                 *
                 * Remove it so the next cast gets a fresh roll.
                 */
                caster.getPersistentData()
                        .remove(VARIANT_TAG);


                break;
        }
    }


    // ============================================================
    // CAST SOUND
    // ============================================================

    @Override
    public void playMagicCastSound(
            LivingEntity player,
            Player caster
    ) {

        /*
         * THIS happens at the beginning of the cast.
         *
         * Therefore this is where we choose the variant.
         */
        int variant =
                rollVariant(player);


        /*
         * Save it so magicUse() gets the EXACT SAME result later.
         */
        caster.getPersistentData()
                .putInt(
                        VARIANT_TAG,
                        variant
                );


        // ========================================================
        // IMMEDIATE CAST SOUND
        // ========================================================

        switch (variant) {


            // ====================================================
            // FINAL FLASH
            // ====================================================

            case ZettaflareBeamEntity.VARIANT_FINAL_FLASH -> {

                System.out.println(
                        "Zettaflare cast started: FINAL FLASH"
                );


                player.level().playSound(
                        null,
                        player.blockPosition(),

                        ModSoundsRM.FINAL_FLASH.get(),

                        SoundSource.PLAYERS,

                        2.0F,
                        1.0F
                );
            }


            // ====================================================
            // KAMEHAMEHA
            // ====================================================

            case ZettaflareBeamEntity.VARIANT_KAMEHAMEHA -> {

                System.out.println(
                        "Zettaflare cast started: KAMEHAMEHA"
                );


                player.level().playSound(
                        null,
                        player.blockPosition(),

                        ModSoundsRM.KAMEHAMEHA.get(),

                        SoundSource.PLAYERS,

                        2.0F,
                        1.0F
                );
            }


            // ====================================================
            // ZETTAFLARE
            // ====================================================

            default -> {

                System.out.println(
                        "Zettaflare cast started: ZETTAFLARE"
                );


                player.level().playSound(
                        null,
                        player.blockPosition(),

                        ModSoundsRM.ZETTAFLARE.get(),

                        SoundSource.PLAYERS,

                        1.0F,
                        1.0F
                );
            }
        }
    }


    // ============================================================
    // VARIANT ROLL
    // ============================================================

    private int rollVariant(
            LivingEntity player
    ) {

        float roll =
                player.getRandom()
                        .nextFloat();


        System.out.println(
                "Zettaflare roll: " + roll
        );


        /*
         * TESTING:
         *
         * 0.90F = 90% easter egg chance.
         *
         * Change this to 0.01F when you're done testing.
         */
        if (roll < 0.01F) {


            // 50 / 50 once easter egg succeeds
            if (
                    player.getRandom()
                            .nextBoolean()
            ) {

                return ZettaflareBeamEntity
                        .VARIANT_FINAL_FLASH;

            } else {

                return ZettaflareBeamEntity
                        .VARIANT_KAMEHAMEHA;
            }
        }


        return ZettaflareBeamEntity
                .VARIANT_ZETTAFLARE;
    }
}