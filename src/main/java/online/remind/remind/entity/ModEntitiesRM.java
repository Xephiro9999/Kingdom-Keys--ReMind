package online.remind.remind.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.client.render.magic.InvisibleEntityRenderer;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.client.model.*;
import online.remind.remind.client.model.mob.chirithyModel;
import online.remind.remind.client.model.reactioncommand.DarkMineModel;
import online.remind.remind.client.model.reactioncommand.LightBeamModel;
import online.remind.remind.client.render.*;
import online.remind.remind.client.render.mob.ChirithyRenderer;
import online.remind.remind.client.render.reactioncommand.DarkMineEntityRenderer;
import online.remind.remind.client.render.reactioncommand.LightBeamEntityRenderer;
import online.remind.remind.client.render.shotlock.BioShotEntityRenderer;
import online.remind.remind.entity.magic.*;
import online.remind.remind.entity.mob.ChirithyEntity;
import online.remind.remind.entity.reactioncommand.*;
import online.remind.remind.entity.shotlock.*;
import online.remind.remind.item.ModItemsRM;

import java.util.function.BiFunction;

import static net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES;

@Mod.EventBusSubscriber(modid = KingdomKeysReMind.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntitiesRM {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ENTITY_TYPES, KingdomKeysReMind.MODID);

    // Magic
    public static final RegistryObject<EntityType<HolyEntity>> TYPE_HOLY = createEntityType(HolyEntity::new, HolyEntity::new, MobCategory.MISC,"entity_holy", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<RuinEntity>> TYPE_RUIN = createEntityType(RuinEntity::new, RuinEntity::new, MobCategory.MISC,"entity_ruin", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<BalloonEntity>> TYPE_BALLOON = createEntityType(BalloonEntity::new, BalloonEntity::new, MobCategory.MISC, "entity_balloon", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<BalloongaEntity>> TYPE_BALLOONGA = createEntityType(BalloongaEntity::new, BalloongaEntity::new, MobCategory.MISC, "entity_balloonga", 1F, 1F);
    public static final RegistryObject<EntityType<UltimaEntity>> TYPE_ULTIMA = createEntityType(UltimaEntity::new, UltimaEntity::new, MobCategory.MISC, "entity_ultima", 1F, 1F);
    public static final RegistryObject<EntityType<CometEntity>> TYPE_COMET = createEntityType(CometEntity::new, CometEntity::new, MobCategory.MISC, "entity_comet", 2F, 2F);
    public static final RegistryObject<EntityType<OsmoseEntity>> TYPE_OSMOSE = createEntityType(OsmoseEntity::new, OsmoseEntity::new, MobCategory.MISC,"entity_osmose", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<DrainEntity>> TYPE_DRAIN = createEntityType(DrainEntity::new, DrainEntity::new, MobCategory.MISC,"entity_drain", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<SilenceEntity>> TYPE_SILENCE = createEntityType(SilenceEntity::new, SilenceEntity::new, MobCategory.MISC,"entity_silence", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<WarpEntity>> TYPE_WARP = createEntityType(WarpEntity::new, WarpEntity::new, MobCategory.MISC,"entity_warp", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<FaithEntity>> TYPE_FAITH = createEntityType(FaithEntity::new, FaithEntity::new, MobCategory.MISC,"entity_faith", 1.5F, 1.5F);
    public static final RegistryObject<EntityType<MeteorEntity>> TYPE_METEOR = createEntityType(MeteorEntity::new, MeteorEntity::new, MobCategory.MISC,"entity_meteor", 1.5F, 1.5F);

    // Shotlocks
    public static final RegistryObject<EntityType<BioBarrageShotEntity>> TYPE_BIO_SHOT = createEntityType(BioBarrageShotEntity::new, BioBarrageShotEntity::new, MobCategory.MISC, "entity_bio_shot", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<FlameSalvoCoreEntity>> TYPE_SHOTLOCK_FLAME_SALVO = createEntityType(FlameSalvoCoreEntity::new, FlameSalvoCoreEntity::new, MobCategory.MISC, "entity_shotlock_flame_salvo_core", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<BubbleBlasterCoreEntity>> TYPE_SHOTLOCK_BUBBLE_BLASTER = createEntityType(BubbleBlasterCoreEntity::new, BubbleBlasterCoreEntity::new, MobCategory.MISC, "entity_shotlock_bubble_blaster_core", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<ThunderstormCoreEntity>> TYPE_SHOTLOCK_THUNDERSTORM = createEntityType(ThunderstormCoreEntity::new, ThunderstormCoreEntity::new, MobCategory.MISC, "entity_shotlock_thunderstorm_core", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<BioBarrageCoreEntity>> TYPE_SHOTLOCK_BIO_BARRAGE = createEntityType(BioBarrageCoreEntity::new, BioBarrageCoreEntity::new, MobCategory.MISC, "entity_shotlock_bio_barrage_core", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<MeteorShowerCoreEntity>> TYPE_SHOTLOCK_METEOR_SHOWER = createEntityType(MeteorShowerCoreEntity::new, MeteorShowerCoreEntity::new, MobCategory.MISC, "entity_shotlock_meteor_shower_core", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<DarkFiragaCoreEntity>> TYPE_SHOTLOCK_DARK_FIRAGA = createEntityType(DarkFiragaCoreEntity::new, DarkFiragaCoreEntity::new, MobCategory.MISC, "entity_shotlock_dark_firaga_core", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<DarkDivideCoreEntity>> TYPE_SHOTLOCK_DARK_DIVIDE = createEntityType(DarkDivideCoreEntity::new, DarkDivideCoreEntity::new, MobCategory.MISC, "entity_shotlock_dark_divide_core", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<DarkDivideShotEntity>> TYPE_SHOTLOCK_DARK_DIVIDE_SHOT = createEntityType(DarkDivideShotEntity::new, DarkDivideShotEntity::new, MobCategory.MISC, "entity_shotlock_dark_divide_shot", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<DarkFiragaShotEntity>> TYPE_SHOTLOCK_DARK_FIRAGA_SHOT = createEntityType(DarkFiragaShotEntity::new, DarkFiragaShotEntity::new, MobCategory.MISC, "entity_shotlock_dark_firaga_shot", 0.5F, 0.5F);

    // Reaction Commands
    public static final RegistryObject<EntityType<LightBeamEntity>> TYPE_LIGHT_BEAM = createEntityType(LightBeamEntity::new, LightBeamEntity::new, MobCategory.MISC, "entity_rc_light_beam", 3F,4F);
    public static final RegistryObject<EntityType<DarkMineEntity>> TYPE_DARK_MINE = createEntityType(DarkMineEntity::new, DarkMineEntity::new, MobCategory.MISC, "entity_rc_dark_mine", 2F,2.5F);
    public static final RegistryObject<EntityType<DualShotEntity>> TYPE_DUAL_SHOT = createEntityType(DualShotEntity::new, DualShotEntity::new, MobCategory.MISC, "entity_rc_dual_shot", 1.5F,1.5F);

    // Misc
    public static final RegistryObject<EntityType<DarkFiragaEntity>> TYPE_DARK_FIRAGA = createEntityType(DarkFiragaEntity::new, DarkFiragaEntity::new, MobCategory.MISC,"entity_dark_firaga", 1f, 1f);

    public static final RegistryObject<EntityType<CounterRushCore>> TYPE_COUNTER_RUSH = createEntityType(CounterRushCore::new, CounterRushCore::new, MobCategory.MISC,"entity_counter_rush", 1f, 1f);

    // Dream Eaters
    public static final Item.Properties PROPERTIES = new Item.Properties();

    public static final RegistryObject<EntityType<ChirithyEntity>> TYPE_CHIRITHY = createEntityType(ChirithyEntity::new, ChirithyEntity::new, MobCategory.MONSTER, "chirithy", 1F, 1F);
    public static final RegistryObject<Item> CHIRITHY_EGG = ModItemsRM.ITEMS.register("chirithy_spawn_egg", () -> new ForgeSpawnEggItem(TYPE_CHIRITHY, 0xAAAAFF, 0xFF00FF, PROPERTIES));


    public static <T extends Entity, M extends EntityType<T>>RegistryObject<EntityType<T>> createEntityType(EntityType.EntityFactory<T> factory, BiFunction<PlayMessages.SpawnEntity, Level, T> clientFactory, MobCategory classification, String name, float sizeX, float sizeY) {
        return ENTITIES.register(name, () -> EntityType.Builder.of(factory, classification)
                .setCustomClientFactory(clientFactory)
                .setShouldReceiveVelocityUpdates(false)
                .setUpdateInterval(1)
                .setTrackingRange(8)
                .sized(sizeX, sizeY)
                .build(name));
    }
    @OnlyIn(Dist.CLIENT)
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(HolyModel.LAYER_LOCATION, HolyModel::createBodyLayer);
        event.registerLayerDefinition(RuinModel.LAYER_LOCATION, RuinModel::createBodyLayer);
        event.registerLayerDefinition(BalloonModel.LAYER_LOCATION, BalloonModel::createBodyLayer);
        event.registerLayerDefinition(BalloongaModel.LAYER_LOCATION, BalloongaModel::createBodyLayer);
        event.registerLayerDefinition(UltimaModel.LAYER_LOCATION, UltimaModel::createBodyLayer);
        event.registerLayerDefinition(CometModel.LAYER_LOCATION, CometModel::createBodyLayer);
        event.registerLayerDefinition(OsmoseModel.LAYER_LOCATION, OsmoseModel::createBodyLayer);
        event.registerLayerDefinition(DrainModel.LAYER_LOCATION, DrainModel::createBodyLayer);
        event.registerLayerDefinition(SilenceModel.LAYER_LOCATION, SilenceModel::createBodyLayer);
        event.registerLayerDefinition(WarpModel.LAYER_LOCATION, WarpModel::createBodyLayer);

        event.registerLayerDefinition(BerserkAuraModel.LAYER_LOCATION, BerserkAuraModel::createBodyLayer);
        event.registerLayerDefinition(AutoLifeModel.LAYER_LOCATION, AutoLifeModel::createBodyLayer);

        event.registerLayerDefinition(LightBeamModel.LAYER_LOCATION, LightBeamModel::createBodyLayer);
        event.registerLayerDefinition(DarkMineModel.LAYER_LOCATION, DarkMineModel::createBodyLayer);

        event.registerLayerDefinition(chirithyModel.LAYER_LOCATION, chirithyModel::createBodyLayer);

    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {

        event.registerEntityRenderer(TYPE_HOLY.get(), HolyEntityRenderer::new);
        event.registerEntityRenderer(TYPE_RUIN.get(), RuinEntityRenderer::new);
        event.registerEntityRenderer(TYPE_BALLOON.get(), BalloonEntityRenderer::new);
        event.registerEntityRenderer(TYPE_BALLOONGA.get(), BalloongaEntityRenderer::new);
        event.registerEntityRenderer(TYPE_COMET.get(), CometEntityRenderer::new);
        event.registerEntityRenderer(TYPE_ULTIMA.get(), UltimaEntityRenderer::new);
        event.registerEntityRenderer(TYPE_OSMOSE.get(),InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_DRAIN.get(),InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_SILENCE.get(),InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_WARP.get(),InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_FAITH.get(),InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_METEOR.get(),InvisibleEntityRenderer::new);

        event.registerEntityRenderer(TYPE_BIO_SHOT.get(), BioShotEntityRenderer::new);
        event.registerEntityRenderer(TYPE_SHOTLOCK_DARK_FIRAGA_SHOT.get(), BioShotEntityRenderer::new);
        event.registerEntityRenderer(TYPE_SHOTLOCK_DARK_DIVIDE_SHOT.get(), BioShotEntityRenderer::new);

        event.registerEntityRenderer(TYPE_SHOTLOCK_FLAME_SALVO.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_SHOTLOCK_BUBBLE_BLASTER.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_SHOTLOCK_BIO_BARRAGE.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_SHOTLOCK_THUNDERSTORM.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_SHOTLOCK_METEOR_SHOWER.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_SHOTLOCK_DARK_FIRAGA.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_SHOTLOCK_DARK_DIVIDE.get(), InvisibleEntityRenderer::new);

        event.registerEntityRenderer(TYPE_LIGHT_BEAM.get(), LightBeamEntityRenderer::new);
        event.registerEntityRenderer(TYPE_DARK_MINE.get(), DarkMineEntityRenderer::new);
        event.registerEntityRenderer(TYPE_DUAL_SHOT.get(),InvisibleEntityRenderer::new);

        event.registerEntityRenderer(TYPE_DARK_FIRAGA.get(),InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_COUNTER_RUSH.get(),InvisibleEntityRenderer::new);

        event.registerEntityRenderer(TYPE_CHIRITHY.get(),ChirithyRenderer::new);


    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event){
        event.put(TYPE_CHIRITHY.get(), ChirithyEntity.registerAttributes().build());
    }

    public static void registerPlacements() {
        SpawnPlacements.register(TYPE_CHIRITHY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMobSpawnRules);
    }


}
