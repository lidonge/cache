package cache.simpledemo.impls.client;

import cache.ILogable;
import cache.client.IClient;
import cache.client.IClientCacheData;
import cache.client.IPhysicalCache;
import cache.impls.util.LockerByName;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public class PhysicalClientCache implements IPhysicalCache, ILogable {
    private Map<String, IClientCacheData> map = new HashMap<>();
    private LockerByName keyLocker = new LockerByName();
    private VirtualCenterInClient virtualCenter;
    private IClient client;

    public PhysicalClientCache() {
        this.virtualCenter = virtualCenter;
    }

    public void setVirtualCenter(VirtualCenterInClient virtualCenter) {
        this.virtualCenter = virtualCenter;
    }

    public void setClient(IClient client) {
        this.client = client;
    }

    @Override
    public boolean isAllAgreed(String key) {
        return virtualCenter.isAgreementReached(key);
    }

    @Override
    public IClientCacheData refreshFromRemote(String key) {
        IClientCacheData data = (IClientCacheData) virtualCenter.get(key);
        getLogger().info("Client {} refreshFromRemote key is {} and data is {}.", client.getName(),key,data);
        map.put(key,data);
        return data;
    }

    @Override
    public void setPrepareDirty(String key) {
        map.get(key).setPrepareDirty();
    }

    @Override
    public boolean isPrepareDirty(String key) {
        IClientCacheData data =map.get(key);
        if(data == null)
            return false;
        return data.isPrepareDirty();
    }

    @Override
    public IClientCacheData get(String key) {
        return map.get(key);
    }

    @Override
    public void setDirty(String key) {
        map.remove(key);
    }

    @Override
    public Object getLocker(String compKey) {
        return keyLocker;
    }
}
