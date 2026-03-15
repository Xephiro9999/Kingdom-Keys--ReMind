package online.remind.remind.handler;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import online.kingdomkeys.kingdomkeys.api.event.client.MenuButtonRegisterEvent;
import online.kingdomkeys.kingdomkeys.api.event.client.TargetSelectorEvent;
import online.kingdomkeys.kingdomkeys.client.gui.StopGui;
import online.kingdomkeys.kingdomkeys.client.gui.elements.CommandMenuItem;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.menu.MenuScreen;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.CommandMenuGui;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.ClientUtilsRM;
import online.remind.remind.client.gui.*;
import online.remind.remind.config.ModConfigs;
import online.remind.remind.dreameater.DreamEater;
import online.remind.remind.dreameater.ModDreamEaters;
import online.remind.remind.driveform.ModDriveFormsRM;
import online.remind.remind.effect.ModMobEffectsRM;
import online.remind.remind.entity.attacks.swiftStrikeCollider;
import online.remind.remind.lib.StringsRM;
import org.joml.Vector3f;

import java.awt.*;
import java.util.ArrayList;

public class ClientEventsRM {
    public enum RMButtons {
        PRESTIGE, DREAMEATER, CREDITS, WIKI, PANEL, WALLET
    }



    @SubscribeEvent
    public void onTargetSelector(TargetSelectorEvent event) {
        IGlobalDataRM globalData = ModDataRM.getGlobal(Minecraft.getInstance().player);
        System.out.println(globalData);
        System.out.println(globalData.getDreamEaterRL());
        System.out.println(globalData.hasDreamEaterSummoned());
        if(globalData == null || globalData.getDreamEaterRL().equals(ModDreamEaters.NONE.get().getRegistryName()) || !globalData.hasDreamEaterSummoned())
            return;
        DreamEater dreamEater = ModDreamEaters.registry.get(ResourceLocation.parse(globalData.getDreamEaterRL()));
        if(dreamEater == null)
            return;

        System.out.println(globalData.hasDreamEaterSummoned());
        if(globalData.hasDreamEaterSummoned()) {
            Entity dreamEaterEntity = ClientUtilsRM.getEntityByUUIDClient(globalData.getDreamEaterUUID());
            int dreamEaterID = dreamEaterEntity.getId();
            event.addTarget(new CommandMenuItem.Builder(ResourceLocation.parse(globalData.getDreamEaterRL()),
                            Component.translatable(dreamEater.getTranslationKey()),
                            item -> event.getSubmenu().getParent().getSelected().onEnter()
                    ).setData(dreamEaterID+"").textColour(Color.CYAN).build(event.getSubmenu())
            );
        }
    }

    @SubscribeEvent
    public void menuButton(MenuButtonRegisterEvent event){
        MenuScreen screen = event.getScreen();
        ArrayList<MenuButton> buttons = event.getButtons();

        float topBarHeight = (float) screen.height * 0.17F;
        int start = (int)(topBarHeight) +5;
        int pos = 0;

        float buttonPosX = (float) screen.width * 0.80F;
        float buttonWidth = ((float) screen.width * 0.1744F) - 22;

        if (ModConfigs.ngpEnabled) {
            buttons.add(new MenuButton((int) buttonPosX, start, (int) buttonWidth, (StringsRM.Gui_Menu_Button_Prestige), MenuButton.ButtonType.BUTTON, true, (e) -> {
                action(RMButtons.PRESTIGE);
            }));
        }
		if (ModConfigs.spiritsEnabled) {
			buttons.add(new MenuButton((int) buttonPosX, start + 18 * ++pos, (int) buttonWidth, (StringsRM.Gui_Menu_Button_DreamEater), MenuButton.ButtonType.BUTTON, true, (e) -> {
				action(RMButtons.DREAMEATER);
			}));
		}

        // Panel
        if (ModConfigs.panelsEnabled) {
            if (PlayerData.get(Minecraft.getInstance().player).getAlignment() != Utils.OrgMember.NONE) {
                buttons.add(new MenuButton((int) buttonPosX, start + 18 * ++pos, (int) buttonWidth, (StringsRM.Gui_Menu_Button_Panel), MenuButton.ButtonType.BUTTON, true, (e) -> {
                    action(RMButtons.PANEL);
                }));
            }
        }

        // Wiki
        buttons.add(new MenuButton((int) buttonPosX, start + 18  * ++pos, (int) buttonWidth, (StringsRM.Gui_Menu_Button_Wiki), MenuButton.ButtonType.BUTTON, true, (e) -> {
            action(RMButtons.WIKI);
        }));
        // Wallet
        buttons.add(new MenuButton((int) buttonPosX, start + 18 * ++pos, (int) buttonWidth, (StringsRM.Gui_Menu_Button_Wallet), MenuButton.ButtonType.BUTTON, true, (e) -> {
            action(RMButtons.WALLET);
        }));
        // Credits
		buttons.add(new MenuButton((int) buttonPosX, start + 18 * ++pos, (int) buttonWidth, (StringsRM.Gui_Menu_Button_Credits), MenuButton.ButtonType.BUTTON, false, (e) -> {
			action(RMButtons.CREDITS);
		}));

    }

