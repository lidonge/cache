package cache.simpledemo.impls.client;

import cache.ILogable;
import cache.center.ICenterCacheData;
import cache.client.IClient;
import cache.client.IClientCacheData;
import cache.client.IPhysicalCache;
import cache.util.LockerByName;

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
    public void refreshFromRemote(String key) {
        ICenterCacheData centerCacheData = (ICenterCacheData) virtualCenter.get(key);
        IClientCacheData data = (IClientCacheData) centerCacheData;
        if(centerCacheData != null && !centerCacheData.isAllAgreementReached()){
            data.setPrepareDirty(true);
        }
        getLogger().info("Client {} refreshFromRemote key is {} and data is {}.", client.getName(),key,data);
        map.put(key,data);
    }

    @Override
    public void setPrepareDirty(String key, boolean prepare) {
        map.get(key).setPrepareDirty(prepare);
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
    public void put(String compKey, IClientCacheData cacheData) {
        map.put(compKey,cacheData);
        virtualCenter.put(compKey, (ICenterCacheData) cacheData);
    }

    @Override
    public void setDirty(String key) {
        map.remove(key);
    }

    @Override
    public boolean isDirty(String key) {
        return map.get(key) == null;
    }

    @Override
    public Object getLocker(String compKey) {
        return keyLocker;
    }

    @Override
    public boolean isKeyInit(String compKey) {
        return get(compKey) == null;
    }
}
