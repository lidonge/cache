package cache;

/**
 * If the centers are distributed, when a center receives an update message,
 * it must notify other centers of the message and wait for all centers to reach a consensus.
 * @author lidong@date 2024-06-13@version 1.0
 */
public interface IPrepareDirtyHandler {
    /**
     * Called asynchronized if a specified data is preparing dirty.
     *
     * Notify other centers/client asynchronous that the update message is arrived,
     * and callback to the listener when other centers/client reached agreement.
     * @param compKey
     * @param iAsynListener
     */
    void prepareDirty(String compKey, IAsynListener iAsynListener);
}
