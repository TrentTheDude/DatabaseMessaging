package me.trent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Messenger {

    public static DB db;

    public static String messengerID;
    private MessengerThread messengerThread;
    public static List<Message> receivedMessages = new ArrayList<>();
    public static List<Message> messagesToSend = new ArrayList<>();

    public static boolean connected = false;

    public Messenger(String messengerID){
        this.messengerID = messengerID;
    }

    public boolean connect(String dbIP, String dbUser, String dbPass, String dbDatabase){
        db = new DB(dbIP, dbDatabase, dbUser, dbPass);
        if (getDatabase().start()){
            //connected to the database...
            this.messengerThread = new MessengerThread();
            this.messengerThread.start(); // start the thread since we have a database connection...
            connected = true;
            return true;
        }
        return false;
    }

    public void disconnect(){
        this.messengerThread.interrupt(); // stop thread
        try {
            getDatabase().connection.close();
        }catch(SQLException e){}
    }

    public void sendMessage(String source, String destination, String message){
        messagesToSend.add(new Message(source, destination, message));
    }

    public void sendMessage(String destination, String message){
        sendMessage(getMessengerID(), destination, message);
    }

    public List<Message> readMessages(){
        List<Message> receviedMessages2 = new ArrayList<>();
        receviedMessages2.addAll(receivedMessages);
        receivedMessages.clear(); // wipe all of the receivedMessages...
        return receviedMessages2; // return all of the current messages we've received...
    }

    public static DB getDatabase() {
        return db;
    }

    public String getMessengerID() {
        return messengerID;
    }
}
