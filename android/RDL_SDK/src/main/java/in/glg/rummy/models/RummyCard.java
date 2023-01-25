package in.glg.rummy.models;

import java.util.Random;

public class RummyCard {
    private static final int BLACK_COLOR = 2;
    private static final int CLUBS = 1;
    private static final int DIMONDS = 2;
    private static final int HEARTS = 3;
    private static final int RED_COLOLOR = 1;
    private static final int SPADES = 4;
    private int cardColor;
    private int cardType;
    private int value;

    public static RummyCard getSampleCard() {
        RummyCard c = new RummyCard();
        c.cardColor = new Random().nextInt(1);
        c.cardType = new Random().nextInt(4);
        c.value = new Random().nextInt(4);
        return c;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getCardType() {
        return this.cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public int getCardColor() {
        return this.cardColor;
    }

    public void setCardColor(int cardColor) {
        this.cardColor = cardColor;
    }
}
