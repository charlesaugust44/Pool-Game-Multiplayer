package game.objects;

import game.Game;
import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PVector;

public class Player extends GameObject {

    public ArrayList<Ball> ballsIn, ballsInNow;
    public String user;
    private int totalWon, totalLost, totalPoints;

    public Player(String user, PApplet parent) {
        super(0, 0, 0, 0, ID.Player, parent);
        ballsIn = new ArrayList();
        ballsInNow = new ArrayList();

        this.user = user;
    }

    public Player(String user) {
        this(user, null);
    }

    public Player(String user, int totalWon, int totalLost, int totalPoints) {
        super(0, 0, 0, 0, ID.Player, null);
        this.user = user;
        this.totalWon = totalWon;
        this.totalLost = totalLost;
        this.totalPoints = totalPoints;
    }

    public void init(PApplet parent) {
        this.parent = parent;
        ballsIn = new ArrayList();
        ballsInNow = new ArrayList();
        PVector p = Corner.get(Game.players.indexOf(this));
        this.w = 33 + this.ballsIn.size() * 13;
        this.h = 33;
        this.x = p.x;
        this.y = p.y;
    }

    public void addBall(Ball b) {
        b.playerOwner = this;
        ballsIn.add(b);
        if (this == Game.me)
            ballsInNow.add(b);
    }

    public void cleanNowPoints() {
        this.ballsInNow = new ArrayList();
    }

    @Override
    public void tick(GameObject other) {
    }

    @Override
    public void render() {
        //p.x - 10 + (other.w+5) * Game.currentPlayer.ballsIn.size();
        this.w = 33 + 31 * this.ballsIn.size();
        parent.noStroke();
        if (Game.currentPlayer == this)
            parent.fill(0, 255, 0);
        else
            parent.fill(255);
        parent.textSize(15);
        parent.text(user + (Game.me == this ? "(you)" : ""), x, y);
        parent.fill(132, 135, 259);
        parent.rect(x, y + 5, w, h);
    }

    @Override
    public boolean intersects(GameObject other) {
        return false;
    }

    @Override
    public void rightClickDown() {
    }

    @Override
    public void leftClickDown() {
    }

    @Override
    public void rightClickUp() {
    }

    @Override
    public void leftClickUp() {
    }

    @Override
    public boolean contains(PVector point) {
        return false;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getTotalWon() {
        return totalWon;
    }

    public void setTotalWon(int totalWon) {
        this.totalWon = totalWon;
    }

    public int getTotalLost() {
        return totalLost;
    }

    public void setTotalLost(int totalLost) {
        this.totalLost = totalLost;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public String JSONStringfy() {
        return "{"
                + "user:\"" + user + "\","
                + "totalWon: " + totalWon + ","
                + "totalPoints: " + totalPoints + ","
                + "totalLost: " + totalLost + "}";
    }

    public void setWinner() {
        totalWon++;
        totalPoints += ballsIn.size();
    }

    public void setLoser() {
        totalLost++;
        totalPoints += ballsIn.size();
    }

    public void setTie() {
        totalPoints += ballsIn.size();
    }
}
