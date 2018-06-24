package cn.inkroom.software.log.server;

/**
 * @author 墨盒
 */
public class Message {
    private long time;
    private String message;

    public Message(long time, String message) {
        this.time = time;
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
