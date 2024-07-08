package springdemo.users.controllers;

import cache.*;
import cache.center.ICenter;
import cache.source.ISource;
import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springdemo.users.models.AppUser;
import springdemo.users.source.VirtualClient;
import springdemo.users.source.VirtualClients;

@RestController
@RequestMapping("/users/cache")
public class CenterController implements ISource{
    @Autowired
    private ICenter center;

    @Autowired
    private VirtualClients virtualClients;

    private String getCompKey(AppUser user) {
        //FIXME here is hard code
        return "userCache-" + user.getId();
    }

    @PostMapping("/put")
    public void put(@RequestBody AppUser user) {
        _cacheUser(user,true);
    }
    @PostMapping("/register/{name}/{key}")
    public boolean registerClient(@PathVariable("name") String name, @PathVariable("key") String key){
        return center.registerClient(name, key, virtualClients.get(name));
    }

    @PostMapping("/un-register/{name}")
    public void unregisterClient(@PathVariable("name") String name ){
        center.unregisterClient(name);
    }
    @GetMapping("/get/{compKey}")
    public AppUser getUser(@PathVariable("compKey") String compKey) {
        ICenterCacheData data = center.get(compKey);
        AppUser user= data == null? null : (AppUser) data.getValue();
        return user;
    }

    @GetMapping("/isAllAgree/{compKey}")
    public boolean isAgreementReached(@PathVariable("compKey") String compKey){
        return center.isAgreementReached(compKey);
    }


    private void _cacheUser(AppUser user, boolean b) {
        center.put(getCompKey(user), new ICenterCacheData() {
            boolean bAllAgree = b;
            @Override
            public Object getValue() {
                return user;
            }

            @Override
            public boolean isAllAgreementReached() {
                return bAllAgree;
            }

            @Override
            public void setAllAgreementReached(boolean b) {
                bAllAgree = b;
            }
        });
    }

    @Override
    public IVirtualCenterInSource getCenter() {
        return center;
    }
}
