package online.remind.remind.client.gui;

import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuColourBox;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ElementPairBuilder {
    private ArrayList<MenuColourBox> list;

    private Supplier<Integer> xLeft;
    private Supplier<Integer> wLeft;
    private Supplier<Integer> wRight;
    private int gap;

    private String keyLeft;
    private String valueLeft;
    private int colorLeft;

    private String keyRight;
    private String valueRight;
    private int colorRight;

    private boolean condition = true;
    private String hiddenName;
    private String hiddenValue;

    private static final int HIDDEN_COLOR = 0x232324;

    /* ---------- REQUIRED ---------- */
    public ElementPairBuilder(ArrayList<MenuColourBox> list) {
        this.list = list;
    }

    /* ---------- SETTERS FLUENT ---------- */
    public ElementPairBuilder setPosition(Supplier<Integer> xLeft) {
        this.xLeft = xLeft;
        return this;
    }

    public ElementPairBuilder setWidths(Supplier<Integer> wLeft, int gap, Supplier<Integer> wRight) {
        this.wLeft = wLeft;
        this.wRight = wRight;
        this.gap = gap;
        return this;
    }

    public ElementPairBuilder setLeft(String key, String value, int color) {
        this.keyLeft = key;
        this.valueLeft = value;
        this.colorLeft = color;
        return this;
    }

    public ElementPairBuilder setRight(String key, String value, int color) {
        this.keyRight = key;
        this.valueRight = value;
        this.colorRight = color;
        return this;
    }

    public ElementPairBuilder setRight(String key, String value, String hiddenKey, String hiddenValue, int color) {
        this.keyRight = key;
        this.valueRight = value;
        this.colorRight = color;
        this.hiddenName = hiddenKey;
        this.hiddenValue = hiddenValue;
        return this;
    }

    public ElementPairBuilder setHidden(String name, String value) {
        this.hiddenName = name;
        this.hiddenValue = value;
        return this;
    }

    public ElementPairBuilder setCondition(boolean condition) {
        this.condition = condition;
        return this;
    }

    /* ---------- BUILD ---------- */
    public void add() {
        String keyL = condition ? keyLeft : "???";
        String valL = condition ? valueLeft : "???";

        String keyR = condition ? keyRight : hiddenName;
        String valR = condition ? valueRight : hiddenValue;

        int colorL = condition ? colorLeft: HIDDEN_COLOR;
        int colorR = condition ? colorRight: HIDDEN_COLOR;

        addElement(list, xLeft.get(), 0, wLeft.get(), keyL, valL, colorL);
        addElement(list, xLeft.get() + wLeft.get() + gap, 0, wRight.get(), keyR, valR, colorR);
    }

    private void addElement(ArrayList<MenuColourBox> list, int x, int y, int w, String name, String value, int color) {
        list.add(new MenuColourBox(x, y, w, name, value, color));
    }

    private void addElementPair(ArrayList<MenuColourBox> list, int xLeft, int y, int wLeft, String nameLeft, String valueLeft, int colorLeft, int gap, int wRight, String nameRight, String valueRight, int colorRight) {
        list.add(new MenuColourBox(xLeft, y, wLeft, nameLeft, valueLeft, colorLeft));
        list.add(new MenuColourBox(xLeft+wLeft+gap, y, wRight, nameRight, valueRight, colorRight));
    }

    private void addHiddenElementPair(ArrayList<MenuColourBox> list, int xLeft, int y, int wLeft, String keyLeft, String valueLeft, int colorLeft, int gap, int wRight, String keyRight, String valueRight, int colorRight, boolean condition, String hiddenName, String hiddenValue) {
        if(condition) {
            addElement(list,xLeft, y, wLeft, keyLeft, valueLeft, colorLeft);
            addElement(list,xLeft + wLeft + gap, y, wRight, keyRight, valueRight, colorRight);
        } else {
            addElement(list,xLeft, y, wLeft, "???", "???", 0x232324); //Hidden color
            addElement(list,xLeft+wLeft+gap, y, wRight, hiddenName, hiddenValue, 0x232324);
        }
    }
}
