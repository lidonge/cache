package cache.simpledemo.impls;

import cache.ICenterCacheData;
import cache.IClientCacheData;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public class DemoCacheData implements ICenterCacheData, IClientCacheData {
    static int sn = 0;
    private String compKey;
    private String value;
    private boolean prepareDirty;

    private boolean allAgreementReached = true;

    private DemoCacheData() {
    }

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

    @Override
    public IClientCacheData clone() {
        DemoCacheData ret = new DemoCacheData();
        ret.compKey = compKey;
        ret.value = value;
        ret.allAgreementReached = allAgreementReached;
        ret.prepareDirty = prepareDirty;
        return ret;
    }

    @Override
    public boolean isAllAgreementReached() {
        return allAgreementReached;
    }

    @Override
    public void setAllAgreementReached(boolean allAgreementReached) {
        this.allAgreementReached = allAgreementReached;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
