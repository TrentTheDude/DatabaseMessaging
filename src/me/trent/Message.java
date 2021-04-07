package me.trent;

public class Message {

    private String sourceID;
    private String destinationID;
    private String message;

    public Message(String sourceID, String destinationID, String message){
        this.sourceID = sourceID;
        this.destinationID = destinationID;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getDestinationID() {
        return destinationID;
    }

    public String getSourceID() {
        return sourceID;
    }
}
