package cache.simpledemo.impls;

import cache.ICompositeKey;

/**
 * @author lidong@date 2024-06-27@version 1.0
 */
public class CompKey implements ICompositeKey {
    private String key;

    public CompKey(String key) {
        this.key = key;
    }

    @Override
    public String getCompositeKey() {
        return key;
    }

    @Override
    public String getSeparator() {
        return "";
    }
}
