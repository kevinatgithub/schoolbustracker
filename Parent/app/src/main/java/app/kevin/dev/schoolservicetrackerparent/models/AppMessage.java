package app.kevin.dev.schoolservicetrackerparent.models;

public class AppMessage {
    private int id;
    private String content;
    private String timestamp;
    private boolean inComming;

    public AppMessage(int id, String content, String timestamp, boolean inComming) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
        this.inComming = inComming;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public boolean isInComming() {
        return inComming;
    }
}
