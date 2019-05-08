package model;

import java.util.ArrayList;

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

    public static int groupAdd(Player p) {
        int i;
        for (i = 1; i < 256; i++)
            if (ids[i] == false)
                break;
        ids[i] = true;
        Match newMatch = new Match("224.0.0." + i, 0, 4, MatchStatus.WAITING, i);
        newMatch.addPlayer(p);
        matches.add(newMatch);
        return i;
    }

    public synchronized static void groupRemove(String groupIp) {
        for (Match m : matches)
            if (m.groupIp.equals(groupIp)) {
                ids[m.id - 1] = false;
                matches.remove(m);
                break;
            }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
        totalPlayers++;
    }

    public synchronized void removePlayer(String player) {
        for (Player p : players)
            if (p.getUser().equals(player)) {
                players.remove(p);
                totalPlayers--;
                break;
            }
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

        String playerArray = "";

        for (Player p : players)
            playerArray += p.JSONStringfy() + ",";

        if (playerArray.length() > 0)
            playerArray = playerArray.substring(0, playerArray.length() - 1);

        json += playerArray + "]}";
        return json;
    }

    public static String JSONArray() {
        String json = "[";

        if (matches.size() == 0)
            json = "[]";
        else {
            for (Match m : matches)
                json += m.JSONstringfy() + ",";

            json = json.substring(0, json.length() - 1);
            json += "]";
        }
        return json;
    }
}
