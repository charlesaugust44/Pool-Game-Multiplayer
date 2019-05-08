package threads;

import main.Main;
import model.Player;
import net.SocketControl;

public class Signup extends Request implements Runnable {

    public Signup(int port, long start) {
        super(port, start);
        thread = new Thread(this);
    }

    @Override
    public void run() {
        showElapsedTime("Signup");

        SocketControl control = new SocketControl(port);

        System.out.println(" from " + control.getAddr());
        String user = control.receiveUTF();
        String pass = control.receiveUTF();

        if (Main.manager.searchEntryByUser(user) != null) {
            control.sendInt(Response.EXISTS);
            return;
        }

        Player p = new Player(user, pass);
        Main.manager.insert(p);

        control.sendInt(Response.CREATED);
    }
}
