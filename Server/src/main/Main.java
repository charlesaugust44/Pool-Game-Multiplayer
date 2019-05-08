package main;


import persistence.FileManager;
import threads.Dispatcher;

public class Main {

    public static FileManager manager = new FileManager("players.txt");

    public static void main(String[] args) {
        System.out.println("Starting server...");
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.start();
    }
}
