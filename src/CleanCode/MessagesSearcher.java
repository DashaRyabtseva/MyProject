package CleanCode;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class MessagesSearcher {

    public MessagesContainer searchByAuthor(ArrayList<ChatMessage> cm, FileWriter forLog, String needAuthor) {
        Iterator<ChatMessage> it = cm.iterator();
        ArrayList<ChatMessage> ret = new ArrayList<>();
        while (it.hasNext()) {
            ChatMessage wow = it.next();
            if (wow.getAuthor().equals(needAuthor)) {
                ret.add(wow);
            }
        }
        try {
            forLog.write(new Date() + ": Search by author '" + needAuthor + "'. Found " + ret.size() + " message/s." + "\n");
        } catch (IOException e) {
            System.out.println (new Date() + ": Trouble with write file");
        }
        return new MessagesContainer(ret);
    }

    public MessagesContainer searchByWord(ArrayList<ChatMessage> cm, FileWriter forLog,String needWord) {
        Iterator<ChatMessage> it = cm.iterator();
        ArrayList<ChatMessage> ret = new ArrayList<>();
        while (it.hasNext()) {
            ChatMessage wow = it.next();
            if (wow.getMessage().contains(needWord))
                ret.add(wow);
        }
        try {
            forLog.write(new Date() + ": Search by word '" + needWord + "'. Found " + ret.size() + " message/s." + "\n");
        } catch (IOException e) {
            System.out.println (new Date() + ": Trouble with write file");
        }
        return new MessagesContainer(ret);
    }

    public MessagesContainer searchByRegularEx(ArrayList<ChatMessage> cm, FileWriter forLog,String needRegularEx) {
        Iterator<ChatMessage> it = cm.iterator();
        ArrayList<ChatMessage> ret = new ArrayList<>();
        Pattern p;
        try {
            p = Pattern.compile(needRegularEx);
            while (it.hasNext()) {
                ChatMessage wow = it.next();
                Matcher m = p.matcher(wow.getMessage());
                if (m.matches())
                    ret.add(wow);
            }
        }
        catch (PatternSyntaxException e) {
            System.out.println("Wrong regular ex");
            try {
                forLog.write(new Date() + ": Search by regular ex '" + needRegularEx + "'. Wrong ex." + "\n");
                return new MessagesContainer(ret);
            } catch (IOException e1) {
                System.out.println (new Date() + ": Trouble with write file");
                return new MessagesContainer(ret);
            }
        }
        try {
            forLog.write(new Date() + ": Search by regular ex '" + needRegularEx + "'. Found " + ret.size() + " message/s." + "\n");
        } catch (IOException e2) {
            System.out.println(new Date() + ": Trouble with write file");
        }
        return new MessagesContainer(ret);
    }

    public MessagesContainer searchByTime(ArrayList<ChatMessage> cm, FileWriter forLog,Date begin, Date end) {
        Iterator<ChatMessage> it = cm.iterator();
        ArrayList<ChatMessage> ret = new ArrayList<>();
        while (it.hasNext()) {
            ChatMessage wow = it.next();
            if (wow.getTimeStamp().getTime() >= begin.getTime() && wow.getTimeStamp().getTime() <= end.getTime())
                ret.add(wow);
        }
        try {
            forLog.write(new Date() + ": Search by time from '" + begin + "' before '" + end + "'. Found " + ret.size() + " message/s." + "\n");
        } catch (IOException e) {
            System.out.println (new Date() + ": Trouble with write file");
        }
        return new MessagesContainer(ret);
    }

    public void forSearchByTime(ArrayList<ChatMessage> cm, FileWriter forLog,Scanner cs) {
        cs.nextLine();
        while (true) {
            try {
                System.out.print("Enter start in formart MM.dd.yyyy HH:mm:ss: ");
                SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy HH:mm:ss");
                sdf.setLenient(false);
                Date start = sdf.parse(cs.nextLine());
                System.out.print("Enter end in formart MM.dd.yyyy HH:mm:ss: ");
                Date end = sdf.parse(cs.nextLine());
                System.out.println(searchByTime(cm, forLog ,start, end).toString());
                break;
            } catch (ParseException e) {
                System.out.println("Wrong date. Try again");
                try {
                    forLog.write(new Date() + ": Incorrect formart of date.\n");
                } catch (IOException e1) {
                    System.out.println (new Date() + ": Trouble with write file");
                }
            }
        }
    }
}
