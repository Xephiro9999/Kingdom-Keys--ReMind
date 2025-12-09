package online.remind.remind;

import com.google.common.base.Suppliers;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.item.ICreativeTab;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.remind.remind.ability.ModAbilitiesRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.command.ModCommands;
import online.remind.remind.config.ModConfigs;
import online.remind.remind.driveform.ModDriveFormsRM;
import online.remind.remind.effect.ModMobEffectsRM;
import online.remind.remind.entity.ModEntitiesRM;
import online.remind.remind.handler.EntityEventsRM;
import online.remind.remind.handler.InputHandlerRM;

import online.remind.remind.integration.epicfight.init.EpicFightIntegrationRM;
import online.remind.remind.item.ICreativeTabRM;
import online.remind.remind.item.ModItemsRM;
import online.remind.remind.lib.ListsRM;
import online.remind.remind.magic.ModMagicsRM;
import online.remind.remind.particle.ReMindParticles;
import online.remind.remind.reactioncommands.ModReactionCommandsRM;
import online.remind.remind.shotlock.ModShotlocksRM;
import org.slf4j.Logger;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;


@Mod(KingdomKeysReMind.MODID)
public class KingdomKeysReMind {
    public static final String MODID = "kkremind";
    public static final String MODNAME = "Kingdom Keys - Re:Mind";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(KingdomKeysReMind.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(KingdomKeysReMind.MODID);

    public static boolean efmLoaded = false;

    
    public KingdomKeysReMind(IEventBus modEventBus, ModContainer modContainer){
        //IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);

        if (ModList.get().isLoaded("epicfight")) {
            efmLoaded = true;
            EpicFightIntegrationRM.initIntegrationRM(modEventBus);
            //KKRMSkills.SKILLS.register(modEventBus);
            //NeoForge.EVENT_BUS.register(new EpicFightEvents()); // <--- No longer exists but may in the future?
        }

        // Register ourselves for server and other game events we are interested in
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(new EntityEventsRM());
        ModMagicsRM.MAGIC.register(modEventBus);
        ModSoundsRM.SOUNDS.register(modEventBus);
        ModItemsRM.ITEMS.register(modEventBus);
        ModEntitiesRM.ENTITIES.register(modEventBus);
        ModAbilitiesRM.ABILITIES.register(modEventBus);
        ModDriveFormsRM.DRIVE_FORMS.register(modEventBus);
        ModShotlocksRM.SHOTLOCKS.register(modEventBus);
        ModMobEffectsRM.MOB_EFFECTS.register(modEventBus);
        ReMindParticles.PARTICLE_TYPES.register(modEventBus);
        ModReactionCommandsRM.REACTION_COMMANDS.register(modEventBus);
        ModDataRM.ATTACHMENT_TYPES.register(modEventBus);
        modEventBus.addListener(this::setup);
        TABS.register(modEventBus);




        modContainer.registerConfig(ModConfig.Type.COMMON, ModConfigs.COMMON_SPEC);
    }

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    private static final Supplier<List<ItemStack>> rmItems = Suppliers.memoize(() -> ModItemsRM.ITEMS.getEntries().stream().map(Supplier::get).map(ItemStack::new).toList());
    private static final Supplier<List<ItemStack>> rmKeyblades = Suppliers.memoize(() -> rmItems.get().stream().filter(item -> item.getItem() instanceof ICreativeTab tab && tab.getTab() == ICreativeTab.Tab.KEYBLADES).toList());
    private static final Supplier<List<ItemStack>> rmKeychains = Suppliers.memoize(() -> rmItems.get().stream().filter(item -> item.getItem() instanceof ICreativeTab tab && tab.getTab() == ICreativeTab.Tab.KEYCHAINS).toList());
    private static final Supplier<List<ItemStack>> rmEquipables = Suppliers.memoize(() -> rmItems.get().stream().filter(item -> item.getItem() instanceof ICreativeTab tab && tab.getTab() == ICreativeTab.Tab.EQUIPABLES).toList());
    private static final Supplier<List<ItemStack>> rmSpells = Suppliers.memoize(() -> rmItems.get().stream().filter(item -> item.getItem() instanceof ICreativeTabRM tab && tab.getTab() == ICreativeTabRM.Tab.SPELLS).toList());
    private static final Supplier<List<ItemStack>> rmShotlocks = Suppliers.memoize(() -> rmItems.get().stream().filter(item -> item.getItem() instanceof ICreativeTabRM tab && tab.getTab() == ICreativeTabRM.Tab.SHOTLOCKS).toList());
    private static final Supplier<List<ItemStack>> rmMisc = Suppliers.memoize(() -> rmItems.get().stream().filter(item -> item.getItem() instanceof ICreativeTabRM tab && tab.getTab() == ICreativeTabRM.Tab.MISC).toList());


    public static final Supplier<CreativeModeTab>

            rmKeybladesTab = TABS.register("addonkeyblades", () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.addonkeyblades"))
                .icon(() -> new ItemStack(ModItemsRM.xephiroKeybladeChain.get()))
                .displayItems(((params, output) -> {
                rmKeyblades.get().forEach(output::accept);
                rmKeychains.get().forEach(output::accept);
            }))
            .build());
    public static final Supplier<CreativeModeTab>
            rmEquipablesTab = TABS.register("addonarmortab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.addonarmortab"))
                    .icon(() -> new ItemStack(ModItemsRM.ultima_ribbon.get()))
                    .displayItems(((params, output) -> {
                    rmEquipables.get().forEach(output::accept);
            }))
            .build());
    public static final Supplier<CreativeModeTab>
            rmSpellsTab = TABS.register("addonmagictab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.addonmagictab"))
            .icon(() -> new ItemStack(ModItemsRM.hasteSpell.get()))
            .displayItems(((params, output) -> {
                rmSpells.get().forEach(output::accept);
            }))
            .build());
    public static final Supplier<CreativeModeTab>
            rmShotlocksTab = TABS.register("addonshotlocktab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.addonshotlocktab"))
            .icon(() -> new ItemStack(ModItemsRM.meteorShower.get()))
            .displayItems(((params, output) -> {
                rmShotlocks.get().forEach(output::accept);
            }))
            .build());
    public static final Supplier<CreativeModeTab>
            rmMiscTab = TABS.register("addonmisctab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.addonmisctab"))
            .icon(() -> new ItemStack(ModItemsRM.heartCoin.get()))
            .displayItems(((params, output) -> {
                rmMisc.get().forEach(output::accept);
            }))
            .build());


    private void setup(final FMLCommonSetupEvent event){
        // Some common setup code
        //event.enqueueWork(ModEntitiesRM::registerPlacements);

        EntityEventsRM.ALLOWED_UUIDS.put(UUID.fromString("70b48fbd-b67f-4f3e-9369-09cef36d51a3"), ModItemsRM.xephiroKeybladeChain.get()); // Xephiro
        EntityEventsRM.ALLOWED_UUIDS.put(UUID.fromString("380df991-f603-344c-a090-369bad2a924a"), ModItems.kibladeChain.get());          // Test - Dev Account
        EntityEventsRM.ALLOWED_UUIDS.put(UUID.fromString("349e3886-bdac-422b-92fb-48dbd33caac0"), ModItemsRM.gazingOmenChain.get());     // RealRegen
        EntityEventsRM.ALLOWED_UUIDS.put(UUID.fromString("0914dede-d686-4786-ad15-3249eb21e718"), ModItemsRM.elementalCrescendoChain.get()); // Goblex
        EntityEventsRM.ALLOWED_UUIDS.put(UUID.fromString("1d9409de-3a3a-4e5c-a249-50958353813a"), ModItemsRM.fierceDeityKeyChain.get());     // NolValue
        EntityEventsRM.ALLOWED_UUIDS.put(UUID.fromString("da1e7feb-6ed3-4f90-992e-6cf8fb1d5514"), ModItemsRM.lyric2025TournamentChain.get());  // Lyric

        // Org Weapons
        ListsRM.loadAddonOrgWeapons();
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        ModCommands.register(dispatcher);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("Kingdom Keys Re:Mind Enabled!");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
    		NeoForge.EVENT_BUS.register(new InputHandlerRM());

            LOGGER.info("Kingdom Keys Re:Mind Enabled!");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }


    }
}
