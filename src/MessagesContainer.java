import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class MessagesContainer extends ArrayList<ChatMessage> {
    private FileWriter fw;

    public MessagesContainer() throws IOException {
        super();
        fw = new FileWriter("logfile.txt");
        // fw = new FileWriter("D:\\Универ 4 сем\\Chat\\logfile.txt", true);
    }

    public MessagesContainer(ArrayList<ChatMessage> cm) {
        for (ChatMessage aCm : cm) this.add(aCm);
    }

    public FileWriter getFW() {
        return fw;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ChatMessage chatMessage : this) {
            sb.append(chatMessage).append("\n");
        }
        return sb.toString();
    }

    public void saveHistory() throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String json = gson.toJson(this);
        FileWriter forHistiory = new FileWriter("history.txt");
        // FileWriter forHistiory = new FileWriter("history"+new Date()+".txt");
        forHistiory.write(json);
        forHistiory.close();
        fw.write(new Date() + ": History saved.\n");
    }

    public MessagesContainer uploadHistory(String fileName) {
        Scanner sc = new Scanner(fileName);
        String str = sc.nextLine();
        return new Gson().fromJson(str, MessagesContainer.class);
    }

    public void deteteMessageById(String id) throws IOException {
        Iterator<ChatMessage> it = this.iterator();
        int cul = 0;
        while (it.hasNext()) {
            ChatMessage wow = it.next();
            if (wow.getId().toString().equals(id)) {
                this.remove(wow);
                cul++;
                break;
            }
        }
        if (cul == 1)
            fw.write(new Date() + ": Delete 1 message, id " + id + "\n");
        else
            fw.write(new Date() + ": No message with id " + id + "\n");
    }

    public ChatMessage newMessage(String userName) throws IOException {
        Scanner in = new Scanner(System.in);
        Date now = new Date();
        UUID newId = UUID.randomUUID();
        System.out.print("Enter message: ");
        ChatMessage newMes = new ChatMessage(newId, in.nextLine(), userName, now);
        add(newMes);
        if (newMes.getMessage().length() > 140)
            fw.write(new Date() + ": Message this id " + newMes.getId() + " is too long\n");
        return newMes;
    }

    public MessagesContainer searchByAuthor(String needAuthor) throws IOException {
        Iterator<ChatMessage> it = this.iterator();
        ArrayList<ChatMessage> ret = new ArrayList<>();
        while (it.hasNext()) {
            ChatMessage wow = it.next();
            if (wow.getAuthor().equals(needAuthor)) {
                ret.add(wow);
            }
        }
        fw.write(new Date() + ": Search by author '" + needAuthor + "'. Found " + ret.size() + " message/s." + "\n");
        return new MessagesContainer(ret);
    }

    public MessagesContainer searchByWord(String needWord) throws IOException {
        Iterator<ChatMessage> it = this.iterator();
        ArrayList<ChatMessage> ret = new ArrayList<>();
        while (it.hasNext()) {
            ChatMessage wow = it.next();
            if (wow.getMessage().contains(needWord))
                ret.add(wow);
        }
        fw.write(new Date() + ": Search by word '" + needWord + "'. Found " + ret.size() + " message/s." + "\n");
        return new MessagesContainer(ret);
    }

    public MessagesContainer searchByRegularEx(String needRegularEx) throws IOException {
        Iterator<ChatMessage> it = this.iterator();
        ArrayList<ChatMessage> ret = new ArrayList<>();
        while (it.hasNext()) {
            ChatMessage wow = it.next();
            Pattern p = Pattern.compile(needRegularEx);
            Matcher m = p.matcher(wow.getMessage());
            if (m.matches())
                ret.add(wow);
        }
        fw.write(new Date() + ": Search by regular ex '" + needRegularEx + "'. Found " + ret.size() + " message/s." + "\n");
        return new MessagesContainer(ret);
    }

    public MessagesContainer searchByTime(Date begin, Date end) throws IOException {
        Iterator<ChatMessage> it = this.iterator();
        ArrayList<ChatMessage> ret = new ArrayList<>();
        while (it.hasNext()) {
            ChatMessage wow = it.next();
            if (wow.getTimeStamp().getTime() >= begin.getTime() && wow.getTimeStamp().getTime() <= end.getTime())
                ret.add(wow);
        }
        fw.write(new Date() + ": Search by time from '" + begin + "' before '" + end + "'. Found " + ret.size() + " message/s." + "\n");
        return new MessagesContainer(ret);
    }

    public void forSearchByTime(Scanner cs) throws IOException {
        cs.nextLine();
        while (true) {
            try {
                System.out.print("Enter start in formart MM.dd.yyyy HH:mm:ss: ");
                SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy HH:mm:ss");
                sdf.setLenient(false);
                Date start = sdf.parse(cs.nextLine());
                System.out.print("Enter end in formart MM.dd.yyyy HH:mm:ss: ");
                Date end = sdf.parse(cs.nextLine());
                System.out.println(searchByTime(start, end).toString());
                break;
            } catch (ParseException e) {
                System.out.println("Wrong date. Try again");
                fw.write(new Date() + ": Incorrect formart of date.\n");
            }
        }
    }
}