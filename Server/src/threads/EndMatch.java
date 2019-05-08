package threads;

import main.Main;
import model.Player;
import net.SocketControl;
import org.json.JSONArray;
import org.json.JSONObject;

public class EndMatch extends Request implements Runnable {

    public EndMatch(int port, long start) {
        super(port, start);
        thread = new Thread(this);
    }

    @Override
    public void run() {
        showElapsedTime("Match end");
        SocketControl control = new SocketControl(port);

        String result = control.receiveUTF();

        System.out.println(" from " + control.getAddr());

        control.close();

        JSONArray array = new JSONArray(result);

        for (int i = 0; i < array.length(); i++) {
            JSONObject player = array.getJSONObject(i);

            Player p = Main.manager.searchEntryByUser(player.getString("user"));

            p.setTotalLost(player.getInt("totalLost"));
            p.setTotalWon(player.getInt("totalWon"));
            p.setTotalPoints(player.getInt("totalPoints"));

            Main.manager.deleteEntry(p.getUser());
            Main.manager.insert(p);
        }

    }
}
