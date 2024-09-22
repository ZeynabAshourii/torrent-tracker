package common.command.res;

import common.command.Response;
import common.net.data.Entity;

import java.io.Serial;

public class FileShared extends Response {

    @Serial
    private static final long serialVersionUID = 8538370964445195288L;

    public FileShared(Entity entity) {
        super(entity);
        addHeader("connection-type", "udp");
        addHeader("type", "file-shared");
    }
}
