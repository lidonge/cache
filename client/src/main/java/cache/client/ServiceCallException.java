package cache.client;

/**
 * @author lidong@date 2024-06-17@version 1.0
 */
public class ServiceCallException extends Exception {
    public ServiceCallException(String msg){
        super(msg);
    }

    public ServiceCallException(Throwable cause) {
        super(cause);
    }
}
