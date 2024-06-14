package cache;

/**
 * @author lidong@date 2023-10-25@version 1.0
 * An Asynchronous communication listener for get the return from client.
 */
public interface IAsynListener {
    public enum Status{
        waiting, success,error,timeout
    }
    void onSuccess();
    void onError();
    void onTimeout();

    Status getStatus();
}
