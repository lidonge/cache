package cache.simpledemo.impls.source;

import cache.IVirtualClient;
import cache.center.IPhysicalCenter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public abstract class AbstractPhysicalCenter implements IPhysicalCenter {
    protected int agreeTimeout = 100;
    private Map<String,Boolean> isOnChangings = new HashMap<>();
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

    @Override
    public boolean isOnChanging(String compKey) {
        Boolean isOnChanging = isOnChangings.get(compKey);
        return isOnChanging != null && isOnChanging ;
    }

    @Override
    public void setOnChanging(String compKey, boolean onChanging) {
        isOnChangings.put(compKey, onChanging);
    }

}
