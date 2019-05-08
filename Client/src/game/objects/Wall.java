package game.objects;


import processing.core.PApplet;
import processing.core.PVector;

public class Wall extends GameObject {

    public float xEnd, yEnd;

    public Wall(float xStart, float yStart, float xEnd, float yEnd, float radius, PApplet parent) {
        super(xStart, yStart, radius * 2, radius * 2, ID.Wall, parent);
        this.xEnd = xEnd;
        this.yEnd = yEnd;
        this.radius = radius;
    }

    public void tick(GameObject other) {
    }

    public void render() {
        parent.noStroke();
        parent.fill(180, 133, 0);
        parent.strokeWeight(0);
        parent.ellipse(x, y, w, h);
        parent.ellipse(xEnd, yEnd, w, h);
        parent.strokeWeight(radius * 2 + 1);
        parent.stroke(180, 133, 0);
        parent.line(x, y, xEnd, yEnd);
        parent.strokeWeight(1);
    }

    public void rightClickDown() {
    }

    public void rightClickUp() {
    }

    public void leftClickDown() {
    }

    public void leftClickUp() {
    }

    public boolean intersects(GameObject other) {

        float lineX1 = this.xEnd - x;
        float lineY1 = this.yEnd - y;

        float lineX2 = other.x - x;
        float lineY2 = other.y - y;

        float edgeLength = lineX1 * lineX1 + lineY1 * lineY1;

        float t = Math.max(0, parent.min(edgeLength, lineX1 * lineX2 + lineY1 * lineY2)) / edgeLength;

        float closestPointX = x + t * lineX1;
        float closestPointY = y + t * lineY1;

        float distance = (float) Math.sqrt((other.x - closestPointX) * (other.x - closestPointX) + (other.y - closestPointY) * (other.y - closestPointY));

        if (distance <= (other.radius + this.radius)) {
            Ball fakeBall = new Ball(closestPointX, closestPointY, this.radius, other.mass, parent.color(255), -2, parent);
            fakeBall.vx = -other.vx;
            fakeBall.vy = -other.vy;

            float overlap = 1 * (distance - other.radius - fakeBall.radius);

            other.vx -= overlap * (other.x - fakeBall.x) / distance;
            other.vy -= overlap * (other.y - fakeBall.y) / distance;

            return true;
        }

        return false;
    }

    public boolean contains(PVector point) {
        return containsEllipse(point);
    }
}
