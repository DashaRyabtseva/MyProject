package by.bsu.up.chat.storage;

import by.bsu.up.chat.Constants;
import by.bsu.up.chat.common.models.Message;
import by.bsu.up.chat.logging.Logger;
import by.bsu.up.chat.logging.impl.Log;
import by.bsu.up.chat.utils.MessageHelper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InMemoryMessageStorage implements MessageStorage {

    private static final String DEFAULT_PERSISTENCE_FILE = "messages.srg";

    private static final Logger logger = Log.create(InMemoryMessageStorage.class);

    private List<Message> messages = new ArrayList<>();

    public InMemoryMessageStorage() {
        messages = new ArrayList<>();
        loadHistory();
    }

    @Override
    public synchronized List<Message> getPortion(Portion portion) {
        int from = portion.getFromIndex(); // начальный индекс
        if (from < 0) {
            throw new IllegalArgumentException(String.format("Portion from index %d can not be less then 0", from));
        }
        int to = portion.getToIndex(); // конечный
        if (to != -1 && to < portion.getFromIndex()) {
            throw new IllegalArgumentException(String.format("Porting last index %d can not be less then start index %d", to, from));
        }
        to = Math.max(to, messages.size()); // конечный становится просто индексом последнего сообщения
        List<Message> temp = messages.subList(from, to); // вернем все нужные сообщения
        List<Message> result = new ArrayList<>();
        for (int i = 0; i < temp.size(); ++i) {
            boolean find = false;
            for (int j = 0; j < result.size(); ++j) {
                Message JJ = result.get(j);
                Message II = temp.get(i);
                if (II.getId().equals( JJ.getId())) {
                    result.remove(JJ);
                    result.add(II);
                    find = true;
                    break;
                }
            }
            if (!find)
                result.add(temp.get(i));
        }
        return result;
    }

    @Override
    public void addMessage(Message message) {
        messages.add(message);
        saveHistory();
    }

    @Override
    public boolean updateMessage(Message message) {
        for (int i = 0; i < size(); ++i) {
            if (messages.get(i).getId().equals(message.getId())) {
                Message now = messages.get(i);
                now.setText(message.getText());
                messages.set(i, now);
                saveHistory();
                return true;
            }
        }
        return false;
        //throw new UnsupportedOperationException("Update for messages is not supported yet");
    }

    @Override
    public synchronized boolean removeMessage(String messageId) {
        for (int i = 0; i < size(); ++i) {
            if (messages.get(i).getId().equals(messageId)) {
                messages.remove(i);
                saveHistory();
                return true;
            }
        }
        return false;
        //throw new UnsupportedOperationException("Removing of messages is not supported yet");
    }

    @Override
    public int size() {
        return messages.size();
    }

    private boolean saveHistory() {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(DEFAULT_PERSISTENCE_FILE), "UTF-8")) {
            JSONArray array = MessageHelper.getJsonArrayOfMessages(messages);
            writer.write(array.toString());
            return true;
        } catch (IOException e) {
            logger.error("Could not parse message.", e);
            return false;
        }
    }

    private void loadHistory() {
        /*try(BufferedReader reader = new BufferedReader(new StringReader(DEFAULT_PERSISTENCE_FILE))) {*/
        File file = new File(DEFAULT_PERSISTENCE_FILE);
        Scanner sc = null;
        try {
            sc = new Scanner(new FileInputStream(file));
        }
        catch (IOException e) {}
        if (sc.hasNextLine()) {
            String jsonArrayString = sc.nextLine();

        try {
            JSONArray jsonArray = (JSONArray) MessageHelper.getJsonParser().parse(jsonArrayString);
            for (int i = 0; i < jsonArray.size(); i++) {
                Message message = new Message();
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                message.setText((String) jsonObject.get(Constants.Message.FIELD_TEXT));
                message.setAuthor((String) jsonObject.get(Constants.Message.FIELD_AUTHOR));
                message.setId((String) jsonObject.get(Constants.Message.FIELD_ID));
                message.setTimeStamp((Long) jsonObject.get(Constants.Message.FIELD_TIMESTAMP));
                message.setIndEdit((Boolean) jsonObject.get(Constants.Message.FIELD_EDIT));
                message.setIndDelete((Boolean) jsonObject.get(Constants.Message.FIELD_DELETE));
                messages.add(message);
            }
        }
        catch (ParseException e) {}
        }
    }

}
