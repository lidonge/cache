package cache.client;

import cache.IApplicationListener;
import cache.IBaseClient;
import cache.IVirtualCenterInClient;
import cache.util.ILogable;

/**
 * @author lidong@date 2023-10-24@version 1.0
 * A cache client.
 */
public interface IClient extends IBaseClient, IApplicationListener, ILogable {
    @Override
    default void onInitializing(){
//        getLogger().info("Client {} on init, the key app flag is {}.", getName(),isKeyApp());
//        if(isKeyApp()) {
//            IVirtualCenter register = getClientRegister();
//            register.registerClient(getName(),this);
//            getLogger().info("Client {} register.", getName());
//        }
    }

    @Override
    default void onStopping(){
        getLogger().info("Client {} on stopping, the key app flag is {}.", getName(),isKeyApp());
        if(isKeyApp()) {
            IVirtualCenterInClient register = getClientRegister();
            register.unregisterClient(getName());
        }
    }

    /**
     * Make sure if the client is a important application.
     * @return
     */
    boolean isKeyApp();

    /**
     * Get the client register which communicate with center.
     * @return
     */
    IVirtualCenterInClient getClientRegister();
}
