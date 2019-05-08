package threads;

import model.Match;
import net.SocketControl;

public class MatchList extends Request implements Runnable {

    public MatchList(int port, long start) {
        super(port, start);
        thread = new Thread(this);
    }

    @Override
    public void run() {
        showElapsedTime("Match List");
        SocketControl control = new SocketControl(port);

        String user = control.receiveUTF();

        System.out.println(" from " + user + "@" + control.getAddr());

        control.sendUTF(Match.JSONArray());
        control.close();
    }
}
