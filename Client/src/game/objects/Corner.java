package game.objects;


import processing.core.PVector;

public class Corner {

    private static PVector topLeft = new PVector(120, 20),
            topRight = new PVector(600, 20),
            bottomLeft = new PVector(120, 550),
            bottomRight = new PVector(600, 550);

    public static PVector get(int index) {
        switch (index) {
            case 0:
                return Corner.topLeft;
            case 1:
                return Corner.topRight;
            case 2:
                return Corner.bottomLeft;
            case 3:
                return Corner.bottomRight;
        }
        return null;
    }
}
