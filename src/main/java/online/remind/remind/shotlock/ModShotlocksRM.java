package online.remind.remind.shotlock;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.lib.StringsRM;

import java.util.function.Supplier;

public class ModShotlocksRM {

    public static DeferredRegister<Shotlock> SHOTLOCKS = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "shotlocks"), KingdomKeysReMind.MODID);


    static int order = 100;

    public static final Supplier<Shotlock>
            // BBS Shotlocks
    	FLAME_SALVO = SHOTLOCKS.register(StringsRM.flameSalvo, () -> new ShotlockFlameSalvo(KingdomKeysReMind.MODID + ":" + StringsRM.flameSalvo, order++, 2, 15)),
        BUBBLE_BLASTER = SHOTLOCKS.register(StringsRM.bubbleBlaster, () -> new ShotlockBubbleBlaster(KingdomKeysReMind.MODID + ":" + StringsRM.bubbleBlaster, order++, 2, 15)),
        THUNDERSTORM = SHOTLOCKS.register(StringsRM.thunderStorm, () -> new ShotlockThunderstorm(KingdomKeysReMind.MODID + ":" + StringsRM.thunderStorm, order++, 2, 5)),
        BIO_BARRAGE = SHOTLOCKS.register(StringsRM.bioBarrage, () -> new ShotlockBioBarrage(KingdomKeysReMind.MODID + ":" + StringsRM.bioBarrage, order++, 2,8)),
        METEOR_SHOWER = SHOTLOCKS.register(StringsRM.meteorShower, () -> new ShotlockMeteorShower(KingdomKeysReMind.MODID + ":" + StringsRM.meteorShower, order++, 2,30)),

            // Series Shotlocks
        DARK_DIVIDE = SHOTLOCKS.register(StringsRM.darkDivide, () -> new ShotlockDarkDivide(KingdomKeysReMind.MODID + ":" + StringsRM.darkDivide, order++, 2,28)),

            // Original Shotlocks
        HEARTLESS_ANGEL = SHOTLOCKS.register(StringsRM.heartlessAngel, () -> new ShotlockHeartlessAngel(KingdomKeysReMind.MODID + ":" + StringsRM.heartlessAngel, order++, 110,1));
}
