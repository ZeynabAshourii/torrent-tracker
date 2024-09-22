package lookup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repository <T, U> {

    protected static final Object lock = new Object();

    protected Map<T, List<U>> map = new HashMap<>();

    public void add(T entity, U obj) {
        synchronized (lock) {
            map.put(entity, List.of(obj));
        }
    }

    public List<U> search(T entity) {
        synchronized (lock) {
            if (map.containsKey(entity))
                return map.get(entity);
            else
                return List.of();
        }
    }

    public void remove(T key, U val) {
        synchronized (lock) {
            map.remove(key);
        }
    }
}
