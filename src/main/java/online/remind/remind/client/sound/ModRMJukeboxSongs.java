package online.remind.remind.client.sound;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.JukeboxSong;
import online.remind.remind.KingdomKeysReMind;

public class ModRMJukeboxSongs {

    public static final ResourceKey<JukeboxSong>
            ONE_WINGED_ANGEL_KH1 = ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "one-winged_angel_kh1")),
            ONE_WINGED_ANGEL_KH2 = ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "one-winged_angel_kh2"));


}
