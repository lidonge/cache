package cache.simpledemo.impls;

import cache.center.ICenterCacheData;
import cache.client.IClientCacheData;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public class DemoCacheData implements ICenterCacheData, IClientCacheData {
    static int sn = 0;
    private String compKey;
    private String value;
    private boolean prepareDirty;
    public DemoCacheData(String compKey) {
        this.compKey = compKey;
        value = "demo_" + sn++;
    }

    @Override
    public String toString() {
        return "DemoCacheData{" +
                "compKey='" + compKey + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean isPrepareDirty() {
        return prepareDirty;
    }

    @Override
    public void setPrepareDirty(boolean prepare) {
        prepareDirty = prepare;
    }
}
