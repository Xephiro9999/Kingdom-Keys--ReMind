//package online.remind.remind.integration.epicfight;
//
//import net.minecraft.world.entity.player.Player;
//import net.minecraftforge.event.entity.living.LivingDeathEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import online.remind.remind.KingdomKeysReMind;
//import yesman.epicfight.skill.SkillSlots;
//import yesman.epicfight.world.capabilities.EpicFightCapabilities;
//import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
//
//public class EpicFightEvents {
//
//    @SubscribeEvent
//    public void onDeath(LivingDeathEvent event) {
//        if (event.getEntity() instanceof Player) {
//            Player player = (Player) event.getEntity();
//            PlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
//            System.out.println(player.level().isClientSide() + " " + playerpatch);
//            if (playerpatch != null) {
//                System.out.println(playerpatch.getSkill(SkillSlots.GUARD).getSkill());
//            }
//        }
//    }
//}
