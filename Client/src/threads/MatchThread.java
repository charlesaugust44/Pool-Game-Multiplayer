package threads;

import game.Game;
import game.Tools;
import main.MatchRoom;
import net.Addr;
import net.Cast;
import net.Response;

public class MatchThread implements Runnable {

    public Thread thread;
    private MatchRoom matchRoomFrame;
    private game.Match match;
    private boolean stop;

    public MatchThread(MatchRoom matchRoomFrame) {
        thread = new Thread(this);
        this.matchRoomFrame = matchRoomFrame;
        stop = false;
    }

    public void start() {
        thread.start();
    }

    public void stop() {
        stop = true;
    }

    @Override
    public void run() {
        while (true) {
            byte[] result = Cast.receive(Addr.MATCH_PORT, Game.group);

            if (stop)
                break;

            String r = Tools.bytesToString(result);
            System.err.println(r);
            if (r.equals(Response.MATCH_START))
                matchRoomFrame.startMatch();
        }
    }
}
