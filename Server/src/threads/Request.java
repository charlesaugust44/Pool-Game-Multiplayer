package threads;

public class Request {

    public static final int LOGIN = 0,
            SIGNUP = 1,
            MATCH_LIST = 2,
            CREATE_MATCH = 3,
            LEAVE_MATCH = 4,
            ENTER_MATCH = 5,
            START_MATCH = 6,
            END_MATCH = 7;

    protected long start;
    protected int port;
    protected Thread thread;

    public Request(int port, long start) {
        this.port = port;
        this.start = start;
    }

    public void start() {
        thread.start();
    }

    protected void showElapsedTime(String name) {
        System.out.print("[ " + ((System.nanoTime() - start) / 1000000) + " ] {" + port + "} ");
        System.out.print(" " + name);
    }
}
