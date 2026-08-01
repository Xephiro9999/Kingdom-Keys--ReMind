package online.remind.remind.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber()
public class DataGeneration {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
    	DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        generator.addProvider(event.includeServer(), new MagicDataProviderRM(output));
        generator.addProvider(event.includeServer(), new ShotlockDataProviderRM(output));
    }
}
