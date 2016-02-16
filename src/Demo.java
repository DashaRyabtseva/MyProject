import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

public class Demo {
    public static void main(String[] args)  {
        try {
            MessagesContainer ms = new MessagesContainer();
            Scanner cs = new Scanner(System.in);
            int numberOfCommand = 0;
            while (numberOfCommand != 10) {
                System.out.println("What you want?\n " +
                        "1 - new message;\n " +
                        "2 - save history;\n " +
                        "3 - upload history;\n " +
                        "4 - show history;\n " +
                        "5 - delete message by id;\n " +
                        "6 - search messages by autor;\n " +
                        "7 - search messages by word;\n " +
                        "8 - search messages by regular ex;\n " +
                        "9 - show history by date;\n " +
                        "10 - exit.\n");
                numberOfCommand = cs.nextInt();
                switch (numberOfCommand) {
                    case 1: {
                        int ok = 1;
                        while (ok == 1) {
                            System.out.print("Enter Username: ");
                            String us = cs.next();
                            ChatMessage f = ms.newMessage(us);
                            System.out.println(f.toString());
                            System.out.println("One more message? 1 - yes, 0 - no");
                            ok = cs.nextInt();
                        }
                        break;
                    }
                    case 2: {
                        ms.saveHistory();
                        break;
                    }
                    case 3: {
                        ms.uploadHistory("history.txt");
                        break;
                    }
                    case 4: {
                        System.out.println (ms.toString());
                        break;
                    }
                    case 5: {
                        System.out.print ("Enter id: ");
                        ms.deteteMessageById(cs.nextInt());
                        break;
                    }
                    case 6: {
                        System.out.print ("Enter autor: ");
                        System.out.println (ms.searchByAuthor(cs.next()).toString());
                        break;
                    }
                    case 7: {
                        System.out.print ("Enter word: ");
                        System.out.println (ms.searchByWord(cs.next()).toString());
                        break;
                    }
                    case 8: {
                        System.out.print ("Enter regular ex: ");
                        System.out.println (ms.searchByRegularEx(cs.next()).toString());
                        break;
                    }
                    case 9: {
                        System.out.print ("Enter start in milliseconds: ");
                        Date start = new Date (cs.nextLong());
                        System.out.print ("Enter end in milliseconds: ");
                        Date end = new Date (cs.nextLong());
                        System.out.println (ms.searchByTime(start, end).toString());
                        break;
                    }
                    case 10: {
                        System.out.println("Goodbye!");
                        break;
                    }
                    default: {
                        System.out.println ("Enter number from 1 to 10!");
                    }
                }
            }
                ms.getFW().close();
        }
        catch (IOException e) {
            System.out.println ("File not found!");
        }
        System.out.printf("well well");
    }
}