package game.objects;

import game.Game;
import processing.core.PApplet;
import processing.core.PVector;

public class Ball extends GameObject {

    private boolean dragging, drawCue;
    private int friction = 500;
    private int color;
    public int number;
    public Player playerOwner;
    public boolean colliding = false, out = false;
    private float ix, iy;

    public Ball(float x, float y, float radius, float mass, int color, int number, PApplet parent) {
        super(x, y, radius * 2, radius * 2, ID.Ball, parent);
        this.ix = x;
        this.iy = y;
        this.color = color;
        this.mass = mass;
        this.radius = radius;
        this.number = number;
    }

    public boolean resetPosition() {
        x = ix;
        y = iy;
        ax = vx = ay = vy = 0;
        return true;
    }

    public void stopInteraction() {
        stop = true;
    }

    public void tick(GameObject other) {

        if ((other != null) && (!colliding)) {
            Game.hit.play();
            colliding = true;
        } else {
            colliding = false;
        }

        if (dragging) {
            y = Game.mousePosition.y;
            x = Game.mousePosition.x;
        }

        vx += ax;
        vy += ay;
        x += vx;
        y += vy;
        ax = -vx / friction;
        ay = -vy / friction;

        if ((vx * vx + vy * vy) <= .0001) {
            vx = vy = 0;
        }
    }

    public void render() {
        parent.fill(color);
        parent.noStroke();
        parent.ellipse(x, y, w, h);

        if (number != -1) {
            parent.fill(255);
            parent.ellipse(x, y, w - w * .5f, h - h * .5f);
            parent.fill(0);

            parent.textSize(w * .38f);
            String n = String.valueOf(number);
            parent.text(n, x - ((n.length() == 2) ? w * .25f : w * .1f), y + h * .17f);
        }

        if (drawCue) {
            parent.stroke(255, 0, 255);
            float d = parent.dist(x, y, Game.mousePosition.x, Game.mousePosition.y);
            float sx = (x - Game.mousePosition.x) / d;
            float sy = (y - Game.mousePosition.y) / d;

            parent.line(x, y, Game.mousePosition.x, Game.mousePosition.y);
            parent.stroke(0, 0, 255);
            parent.line(x, y, x + sx * 1000, y + sy * 1000);
        }
        parent.stroke(0);
    }

    public void rightClickDown() {
        if (!Game.cueStatus) {
            return;
        }

        if (this.number == -1) {
            drawCue = true;
        }
    }

    public void rightClickUp() {
        if (!drawCue) {
            return;
        }

        float d = parent.dist(x, y, Game.mousePosition.x, Game.mousePosition.y);
        d = (d > 200) ? 200 : d;

        float r = 0.8f;

        ax = (x - Game.mousePosition.x) / d * r;
        ay = (y - Game.mousePosition.y) / d * r;

        Game.sendCueHit(ax, ay);

        drawCue = false;
        Game.cue.play();
        Game.disableCue();
    }

    public void leftClickDown() {
        vx = vy = 0;
        ax = ay = 0;
        dragging = true;
    }

    public void leftClickUp() {
        dragging = false;
    }

    public boolean intersects(GameObject other) {
        if (stop) {
            return false;
        }

        if (other.id == ID.Bin) {
            other.intersects(this);
        } else {
            return intersectsEllipse(other);
        }
        return false;
    }

    public boolean contains(PVector point) {
        return containsEllipse(point);
    }
}
