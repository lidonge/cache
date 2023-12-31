package cache.simpledemo.impls.source;

import cache.ILogable;
import cache.center.IAsynListener;
import cache.center.ICenterCacheData;
import cache.impls.center.AbstractPhysicalCenter;
import cache.impls.util.LockerByName;
import cache.simpledemo.impls.DemoCacheData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public class PhysicalCenter extends AbstractPhysicalCenter implements ILogable {
    private Map<String, ICenterCacheData> localCache = new HashMap<>();
    private Map<String, Boolean> agreementFlags = new ConcurrentHashMap<>();

    private LockerByName locker = new LockerByName();
    @Override
    public void clearAgreementFlag(String compKey) {
        agreementFlags.put(compKey, false);
    }

    @Override
    public void setAgreementFlag(String compKey) {
        agreementFlags.put(compKey, true);
    }

    public boolean isAgreementReached(String compKey) {
        return agreementFlags.get(compKey);
    }

    @Override
    public void makeNewDataAvailable(String compKey) {
        localCache.put(compKey, new DemoCacheData(compKey));
    }

    @Override
    public boolean isConsistencyFirst() {
        return true;
    }

    @Override
    public void logAgreementTimeout(String compKey) {
        getLogger().warn("Cache {} agreement timeout!");
    }

    @Override
    public boolean keepWaitAgreement(String compKey) {
        return false;
    }

    public ICenterCacheData getFromLocalCache(String compKey) {
        return localCache.get(compKey);
    }

    @Override
    public Object getLocker(String compKey) {
        return locker.getLocker(compKey);
    }

    @Override
    public void waitAllClientFinish(List<IAsynListener> asynListeners) {
        //TODO
    }
}
