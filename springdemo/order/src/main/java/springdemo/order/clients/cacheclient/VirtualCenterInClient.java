package springdemo.order.clients.cacheclient;

import cache.*;
import cache.impls.client.ClientCacheData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springdemo.order.clients.UserFeignClient;
import springdemo.users.models.AppUser;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
@Service
public class VirtualCenterInClient implements IVirtualCenterInClient {
    @Autowired
    private UserFeignClient center;

    public VirtualCenterInClient() {
    }

    @Override
    public boolean registerClient(String name, String key, IBaseClient client) {
        return center.registerClient(name,key);
    }

    @Override
    public void unregisterClient(String name) {
        center.unregisterClient(name);
    }

    @Override
    public ICacheData get(String compKey) {
        AppUser user = center.getUser(compKey);;
        return new ClientCacheData().setValue(user);
    }

    @Override
    public void put(String compKey, ICacheData cacheData) {
        center.put((AppUser) cacheData.getValue());
    }

    @Override
    public boolean isAgreementReached(String compKey) {
        return center.isAgreementReached(compKey);
    }
}
