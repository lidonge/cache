package springdemo.order.clients.cacheclient;

import cache.IVirtualCenterInClient;
import cache.client.IClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author lidong@date 2023-10-25@version 1.0
 */
@Service
public class Client implements IClient {
    static int sn = 0;
    private boolean isKeyApp = false;
    @Autowired
    private IVirtualCenterInClient clientRegister;
    //FIXME hard code
    private String name = "order-service";

    public Client() {
        //FIXME hard code
        this.isKeyApp = true;
    }

    public void setVirtualCenter(VirtualCenterInClient clientRegister) {
        this.clientRegister = clientRegister;
    }

    @Override
    public boolean isKeyApp() {
        return isKeyApp;
    }

    @Override
    public IVirtualCenterInClient getClientRegister() {
        return clientRegister;
    }

    @Override
    public String getName() {
        return name;
    }
}
