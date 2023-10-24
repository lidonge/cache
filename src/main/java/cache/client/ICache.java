package cache.client;

import cache.ICacheData;

import java.util.Map;

/**
 * @author lidong@date 2023-10-24@version 1.0
 * A cache which store the master data.
 */
public interface ICache {
    /**
     * Get specified data from cache.
     * @param keys
     * @return
     */
    default ICacheData get(String[] keys){
        IPhysicalCache pc = getPhysicalContainer();
        String compKey = pc.getKeyCompositor().compositKeys(keys);
        return refresh(compKey);
    }

    /**
     * Get the physical cache implement, maybe local or remote e.g. redis
     * @return
     */
    IPhysicalCache getPhysicalContainer();

    /**
     * Make a specified data dirty.
     * @param key
     * @param dirty
     */
    default void setPrepareDirty(String key, boolean dirty){
        getPhysicalContainer().setPrepareDirty(key, dirty);
    }

    /**
     * Test if the specified data dirty.
     * @param key
     * @return true is dirty.
     */
    default boolean isPrepareDirty(String key){
        return getPhysicalContainer().isPrepareDirty(key);
    }

    private ICacheData _get(IPhysicalCache pc, String key) {
        ICacheData ret;
        Map<String, ICacheData> map = pc.getMap();
        ret = map.get(key);
        return ret;
    }

    private ICacheData refresh(String compKey){
        ICacheData ret = null;
        IPhysicalCache pc = getPhysicalContainer();
        if(isPrepareDirty(compKey)){
            boolean isAllAgreed = pc.isAllAgreed(compKey);
            if(isAllAgreed){
                ret = pc.refreshFromRemote(compKey);
                setPrepareDirty(compKey,false);
            }
        }
        if(ret == null){
            //not reach agreement or not prepare dirty
            ret = _get(pc,compKey);

            if(ret == null) {
                //First read from cache, null means dirty
                ret = pc.refreshFromRemote(compKey);
            }
        }

        return ret;
    }

}
