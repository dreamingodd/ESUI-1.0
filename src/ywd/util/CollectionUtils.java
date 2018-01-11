package ywd.util;

import java.util.Collection;
import java.util.Map;

/**
 * @author dreamingodd
 *
 */
public class CollectionUtils {

    private CollectionUtils() { }

    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Collection collection) {
        if (collection == null || collection.size() == 0) {
            return true;
        } else return false;
    }
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Map collection) {
        if (collection == null || collection.size() == 0) {
            return true;
        } else return false;
    }
}
