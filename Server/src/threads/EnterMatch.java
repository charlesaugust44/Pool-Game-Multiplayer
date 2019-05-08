package threads;

import main.Main;
import model.Match;
import model.Player;
import net.SocketControl;

public class EnterMatch extends Request implements Runnable {

    public EnterMatch(int port, long start) {
        super(port, start);
        thread = new Thread(this);
    }

    @Override
    public void run() {
        showElapsedTime("Match Enter");
        SocketControl control = new SocketControl(port);

        String user = control.receiveUTF();
        String id = control.receiveUTF();

        System.out.println(" from " + user + "@" + control.getAddr());
        Player p = Main.manager.searchEntryByUser(user);
        boolean added = false;

        for (Match m : Match.matches)
            if (m.getGroupIp().equals(id)) {
                m.addPlayer(p);
                added = true;
                break;
            }

        if (added)
            control.sendInt(Response.ENTERED);
        else
            control.sendInt(Response.MATCH_DOES_NOT_EXIST);

        control.close();
    }
}
