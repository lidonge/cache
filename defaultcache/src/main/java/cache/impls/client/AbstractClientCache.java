package cache.impls.client;

import cache.client.IPhysicalCache;
import cache.util.ILogable;
import cache.util.LockerByName;

/**
 * @author lidong@date 2024-07-05@version 1.0
 */
public abstract class AbstractClientCache implements IPhysicalCache, ILogable {
    private LockerByName keyLocker = new LockerByName();

    @Override
    public boolean isDirty(String key) {
        return get(key) == null;
    }

    @Override
    public Object getLocker(String compKey) {
        return keyLocker;
    }

    @Override
    public boolean isKeyInit(String compKey) {
        return get(compKey) != null;
    }
}
