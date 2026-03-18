package online.remind.remind.styles.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import java.util.Map;

public class ContributionDataReloadListener extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FOLDER = "styles/contributions";

    public ContributionDataReloadListener() {
        super(GSON, FOLDER);
    }

    @SubscribeEvent
    public void onReload(AddReloadListenerEvent event) {
        event.addListener(new ContributionDataReloadListener());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap,
                         ResourceManager resourceManager,
                         ProfilerFiller profiler) {

        System.out.println("Reloading Contribution JSONs…");

        ContributionLoader.clear();

        jsonMap.forEach((id, element) -> {
            if (!id.getNamespace().equals("kkremind"))
                return;

            if (!element.isJsonObject())
                return;

            JsonObject json = element.getAsJsonObject();

            System.out.println("Found Contribution JSON: " + id);

            ContributionLoader.load(json, id);
        });

        ContributionRegistry.applyDefinitions();
    }
}
