package cache.client;

import cache.IApplicationListener;
import cache.IBaseClient;
import cache.ILogable;
import cache.center.IVirtualClient;

/**
 * @author lidong@date 2023-10-24@version 1.0
 * A cache client.
 */
public interface IClient extends IBaseClient, IApplicationListener, ILogable {
    /**
     * Called from center if a specified data is preparing dirty.
     * @param compKey
     */
    default void prepareDirty(String compKey){
        getLogger().info("Client {} prepareDirty with key {}.",getName(),compKey);
        getCache().setPrepareDirty(compKey);
    }

    @Override
    default void onInitializing(){
        getLogger().info("Client {} on init, the key app flag is {}.", getName(),isKeyApp());
        if(isKeyApp()) {
            IVirtualCenter register = getClientRegister();
            register.registerClient(getName(),this);
            getLogger().info("Client {} register.", getName());
        }
    }

    @Override
    default void onStopping(){
        getLogger().info("Client {} on stopping, the key app flag is {}.", getName(),isKeyApp());
        if(isKeyApp()) {
            IVirtualCenter register = getClientRegister();
            register.unrigister(getName());
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
    IVirtualCenter getClientRegister();
}
