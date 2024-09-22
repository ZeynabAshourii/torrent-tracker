package common.command.req;

import common.command.Request;
import common.net.data.Command;
import common.net.data.Entity;
import control.Tracker;
import lombok.SneakyThrows;
import model.Log;

import java.io.Serial;
import java.net.InetAddress;

public class FileStat extends Request {

    @Serial
    private static final long serialVersionUID = 141545784986322584L;

    public FileStat(Entity entity) {
        super(entity);
    }

    public FileStat(Command incoming, Entity sender) {
        super(sender);
        addHeader("file-name", incoming.getHeader("file-name"));
        addHeader("result", incoming.getHeader("result"));
        addHeader("local-address", incoming.getHeader("local-address"));
    }

    @Override @SneakyThrows
    public void run() {
        var file = Tracker.instance.queryFileName((String) getHeader("file-name"));
        var peer = Tracker.instance.queryPeer(InetAddress.getByName((String) getHeader("local-address")));
        new Log("get", peer, file, (Boolean) getHeader("result"));
    }

    @Override
    public boolean isValid(Command command) {
        return "file-stat".equals(command.getHeader("type"));
    }
}
