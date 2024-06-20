package cache.simpledemo.impls.source;

import cache.IVirtualCenterInSource;
import cache.source.ISource;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public class Source implements ISource {
    private VirtualCenterInSource virtualCenter;

    public Source() {
    }

    public void setVirtualCenter(VirtualCenterInSource virtualCenter) {
        this.virtualCenter = virtualCenter;
    }

    @Override
    public IVirtualCenterInSource getCenter() {
        return virtualCenter;
    }
}
