package cache.center;

import cache.IAsynListener;
import cache.util.ITimeoutListener;
import cache.util.TimeoutUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lidong@date 2024-06-05@version 1.0
 */
public class CallbackHandler implements ITimeoutListener {
    private String compKey;
    private IAsynCallbackable callbackable;
    private int callingCount = 0;
    private int finishCount = 0;
    private List<IAsynListener> listeners = new ArrayList<>();

    public CallbackHandler(String compKey) {
        this.compKey = compKey;
    }

    public void setCallbackable(IAsynCallbackable callbackable) {
        this.callbackable = callbackable;
    }

    public String getCompKey() {
        return compKey;
    }

    public IAsynCallbackable getCallbackable() {
        return callbackable;
    }

    public int getCallingCount() {
        return callingCount;
    }

    public int getFinishCount() {
        return finishCount;
    }

    public List<IAsynListener> getListeners() {
        return listeners;
    }

    public void start(int timeout){
        TimeoutUtil.getInstance().addMonitor(this,compKey,timeout);
    }
    IAsynListener addOne(){
        IAsynListener ret = new IAsynListener() {
            int pos = callingCount;
            Status status = Status.waiting;
            @Override
            public void onSuccess() {
                status = Status.success;
                finishOne(pos);
            }

            @Override
            public void onError() {
                status = Status.error;
                finishOne(pos);
            }

            @Override
            public void onTimeout() {
                status = Status.timeout;
                finishOne(pos);
            }

            @Override
            public Status getStatus() {
                return status;
            }
        };
        callingCount++;
        listeners.add(ret);
        return ret;
    }

    private void finishOne(int pos){
        finishCount++;
        if(finishCount == callingCount)
            callbackable.allFinished(compKey);
    }
    public boolean isFinished(){
        if(callingCount != 0 && callingCount == finishCount)
            return true;
        return false;
    }

    @Override
    public void onTimeout(String name) {
        if(!isFinished()) {
            for (IAsynListener listener : this.listeners) {
                if (listener.getStatus() == IAsynListener.Status.waiting)
                    listener.onTimeout();
            }
        }
    }
}
