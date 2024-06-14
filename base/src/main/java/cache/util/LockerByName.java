package cache.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public class LockerByName {
    private Map<String, Object> keyLocker = new HashMap<>();

    public Object getLocker(String key) {
        Object locker = keyLocker.get(key);
        if(locker == null) {
            synchronized (this) {
                locker = keyLocker.get(key);
                if (locker == null) {
                    locker = new Object();
                    keyLocker.put(key, locker);
                }
            }
        }
        return locker;
    }
}
