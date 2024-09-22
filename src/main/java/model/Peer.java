package model;

import common.net.data.Entity;
import lombok.Getter;

import java.net.InetAddress;
import java.util.ArrayList;

public class Peer extends Bean {
    public ArrayList<Log> logs = new ArrayList<>();

    @Getter
    private final InetAddress address;

    @Getter
    private transient final Entity entity;


    public Peer(long ID, Entity entity, InetAddress address) {
        super(ID);
        this.entity = entity;
        this.address = address;
    }

    public static final class PeerBuilder {
        private long ID;
        private InetAddress address;
        private Entity entity;

        private PeerBuilder() {
        }

        public static PeerBuilder aPeer() {
            return new PeerBuilder();
        }

        public PeerBuilder withID(long ID) {
            this.ID = ID;
            return this;
        }

        public PeerBuilder withAddress(InetAddress address) {
            this.address = address;
            return this;
        }

        public PeerBuilder withEntity(Entity entity) {
            this.entity = entity;
            return this;
        }

        public Peer build() {
            return new Peer(ID, entity, address);
        }
    }

}
