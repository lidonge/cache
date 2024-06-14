package cache;

import cache.ICacheData;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public interface ICenterCacheData extends ICacheData {
    /**
     * Test if all clients of this data agreement is reached
     * @return
     */
    boolean isAllAgreementReached();

    /**
     * Set the flag.
     * @param allAgreementReached
     */
    void setAllAgreementReached(boolean allAgreementReached);

}
