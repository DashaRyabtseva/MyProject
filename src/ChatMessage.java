import java.util.Date;

/**
 * Created by Dasha on 13.02.2016.
 */
public class ChatMessage {
    private int id;
    private String message;
    private String author;
    private Date timeStamp;

    public ChatMessage (int i, String m, String a, Date t) {
        id = i;
        message = m;
        author = a;
        timeStamp = t;
    }
    public String toString() {
        return author+": "+message+"   "+timeStamp+"("+timeStamp.getTime()+ ")    "+id;
    }
    public String getAuthor () {
        return author;
    }
    public String getMessage () {
        return message;
    }
    public Date getTimeStamp () {
        return timeStamp;
    }
    public int getId () {
        return id;
    }
}
