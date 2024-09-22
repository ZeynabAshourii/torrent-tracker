package util;

public class IDFactory {
    private static final Object lock = new Object();
    private static long lastID;

    public static long generate() {
        synchronized (lock) {
            return lastID++;
        }
    }
}
