package cache;

/**
 * @author lidong@date 2023-10-24@version 1.0
 * Cmposite keys to a single key.
 */
public interface ICompositeKey {
    static String DEFAULT_SEPERATOR = "_";

    static String compositeKeys(String[] keys,String seperator){
        String ret = "";
        for(int i = 0;i<keys.length;i++){
            ret += keys[i];
            if(i!= keys.length -1)
                ret +=seperator;
        }
        return ret;
    }

    /**
     * Get key string of all.
     * @return
     */
    String getCompositeKey();

    /**
     * Get composite separator
     * @return
     */
    String getSeparator();
}
