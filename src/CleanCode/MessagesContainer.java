package CleanCode;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class MessagesContainer {
    private ArrayList<ChatMessage> cm;
    private FileWriter forLog;
    private MessagesSearcher searcher;

    public MessagesContainer() {
        cm = new ArrayList<>();
        searcher = new MessagesSearcher();
        try {
            forLog = new FileWriter("logfile"+ new Date().getTime()+".txt");
        } catch (IOException e) {
            System.out.println("File for log not created.");
        }
    }

    public MessagesContainer(ArrayList<ChatMessage> hi) {
        cm = new ArrayList<>();
        cm.addAll(hi.stream().collect(Collectors.toList()));
    }
    public ArrayList<ChatMessage> getCM () {
        return cm;
    }
    public FileWriter getFW() {
        return forLog;
    }
    public MessagesSearcher getSearcher () {
        return searcher;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ChatMessage chatMessage : cm) {
            sb.append(chatMessage).append("\n");
        }
        return sb.toString();
    }

    public void saveHistory() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String json = gson.toJson(cm);
        FileWriter forHistiory = null;
        try {
            forHistiory = new FileWriter("history.txt", true);
            forHistiory.write(json);
            forHistiory.close();
            forLog.write(new Date() + ": History saved.\n");
        } catch (IOException e) {
            System.out.println("File for history not created.");
        }
    }

    public MessagesContainer uploadHistory(String fileName) {
        Scanner sc = new Scanner(fileName);
        String str = sc.nextLine();
        sc.close();
        return new Gson().fromJson(str, MessagesContainer.class);
    }

    public void deteteMessageById(String id) {
        Iterator<ChatMessage> it = cm.iterator();
        int cul = 0;
        while (it.hasNext()) {
            ChatMessage wow = it.next();
            if (wow.getId().toString().equals(id)) {
                cm.remove(wow);
                cul++;
                break;
            }
        }
        try {
            if (cul == 1)
                forLog.write(new Date() + ": Delete 1 message, id " + id + "\n");
            else
                forLog.write(new Date() + ": No message with id " + id + "\n");
        } catch (IOException e) {
            System.out.println(new Date() + ": Trouble with write file");
        }
    }

    public ChatMessage newMessage(String userName) {
        Scanner in = new Scanner(System.in);
        Date now = new Date();
        UUID newId = UUID.randomUUID();
        System.out.print("Enter message: ");
        ChatMessage newMes = new ChatMessage(newId, in.nextLine(), userName, now);
        cm.add(newMes);
        if (newMes.getMessage().length() > 140)
            try {
                forLog.write(new Date() + ": Message this id " + newMes.getId() + " is too long\n");
            } catch (IOException e) {
                System.out.println(new Date() + ": Trouble with write file");
            }
        return newMes;
    }
}