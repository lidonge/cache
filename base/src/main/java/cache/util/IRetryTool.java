package cache.util;

/**
 * @author lidong@date 2024-06-17@version 1.0
 */
public interface IRetryTool {
    void retry(IRetryHandler handler);
}
