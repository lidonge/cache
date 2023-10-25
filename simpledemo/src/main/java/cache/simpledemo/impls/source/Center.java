package cache.simpledemo.impls.source;

import cache.center.ICenter;
import cache.center.IPhysicalCenter;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public class Center implements ICenter {
    private PhysicalCenter physicalCenter = new PhysicalCenter();

    @Override
    public boolean isAgreementReached(String compKey) {
        return physicalCenter.isAgreementReached(compKey);
    }

    @Override
    public IPhysicalCenter getPhysicalCenter() {
        return physicalCenter;
    }

}
