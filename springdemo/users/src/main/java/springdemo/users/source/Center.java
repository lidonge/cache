package springdemo.users.source;

import cache.center.ICenter;
import cache.center.IPhysicalCenter;
import cache.impls.sourcecenter.SimplePhysicalCenter;
import org.springframework.stereotype.Service;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
@Service
public class Center implements ICenter {
    private SimplePhysicalCenter physicalCenter = new SimplePhysicalCenter();

    @Override
    public IPhysicalCenter getPhysicalCenter() {
        return physicalCenter;
    }

}
