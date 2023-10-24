package cache.source;

import cache.ICacheData;
import cache.IKeyCompositor;
import cache.IVirtualCenter;
import cache.center.IPhysicalCenter;

/**
 * @author lidong@date 2023-10-24@version 1.0
 * A source side application should implement this inteface.
 */
public interface ISource extends IContentChangedListener {
    @Override
    default void onContentChanged(String[] keys){
        String compKey = getKeyCompositor().compositKeys(keys);
//        ICacheData cached = loadCachedDataFromStorage(compKey);
        IVirtualCenter center = getCenter();
        center.onCacheChanged(compKey);
    }

    /**
     * Get the  local/remote cache managing center.
     * @return
     */
    IVirtualCenter getCenter();

    /**
     * Get the key compositor.
     * @return
     */
    IKeyCompositor getKeyCompositor();
}
