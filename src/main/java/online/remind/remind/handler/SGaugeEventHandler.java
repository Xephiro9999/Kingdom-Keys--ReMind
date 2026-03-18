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
            return; // Only players accumulate SGauge
        }

        ResourceLocation spellId = event.getSpellID();
        if (spellId == null) {
            return;
        }

        // Convert KK's 0-based spell level to our 1-based level
        int level = event.getLevel() + 1;

        System.out.println("Spell cast: " + spellId + " | Level from event: " + level);

        // Look up SGauge contribution JSON for this spell
        ContributionDefinition def = ContributionRegistry.getForSpell(spellId);
        if (def == null) {
            return; // No SGauge contribution defined
        }

        // Pass corrected level to SGaugeHandler
        SGaugeHandler.addContribution(
                player,
                def.elements(),
                def.specificStyles(),
                level
        );
    }
}
