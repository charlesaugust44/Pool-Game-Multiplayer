package game;

import net.Cast;
import threads.CueHitThread;
import threads.TurnThread;
import game.objects.GameObject;
import game.objects.Ball;
import game.objects.ID;
import game.objects.Player;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import processing.core.PApplet;
import processing.core.PVector;
import processing.sound.SoundFile;

public class Game {

    private static LinkedList<GameObject> collisionObjects;
    private static boolean[] keys;
    private static boolean readLeftMouse, readRightMouse;

    public static Player me;
    public static ArrayList<Player> players;
    public static Player currentPlayer;
    public static SoundFile hit, cue, in;
    public static PVector mouseDownPosition, mouseUpPosition, mousePosition;
    public static boolean mouseLeft, mouseRight, cueStatus, backMoving = false;
    public static CueHitThread cueHitListener;
    public static TurnThread turnListener;
    public static float rx, ry, sx, sy;
    public static String group;
    public static int cueHitPort = 9000, turnPort = 9001;
    public static Match parent;
    public static String winner;

    public static void tick() {
        int moving = 0;

        for (int i = 0; i < collisionObjects.size(); i++) {
            GameObject obj = collisionObjects.get(i);

            if (obj.vx != 0 || obj.vy != 0)
                moving++;

            for (int j = i + 1; j < collisionObjects.size(); j++) {
                boolean collide = obj.intersects(collisionObjects.get(j));

                if (collide)
                    obj.tick(collisionObjects.get(j));
            }

            if (!readLeftMouse) {
                if (mouseLeft && obj.contains(mouseDownPosition)) {
                    obj.leftClicked = true;
                    obj.leftClickDown();
                    readLeftMouse = true;
                }
            } else if (!mouseLeft && obj.leftClicked) {
                obj.leftClicked = false;
                obj.leftClickUp();
                readLeftMouse = false;
            }

            if (!readRightMouse) {
                if (mouseRight && obj.contains(mouseDownPosition)) {
                    obj.rightClicked = true;
                    obj.rightClickDown();
                    readRightMouse = true;
                }
            } else if (!mouseRight && obj.rightClicked) {
                obj.rightClicked = false;
                obj.rightClickUp();
                readRightMouse = false;
            }

            obj.tick(null);
        }

        if ((moving == 0) && (backMoving))
            Game.ballsStopped();

        if (moving > 0)
            backMoving = true;
    }

    public static void init(Match parent) {
        Game.parent = parent;
        mouseDownPosition = new PVector(-1, -1);
        mouseUpPosition = new PVector(-1, -1);
        mousePosition = new PVector(-1, -1);
        mouseLeft = mouseRight = false;
        keys = new boolean[300];
        collisionObjects = new LinkedList<>();
        hit = new SoundFile(parent, "audio/hit.wav");
        cue = new SoundFile(parent, "audio/cue.wav");
        in = new SoundFile(parent, "audio/in.wav");
        cueStatus = true;
    }

    public static void render() {
        for (Player p : players)
            p.render();

        for (GameObject obj : collisionObjects)
            obj.render();
    }

    public static void add(GameObject object) {
        if (object.id == ID.Ball && ((Ball) object).number != -1)
            parent.ballsLeft++;

        collisionObjects.add(object);
    }

    public static void remove(GameObject object) {
        collisionObjects.remove(object);
    }

    public static boolean keyState(int key) {
        return keys[key];
    }

    public static void keyState(int key, boolean state) {
        keys[key] = state;
    }

    public static void mouseState(float x, float y, int button, boolean state) {
        if (button == parent.RIGHT)
            mouseRight = state;
        else
            mouseLeft = state;

        if (state)
            mouseDownPosition = new PVector(x, y);
        else
            mouseUpPosition = new PVector(x, y);
    }

    public static void mousePosition(float x, float y) {
        mousePosition = new PVector(x, y);
    }

    public static void disableCue() {
        cueStatus = false;
    }

    public static void enableCue() {
        cueStatus = true;
    }

    public static void sendCueHit(float ax, float ay) {
        Game.cueHitListener.toggleListener();
        byte[] buffer = Tools.merge(ax, ay);
        Cast.send(buffer, Game.group, Game.cueHitPort);
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        Game.cueHitListener.toggleListener();
    }

    public static void initPlayers(PApplet parent) {
        for (Player p : players)
            p.init(parent);
    }

    public static void ballsStopped() {
        backMoving = false;
        if (Game.me == Game.currentPlayer)
            if (Game.me.ballsInNow.size() > 0) // if has point, do again
            {
                Game.enableCue();
                Game.me.cleanNowPoints();
            } else // if missed go next
            {
                int p = Game.players.indexOf(Game.me);
                Player next;

                if (p == Game.players.size() - 1)
                    next = Game.players.get(0);
                else
                    next = Game.players.get(p + 1);

                currentPlayer = next;
                Cast.send(next.user.getBytes(), Game.group, Game.turnPort);
                Thread tlt = new Thread(Game.turnListener);
                tlt.start();
            }
    }

    public static int ballsLeft() {
        int left = 0;
        System.out.print("|");
        for (GameObject g : collisionObjects)
            if (g.id == ID.Ball && ((Ball) g).number != -1) {
                Ball b = (Ball) g;
                if (!b.out)
                    left++;
            }

        return left;
    }

    public static String getResult() {
        Player winner = players.get(0);
        int in = players.get(0).ballsIn.size();

        for (Player p : players)
            if (in != p.ballsIn.size()) {
                in = -1;
                break;
            }

        if (in != -1)
            winner = null;

        if (winner != null) {
            for (Player p : players)
                if (p.ballsIn.size() > winner.ballsIn.size())
                    winner = p;

            winner.setWinner();

            for (Player p : players)
                if (p != winner)
                    p.setLoser();

        } else {
            for (Player p : players)
                if (p != winner)
                    p.setTie();
            Game.winner = "tie";
        }

        String array = "";

        for (Player pl : Game.players)
            array += pl.JSONStringfy() + ",";

        if (array.length() > 0)
            array = array.substring(0, array.length() - 1);

        System.out.println("[" + array + "]");
        return "[" + array + "]";
    }

    public static void showResult() {
        if (winner == null)
            return;

        if (winner.equals(Game.me.user))
            JOptionPane.showMessageDialog(parent.parent, "You won the match!");
        else if ("tie".equals(winner))
            JOptionPane.showMessageDialog(parent.parent, "The match tied!");
        else if (!winner.equals(Game.me.user))
            JOptionPane.showMessageDialog(parent.parent, winner + " wins the match!");
        else
            JOptionPane.showMessageDialog(parent.parent, " ??????????????????");
    }
}
