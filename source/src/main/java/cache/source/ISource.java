package cache.source;

import cache.ICompositeKey;
import cache.IVirtualCenterInSource;

/**
 * @author lidong@date 2023-10-24@version 1.0
 * A source side application should implement this inteface.
 */
public interface ISource extends IContentChangedListener {
    @Override
    default void onContentChanged(ICompositeKey keys){
        String compKey = keys.getCompositeKey();
//        ICacheData cached = loadCachedDataFromStorage(compKey);
        IVirtualCenterInSource center = getCenter();
        center.onCacheChanged(compKey);
    }

    //TODO Optimized
    default void registerKey(){

    }
    /**
     * Get the  local/remote cache managing center.
     * @return
     */
    IVirtualCenterInSource getCenter();
}
