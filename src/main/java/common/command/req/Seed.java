package common.command.req;

import common.command.Request;
import common.net.data.Command;
import common.net.data.Entity;
import control.Tracker;
import lombok.SneakyThrows;

import java.io.Serial;
import java.net.InetAddress;

public class Seed extends Request {

    @Serial
    private static final long serialVersionUID = 2225373047916203135L;

    public Seed(Entity entity) {
        super(entity);
    }

    public Seed(Command incoming, Entity sender) {
        super(sender);
        addHeader("file-name", incoming.getHeader("file-name"));
        addHeader("address", incoming.getHeader("address"));
        addHeader("port", incoming.getHeader("port"));
    }

    @Override
    public boolean isValid(Command command) {
        return "seed".equals(command.getHeader("type"));
    }

    @Override @SneakyThrows
    public void run() {
        Tracker.instance.registerSeeder((String) getHeader("file-name"), InetAddress.getByName((String) getHeader("address")),
                 ((Double) getHeader("port")).intValue(), entity);
    }
}
