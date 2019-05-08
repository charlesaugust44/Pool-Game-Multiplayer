package persistence;

import model.Player;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager {

    private File file;
    private String pathName;

    public FileManager(String pathName) {
        this.pathName = pathName;
        createFile();
    }

    private void createFile() {
        file = new File(pathName);

        try {
            file.createNewFile();
        } catch (IOException e) {
            System.err.println("ERROR: " + e.toString());
        }
    }

    public void insert(Player data) {
        try {
            DataOutputStream dout = new DataOutputStream(new FileOutputStream(file, true));

            dout.writeUTF(data.JSONStringfy(true));

            dout.flush();
            dout.close();
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    public ArrayList<Player> getAll() {
        ArrayList<Player> all = new ArrayList();
        try {
            DataInputStream din = new DataInputStream(new FileInputStream(file));

            while (din.available() > 0)
                all.add(new Player(din.readUTF()));

            din.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return all;
    }

    public boolean deleteFile() {
        return file.delete();
    }

    public boolean deleteEntry(String id) {
        boolean flag = false;
        ArrayList<Player> all = getAll();
        deleteFile();
        createFile();

        for (int i = 0; i < all.size(); i++)
            if (!all.get(i).getUser().equals(id))
                insert(all.get(i));
            else
                flag = true;

        return flag;
    }

    public File rename(String name) {
        pathName = pathName.substring(0, pathName.lastIndexOf('\\') + 1) + name;
        File f = new File(pathName);
        file.renameTo(f);
        return f;
    }

    public Player searchEntryByUser(String key) {
        try {
            DataInputStream din = new DataInputStream(new FileInputStream(file));

            while (din.available() > 0) {
                Player p = new Player(din.readUTF());

                if (p.getUser().equals(key))
                    return p;
            }

            din.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getPathName() {
        return pathName;
    }
}
