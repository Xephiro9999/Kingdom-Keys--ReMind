package online.remind.remind.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetAerialDodgeTicksPacket;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetGlidingPacket;
import online.remind.remind.network.GrowthPanelAction;
import online.remind.remind.network.cts.CSGrowthPanelActionPacket;
import online.remind.remind.panels.OrganizationPanelAbilityHelper;
import online.remind.remind.panels.PanelRegistry;

public class GrowthPanelClientEvents {

    private static boolean wasJumpDown = false;
    private static int airborneTicks = 0;
    private static boolean releasedJumpSinceLeavingGround = false;
    private static boolean wasGliding = false;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null || minecraft.options == null) {
            return;
        }

        boolean jumpDown = minecraft.options.keyJump.isDown();

        if (minecraft.player.onGround()) {
            airborneTicks = 0;
            releasedJumpSinceLeavingGround = false;

            if (wasGliding) {
                PacketDistributor.sendToServer(
                        new CSGrowthPanelActionPacket(GrowthPanelAction.GLIDE_STOP)
                );

                wasGliding = false;
            }
        } else {
            airborneTicks++;

            if (!jumpDown) {
                releasedJumpSinceLeavingGround = true;
            }
        }

        /*
         * Aerial Dodge:
         * Requires a second jump press after leaving the ground.
         */
        if (jumpDown && !wasJumpDown) {
            if (!minecraft.player.onGround()
                    && airborneTicks >= 4
                    && releasedJumpSinceLeavingGround
                    && OrganizationPanelAbilityHelper.hasAbilityPanelEquipped(minecraft.player, Strings.aerialDodge)) {

                PacketHandler.sendToServer(new CSSetAerialDodgeTicksPacket(true, 10));

                PacketDistributor.sendToServer(
                        new CSGrowthPanelActionPacket(GrowthPanelAction.AERIAL_DODGE)
                );
            }
        }

        /*
         * Glide:
         * Hold Jump while falling with Glide Panel equipped.
         * Sends GLIDE_START once, then GLIDE_STOP when released/landed/panel inactive.
         */
        boolean shouldGlide = !minecraft.player.onGround()
                && minecraft.player.fallDistance > 0
                && jumpDown
                && OrganizationPanelAbilityHelper.hasAbilityPanelEquipped(minecraft.player, Strings.glide);

        if (shouldGlide != wasGliding) {
            PlayerData playerData = PlayerData.get(minecraft.player);

            if (playerData != null) {
                playerData.setIsGliding(shouldGlide);

                if (shouldGlide) {
                    playerData.setAerialDodgeTicks(0);
                    PacketHandler.sendToServer(new CSSetAerialDodgeTicksPacket(true, 0));
                }
            }

            PacketHandler.sendToServer(new CSSetGlidingPacket(shouldGlide));

            PacketDistributor.sendToServer(
                    new CSGrowthPanelActionPacket(
                            shouldGlide ? GrowthPanelAction.GLIDE_START : GrowthPanelAction.GLIDE_STOP
                    )
            );

            wasGliding = shouldGlide;
        }

        wasJumpDown = jumpDown;
    }

    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!OrganizationPanelAbilityHelper.hasAbilityPanelEquipped(player, Strings.highJump)) {
            return;
        }

        double boost = 0.05D * OrganizationPanelAbilityHelper.getEquippedPanelCount(player, PanelRegistry.HIGH_JUMP_PANEL);
        System.out.println(boost);

        player.setDeltaMovement(
                player.getDeltaMovement().x,
                player.getDeltaMovement().y + boost,
                player.getDeltaMovement().z
        );

        player.hurtMarked = true;
        player.hasImpulse = true;
    }
}