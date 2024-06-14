package cache;

import cache.ICacheData;

/**
 * @author lidong@date 2023-10-25@version 1.0
 * Cache data of client side.
 */
public interface IClientCacheData extends ICacheData {
    /**
     * Test if the data is dirty
     * @return
     */
    boolean isPrepareDirty();

    /**
     * Set the data to dirty
     */
    void setPrepareDirty(boolean prepare);
}
