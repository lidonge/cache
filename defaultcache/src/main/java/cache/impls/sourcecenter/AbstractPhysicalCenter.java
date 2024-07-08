package cache.impls.sourcecenter;

import cache.ICenterCacheData;
import cache.IPrepareDirtyHandler;
import cache.IVirtualClient;
import cache.center.IPhysicalCenter;
import cache.util.ILogable;
import cache.util.IRetryHandler;
import cache.util.IRetryTool;
import cache.util.LockerByName;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public abstract class AbstractPhysicalCenter implements IPhysicalCenter, ILogable {
    private int agreeTimeout = 100;
    private boolean consistencyFirst = true;
    private boolean keepWaitAgreement = true;
    private Map<String,Boolean> isOnChangings = new HashMap<>();
    private Map<String, IVirtualClient> map = new ConcurrentHashMap<>();
    private Map<String, Boolean> dirtyMap = new ConcurrentHashMap<>();

    private IPrepareDirtyHandler multiCenter;

    private LockerByName locker = new LockerByName();

    public void setConsistencyFirst(boolean consistencyFirst) {
        this.consistencyFirst = consistencyFirst;
    }

    public void setKeepWaitAgreement(boolean keepWaitAgreement) {
        this.keepWaitAgreement = keepWaitAgreement;
    }

    @Override
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
    public Object getLocker(String compKey) {
        return locker.getLocker(compKey);
    }


    @Override
    public boolean isConsistencyFirst() {
        return consistencyFirst;
    }

    @Override
    public void logAgreementTimeout(String compKey) {
        getLogger().warn("Cache {} agreement timeout!", compKey);
    }

    @Override
    public boolean isKeepWaitAgreement(String compKey) {
        return keepWaitAgreement;
    }

    @Override
    public IPrepareDirtyHandler getMultiCenter() {
        return multiCenter;
    }

    public void setMultiCenter(IPrepareDirtyHandler multiCenter) {
        this.multiCenter = multiCenter;
    }

    @Override
    public void clearAgreementFlag(String compKey) {
        getFromLocalCache(compKey).setAllAgreementReached(false);
    }

    @Override
    public void setAgreementFlag(String compKey) {
        ICenterCacheData data = getFromLocalCache(compKey);
        if(data != null)
            data.setAllAgreementReached(true);
        else{
            //Uninitialized data is updated
        }
    }

    @Override
    public boolean isAgreementReached(String compKey) {
        ICenterCacheData cacheData = getFromLocalCache(compKey);
        return cacheData == null || cacheData.isAllAgreementReached();
    }
    @Override
    public IRetryTool getRetryTool() {
        return new IRetryTool() {
            @Override
            public void retry(IRetryHandler handler) {
                handler.exec();
            }
        };
    }
}
