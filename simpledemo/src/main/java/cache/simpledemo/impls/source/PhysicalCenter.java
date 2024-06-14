package cache.simpledemo.impls.source;

import cache.ILogable;
import cache.ICenterCacheData;
import cache.center.IMultiCenter;
import cache.util.LockerByName;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public class PhysicalCenter extends AbstractPhysicalCenter implements ILogable {
    private Map<String, ICenterCacheData> localCache = new HashMap<>();
//    private Map<String, Boolean> agreementFlags = new ConcurrentHashMap<>();
    private Map<String, Boolean> dirtyMap = new ConcurrentHashMap<>();

    private LockerByName locker = new LockerByName();
    @Override
    public void clearAgreementFlag(String compKey) {
        localCache.get(compKey).setAllAgreementReached(false);
    }

    @Override
    public void setAgreementFlag(String compKey) {
        ICenterCacheData data = localCache.get(compKey);
        if(data != null)
            data.setAllAgreementReached(true);
        else{
            //Uninitialized data is updated
        }
    }

    @Override
    public boolean isAgreementReached(String compKey) {
        ICenterCacheData cacheData = localCache.get(compKey);
        return cacheData == null || cacheData.isAllAgreementReached();
    }
    @Override
    public void setDirty(String compKey, boolean dirty) {
        dirtyMap.put(compKey, dirty);
    }

    @Override
    public boolean isDirty(String compKey) {
        Boolean b = dirtyMap.get(compKey);
        return b == null || b;
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
    public void putToLocalCache(String compKey, ICenterCacheData cacheData) {
        localCache.put(compKey,cacheData);
    }

    @Override
    public IMultiCenter getMultiCenter() {
        return null;
    }
}
