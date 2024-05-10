package cache.impls.center;

import cache.center.IVirtualClient;
import cache.center.IPhysicalCenter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public abstract class AbstractPhysicalCenter implements IPhysicalCenter {
    protected int agreeTimeout = 100;
    private Map<String, IVirtualClient> map = new ConcurrentHashMap<>();

    public Map<String, IVirtualClient> getClients() {
        return map;
    }

    @Override
    public void setAgreeTimeout(int agreeTimeout) {
        this.agreeTimeout = agreeTimeout;
    }

    @Override
    public int getAgreeTimeout() {
        return agreeTimeout;
    }
}
