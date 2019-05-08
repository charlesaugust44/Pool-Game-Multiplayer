package game.objects;

import game.Game;
import net.Addr;
import net.Request;
import net.SocketControl;
import processing.core.PApplet;
import processing.core.PVector;

public class Bin extends GameObject {

    public float xEnd, yEnd;

    public Bin(float xStart, float yStart, float radius, PApplet parent) {
        super(xStart, yStart, radius * 2, radius * 2, ID.Bin, parent);
        this.radius = radius;
    }

    @Override
    public void tick(GameObject other) {
    }

    @Override
    public void render() {
        parent.noStroke();
        parent.fill(0);
        parent.ellipse(x, y, w, h);
    }

    @Override
    public void rightClickDown() {
    }

    @Override
    public void rightClickUp() {
    }

    @Override
    public void leftClickDown() {
    }

    @Override
    public void leftClickUp() {
    }

    @Override
    public boolean intersects(GameObject other) {
        if (!(other instanceof Ball))
            return false;

        Ball b = (Ball) other;
        float distance = PApplet.dist(x, y, other.x, other.y);

        if (distance < this.radius && !b.stop) {
            if (b.number == -1)
                return b.resetPosition();

            b.stopInteraction();
            Game.currentPlayer.addBall(b);

            PVector p = Corner.get(Game.players.indexOf(Game.currentPlayer));

            b.vx = other.vy = 0;

            b.x = p.x - 10 + (b.w + 5) * Game.currentPlayer.ballsIn.size();
            b.y = p.y + 22;

            b.out = true;
            System.out.println(Game.ballsLeft());
            if (Game.ballsLeft() == 0) {
                if (Game.currentPlayer == Game.me) {
                    SocketControl control = new SocketControl(Addr.SERVER_ADDRESS, Addr.SERVER_PORT);

                    control.sendInt(Request.END_MATCH);
                    control.waitResponse();
                    int port = control.receiveInt();
                    control.close();
                    control.sleep(50);

                    control = new SocketControl(Addr.SERVER_ADDRESS, port);

                    control.sendUTF(Game.getResult());
                    control.close();
                }
                Game.showResult();
                ((game.Match) parent).closeMatch();
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean contains(PVector point) {
        return containsEllipse(point);
    }
}
