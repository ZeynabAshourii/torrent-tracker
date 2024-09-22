package model;

import java.util.ArrayList;
import java.util.Date;

public class Log {
    public static ArrayList<Log> allLogs = new ArrayList<>();
    public boolean successfully = true;
    public String type;
    public Peer peer;
    public File file;
    public Date date;

    public Log(String type, Peer peer, File file, boolean successfully) {
        this.type = type;
        this.peer = peer;
        this.file = file;
        this.date = new Date();
        this.successfully = successfully;
        if (file != null) {
            file.logs.add(this);
        }
        if (peer != null) {
            peer.logs.add(this);
        }
        allLogs.add(this);
    }

    @Override
    public String toString() {
        return "Log{" +
                "successfully=" + successfully +
                ", type='" + type + '\'' +
                ", peer=" + "ID:" + peer.getID() +
                ", file=" + file.getName() +
                ", date=" + date +
                '}';
    }
}
