package cache.impls.client;

import cache.IClientCacheData;

/**
 * @author lidong@date 2024-06-26@version 1.0
 */
public class ClientCacheData implements IClientCacheData {
    private boolean bPrepared = false;
    private Object value;

    @Override
    public boolean isPrepareDirty() {
        return bPrepared;
    }

    @Override
    public void setPrepareDirty(boolean b) {
        bPrepared = b;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public ClientCacheData setValue(Object value) {
        this.value = value;
        return this;
    }
    @Override
    public IClientCacheData clone() {
        ClientCacheData ret = new ClientCacheData();
        ret.value = value;
        ret.bPrepared = bPrepared;
        return ret;
    }
}
