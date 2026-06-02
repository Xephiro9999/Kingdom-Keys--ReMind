package online.remind.remind.panels;

public class PanelStats {

    private int strength;
    private int magic;
    private int defense;
    private int ap;
    private int levelBonus;

    public void add(PanelData data) {
        strength += data.getStrength();
        magic += data.getMagic();
        defense += data.getDefense();
        ap += data.getAp();
        levelBonus += data.getLevelBonus();
    }

    public int getStrength() {
        return strength;
    }

    public int getMagic() {
        return magic;
    }

    public int getDefense() {
        return defense;
    }

    public int getAp() {
        return ap;
    }

    public int getLevelBonus() {
        return levelBonus;
    }
}