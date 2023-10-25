package cache.simpledemo.impls.client;

import cache.client.ICache;
import cache.client.IClient;
import cache.client.IVirtualCenter;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public class Client implements IClient {
    static int sn = 0;
    private boolean isKeyApp = false;
    private ClientCache cache;
    private VirtualCenterInClient clientRegister;
    private String name = "DemoClient_" + sn++;

    public Client(boolean isKeyApp, ClientCache cache) {
        this.isKeyApp = isKeyApp;
        this.cache = cache;
    }

    public void setVirtualCenter(VirtualCenterInClient clientRegister) {
        this.clientRegister = clientRegister;
    }

    @Override
    public boolean isKeyApp() {
        return isKeyApp;
    }

    @Override
    public ICache getCache() {
        return cache;
    }

    @Override
    public IVirtualCenter getClientRegister() {
        return clientRegister;
    }

    @Override
    public String getName() {
        return name;
    }
}
