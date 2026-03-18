package online.remind.remind.styles.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.bus.api.SubscribeEvent;

import online.remind.remind.KingdomKeysReMind;

import java.util.Map;

public class StyleDataReloadListener extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FOLDER = "styles";

    public StyleDataReloadListener() {
        super(GSON, FOLDER);
    }

    @SubscribeEvent
    public void onReload(AddReloadListenerEvent event) {
        event.addListener(new StyleDataReloadListener());
        //System.out.println("AddReloadListenerEvent fired!");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap,
                         ResourceManager resourceManager,
                         ProfilerFiller profiler) {

        //System.out.println("### StyleDataReloadListener.apply() CALLED ###");

        KingdomKeysReMind.LOGGER.info("Reloading Style JSONs…");

        StyleLoader.clear();

        jsonMap.forEach((id, element) -> {
            if (!id.getNamespace().equals("kkremind"))
                return;

            // Only load actual StyleDefinitions
            if (!id.getPath().startsWith("form_"))
                return;

            if (!element.isJsonObject()) {
                KingdomKeysReMind.LOGGER.error("Style JSON {} is not an object", id);
                return;
            }

            JsonObject json = element.getAsJsonObject();

            KingdomKeysReMind.LOGGER.info("Loading Style JSON: {}", id);

            try {
                StyleLoader.load(json, id);
            } catch (Exception e) {
                KingdomKeysReMind.LOGGER.error("Failed to load Style JSON {}: {}", id, e.getMessage());
            }
        });


        StyleRegistry.applyDefinitions();
    }
}