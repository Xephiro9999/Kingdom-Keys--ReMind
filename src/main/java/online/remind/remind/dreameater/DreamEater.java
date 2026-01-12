package online.remind.remind.dreameater;

import net.minecraft.resources.ResourceLocation;
import online.remind.remind.KingdomKeysReMind;

public class DreamEater {

    ResourceLocation name;
    int id;

    public DreamEater(ResourceLocation registryName, int id){
        this.name = registryName;
        this.id = id;
    }

    public String getName() {
        return name.toString();
    }

    public int getId() {
        return id;
    }

    public ResourceLocation getRegistryName() {
        return ResourceLocation.parse(KingdomKeysReMind.MODID+":"+name);
    }

}