    protected void action(RMButtons buttonID){
        switch (buttonID){
            case PRESTIGE -> Minecraft.getInstance().setScreen(new PrestigeMenu());
            case DREAMEATER -> Minecraft.getInstance().setScreen(new DreamEaterMenu());
            case CREDITS -> Minecraft.getInstance().setScreen(new CreditsScreen());
            case PANEL -> Minecraft.getInstance().setScreen(new PanelsMenu());
            case WIKI -> Minecraft.getInstance().setScreen(new WikiMenu());
            case WALLET -> Minecraft.getInstance().setScreen(new WalletMenu());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
	public void RenderEntity(RenderLivingEvent.Pre event){
		if (event.getEntity() != null){
			if (event.getEntity() instanceof Player) {
				Player player = (Player) event.getEntity();
				PlayerData playerData = PlayerData.get(player);
				IGlobalDataRM globalData = ModDataRM.getGlobal(event.getEntity());
				if (playerData != null){
					// Light and Dark Step VFX
					if(globalData.getStepTicks() > 0) {
						event.setCanceled(true);
						player.invulnerableTime = globalData.getStepTicks();

                        if (globalData.getStepType() == StringsRM.orgStepType) {
							if (playerData.getAlignment().equals(Utils.OrgMember.XEMNAS)){
								player.level().addAlwaysVisibleParticle(ParticleTypes.END_ROD, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ()  + player.level().random.nextDouble() - 0.5D, 0,0,0);
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0F,0F,0F),1F),player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							}
							if (playerData.getAlignment().equals(Utils.OrgMember.XIGBAR)){
								player.level().addAlwaysVisibleParticle(ParticleTypes.ASH, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ()  + player.level().random.nextDouble() - 0.5D, 3,3,3);
							}
							if (playerData.getAlignment().equals(Utils.OrgMember.XALDIN)){
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0.9f,0.9F,1F),1F),player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
								player.level().addAlwaysVisibleParticle(ParticleTypes.POOF, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ()  + player.level().random.nextDouble() - 0.5D, 3,3,3);
							}
							if (playerData.getAlignment().equals(Utils.OrgMember.VEXEN)){
								player.level().addAlwaysVisibleParticle(ParticleTypes.SNOWFLAKE, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ()  + player.level().random.nextDouble() - 0.5D, 0,0,0);
								player.level().addAlwaysVisibleParticle(ParticleTypes.ITEM_SNOWBALL, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ()  + player.level().random.nextDouble() - 0.5D, 0,0,0);
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0F,0F,0F),1F),player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							}
							if (playerData.getAlignment().equals(Utils.OrgMember.LEXAEUS)){
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0F,0F,0F),1F),player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(1F,0.95F,0F),1F),player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(1F,0.25F,0.35F),1F),player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							}
							if (playerData.getAlignment().equals(Utils.OrgMember.ZEXION)){
								player.level().addAlwaysVisibleParticle(ParticleTypes.SOUL_FIRE_FLAME, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ()  + player.level().random.nextDouble() - 0.5D, 0,0,0);
								player.level().addAlwaysVisibleParticle(ParticleTypes.SOUL, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ()  + player.level().random.nextDouble() - 0.5D, 0,0,0);
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0F,0F,0F),1F),player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							}
							if (playerData.getAlignment().equals(Utils.OrgMember.SAIX)){
								player.level().addAlwaysVisibleParticle(ParticleTypes.SOUL_FIRE_FLAME, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ()  + player.level().random.nextDouble() - 0.5D, 0,0,0);
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0F,0F,0F),1F),player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0.2F,0.2F,1F),1F),player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							}
							if (playerData.getAlignment().equals(Utils.OrgMember.AXEL)){
								player.level().addAlwaysVisibleParticle(ParticleTypes.FLAME, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ()  + player.level().random.nextDouble() - 0.5D, 0,0,0);
								player.level().addAlwaysVisibleParticle(ParticleTypes.SMALL_FLAME, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ()  + player.level().random.nextDouble() - 0.5D, 0,0,0);
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0F,0F,0F),1F),player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							}
							if (playerData.getAlignment().equals(Utils.OrgMember.DEMYX)){
								player.level().addAlwaysVisibleParticle(ParticleTypes.BUBBLE, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ()  + player.level().random.nextDouble() - 0.5D, 0,0,0);
								player.level().addAlwaysVisibleParticle(ParticleTypes.NOTE, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ()  + player.level().random.nextDouble() - 0.5D, 0,0,0);
								player.level().addAlwaysVisibleParticle(ParticleTypes.DRIPPING_WATER, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ()  + player.level().random.nextDouble() - 0.5D, 0,0,0);
							}
							if (playerData.getAlignment().equals(Utils.OrgMember.LUXORD)){
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0F,0F,0F),1F),player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
								player.level().addAlwaysVisibleParticle(ParticleTypes.ENCHANT, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ()  + player.level().random.nextDouble() - 0.5D, 0,0,0);
							}
							if (playerData.getAlignment().equals(Utils.OrgMember.MARLUXIA)){
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(1F,0.4F,0.5F),1F),player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(1F,0.2F,0.3F),1F),player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							}
							if (playerData.getAlignment().equals(Utils.OrgMember.LARXENE)){
								player.level().addAlwaysVisibleParticle(ParticleTypes.ELECTRIC_SPARK, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ()  + player.level().random.nextDouble() - 0.5D, 0,0,0);
								player.level().addAlwaysVisibleParticle(ParticleTypes.CRIT, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ()  + player.level().random.nextDouble() - 0.5D, 0,0,0);
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0F,0F,0F),1F),player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							}
							if (playerData.getAlignment().equals(Utils.OrgMember.ROXAS)){
								player.level().addAlwaysVisibleParticle(ParticleTypes.ENCHANTED_HIT, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ()  + player.level().random.nextDouble() - 0.5D, 0,0,0);
								player.level().addAlwaysVisibleParticle(ParticleTypes.END_ROD, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ()  + player.level().random.nextDouble() - 0.5D, 0,0,0);

							}



						} else if (globalData.getStepType() == StringsRM.twilightStepType){
							player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(1F,1F,1F),1F),player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0F,0F,0F),1F),player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0.5F,0.5F,0.5F),1F),player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
						} else if (globalData.getStepType() == StringsRM.rageStepType){
							player.level().addParticle(new DustParticleOptions(new Vector3f(0.1F,0F,0F),1F),player.getX() + player.level().random.nextDouble() - 0.55D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.55D, 0, 0, 0);
							player.level().addParticle(new DustParticleOptions(new Vector3f(0.3F,0F,0F),1F),player.getX() + player.level().random.nextDouble() - 0.55D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.55D, 0, 0, 0);
							player.level().addParticle(new DustParticleOptions(new Vector3f(0F,0F,0F),1F),player.getX() + player.level().random.nextDouble() - 0.55D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.55D, 0, 0, 0);
						} else if (globalData.getStepType() == StringsRM.darkStepType && !playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID + ":" + StringsRM.twilight)) {
							player.level().addAlwaysVisibleParticle(ParticleTypes.SQUID_INK, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0.5F,0F,0.5F),1F),player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0.5F,0F,1F),1F),player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0.2F,0F,0F),1F),player.getX() + player.level().random.nextDouble() - 0.55D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.55D, 0, 0, 0);
						} else if (globalData.getStepType() == StringsRM.lightStepType && !playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID + ":" + StringsRM.twilight)) {
							player.level().addAlwaysVisibleParticle(ParticleTypes.END_ROD, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							player.level().addAlwaysVisibleParticle(ParticleTypes.CLOUD, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0F,0.9F,0.9F),1F),player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(1F,1F,0.7F),1F),player.getX() + player.level().random.nextDouble() - 0.55D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.55D, 0, 0, 0);
						}
					}

					// Rage Form Active and Walk particles
					if (playerData.getActiveDriveForm().equals(ModDriveFormsRM.RAGE.get().getRegistryName().toString())){
						player.level().addParticle(new DustParticleOptions(new Vector3f(0.1F,0F,0F),1F),player.getX() + player.level().random.nextDouble() - 0.55D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.55D, 0, 0, 0);
	
						if (player.onGround()){
							player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0.2F,0F,0F),1F),player.getX(), player.getY(), player.getZ(), 0, 0, 0);
	
						}
					}

					// Regen Form Active
					if (playerData.getActiveDriveForm().equals(ModDriveFormsRM.REGEN.get().getRegistryName().toString())){
						player.level().addParticle(new DustParticleOptions(new Vector3f(0f,0f,0f),1),player.getX() + player.level().random.nextDouble() - 0.45D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.45D, -1, -1, -1);
						player.level().addParticle(new DustParticleOptions(new Vector3f(1f,1f,1f),1),player.getX() + player.level().random.nextDouble() - 0.45D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.45D, -1, -1, -1);

					}

					// Twilight Form Active
					if (playerData.getActiveDriveForm().equals(ModDriveFormsRM.TWILIGHT.get().getRegistryName().toString())){
						player.level().addParticle(new DustParticleOptions(new Vector3f(0.45F,0.45F,0.45F),0.25F),player.getX() + player.level().random.nextDouble() - 0.45D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.45D, -1, -1, -1);
						player.level().addParticle(new DustParticleOptions(new Vector3f(0.55F,0.55F,0.55F),0.25F),player.getX() + player.level().random.nextDouble() - 0.45D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.45D, -1, -1, -1);

					}

					// Firestorm Active
					if (playerData.getActiveDriveForm().equals(ModDriveFormsRM.FIRESTORM.get().getRegistryName().toString())){
						player.level().addAlwaysVisibleParticle(ParticleTypes.SMALL_FLAME, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ()  + player.level().random.nextDouble() - 0.5D, 0,0,0);
					}

					// Diamond Dust Active
					if (playerData.getActiveDriveForm().equals(ModDriveFormsRM.DIAMOND_DUST.get().getRegistryName().toString())){
						player.level().addAlwaysVisibleParticle(ParticleTypes.SNOWFLAKE, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ()  + player.level().random.nextDouble() - 0.5D, 0,0,0);
					}

					// Thunder Bolt Active
					if (playerData.getActiveDriveForm().equals(ModDriveFormsRM.THUNDER_BOLT.get().getRegistryName().toString())){
						player.level().addAlwaysVisibleParticle(ParticleTypes.ELECTRIC_SPARK, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ()  + player.level().random.nextDouble() - 0.5D, 0,0,0);
					}


					// When I can get particles in other hand
					//if (playerData.getActiveDriveForm().equals(ModDriveFormsRM.DARK.get().getRegistryName().toString())){
						//player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0.5F,0F,0.5F),1F),player.getX(), player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
					//}

					// Spellblade Visual Effects

					if (playerData.isAbilityEquipped(StringsRM.spellblade) && playerData.getNumberOfAbilitiesEquipped(Strings.fireBoost) >= 4){
						player.level().addParticle(new DustParticleOptions(new Vector3f(0.55F,0.0f,0.0F),0.25F),player.getX() + player.level().random.nextDouble() - 0.45D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.45D, -1, -1, -1);
					}
					if (playerData.isAbilityEquipped(StringsRM.spellblade) && playerData.getNumberOfAbilitiesEquipped(Strings.blizzardBoost) >= 4){
						player.level().addParticle(new DustParticleOptions(new Vector3f(0.0F,0.95f,1F),0.25F),player.getX() + player.level().random.nextDouble() - 0.45D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.45D, -1, -1, -1);
					}
					if (playerData.isAbilityEquipped(StringsRM.spellblade) && playerData.getNumberOfAbilitiesEquipped(Strings.thunderBoost) >= 4){
						player.level().addParticle(new DustParticleOptions(new Vector3f(1.0F,1.00f,0F),0.25F),player.getX() + player.level().random.nextDouble() - 0.45D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.45D, -1, -1, -1);
					}
					if (playerData.isAbilityEquipped(StringsRM.spellblade) && playerData.getNumberOfAbilitiesEquipped(Strings.waterBoost) >= 4){
						player.level().addAlwaysVisibleParticle(ParticleTypes.BUBBLE, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY()+ player.level().random.nextDouble() *2D, player.getZ()  + player.level().random.nextDouble() - 0.5D, 0,0,0);
					}

					// Haste and Slow Visual
					if (globalData != null) {

						if (globalData.getHasteTicks() > 0) {
							player.level().addParticle(new DustParticleOptions(new Vector3f(1F, 0.83F, 0F), 0.25F), player.getX() + player.level().random.nextDouble() - 0.45D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.45D, -1, -1, -1);
							//System.out.println("Haste is active");
						}

						if (globalData.getSlowTicks() > 0) {
							player.level().addParticle(new DustParticleOptions(new Vector3f(0F, 0.83F, 1F), 0.25F), player.getX() + player.level().random.nextDouble() - 0.45D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.45D, -1, -1, -1);

						}
					}
				}
			}
		}
	}

    @SubscribeEvent
    public void onLivingUpdate(EntityTickEvent.Pre event) {
        if (event.getEntity() instanceof Player player) {
            if (player.hasEffect(ModMobEffectsRM.STONE)) {
                if (event.getEntity().level().isClientSide && player == Minecraft.getInstance().player) {
                    if (Minecraft.getInstance().screen == null)
                        Minecraft.getInstance().setScreen(new StopGui());
                }
                event.setCanceled(true);
            }

            PlayerData playerData = PlayerData.get(player);
            if (playerData != null){
                if (player.hasEffect(ModMobEffectsRM.CONFUSE)) {
                    MobEffectInstance confuse = player.getEffect(ModMobEffectsRM.CONFUSE);
                    int amp = confuse.getAmplifier();
                    RandomSource rand = player.getRandom();

                    if (rand.nextInt(Math.max(2, 14 - amp)) == 0) {
                        CommandMenuGui.down();
                    }
                    if (rand.nextInt(Math.max(5, 15 - amp)) == 0) {
                        CommandMenuGui.up();
                    }
                    if (rand.nextInt(Math.max(3, 18 - amp)) == 0) {
                        if (rand.nextInt(Math.max(5, 15 - amp)) != 0) {
                            if (playerData.getEquippedItems() != null) {
                                CommandMenuGui.enter();
                            }
                        }
                    }
                    if (rand.nextInt(Math.max(4, 16 - amp)) == 0) {
                        if (rand.nextInt(Math.max(5, 15 - amp)) != 0) {
                            CommandMenuGui.cancel();
                        }
                    }
                }
            }
        }


    }
}
