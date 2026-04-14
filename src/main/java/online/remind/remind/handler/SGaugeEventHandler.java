package online.remind.remind.handler;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import online.kingdomkeys.kingdomkeys.api.event.MagicSpellCastEvent;

import online.remind.remind.styles.SGaugeHandler;
import online.remind.remind.styles.data.ContributionDefinition;
import online.remind.remind.styles.data.ContributionRegistry;

public class SGaugeEventHandler {

    public static void register() {
        NeoForge.EVENT_BUS.register(SGaugeEventHandler.class);
    }

    @SubscribeEvent
    public static void onMagicCast(MagicSpellCastEvent event) {

        LivingEntity caster = event.getCaster();
        if (!(caster instanceof Player player)) {
            return;
        }

        ResourceLocation spellId = event.getSpellID();
        if (spellId == null) {
            return;
        }

        // NEW: Look up contribution by spell → elements/specific styles
        ContributionDefinition def = ContributionRegistry.getForSpell(spellId);
        if (def == null) {
            return;
        }

        int level = event.getLevel() + 1;

        SGaugeHandler.addContribution(
                player,
                spellId,
                def.elements(),
                def.specificStyles(),
                level
        );
    }


}
