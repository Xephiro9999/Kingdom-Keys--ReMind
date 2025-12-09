package online.remind.remind.item;

public interface ICreativeTabRM {

    enum Tab {
        SPELLS, SHOTLOCKS, MISC, NONE
    }

    Tab getTab();
}
