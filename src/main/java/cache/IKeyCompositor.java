package cache;

/**
 * @author lidong@date 2023-10-24@version 1.0
 * Cmposite keys to a single key.
 */
public interface IKeyCompositor {

    default String compositKeys(String[] keys){
        String ret = "";
        String seperator = getSeperator();
        for(int i = 0;i<keys.length;i++){
            ret += keys[i];
            if(i!= keys.length -1)
                ret +=seperator;
        }
        return ret;
    }

    String getSeperator();
}
