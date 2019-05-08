package threads;

import java.util.ArrayList;
import java.util.Random;
import net.Addr;
import net.SocketControl;

public class Dispatcher implements Runnable {

    public Thread thread;
    public static ArrayList<Integer> portsInUse;

    public Dispatcher() {
        thread = new Thread(this);
        portsInUse = new ArrayList<>();
    }

    public void start() {
        thread.start();
    }

    @Override
    public void run() {
        System.out.println("Dispatcher Start...");
        while (true) {
            SocketControl s = new SocketControl(Addr.INCOMING);
            long start = System.nanoTime();

            int request = s.receiveInt();
            Integer port;

            do
                port = new Random().nextInt(60000) + 10000;
            while (port >= 65535 || s.portInUse(port));

            s.sendInt(port);
            s.close();

            switch (request) {
                case Request.LOGIN:
                    Login login = new Login(port, start);
                    login.start();
                    break;
                case Request.SIGNUP:
                    Signup signup = new Signup(port, start);
                    signup.start();
                    break;
                case Request.MATCH_LIST:
                    MatchList matchList = new MatchList(port, start);
                    matchList.start();
                    break;
                case Request.CREATE_MATCH:
                    CreateMatch createMatch = new CreateMatch(port, start);
                    createMatch.start();
                    break;
                case Request.LEAVE_MATCH:
                    LeaveMatch leaveMatch = new LeaveMatch(port, start);
                    leaveMatch.start();
                    break;
                case Request.ENTER_MATCH:
                    EnterMatch enterMatch = new EnterMatch(port, start);
                    enterMatch.start();
                    break;
                case Request.END_MATCH:
                    EndMatch endMatch = new EndMatch(port, start);
                    endMatch.start();
                    break;
            }

        }
    }

}
