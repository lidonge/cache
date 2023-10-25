package cache.source;

/**
 * @author lidong@date 2023-10-24@version 1.0
 * Call by Master-Data manager, when specified data is changed
 */
public interface IContentChangedListener {
    /**
     * Called when data specified by keys changed.
     * @param keys
     */
    void onContentChanged(String[] keys);
}
