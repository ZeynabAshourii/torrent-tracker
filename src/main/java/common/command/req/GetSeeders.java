package common.command.req;

import common.command.Request;
import common.command.res.Seeders;
import common.net.data.Command;
import common.net.data.Entity;
import control.Tracker;

import java.io.Serial;

public class GetSeeders extends Request {

    @Serial
    private static final long serialVersionUID = -1136858753128091726L;

    public GetSeeders(Entity entity) {
        super(entity);
    }

    public GetSeeders(Command incoming, Entity sender) {
        super(sender);
        addHeader("file-name", incoming.getHeader("file-name"));
        addHeader("address", incoming.getHeader("address"));
        addHeader("port", incoming.getHeader("port"));
    }

    @Override
    public boolean isValid(Command command) {
        return "get-seeders".equals(command.getHeader("type"));
    }

    @Override
    public void run() {
        var out = Tracker.instance.getSeeders((String) getHeader("file-name"));
        var server = Tracker.instance.getServer();
        server.send(new Seeders(entity, out, (String) getHeader("file-name")));
    }
}
