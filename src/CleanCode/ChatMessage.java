package CleanCode;

import java.util.Date;
import java.util.UUID;


public class ChatMessage {
    private UUID id;
    private String message;
    private String author;
    private Date timeStamp;

    public ChatMessage(UUID i, String m, String a, Date t) {
        id = i;
        message = m;
        author = a;
        timeStamp = t;
    }

    public String toString() {
        return author + ": " + message + "   " + timeStamp + "    " + id;
    }

    public String getAuthor() {
        return author;
    }

    public String getMessage() {

        return message;
    }

    public Date getTimeStamp() {

        return timeStamp;
    }

    public UUID getId() {

        return id;
    }
}
