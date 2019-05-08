package threads;

import main.Main;
import model.Player;
import net.SocketControl;

public class Login extends Request implements Runnable {

    public Login(int port, long start) {
        super(port, start);
        thread = new Thread(this);
    }

    @Override
    public void run() {
        showElapsedTime("Login");
        SocketControl control = new SocketControl(port);
        
        String user = control.receiveUTF();
        String pass = control.receiveUTF();

        System.out.println(" from " + user + "@" + control.getAddr());

        Player p = Main.manager.searchEntryByUser(user);

        if (p != null && p.getPassword().equals(pass))
            control.sendInt(Response.AUTHORIZED);
        else
            control.sendInt(Response.UNAUTHORIZED);

        control.close();
    }

}
