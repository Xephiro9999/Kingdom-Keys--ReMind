package online.remind.remind.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.client.render.magic.InvisibleEntityRenderer;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.client.model.*;
import online.remind.remind.client.model.mob.chirithyModel;
import online.remind.remind.client.model.reactioncommand.DarkMineModel;
import online.remind.remind.client.model.reactioncommand.LightBeamModel;
import online.remind.remind.client.render.*;
import online.remind.remind.client.render.mob.ChirithyRenderer;
import online.remind.remind.client.render.mob.MeowWowRenderer;
import online.remind.remind.client.render.reactioncommand.DarkMineEntityRenderer;
import online.remind.remind.client.render.reactioncommand.LightBeamEntityRenderer;
import online.remind.remind.client.render.shotlock.BioShotEntityRenderer;
import online.remind.remind.entity.attacks.*;
import online.remind.remind.entity.limits.firagaPillarEntity;
import online.remind.remind.entity.magic.*;
import online.remind.remind.entity.reactioncommand.*;
import online.remind.remind.entity.shotlock.*;
import online.remind.remind.entity.spirits.ChirithyEntity;
import online.remind.remind.entity.spirits.MeowWowEntity;
import online.remind.remind.item.ModItemsRM;

import java.util.function.Supplier;


@EventBusSubscriber(modid = KingdomKeysReMind.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEntitiesRM {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, KingdomKeysReMind.MODID);

    // Magic
    public static final Supplier<EntityType<HolyEntity>> TYPE_HOLY = createEntityType(HolyEntity::new, MobCategory.MISC,"entity_holy", 0.5F, 0.5F);
    public static final Supplier<EntityType<RuinEntity>> TYPE_RUIN = createEntityType(RuinEntity::new, MobCategory.MISC,"entity_ruin", 0.5F, 0.5F);
    public static final Supplier<EntityType<UltimaEntity>> TYPE_ULTIMA = createEntityType(UltimaEntity::new, MobCategory.MISC, "entity_ultima", 1F, 1F);
    public static final Supplier<EntityType<CometEntity>> TYPE_COMET = createEntityType(CometEntity::new, MobCategory.MISC, "entity_comet", 2F, 2F);
    public static final Supplier<EntityType<OsmoseEntity>> TYPE_OSMOSE = createEntityType(OsmoseEntity::new, MobCategory.MISC,"entity_osmose", 0.5F, 0.5F);
    public static final Supplier<EntityType<DrainEntity>> TYPE_DRAIN = createEntityType(DrainEntity::new, MobCategory.MISC,"entity_drain", 0.5F, 0.5F);
    public static final Supplier<EntityType<SilenceEntity>> TYPE_SILENCE = createEntityType(SilenceEntity::new, MobCategory.MISC,"entity_silence", 0.5F, 0.5F);
    public static final Supplier<EntityType<FaithEntity>> TYPE_FAITH = createEntityType(FaithEntity::new, MobCategory.MISC,"entity_faith", 1.5F, 1.5F);
    public static final Supplier<EntityType<MeteorEntity>> TYPE_METEOR = createEntityType(MeteorEntity::new, MobCategory.MISC,"entity_meteor", 1.5F, 1.5F);


    // Attack Commands
    public static final Supplier<EntityType<quickBlitzCollider>> TYPE_QUICK_BLITZ = createEntityType(quickBlitzCollider::new, MobCategory.MISC,"quick_blitz_collider", 1.5F, 1.5F);
    public static final DeferredHolder<EntityType<?>, EntityType<BlitzCollider>> TYPE_BLITZ = ENTITIES.register("blitz", () -> EntityType.Builder.<BlitzCollider>of(BlitzCollider::new, MobCategory.MISC).sized(1.5F, 1.8F).clientTrackingRange(64).updateInterval(1).build("blitz"));
    public static final DeferredHolder<EntityType<?>, EntityType<SlotEdgeCollider>> TYPE_SLOT_EDGE = ENTITIES.register("slot_edge", () -> EntityType.Builder.<SlotEdgeCollider>of(SlotEdgeCollider::new, MobCategory.MISC).sized(1.5F, 1.8F).clientTrackingRange(64).updateInterval(1).build("slot_edge"));
    public static final Supplier<EntityType<slidingDashCollider>> TYPE_SLIDING_DASH = createEntityType(slidingDashCollider::new, MobCategory.MISC,"sliding_dash_collider", 1.5F, 1.5F);
    public static final Supplier<EntityType<fireSurgeCollider>> TYPE_FIRE_SURGE = createEntityType(fireSurgeCollider::new, MobCategory.MISC,"fire_surge_collider", 1.5F, 1.5F);
    public static final Supplier<EntityType<thunderSurgeCollider>> TYPE_THUNDER_SURGE = createEntityType(thunderSurgeCollider::new, MobCategory.MISC,"thunder_surge_collider", 1.5F, 1.5F);
    public static final Supplier<EntityType<blizzardSurgeCollider>> TYPE_BLIZZARD_SURGE = createEntityType(blizzardSurgeCollider::new, MobCategory.MISC,"blizzard_surge_collider", 1.5F, 1.5F);
    public static final Supplier<EntityType<waterSurgeCollider>> TYPE_WATER_SURGE = createEntityType(waterSurgeCollider::new, MobCategory.MISC,"water_surge_collider", 1.5F, 1.5F);
    public static final Supplier<EntityType<aeroSurgeCollider>> TYPE_AERO_SURGE = createEntityType(aeroSurgeCollider::new, MobCategory.MISC,"aero_surge_collider", 1.5F, 1.5F);
    public static final Supplier<EntityType<lightSurgeCollider>> TYPE_LIGHT_SURGE = createEntityType(lightSurgeCollider::new, MobCategory.MISC,"light_surge_collider", 1.5F, 1.5F);
    public static final Supplier<EntityType<darkSurgeCollider>> TYPE_DARK_SURGE = createEntityType(darkSurgeCollider::new, MobCategory.MISC,"dark_surge_collider", 1.5F, 1.5F);
    public static final Supplier<EntityType<zantetsukenCollider>> TYPE_ZANTETSUKEN = createEntityType(zantetsukenCollider::new, MobCategory.MISC,"zantetsuken_collider", 1.5F, 1.5F);
    public static final DeferredHolder<EntityType<?>, EntityType<ElementStrikeCollider>> TYPE_ELEMENT_STRIKE = ENTITIES.register("element_strike", () -> EntityType.Builder.<ElementStrikeCollider>of(ElementStrikeCollider::new, MobCategory.MISC).sized(1.0F, 1.5F).clientTrackingRange(64).updateInterval(1).build("element_strike"));

    // Limits
    public static final Supplier<EntityType<firagaPillarEntity>> TYPE_FIRAGA_PILLAR = createEntityType(firagaPillarEntity::new, MobCategory.MISC,"firaga_pillar", 2f, 5f);



    // Shotlocks
    public static final Supplier<EntityType<BioBarrageShotEntity>> TYPE_BIO_SHOT = createEntityType(BioBarrageShotEntity::new, MobCategory.MISC, "entity_bio_shot", 0.5F, 0.5F);
    public static final Supplier<EntityType<FlameSalvoCoreEntity>> TYPE_SHOTLOCK_FLAME_SALVO = createEntityType(FlameSalvoCoreEntity::new, MobCategory.MISC, "entity_shotlock_flame_salvo_core", 0.5F, 0.5F);
    public static final Supplier<EntityType<BubbleBlasterCoreEntity>> TYPE_SHOTLOCK_BUBBLE_BLASTER = createEntityType(BubbleBlasterCoreEntity::new, MobCategory.MISC, "entity_shotlock_bubble_blaster_core", 0.5F, 0.5F);
    public static final Supplier<EntityType<ThunderstormCoreEntity>> TYPE_SHOTLOCK_THUNDERSTORM = createEntityType(ThunderstormCoreEntity::new, MobCategory.MISC, "entity_shotlock_thunderstorm_core", 0.5F, 0.5F);
    public static final Supplier<EntityType<BioBarrageCoreEntity>> TYPE_SHOTLOCK_BIO_BARRAGE = createEntityType(BioBarrageCoreEntity::new, MobCategory.MISC, "entity_shotlock_bio_barrage_core", 0.5F, 0.5F);
    public static final Supplier<EntityType<MeteorShowerCoreEntity>> TYPE_SHOTLOCK_METEOR_SHOWER = createEntityType(MeteorShowerCoreEntity::new, MobCategory.MISC, "entity_shotlock_meteor_shower_core", 0.5F, 0.5F);
    public static final Supplier<EntityType<DarkFiragaCoreEntity>> TYPE_SHOTLOCK_DARK_FIRAGA = createEntityType(DarkFiragaCoreEntity::new, MobCategory.MISC, "entity_shotlock_dark_firaga_core", 0.5F, 0.5F);
    public static final Supplier<EntityType<DarkDivideCoreEntity>> TYPE_SHOTLOCK_DARK_DIVIDE = createEntityType(DarkDivideCoreEntity::new, MobCategory.MISC, "entity_shotlock_dark_divide_core", 0.5F, 0.5F);
    public static final Supplier<EntityType<DarkDivideShotEntity>> TYPE_SHOTLOCK_DARK_DIVIDE_SHOT = createEntityType(DarkDivideShotEntity::new, MobCategory.MISC, "entity_shotlock_dark_divide_shot", 0.5F, 0.5F);
    public static final Supplier<EntityType<DarkFiragaShotEntity>> TYPE_SHOTLOCK_DARK_FIRAGA_SHOT = createEntityType(DarkFiragaShotEntity::new, MobCategory.MISC, "entity_shotlock_dark_firaga_shot", 0.5F, 0.5F);

    // Reaction Commands
    public static final Supplier<EntityType<LightBeamEntity>> TYPE_LIGHT_BEAM = createEntityType(LightBeamEntity::new, MobCategory.MISC, "entity_rc_light_beam", 3F,4F);
    public static final Supplier<EntityType<DarkMineEntity>> TYPE_DARK_MINE = createEntityType(DarkMineEntity::new, MobCategory.MISC, "entity_rc_dark_mine", 2F,2.5F);
    public static final Supplier<EntityType<DualShotEntity>> TYPE_DUAL_SHOT = createEntityType(DualShotEntity::new, MobCategory.MISC, "entity_rc_dual_shot", 1.5F,1.5F);
    public static final Supplier<EntityType<ThornsEntity>> TYPE_THORNS = createEntityType(ThornsEntity::new, MobCategory.MISC, "entity_rc_thorns", 1.5F,1.5F);

    public static final Supplier<EntityType<ravenousSaberCollider>> TYPE_RAVE_SABER = createEntityType(ravenousSaberCollider::new, MobCategory.MISC, "entity_rc_ravesaber", 1.5F,1.5F);

    // Misc
    public static final Supplier<EntityType<DarkFiragaEntity>> TYPE_DARK_FIRAGA = createEntityType(DarkFiragaEntity::new, MobCategory.MISC,"entity_dark_firaga", 1f, 1f);

    public static final Supplier<EntityType<CounterRushCore>> TYPE_COUNTER_RUSH = createEntityType(CounterRushCore::new, MobCategory.MISC,"entity_counter_rush", 1f, 1f);

    // Dream Eaters
    public static final Item.Properties PROPERTIES = new Item.Properties();

    public static final Supplier<EntityType<ChirithyEntity>> TYPE_CHIRITHY = createEntityType(ChirithyEntity::new, MobCategory.MONSTER, "chirithy", 1F, 1F);
    public static final Supplier<EntityType<MeowWowEntity>> TYPE_MEOW_WOW = createEntityType(MeowWowEntity::new, MobCategory.MONSTER, "meow_wow", 1F, 1F);
    public static final Supplier<Item> CHIRITHY_EGG = ModItemsRM.ITEMS.register("chirithy_spawn_egg", () -> new DeferredSpawnEggItem(TYPE_CHIRITHY, 0xAAAAFF, 0xFF00FF, PROPERTIES));


    public static <T extends Entity, M extends EntityType<T>>Supplier<EntityType<T>> createEntityType(EntityType.EntityFactory<T> factory, MobCategory classification, String name, float sizeX, float sizeY) {        return ENTITIES.register(name, () -> EntityType.Builder.of(factory, classification)
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
        event.registerLayerDefinition(SparkModel.LAYER_LOCATION, SparkModel::createBodyLayer);
        event.registerLayerDefinition(mineModel.LAYER_LOCATION, mineModel::createBodyLayer);

        event.registerLayerDefinition(BerserkAuraModel.LAYER_LOCATION, BerserkAuraModel::createBodyLayer);
        event.registerLayerDefinition(AutoLifeModel.LAYER_LOCATION, AutoLifeModel::createBodyLayer);
        event.registerLayerDefinition(ConfuseModel.LAYER_LOCATION, ConfuseModel::createBodyLayer);

        event.registerLayerDefinition(LightBeamModel.LAYER_LOCATION, LightBeamModel::createBodyLayer);
        event.registerLayerDefinition(DarkMineModel.LAYER_LOCATION, DarkMineModel::createBodyLayer);

        event.registerLayerDefinition(chirithyModel.LAYER_LOCATION, chirithyModel::createBodyLayer);

    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {

        event.registerEntityRenderer(TYPE_HOLY.get(), HolyEntityRenderer::new);
        event.registerEntityRenderer(TYPE_RUIN.get(), RuinEntityRenderer::new);
        event.registerEntityRenderer(TYPE_COMET.get(), CometEntityRenderer::new);
        event.registerEntityRenderer(TYPE_ULTIMA.get(), UltimaEntityRenderer::new);
        event.registerEntityRenderer(TYPE_OSMOSE.get(),InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_DRAIN.get(),InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_SILENCE.get(),InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_FAITH.get(),InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_METEOR.get(),InvisibleEntityRenderer::new);

        event.registerEntityRenderer(TYPE_QUICK_BLITZ.get(),InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_BLITZ.get(),InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_SLOT_EDGE.get(),InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_SLIDING_DASH.get(),InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_AERO_SURGE.get(),InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_BLIZZARD_SURGE.get(),InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_DARK_SURGE.get(),InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_FIRE_SURGE.get(),InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_LIGHT_SURGE.get(),InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_WATER_SURGE.get(),InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_ZANTETSUKEN.get(),InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_ELEMENT_STRIKE.get(),InvisibleEntityRenderer::new);


        event.registerEntityRenderer(TYPE_RAVE_SABER.get(), InvisibleEntityRenderer::new);

        event.registerEntityRenderer(TYPE_FIRAGA_PILLAR.get(), InvisibleEntityRenderer::new);



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
        event.registerEntityRenderer(TYPE_THORNS.get(),InvisibleEntityRenderer::new);

        event.registerEntityRenderer(TYPE_DARK_FIRAGA.get(),InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_COUNTER_RUSH.get(),InvisibleEntityRenderer::new);

        event.registerEntityRenderer(TYPE_CHIRITHY.get(),ChirithyRenderer::new);
        event.registerEntityRenderer(TYPE_MEOW_WOW.get(), MeowWowRenderer::new);


    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event){
        event.put(TYPE_CHIRITHY.get(), ChirithyEntity.registerAttributes().build());
        event.put(TYPE_MEOW_WOW.get(), MeowWowEntity.createAttributes().build());
    }


    public static void registerPlacements(RegisterSpawnPlacementsEvent event) {
        //event.register(TYPE_CHIRITHY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMobSpawnRules);
    }


}
