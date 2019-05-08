package net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketControl {

    private Socket client;
    private ServerSocket server;
    private DataOutputStream ouput;
    private DataInputStream input;

    public SocketControl(String addr, int port) {
        try {
            client = new Socket(addr, port);
            getInOut();
        } catch (Exception e) {
            System.err.println("Error: client connection refused");
        }
    }

    public SocketControl(int port) {
        try {
            server = new ServerSocket(port);
            this.client = server.accept();
            getInOut();
        } catch (Exception e) {
            System.err.println("Error: server wait error");
        }
    }

    public SocketControl(int port, int timeout) {
        try {
            server = new ServerSocket(port);
            if (timeout != -1)
                server.setSoTimeout(timeout);
            this.client = server.accept();
            getInOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasServerSocket() {
        if (server == null)
            return false;
        return true;
    }

    public boolean hasClientSocket() {
        if (client == null)
            return false;
        return true;
    }

    public void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void waitResponse() {
        try {
            while (input.available() == 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getInOut() {
        try {
            ouput = new DataOutputStream(client.getOutputStream());
            input = new DataInputStream(client.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAddr() {
        return client.getInetAddress().getHostAddress();
    }

    public String receiveUTF() {
        try {
            return input.readUTF();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendUTF(String data) {
        try {
            ouput.writeUTF(data);
            ouput.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int receiveInt() {
        try {
            return input.readInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void sendInt(int data) {
        try {
            ouput.writeInt(data);
            ouput.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            input.close();
            ouput.close();
            client.close();
            if (server != null)
                server.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean portInUse(int port) {
        try {
            ServerSocket s = new ServerSocket(port);
            return false;
        } catch (IOException e) {
            return true;
        }
    }
}
