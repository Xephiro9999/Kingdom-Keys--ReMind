package online.remind.remind;

import com.google.common.base.Suppliers;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.integration.epicfight.init.EpicKKWeapons;
import online.kingdomkeys.kingdomkeys.integration.epicfight.init.KKAnimations;
import online.kingdomkeys.kingdomkeys.integration.epicfight.skills.KKSkills;
import online.kingdomkeys.kingdomkeys.lib.Tags;
import online.remind.remind.ability.ModAbilitiesRM;
import online.remind.remind.capabilities.ModCapabilitiesRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.driveform.ModDriveFormsRM;
import online.remind.remind.effect.ModEffects;
import online.remind.remind.entity.ModEntitiesRM;
import online.remind.remind.handler.EntityEventsRM;
import online.remind.remind.handler.InputHandlerRM;
import online.remind.remind.integration.epicfight.EpicFightEvents;
import online.remind.remind.integration.epicfight.skills.KKRMSkills;
import online.remind.remind.item.ModItemsRM;
import online.remind.remind.lib.ListsRM;
import online.remind.remind.magic.ModMagicsRM;
import online.remind.remind.network.PacketHandlerRM;
import online.remind.remind.particle.ReMindParticles;
import online.remind.remind.reactioncommands.ModReactionCommandsRM;
import online.remind.remind.shotlock.ModShotlocksRM;
import org.slf4j.Logger;

import java.util.List;
import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(KingdomKeysReMind.MODID)
public class KingdomKeysReMind {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "magicksaddon";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static boolean efmLoaded = false;

    
    public KingdomKeysReMind(){
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new EntityEventsRM());
        ModMagicsRM.MAGIC.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(new ModCapabilitiesRM());
        ModSoundsRM.SOUNDS.register(modEventBus);
        ModItemsRM.ITEMS.register(modEventBus);
        ModEntitiesRM.ENTITIES.register(modEventBus);
        ModAbilitiesRM.ABILITIES.register(modEventBus);
        ModDriveFormsRM.DRIVE_FORMS.register(modEventBus);
        ModShotlocksRM.SHOTLOCKS.register(modEventBus);
        ModEffects.MOB_EFFECTS.register(modEventBus);
        ReMindParticles.PARTICLE_TYPES.register(modEventBus);
        ModReactionCommandsRM.REACTION_COMMANDS.register(modEventBus);
        modEventBus.addListener(this::setup);

        TABS.register(modEventBus);

        if (ModList.get().isLoaded("epicfight")) {
            efmLoaded = true;
            KKRMSkills.SKILLS.register(modEventBus);
            MinecraftForge.EVENT_BUS.register(new EpicFightEvents());
        }
    }

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    private static final Supplier<List<ItemStack>> maItems = Suppliers.memoize(() -> ModItemsRM.ITEMS.getEntries().stream().map(RegistryObject::get).map(ItemStack::new).toList());


    public static final RegistryObject<CreativeModeTab>

            misc_tab = TABS.register("magicksaddontab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.magicksaddontab"))
            .icon(() -> new ItemStack(ModItemsRM.hasteSpell.get()))
            .displayItems(((params, output) -> {
                maItems.get().forEach(output::accept);
            }))
            .build());

    private void setup(final FMLCommonSetupEvent event){
        // Some common setup code
		event.enqueueWork(PacketHandlerRM::register);
        event.enqueueWork(ModEntitiesRM::registerPlacements);


        // Org Weapons
        ListsRM.loadAddonOrgWeapons();
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("Kingdom Keys Re:Mind Enabled!");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
    		MinecraftForge.EVENT_BUS.register(new InputHandlerRM());

            LOGGER.info("Kingdom Keys Re:Mind Enabled!");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }


    }
}
