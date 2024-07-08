package springdemo.order.controllers;

import cache.client.ICache;
import cache.client.IClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lidong@date 2024-06-26@version 1.0
 */
@RestController
@RequestMapping("/orders/cache")
public class ClientController {
    @Autowired
    private ICache cache;
    @PostMapping("/prepareDirty/{compKey}")
    public void setPrepareDirty(@PathVariable("compKey") String compKey){
        cache.setPrepareDirty(compKey,true);
    }
}
