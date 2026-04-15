package online.remind.remind.client.sound;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.remind.remind.KingdomKeysReMind;

public class ModSoundsRM {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, KingdomKeysReMind.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent>
            HASTE = registerSound("haste"),
            SLOW = registerSound("slow"),
            HOLY = registerSound("holy"),
            RUIN = registerSound("ruin"),
            BALLOON = registerSound("balloon"),
            BALLOON_BOUNCE = registerSound("balloon_bounce"),
            PLAYER_CAST = registerSound("player_cast"),
            ULTIMA_CAST = registerSound("ultima_cast"),
            ULTIMA_EXPLOSION = registerSound("ultima_explosion"),
            BERSERK = registerSound("berserk"),
            BERSERK2 = registerSound("berserk2"),
            AUTOLIFE = registerSound("auto_life"),
            DARKSTEP1 = registerSound("darkstep1"),
            DARKSTEP2 = registerSound("darkstep2"),
            LIGHTSTEP1 = registerSound("lightstep1"),
            LIGHTSTEP2 = registerSound("lightstep2"),
            RISKCHARGE = registerSound("riskcharge"),
            DRAIN = registerSound("drain"),
            OSMOSE = registerSound("osmose"),
            SILENCEHIT = registerSound("silencehit"),
            WARPHITPLAYER = registerSound("warp_hit_player"),
            ESUNA = registerSound("esuna"),
            REGEN = registerSound("regen"),
            DEATH_CAST = registerSound("death_cast"),
            DEATH_HIT = registerSound("death_hit"),
            ROYAL_GUARD = registerSound("royal_block"),
            ROYAL_PARRY = registerSound("royal_parry"),
            SPARK = registerSound("spark"),
            SPARKRA = registerSound("sparkra"),
            SPARKGA = registerSound("sparkga"),
            CONFUSE = registerSound("confuse"),
            DARK_FIRAGA = registerSound("dark_firaga"),
            ZANTETSUKEN = registerSound("zantetsuken"),
            SWIFT_STRIKE = registerSound("swift_strike"),
            SWIFT_STRIKE_EN = registerSound("swift_strike_en"),

            SPIRIT_SUMMON = registerSound("spirit_summon"),
            SPIRIT_DESUMMON = registerSound("spirit_desummon"),





            DISPEL = registerSound("dispel"),
            LIGHT_BEAM = registerSound("light_beam"),
            DARK_MINE = registerSound("dark_mine"),
            DARK_MINE_ALIVE = registerSound("dark_mine_alive"),
            TWILIGHT_STEP = registerSound("twilight_step"),
            DUAL_SHOT = registerSound("dual_shot"),
            HEARTLESS_ANGEL = registerSound("heartless_angel"),
            HEARTLESS_ANGEL_EN = registerSound("heartless_angel_en"),
            DARK_MODE = registerSound("darkness"),

            // Music
            DREAM_EATERS = registerSound("dream_eaters"),
            Record_One_Winged_Angel_KH1 = registerSound("records/one-winged_angel_kh1"),
            Record_One_Winged_Angel_KH2 = registerSound("records/one-winged_angel_kh2");


    public static DeferredHolder<SoundEvent, SoundEvent> registerSound(String name) {
        final ResourceLocation soundID = ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, name);
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(soundID));
    }

}
