package cache.client;

import cache.IApplicationListener;
import cache.IVirtualClient;

/**
 * @author lidong@date 2023-10-24@version 1.0
 * A cache client.
 */
public interface IClient extends IVirtualClient, IApplicationListener {
    @Override
    default void prepareDirty(String compKey){
        getCache().setPrepareDirty(compKey,true);
    }

    @Override
    default void onInitializing(){
        if(isKeyApp()) {
            IClientRegister register = getClientRegister();
            register.registerClient();
        }
    }

    @Override
    default void onStopping(){
        if(isKeyApp()) {
            IClientRegister register = getClientRegister();
            register.unregisterClient();
        }
    }

    /**
     * Make sure if the client is a important application.
     * @return
     */
    boolean isKeyApp();

    /**
     * Get the implement of the cache.
     * @return
     */
    ICache getCache();

    /**
     * Get the client register which communicate with center.
     * @return
     */
    IClientRegister getClientRegister();
}
