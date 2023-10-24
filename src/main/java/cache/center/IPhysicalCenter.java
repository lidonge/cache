package cache.center;

import cache.ICacheData;
import cache.IVirtualClient;

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
    void setDirty(String compKey);

    /**
     * Clear the flag of spacied agreement if dirty.
     * @param compKey
     */
    void clearAgreementFlag(String compKey);

    /**
     * Set the flag of spacied agreement if all client reached or timeout.
     * @param compKey
     */
    void setAgreementFlag(String compKey);

    /**
     * Make new data available in source local or global cache.
     *
     * @param compKey
     * @return
     */
    void makeNewDataAvailable(String compKey);

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
    boolean keepWaitAgreement();
}
