package net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Cast {

    public static final String BROADCAST = "255.255.255.255";
    private static String lastAddr = "";

    public synchronized static String getLastAddr() {
        return lastAddr;
    }

    public static void send(byte[] data, String address, int port) {
        try {
            InetAddress addr = InetAddress.getByName(address);
            DatagramPacket pkg = new DatagramPacket(data, data.length, addr, port);
            DatagramSocket ds = new DatagramSocket();
            ds.send(pkg);
            ds.close();
        } catch (Exception e) {
            System.out.println("Cast Error: " + e.getMessage());
        }
    }

    public static byte[] receive(int port, String address) {
        try {
            MulticastSocket mcs = new MulticastSocket(port);

            if (address != BROADCAST) {
                InetAddress grp = InetAddress.getByName(address);
                mcs.joinGroup(grp);
            }

            byte rec[] = new byte[256];
            DatagramPacket pkg = new DatagramPacket(rec, rec.length);
            mcs.receive(pkg);
            lastAddr = pkg.getAddress().toString().substring(1);
            mcs.close();
            return pkg.getData();
        } catch (Exception e) {
            System.out.println("Cast Error: " + e.getMessage());
        }
        return null;
    }
}