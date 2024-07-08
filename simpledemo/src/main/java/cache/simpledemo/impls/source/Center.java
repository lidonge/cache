package cache.simpledemo.impls.source;

import cache.center.ICenter;
import cache.center.IPhysicalCenter;
import cache.impls.sourcecenter.SimplePhysicalCenter;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public class Center implements ICenter {
    private SimplePhysicalCenter physicalCenter = new SimplePhysicalCenter();

    @Override
    public IPhysicalCenter getPhysicalCenter() {
        return physicalCenter;
    }

}
