package threads;

import game.Tools;
import game.Game;
import net.Cast;
import game.objects.Ball;

public class CueHitThread implements Runnable {

    private Ball ball;
    private boolean listen;
    public Thread thread;

    public CueHitThread(Ball ball) {
        this.ball = ball;
        this.listen = true;
        thread = new Thread(this);
    }

    public void start() {
        thread.start();
    }

    public void toggleListener() {
        listen = !listen;
    }

    public void run() {
        while (true) {
            byte[] buffer = Cast.receive(Game.cueHitPort, Game.group);
            if (listen) {
                float[] acc = Tools.divide(buffer);

                ball.ax = acc[0];
                ball.ay = acc[1];
                Game.rx = acc[0];
                Game.ry = acc[1];
            }
        }
    }
}
