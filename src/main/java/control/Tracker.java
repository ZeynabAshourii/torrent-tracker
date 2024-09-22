package control;

import common.command.res.FileShared;
import common.net.data.Entity;
import lombok.Getter;
import lookup.Repository;
import lookup.SeederRepo;
import model.File;
import model.Log;
import model.Peer;
import policies.RetrievalBehaviour;
import policies.Validator;
import server.Server;
import util.IDFactory;

import java.net.InetAddress;
import java.util.AbstractMap;
import java.util.List;

public class Tracker {

    public static Tracker instance;

    private final Object registryLock = new Object();

    private final Object fileLock = new Object();

    private final Repository<InetAddress, Peer> users = new Repository<>();

    private final Repository<String, File> files = new Repository<>();

    private final SeederRepo seeders = new SeederRepo();

    private int port;

    private boolean alive;

    @Getter
    Server server = new Server();

    public Tracker(int port) {
        this.port = port;
        this.alive = true;
        server.setPortNumber(port);
        server.setRetrievalAction(new RetrievalBehaviour(server, new Validator()));
        server.init();
        server.start();
        instance = this;
    }

    public void registerSeeder(String filename, InetAddress address, int port, Entity sender) {
        try {
            // input : user address and port number + filename
            Peer peer = null;
            File file = null ;

            // if peer doesn't exist, create it first
            synchronized (registryLock) {
                List<Peer> matches = users.search(address);
                if (matches.isEmpty()) {
                    long id = IDFactory.generate();
                    peer = Peer.PeerBuilder.aPeer().withEntity(sender).withAddress(address).withID(id).build();
                    users.add(address, peer);
                } else {
                    peer = matches.get(0);
                    if (!peer.getEntity().isAlive())
                        peer.getEntity().setAlive(true);
                }
            }

            // if file doesn't exist create it first
            synchronized (fileLock) {
                List<File> matches = files.search(filename);
                if (matches.isEmpty()) {
                    long id = IDFactory.generate();
                    file = File.FileBuilder.aFile().withID(id).withName(filename).build();
                    files.add(filename, file);
                } else {
                    file = matches.get(0);
                }
            }

            // add peer to seeders list
            synchronized (file) {
                Peer finalPeer = peer;
                if (seeders.search(file).stream().parallel().map(AbstractMap.SimpleEntry::getKey)
                        .noneMatch(peer1 -> peer1 == finalPeer))
                    seeders.add(file, new AbstractMap.SimpleEntry<>(peer, port));
            }

            // signal the result
            server.send(new FileShared(sender).addHeader("file-name", filename));

            new Log("share" , peer , file, true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File queryFileName(String fileName) {
        synchronized (fileLock) {
            // if no one has the file, abort
            if (files.search(fileName).isEmpty())
                return null;

            // get file
            return files.search(fileName).get(0);
        }
    }

    public Peer queryPeer(InetAddress address) {
        List<Peer> list = null;
        synchronized (registryLock) {
            list = users.search(address);
        }
        if (list.isEmpty())
            return null;
        else
            return list.get(0);
    }

    public List<AbstractMap.SimpleEntry<Peer, Integer>> getSeeders(String filename) {
        // input : filename
        File file = null;
        Peer peer = null;

        file = queryFileName(filename);
        if (file == null)
            return List.of();

        List<AbstractMap.SimpleEntry<Peer, Integer>> out = null;
        synchronized (file) {

            // if no seeders are available, abort
            if (seeders.search(file).isEmpty())
                return List.of();


            // get seeders
            out = seeders.search(file);

            for (int i = out.size() - 1; i >= 0; i--) {
                var pair = out.get(i);
                if (!pair.getKey().getEntity().isAlive()) {
                    seeders.remove(file, pair);
                    out.remove(i);
                }
            }
        }

        return out;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
