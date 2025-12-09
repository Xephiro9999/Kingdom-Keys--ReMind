package online.remind.remind.item;

import online.kingdomkeys.kingdomkeys.item.MagicSpellItem;

public class RMMagicSpellItem extends MagicSpellItem implements ICreativeTabRM {
    public RMMagicSpellItem(Properties properties, String name) {
        super(properties, name);
    }

    @Override
    public Tab getTab() {
        return Tab.SPELLS;
    }
}
