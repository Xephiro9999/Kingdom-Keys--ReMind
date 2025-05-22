package online.remind.remind.client.sound;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import online.remind.remind.KingdomKeysReMind;

public class ModSoundsRM {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, KingdomKeysReMind.MODID);

    public static final RegistryObject<SoundEvent>
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

            DISPEL = registerSound("dispel"),
            LIGHT_BEAM = registerSound("light_beam"),
            DARK_MINE = registerSound("dark_mine"),
            DARK_MINE_ALIVE = registerSound("dark_mine_alive"),
            TWILIGHT_STEP = registerSound("twilight_step"),
            DUAL_SHOT = registerSound("dual_shot"),
            HEARTLESS_ANGEL = registerSound("heartless_angel"),
            DARK_MODE = registerSound("darkness"),

            ROYAL_GUARD = registerSound("royal_block"),
            ROYAL_PARRY = registerSound("royal_parry");


    public static RegistryObject<SoundEvent> registerSound(String name) {
        final ResourceLocation soundID = new ResourceLocation(KingdomKeysReMind.MODID, name);
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(soundID));
    }

}
