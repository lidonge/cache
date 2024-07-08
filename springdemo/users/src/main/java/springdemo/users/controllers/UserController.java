package springdemo.users.controllers;

import cache.ICenterCacheData;
import cache.IVirtualCenterInSource;
import cache.center.ICenter;
import cache.source.ISource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springdemo.users.handlers.UserRepository;
import springdemo.users.models.AppUser;

@RestController
@RequestMapping("/users")
public class UserController{
    @Autowired
    private ICenter iCenter;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public AppUser createUser(@RequestBody AppUser user) {
        AppUser user1 =  userRepository.save(user);

//        iCenter.onCacheChanged(getCompKey(user));
        //TODO here can cache user directly if all agreement arrived
//        _cacheUser(user);
        return user1;
    }

    @PutMapping("/update")
    public AppUser updateUser(@RequestBody AppUser user) {
        AppUser user1 =  userRepository.save(user);

        iCenter.onCacheChanged(getCompKey(user));
        //TODO here can cache user directly if all agreement arrived
//        _cacheUser(user);
        return user1;
    }

    private String getCompKey(AppUser user) {
        //FIXME here is hard code
        return "userCache-" + user.getId();
    }

    @GetMapping("/{id}")
    public AppUser getUser(@PathVariable("id") Long id) {
        AppUser user= userRepository.findById(id).orElse(null);
        return user;
    }
}
