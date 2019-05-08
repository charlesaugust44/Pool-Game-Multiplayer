package threads;

import model.Match;
import net.SocketControl;

public class LeaveMatch extends Request implements Runnable {

    public LeaveMatch(int port, long start) {
        super(port, start);
        thread = new Thread(this);
    }

    @Override
    public void run() {
        showElapsedTime("Match Leave");
        SocketControl control = new SocketControl(port);

        String user = control.receiveUTF();
        int id = control.receiveInt();

        System.out.println(" from " + user + "@" + control.getAddr());

        for (Match m : Match.matches)
            if (m.getId() == id) {
                m.removePlayer(user);
                if (m.getPlayers().isEmpty())
                    Match.groupRemove(m.getGroupIp());
                break;
            }

        control.sendInt(Response.LEFT);
        control.close();
    }
}
