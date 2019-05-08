package game;

import threads.CueHitThread;
import threads.TurnThread;
import game.objects.Bin;
import game.objects.Wall;
import game.objects.Ball;
import game.objects.Player;
import java.util.ArrayList;
import main.MatchRoom;
import processing.core.PApplet;

public class Match extends PApplet {

    private final int ballSize;
    private final int ballMass;
    public int ballsLeft;
    public MatchRoom parent;

    public Match(ArrayList<Player> players, MatchRoom parent) {
        this.ballMass = 11;
        this.ballSize = 13;
        Game.players = players;
        this.parent = parent;
    }

    @Override
    public void settings() {
        size(1062, 600);
    }

    @Override
    public void setup() {
        frameRate(120);

        Game.init(this);
        Game.initPlayers(this);

        Game.disableCue();
        if (Game.me == Game.currentPlayer)
            Game.enableCue();

        int radius = 40;

        Game.add(new Bin(80, 80, radius, this));
        Game.add(new Bin(983, 521, radius, this));
        Game.add(new Bin(80, 521, radius, this));
        Game.add(new Bin(983, 80, radius, this));

        radius = 10;

        Game.add(new Wall(132, 90, 930, 90, radius, this)); //UP
        Game.add(new Wall(132, 510, 930, 510, radius, this)); //DOWN

        Game.add(new Wall(90, 132, 90, 468, radius, this)); //LEFT
        Game.add(new Wall(972, 132, 972, 468, radius, this)); //RIGHT

        Game.add(new Ball(800, 250, ballSize, ballMass, color(247, 143, 30), 15, this));
        Game.add(new Ball(800, 278, ballSize, ballMass, color(25, 85, 199), 2, this));
        /*Game.add(new Ball(800, 306, ballSize, ballMass, color(238, 68, 40), 3, this));
        Game.add(new Ball(800, 334, ballSize, ballMass, color(92, 13, 172), 4, this));
        Game.add(new Ball(800, 362, ballSize, ballMass, color(225, 25, 193), 5, this));

        Game.add(new Ball(774, 265, ballSize, ballMass, color(78, 149, 43), 6, this));
        Game.add(new Ball(774, 293, ballSize, ballMass, color(247, 143, 30), 7, this));
        Game.add(new Ball(774, 321, ballSize, ballMass, color(238, 68, 40), 11, this));
        Game.add(new Ball(774, 349, ballSize, ballMass, color(247, 217, 69), 9, this));

        Game.add(new Ball(748, 280, ballSize, ballMass, color(25, 85, 199), 10, this));
        Game.add(new Ball(748, 308, ballSize, ballMass, color(29, 14, 9), 8, this));
        Game.add(new Ball(748, 336, ballSize, ballMass, color(92, 13, 172), 12, this));

        Game.add(new Ball(722, 295, ballSize, ballMass, color(225, 25, 193), 13, this));
        Game.add(new Ball(722, 323, ballSize, ballMass, color(78, 149, 43), 14, this));

        Game.add(new Ball(696, 310, ballSize, ballMass, color(243, 204, 48), 1, this));*/

        Ball ball = new Ball(200, 310, 14, 11, color(255), -1, this);
        Game.add(ball);

        Game.cueHitListener = new CueHitThread(ball);
        Game.cueHitListener.start();

        Game.turnListener = new TurnThread();
        Game.turnListener.start();
    }

    @Override
    public void draw() {
        clear();

        noStroke();

        fill(0, 255, 0);
        rect(100, 100, width - 200, height - 200);
        rect(80, 80, 52, 52);
        rect(931, 469, 52, 52);
        rect(80, 469, 52, 52);
        rect(931, 80, 52, 52);

        Game.render();
        for (int i = 0; i < 10; i++)
            Game.tick();
        fill(0, 0, 255);
        rect(mouseX - 5, mouseY - 5, 10, 10);

        textSize(15);
        fill(25);

        if (ballsLeft == 0)
            Game.disableCue();
    }

    public void closeMatch() {
        Game.winner = null;
        parent.setVisible(true);
    }

    @Override
    public void keyPressed() {
        if (!Game.keyState(keyCode))
            Game.keyState(keyCode, true);
    }

    @Override
    public void keyReleased() {
        Game.keyState(keyCode, false);
    }

    @Override
    public void mousePressed() {
        Game.mouseState(mouseX, mouseY, mouseButton, true);
    }

    @Override
    public void mouseDragged() {
        Game.mousePosition(mouseX, mouseY);
    }

    @Override
    public void mouseMoved() {
        Game.mousePosition(mouseX, mouseY);
    }

    @Override
    public void mouseReleased() {
        Game.mouseState(mouseX, mouseY, mouseButton, false);
    }
}
