package threads;

import game.Game;
import game.Tools;
import net.Cast;
import game.objects.Player;

public class TurnThread implements Runnable {

    public Thread thread;

    public TurnThread() {
        thread = new Thread(this);
    }

    public void start() {
        thread.start();
    }

    public void run() {
        boolean isMyTurn = false;
        while (!isMyTurn) {
            byte[] result = Cast.receive(Game.turnPort, Game.group);

            String user = Tools.bytesToString(result);

            for (Player p : Game.players)
                if (p.user.equals(user)) {
                    Game.currentPlayer = p;
                    break;
                }

            if (user.equals(Game.me.user))
                isMyTurn = true;
        }
        Game.enableCue();
    }
}
