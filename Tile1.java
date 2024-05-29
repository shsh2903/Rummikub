import java.awt.*;

import static java.awt.Color.*;
import static java.awt.Color.YELLOW;

class Tile1 {
    private final Color color;
    private final int number;

    public Tile1(Color color, int number) {
        this.color = color;
        this.number = number;
    }

    public Color getColor() {
        return color;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "(" + getColorName(color) + ", " + number + ")";
    }
    public static String getColorName(Color color) {
        if (BLUE.equals(color)) {
            return "blue";
        } else if (RED.equals(color)) {
            return "red";
        } else if (BLACK.equals(color)) {
            return "black";
        } else if (YELLOW.equals(color)) {
            return "yellow";
        }
        return "unknown";
    }
}