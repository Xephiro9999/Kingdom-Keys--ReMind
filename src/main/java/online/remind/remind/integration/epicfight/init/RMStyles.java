package online.remind.remind.integration.epicfight.init;

import yesman.epicfight.world.capabilities.item.Style;

public enum RMStyles implements Style {
    DARK(false), LIGHT(false), RAGE(false), TWILIGHT(true);
    private final boolean canUseOffhand;
    private final int id;

    RMStyles(boolean canUseOffhand){
        this.id = Style.ENUM_MANAGER.assign(this);
        this.canUseOffhand = canUseOffhand;
    }

    @Override
    public int universalOrdinal() {
        return id;
    }

    @Override
    public boolean canUseOffhand() {
        return false;
    }
}
