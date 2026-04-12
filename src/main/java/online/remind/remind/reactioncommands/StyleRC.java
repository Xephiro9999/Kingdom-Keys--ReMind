package online.remind.remind.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;

import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.network.PacketHandlerRM;
import online.remind.remind.styles.data.StyleDefinition;
import online.remind.remind.styles.data.StyleRegistry;

public abstract class StyleRC extends ReactionCommand {

    public StyleRC(ResourceLocation registryName, boolean constantCheck) {
        super(registryName, constantCheck);
    }

    // --------- ABSTRACTS: subclasses must define these ---------

    /** Style flag written by SGaugeHandler (e.g. "kkremind:form_firestorm") */
    protected abstract String getStyleId();

    /** DriveForm ID string (e.g. ModDriveFormsRM.FIRESTORM.get().getRegistryName().toString()) */
    protected abstract String getDriveFormId();

    /** DriveForm instance (e.g. ModDriveFormsRM.FIRESTORM.get()) */
    protected abstract DriveForm getDriveForm();

    /** How long the Style lasts after activation (ticks) */
    protected abstract int getStyleDuration();

    /** Finisher behavior when already in this Style */
    protected abstract void performFinisher(Player player);

    // ---------------------- MAIN RC LOGIC -----------------------

    @Override
    public void onUse(Player player, LivingEntity target, LivingEntity ignored) {

        if (!conditionsToAppear(player, player))
            return;

        PlayerData playerData = PlayerData.get(player);
        IGlobalDataRM global = ModDataRM.getGlobal(player);

        String driveId = getDriveFormId();

        // 1. Activate Style (not in this Style yet)
        if (!playerData.getActiveDriveForm().equals(driveId)) {

            DriveForm form = getDriveForm();
            if (form != null) {
                form.initDrive(player);
            }

            // Reset SGauge + Style state
            global.setSituationValue(0);
            global.setStyle("NONE");
            global.setStyleTicks(getStyleDuration());

            PacketHandlerRM.syncGlobalToAllAround(player, global);

            // Remove RC after activation
            playerData.removeReactionCommand(getRegistryName().toString());
            return;
        }

        // 2. Finisher (already in this Style)
        performFinisher(player);

        // Exit Style
        playerData.addFP(-1000);
        global.setSituationValue(0);
        global.setStyle("NONE");
        global.setStyleTicks(0);

        PacketHandlerRM.syncGlobalToAllAround(player, global);
    }

    // ------------------ RC VISIBILITY LOGIC --------------------

    @Override
    public boolean conditionsToAppear(Player player, LivingEntity ignored) {

        PlayerData playerData = PlayerData.get(player);
        IGlobalDataRM global = ModDataRM.getGlobal(player);

        if (playerData == null || global == null)
            return false;

        String style = global.getStyle();
        double gauge = global.getSituationValue();
        String driveId = getDriveFormId();

        // Activation RC: not in any Style
        if (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
            return gauge >= 100 && styleContains(style, getStyleId());
        }

        // Chain-up RC: currently in a Style, and this RC is for the next tier
        if (!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {

            StyleDefinition current = StyleRegistry.getCurrentStyleDefinition(player);
            StyleDefinition target = StyleRegistry.getStyleForDriveForm(ResourceLocation.parse(getStyleId()));

            if (current != null && target != null) {
                if (target.styleLevel() == current.styleLevel() + 1) {
                    return gauge >= 100 && styleContains(style, getStyleId());
                }
            }
        }

        // Finisher RC: already in this Style
        if (playerData.getActiveDriveForm().equals(driveId)) {
            return gauge >= 100;
        }

        return false;
    }

    private boolean styleContains(String styleString, String styleId) {
        if (styleString == null || styleString.isEmpty())
            return false;

        for (String s : styleString.split(",")) {
            if (s.equals(styleId))
                return true;
        }
        return false;
    }

}
