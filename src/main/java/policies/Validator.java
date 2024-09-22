package policies;

import common.command.req.FileStat;
import common.command.req.GetSeeders;
import common.command.req.Seed;
import common.net.agent.PacketValidator;

public class Validator extends PacketValidator {
    public Validator() {
        commandList.add(new Seed(null));
        commandList.add(new GetSeeders(null));
       commandList.add(new FileStat(null));
    }
}
