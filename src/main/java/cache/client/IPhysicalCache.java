package cache.client;

import cache.ICacheData;
import cache.IKeyCompositor;

import java.util.Map;

/**
 * @author lidong@date 2023-10-24@version 1.0
 * A physical cache implement by client application.
 */
public interface IPhysicalCache {
    /**
     * Get the name/value pair map which store cache data.
     * @return
     */
    Map<String, ICacheData> getMap();

    /**
     * Test if all client reached the specified agreement.
     * @param key
     * @return
     */
    boolean isAllAgreed(String key);

    /**
     * Get key compositor.
     * @return
     */
    IKeyCompositor getKeyCompositor();

    /**
     * Refresh local cache from remote source side or redis.
     * @param key
     * @return
     */
    ICacheData refreshFromRemote(String key);

    /**
     * Set the prepare-dirty flag for the specified data.
     * @param key
     * @param dirty
     */
    void setPrepareDirty(String key, boolean dirty);

    /**
     * Test if current status is prepare-dirty of the specified data.
     * @param key
     * @return
     */
    boolean isPrepareDirty(String key);
}
