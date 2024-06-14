package cache.util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public class TimeoutUtil {
    static TimeoutUtil timeoutUtil;
    Timer timer = new Timer("Timeout Timer");

    public static final TimeoutUtil getInstance(){
        if(timeoutUtil == null){
            synchronized (TimeoutUtil.class){
                if(timeoutUtil == null)
                    timeoutUtil = new TimeoutUtil();
            }
        }
        return timeoutUtil;
    }

    private TimeoutUtil() {
    }

    public void addMonitor(ITimeoutListener listener, String name, int timeout){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                listener.onTimeout(name);
            }
        }, timeout);
    }
}
