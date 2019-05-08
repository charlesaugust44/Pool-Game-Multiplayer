package model;

import org.json.JSONObject;

public class Player {

    private String user, password;
    private int totalWon, totalLost, totalPoints;

    public Player(String jsonData) {
        JSONparse(jsonData);
    }

    public Player(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public Player(String user, String password, int totalWon, int totalLost, int totalPoints) {
        this.user = user;
        this.password = password;
        this.totalWon = totalWon;
        this.totalLost = totalLost;
        this.totalPoints = totalPoints;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTotalWon() {
        return totalWon;
    }

    public void setTotalWon(int totalWon) {
        this.totalWon = totalWon;
    }

    public int getTotalLost() {
        return totalLost;
    }

    public void setTotalLost(int totalLost) {
        this.totalLost = totalLost;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public String JSONStringfy(boolean withPass) {
        return "{"
                + "user:\"" + user + "\"," + ((withPass) ? ("password:\"" + password + "\",") : "")
                + "totalWon: " + totalWon + ","
                + "totalPoints: " + totalPoints + ","
                + "totalLost: " + totalLost + "}";
    }

    public String JSONStringfy() {
        return JSONStringfy(false);
    }

    private void JSONparse(String source) {
        JSONObject obj = new JSONObject(source);

        this.user = obj.getString("user");
        this.password = obj.getString("password");
        this.totalWon = obj.getInt("totalWon");
        this.totalPoints = obj.getInt("totalPoints");
        this.totalLost = obj.getInt("totalLost");
    }
}
