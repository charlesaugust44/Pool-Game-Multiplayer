package game.objects;

import java.awt.Rectangle;
import processing.core.PApplet;
import processing.core.PVector;

public abstract class GameObject {

    public float x, y, vx, vy, w, h;
    public float ax, ay, radius, mass;
    public boolean rightClicked, leftClicked, stop = false;
    public ID id;
    protected PApplet parent;

    public GameObject(float x, float y, float w, float h, ID id, PApplet parent) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.id = id;
        this.parent = parent;
    }

    public abstract void tick(GameObject other);

    public abstract void render();

    public abstract boolean contains(PVector point);

    public abstract boolean intersects(GameObject other);

    public abstract void rightClickDown();

    public abstract void leftClickDown();

    public abstract void rightClickUp();

    public abstract void leftClickUp();

    protected boolean containsEllipse(PVector point) {
        return parent.dist(point.x, point.y, x, y) <= radius;
    }

    protected boolean containsRectangle(PVector point) {
        return getRectangle().contains(point.x, point.y);
    }

    public boolean intersectsRectangle(GameObject other) {
        return getRectangle().intersects(other.getRectangle());
    }

    public boolean intersectsEllipse(GameObject other) {
        float d = parent.dist(x, y, other.x, other.y);
        float radial = radius + other.radius;

        if (d < radial) {
            if (this.id != ID.Wall) {
                float overlap = (d - radius - other.radius) * 0.5f;

                other.x += overlap * (x - other.x) / d;
                other.y += overlap * (y - other.y) / d;

                x -= overlap * (x - other.x) / d;
                y -= overlap * (y - other.y) / d;

                if ((other.vx != 0 && other.vy != 0) || (vy != 0 && vx != 0)) {
                    float nx = (other.x - x) / d;
                    float ny = (other.y - y) / d;

                    float kx = (vx - other.vx);
                    float ky = (vy - other.vy);
                    float p = 2.0f * (nx * kx + ny * ky) / (mass + other.mass);
                    vx = vx - p * other.mass * nx;
                    vy = vy - p * other.mass * ny;
                    other.vx = other.vx + p * mass * nx;
                    other.vy = other.vy + p * mass * ny;
                } else {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    private Rectangle getRectangle() {
        return new Rectangle((int) x, (int) y, (int) w, (int) h);
    }

    public void setParent(PApplet parent) {
        this.parent = parent;
    }

}
