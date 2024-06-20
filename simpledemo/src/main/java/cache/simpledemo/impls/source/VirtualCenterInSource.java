package cache.simpledemo.impls.source;

import cache.*;
import cache.util.ILogable;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public class VirtualCenterInSource implements IVirtualCenterInSource, ILogable {
    Center center;
    public VirtualCenterInSource(Center center) {
        this.center = center;
    }

    @Override
    public void onCacheChanged(String compKey) {
        center.onCacheChanged(compKey);
    }
}
