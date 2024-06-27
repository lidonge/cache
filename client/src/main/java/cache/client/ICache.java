package cache.client;

import cache.ICacheData;
import cache.IClientCacheData;
import cache.ICompositeKey;
import cache.IVirtualCenterInClient;
import cache.util.ILogable;

/**
 * @author lidong@date 2023-10-24@version 1.0
 * A cache which store the master data.
 */
public interface ICache extends ILogable {
    /**
     * Get the client which the cache belong to.
     *
     * @return
     */
    IClient getClient();

    /**
     * Get specified data from cache, if data is dirty then refresh it from center.
     *
     * @param key
     * @return null if center is dirty
     */
    default IClientCacheData get(ICompositeKey key){
        IPhysicalCache pc = getPhysicalCache();
        String compKey = key.getCompositeKey();
        Object locker = pc.getLocker(compKey);
        IClientCacheData data = null;
        synchronized (locker) {
            if (!pc.isKeyInit(compKey)) {
                //It's the first time to get data, register the key to the center
                IClient client = getClient();
                IVirtualCenterInClient virtualCenter = client.getClientRegister();
                boolean isAgreementReached = virtualCenter.registerClient(client.getName(), compKey, client);
                getLogger().info("Client {} regiester {} to center and isAgreementReached {}.", getClient().getName(), compKey,isAgreementReached);
                if (!isAgreementReached)
                    setPrepareDirty(compKey,true);
            }
            if (isPrepareDirty(compKey)) {
                dirtyIfAgreementReached(compKey);
            }
            refreshIfDirty(compKey);
            data = pc.get(compKey);
        }
        getLogger().info("Client {} get {} value {} from client cache.", getClient().getName(), compKey, data);

        return data;
    }
    /**
     * Get specified data from cache, if center is dirty, put new data to the local and center.
     *
     * @param key
     * @return
     */
    default IClientCacheData getAndPutIfDirty(ICompositeKey key, IBusinessService service) throws ServiceCallException{
        IPhysicalCache pc = getPhysicalCache();
        String compKey = key.getCompositeKey();
        Object locker = pc.getLocker(compKey);
        synchronized (locker) {
            IClientCacheData data = get(key);
            if(data  == null){
                //TODO Concurrency for a specific key may cause efficiency problems, but this is very rare.
                data = service.getData();
                putToLocalAndCenter(compKey, data);
                getLogger().info("After put new data, {} value is {}, new data put to cache.",getClient().getName(), data);
            }

            return data;
        }
    }

    /**
     * Should be called if the cache is dirty, put new data to the local and center.
     *
     * @param compKey
     * @param cacheData
     */
    default void putToLocalAndCenter(String compKey, IClientCacheData cacheData) {
        IPhysicalCache pc = getPhysicalCache();
        pc.put(compKey, cacheData);
        IClient client = getClient();
        client.getClientRegister().put(compKey, cacheData);
    }

    /**
     * Get the physical cache implement, maybe local or remote e.g. redis
     *
     * @return
     */
    IPhysicalCache getPhysicalCache();

    /**
     * Make a specified data dirty, called from center.
     *
     * @param key
     */
    default void setPrepareDirty(String key, boolean prepare) {
        getLogger().info("Client {} setPrepareDirty {} .", getClient().getName(), key);
        IClientCacheData data = getPhysicalCache().get(key);
        data.setPrepareDirty(prepare);
    }

    /**
     * Test if the specified data dirty.
     *
     * @param key
     * @return true is dirty.
     */
    default boolean isPrepareDirty(String key) {
        IClientCacheData data =getPhysicalCache().get(key);
        if(data == null)
            return false;
        boolean ret = data.isPrepareDirty();
        getLogger().info("Client {}'s data {} prepare dirty flag is {} .", getClient().getName(), key, ret);
        return ret;
    }

    private void refreshIfDirty(String compKey) {
        IPhysicalCache pc = getPhysicalCache();
        IClientCacheData ret = null;
        boolean dirty = pc.isDirty(compKey);
        getLogger().info("Client {}'s {} refreshIfDirty is {}.", getClient().getName(), compKey, dirty);
        if (dirty) {
            // null means dirty or first time read from cache
            ICacheData data = getClient().getClientRegister().get(compKey);
            if(data != null)
                pc.put(compKey, (IClientCacheData) data);
        }
    }

    private void dirtyIfAgreementReached(String compKey) {
        IPhysicalCache pc = getPhysicalCache();
        boolean isAllAgreed = getClient().getClientRegister().isAgreementReached(compKey);
        getLogger().info("All agreed flag is {}, if true Refresh client {} .", isAllAgreed, getClient().getName());

        if (isAllAgreed) {
            setPrepareDirty(compKey, false);
            pc.setDirty(compKey);
        }
    }

}
