package me.trent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MessengerThread extends Thread{

    public void run(){

        while(true) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                break;
            }

            if (!Messenger.connected){
                return;
            }

            //send messages to the database...
            for (int i = Messenger.messagesToSend.size() - 1; i >= 0; i--) {
                Message message = Messenger.messagesToSend.get(i);
                Messenger.getDatabase().setColumn("messages", "message", message.getMessage(), null, null);
                Messenger.getDatabase().setColumn("messages", "source", message.getSourceID(), "message", message.getMessage());
                Messenger.getDatabase().setColumn("messages", "destination", message.getDestinationID(), "message", message.getMessage());
            }
            Messenger.messagesToSend.clear(); // wipe the messages to send after we send them out...

            //read messages from the database...
            try {
                Statement statement = Messenger.getDatabase().connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM messages WHERE destination='"+Messenger.messengerID+"'");

                while (resultSet.next()){
                    /*
                    1 = source
                    2 = destination
                    3 = message
                     */
                    String source = resultSet.getString(1);
                    String destination = resultSet.getString(2);
                    String message_rec = resultSet.getString(3);

                    Messenger.receivedMessages.add(new Message(source, destination, message_rec));
                    Messenger.getDatabase().removeRow("messages", "message", message_rec);

                }

            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
}
