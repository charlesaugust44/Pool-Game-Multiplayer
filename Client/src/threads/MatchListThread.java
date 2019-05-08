package threads;

import game.Game;
import main.MatchList;
import main.MatchRoom;
import net.Addr;
import net.Request;
import net.SocketControl;
import model.Match;

public class MatchListThread implements Runnable {

    private Thread thread;
    private MatchList matchListFrame;
    private MatchRoom matchRoomFrame;

    public MatchListThread(MatchList matchListFrame) {
        this.thread = new Thread(this);
        this.matchListFrame = matchListFrame;
    }

    public void setMatchRoom(MatchRoom matchRoom) {
        this.matchRoomFrame = matchRoom;
    }

    public void start() {
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            SocketControl control = new SocketControl(Addr.SERVER_ADDRESS, Addr.SERVER_PORT);

            control.sendInt(Request.MATCH_LIST);
            control.waitResponse();

            int port = control.receiveInt();

            control.close();
            control.sleep(50);

            control = new SocketControl(Addr.SERVER_ADDRESS, port);
            
            if (control.hasClientSocket()) {
                control.sendUTF(Game.me.user);
                control.waitResponse();

                String list = control.receiveUTF();
                control.close();

                Match.JSONArrayParse(list);
                matchListFrame.updateModel();
                if (matchRoomFrame != null)
                    matchRoomFrame.updateModel();
            }
            
            control.sleep(1000);
        }
    }

}
