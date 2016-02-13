import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Dasha on 13.02.2016.
 */
public class Demo {
    public static void main(String[] args)  {
        MessagesContainer ms = new MessagesContainer();
        int ok = 1;
        while (ok == 1) {
            System.out.print("Enter Username: ");
            Scanner cs = new Scanner(System.in);
            String us = cs.next();
            ChatMessage f = ms.newMessage(us);
            System.out.println(f.toString());
            System.out.println("One more message? 1 - yes, 0 - no");
            ok = cs.nextInt();
        }
        try {
            ms.saveHistory();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
