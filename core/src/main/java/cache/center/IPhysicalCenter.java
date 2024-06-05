package cache.center;

import java.util.List;
import java.util.Map;

/**
 * @author lidong@date 2023-10-24@version 1.0
 */
public interface IPhysicalCenter {
    /**
     * Get map of name/client pair.
     * @return
     */
    Map<String, IVirtualClient> getClients();

    /**
     * Set specified data dirty.
     * @param compKey
     */
    void setDirty(String compKey,  boolean dirty);

    boolean isDirty(String compKey);

    /**
     * Clear the flag of specified agreement if dirty.
     * @param compKey
     */
    void clearAgreementFlag(String compKey);

    /**
     * Set the flag of specified agreement if all client reached or timeout.
     * @param compKey
     */
    void setAgreementFlag(String compKey);

    /**
     * Get the flag of specified agreement.
     * @param compKey
     */
    boolean isAgreementReached(String compKey);

    /**
     * Make sure if the policy is consistency or available.
     * @return true if consistency.
     */
    boolean isConsistencyFirst();

    /**
     * Logged when agreement is timeout.
     * @param compKey
     */
    void logAgreementTimeout(String compKey);

    /**
     * Depended on policy, waiting until timeout.
     * @return if not timeout.
     */
    boolean keepWaitAgreement(String compKey);

    /**
     * Get agreement timeout.
     * @return
     */
    int getAgreeTimeout();

    /**
     * Set the agreement timeout.
     * @param agreeTimeout
     */
    void setAgreeTimeout(int agreeTimeout);

    /**
     * Get the specified data from local cache of center side.
     * @param compKey
     * @return
     */
    ICenterCacheData getFromLocalCache(String compKey);

    /**
     * Get specified locker for atomic operations
     * @param compKey
     * @return
     */
    Object getLocker(String compKey);

    void putToLocalCache(String compKey, ICenterCacheData cacheData);

    boolean isOnChanging();
    void setOnChanging(boolean onChanging);
}
