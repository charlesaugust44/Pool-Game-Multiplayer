package model;

import game.objects.Player;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class Match {

    public static ArrayList<Match> matches = new ArrayList<>();
    private static boolean[] ids = new boolean[255];

    private String groupIp, status;
    private int maxPlayers, totalPlayers, id;
    private ArrayList<Player> players;

    public Match(String groupIp, int totalPlayers, int maxPlayers, String status, int id) {
        this.groupIp = groupIp;
        this.status = status;
        this.maxPlayers = maxPlayers;
        this.totalPlayers = totalPlayers;
        players = new ArrayList<>();
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static int groupAdd() {
        int i;
        for (i = 1; i < 256; i++)
            if (ids[i] == false)
                break;
        ids[i] = true;
        String group = "224.0.0." + (i + 1);
        matches.add(new Match(group, 1, 4, MatchStatus.WAITING, i + 1));
        return i + 1;
    }

    public static void groupRemove(String groupIp) {
        for (Match m : matches)
            if (m.groupIp.equals(groupIp)) {
                ids[m.id - 1] = false;
                matches.remove(m);
            }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public String getGroupIp() {
        return groupIp;
    }

    public void setGroupIp(String groupIp) {
        this.groupIp = groupIp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getTotalPlayers() {
        return totalPlayers;
    }

    public void setTotalPlayers(int totalPlayers) {
        this.totalPlayers = totalPlayers;
    }

    public String JSONstringfy() {
        String json = "{"
                + "groupIp:\"" + groupIp + "\","
                + "status:\"" + status + "\","
                + "maxPlayers:" + maxPlayers + ","
                + "totalPlayers:" + totalPlayers + ","
                + "id:" + id + ","
                + "players: [";

        for (Player p : players)
            json += p.JSONStringfy() + ",";

        json = json.substring(0, json.length() - 1);
        json += "]}";
        return json;
    }

    public static String JSONArray() {
        String json = "[";

        for (Match m : matches)
            json += m.JSONstringfy() + ",";

        json = json.substring(0, json.length() - 1);
        json += "]";
        return json;
    }

    public static void JSONArrayParse(String source) {
        matches.clear();
        JSONArray array = new JSONArray(source);

        for (int i = 0; i < array.length(); i++) {
            JSONObject matchObj = array.getJSONObject(i);
            JSONArray playerArray = matchObj.getJSONArray("players");

            Match m = new Match(matchObj.getString("groupIp"), matchObj.getInt("totalPlayers"), matchObj.getInt("maxPlayers"), matchObj.getString("status"), matchObj.getInt("id"));

            for (int j = 0; j < playerArray.length(); j++) {
                JSONObject playerObj = playerArray.getJSONObject(j);
                m.addPlayer(new Player(playerObj.getString("user"), playerObj.getInt("totalWon"), playerObj.getInt("totalLost"), playerObj.getInt("totalPoints")));
            }
            matches.add(m);
        }
    }
}
