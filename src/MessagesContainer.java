import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Dasha on 13.02.2016.
 */
public class MessagesContainer extends ArrayList<ChatMessage> {
    public MessagesContainer() {
        super();
    }
    public MessagesContainer (ChatMessage e) {}

    public String toString () {
        StringBuilder sb = new StringBuilder();
        Iterator<ChatMessage> it = this.iterator();
        while(it.hasNext()){
            sb.append(it.next() + "\n");
        }
        return sb.toString();
    }
    public void saveHistory () throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String json = gson.toJson(this);
        FileWriter fw = new FileWriter("D:\\Универ 4 сем\\Chat\\history.txt");
        fw.write(json);
        fw.flush();
        fw.close();
    }
    public MessagesContainer uploadHistory (String fileName) {
        Scanner sc = new Scanner(fileName);
        String str = sc.nextLine();
        MessagesContainer h = new Gson().fromJson(str, MessagesContainer.class);
        return h;
    }
    public void deteteMessageById (int id) {
        Iterator<ChatMessage> it = this.iterator();
        while(it.hasNext()){
            ChatMessage wow = it.next();
            if (wow.getId() == id) {
                this.remove(wow);
                break;
            }
        }
    }
    public ChatMessage newMessage (String UserName) {
        Scanner in = new Scanner(System.in);
        Date now = new Date();
        Random newId = new Random();
        System.out.print("Enter message: ");
        ChatMessage newMes = new ChatMessage (newId.nextInt(2147483647), in.nextLine(), UserName, now);
        add(newMes);
        return newMes;
    }
    public ArrayList<ChatMessage> searchByAuthor (String needAuthor) {
        Iterator<ChatMessage> it = this.iterator();
        ArrayList<ChatMessage> ret = new ArrayList<> ();
        while(it.hasNext()){
            ChatMessage wow = it.next();
            if (wow.getAuthor().equals(needAuthor))
                ret.add(wow);
        }
        return ret;
    }
    public ArrayList<ChatMessage> searchByWord (String needWord) {
        Iterator<ChatMessage> it = this.iterator();
        ArrayList<ChatMessage> ret = new ArrayList<> ();
        while(it.hasNext()){
            ChatMessage wow = it.next();
            if (wow.getMessage().indexOf(needWord)>=0)
                ret.add(wow);
        }
        return ret;
    }
    public ArrayList<ChatMessage> searchByRegularEx (String needRegularEx) {
        Iterator<ChatMessage> it = this.iterator();
        ArrayList<ChatMessage> ret = new ArrayList<> ();
        while(it.hasNext()){
            ChatMessage wow = it.next();
            Pattern p = Pattern.compile(needRegularEx);
            Matcher m = p.matcher(wow.getMessage());
            if (m.matches())
                ret.add(wow);
        }
        return ret;
    }
    public ArrayList<ChatMessage> searchByTime (Date begin, Date end) {
        Iterator<ChatMessage> it = this.iterator();
        ArrayList<ChatMessage> ret = new ArrayList<> ();
        while(it.hasNext()){
            ChatMessage wow = it.next();
            if (wow.getTimeStamp().after(begin) && wow.getTimeStamp().before(end))
                ret.add(wow);
        }
        return ret;
    }
}