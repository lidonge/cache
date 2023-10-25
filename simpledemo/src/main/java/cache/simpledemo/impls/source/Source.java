package cache.simpledemo.impls.source;

import cache.IKeyCompositor;
import cache.client.IVirtualCenter;
import cache.center.ICenter;
import cache.center.IPhysicalCenter;
import cache.source.ISource;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public class Source implements ISource {
    private IKeyCompositor keyCompositor;
    private VirtualCenterInSource virtualCenter;

    public Source() {
        this.keyCompositor = IKeyCompositor.getDefaultKeyCompositor();
    }

    public void setVirtualCenter(VirtualCenterInSource virtualCenter) {
        this.virtualCenter = virtualCenter;
    }

    @Override
    public IVirtualCenter getCenter() {
        return virtualCenter;
    }

    @Override
    public IKeyCompositor getKeyCompositor() {
        return keyCompositor;
    }
}
