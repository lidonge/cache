package cache.client;

/**
 * @author lidong@date 2023-10-24@version 1.0
 * The client register which communicate with center.
 */
public interface IClientRegister {
    /**
     * Register this client to the center.
     */
    void registerClient();

    /**
     * Unregister this client from the center.
     */
    void unregisterClient();
}
