package common.command.res;

import common.command.Response;
import common.net.data.Entity;
import model.Peer;

import java.io.Serial;
import java.util.AbstractMap;
import java.util.List;
import java.util.stream.Collectors;

public class Seeders extends Response {

    @Serial
    private static final long serialVersionUID = 8173856641895933717L;

    public Seeders(Entity entity, List<AbstractMap.SimpleEntry<Peer, Integer>> list, String file_name) {
        super(entity);
        addHeader("type", "seeders");
        addHeader("file-name", file_name);
        var parsed = list.stream().parallel().map(p -> p.getKey().getAddress().getHostAddress() + ":" +
                p.getValue()).collect(Collectors.toList());
        addHeader("list", parsed);
        addHeader("connection-type", "udp");
    }
}
