package online.remind.remind.dreameater;

import net.minecraft.resources.ResourceLocation;

public class DreamEater {

    ResourceLocation rl;
    int id;
    String translationKey;

    public DreamEater(ResourceLocation registryName, int id){
        this.rl = registryName;
        this.id = id;
        translationKey = "dreameater." + registryName.getPath() + ".name";
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public String getName() {
        return rl.toString();
    }

    public int getId() {
        return id;
    }

    public ResourceLocation getRegistryName() {
        return rl;
    }

}
