package lookup;

import model.File;
import model.Peer;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SeederRepo extends Repository<File, AbstractMap.SimpleEntry<Peer, Integer>> {


    private final ExecutorService servicePool = Executors.newSingleThreadExecutor();


    @Override
    public void add(File entity, AbstractMap.SimpleEntry<Peer, Integer> obj) {
        servicePool.submit(new Add(entity, obj));
    }


    @Override
    public void remove(File key, AbstractMap.SimpleEntry<Peer, Integer> val) {
        servicePool.submit(new Remove(key, val));
    }


    private class Add implements Runnable {

        File entity;
        AbstractMap.SimpleEntry<Peer, Integer> obj;

        public Add(File entity, AbstractMap.SimpleEntry<Peer, Integer> obj) {
            this.entity = entity;
            this.obj = obj;
        }

        @Override
        public void run() {
            if (map.containsKey(entity))
                map.get(entity).add(obj);
            else
                map.put(entity, new ArrayList<>(Set.of(obj)));
            System.out.println(map);
        }
    }


    private class Remove implements Runnable {
        File entity;
        AbstractMap.SimpleEntry<Peer, Integer> val;

        public Remove(File entity, AbstractMap.SimpleEntry<Peer, Integer> val) {
            this.entity = entity;
            this.val = val;
        }

        public void run() {
            if (map.containsKey(entity)) {
                if (map.get(entity).isEmpty())
                    map.remove(entity);
                else
                    map.get(entity).remove(val);
            }
        }
    }


}
