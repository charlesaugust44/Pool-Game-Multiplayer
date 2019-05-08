package threads;

import main.Main;
import model.Match;
import model.Player;
import net.SocketControl;

public class CreateMatch extends Request implements Runnable {

    public CreateMatch(int port, long start) {
        super(port, start);
        thread = new Thread(this);
    }

    @Override
    public void run() {
        showElapsedTime("Match Create");
        
        SocketControl control = new SocketControl(port);

        String user = control.receiveUTF();
        Player p = Main.manager.searchEntryByUser(user);

        System.out.println(" from " + user + "@" + control.getAddr());

        int group = Match.groupAdd(p);

        control.sendInt(group);
        control.close();
    }
}
