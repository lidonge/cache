package cache;

/**
 * @author lidong@date 2023-10-24@version 1.0
 * A listener of an application, called when it start and stopping.
 */
public interface IApplicationListener {
    /**
     * Called when client is initializing.
     */
    void onInitializing();

    /**
     * Called when client is stopping.
     */
    void onStopping();
}
