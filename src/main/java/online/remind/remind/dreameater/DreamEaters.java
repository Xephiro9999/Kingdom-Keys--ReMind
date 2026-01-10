package online.remind.remind.dreameater;

import net.minecraft.resources.ResourceLocation;

public class DreamEaters {

    ResourceLocation name;
    int order;
    int id;

    public DreamEaters(ResourceLocation registryName, int order, int id){
        this.name = registryName;
        this.order = order;
        this.id = id;
    }
}
