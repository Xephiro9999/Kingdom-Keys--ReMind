package online.remind.remind.leveling;

import net.minecraft.resources.ResourceLocation;

public class DreamEaterLevel {

    ResourceLocation name;
    int maxLevel;

    private DreamEaterLevelingData data;

    public DreamEaterLevel(ResourceLocation registryName){
        this.name = registryName;
        this.maxLevel = 100;
    }

    public DreamEaterLevel(String registryName){
        this(new ResourceLocation(registryName));
    }

    public void setLevelingData(DreamEaterLevelingData data) {
        this.data = data;
    }

    public DreamEaterLevelingData getLevelingData() {
        return data;
    }

    public String getName() {
        return name.toString();
    }


}
