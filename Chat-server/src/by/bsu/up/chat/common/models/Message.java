package by.bsu.up.chat.common.models;

import java.io.Serializable;

public class Message implements Serializable {

    private String id;
    private String author;
    //private String idAuthor;
    private long timeStamp;
    private String text;
    private boolean indEdit;
    private boolean indDelete;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getTimeStamp() { return timeStamp; }

    public void setTimeStamp(long timestamp) {
        this.timeStamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    //public String getIdAuthor() { return idAuthor; }

    //public void setIdAuthor(String idAuthor) { this.idAuthor = idAuthor; }

    public boolean isIndEdit() { return indEdit; }

    public void setIndEdit(boolean indEdit) { this.indEdit = indEdit; }

    public boolean isIndDelete() { return indDelete; }

    public void setIndDelete(boolean indDelete) { this.indDelete = indDelete; }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", author='" + author + '\'' +
                ", timeStamp=" + timeStamp +
                ", text='" + text + '\'' +
                ", indEdit=" + indEdit +
                ", indDelete=" + indDelete +
                '}';
    }
}
