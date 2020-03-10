package jalikejdbc;

import java.util.HashMap;
import java.util.Map;

public class ConnectionPool {
    private static String DEFAULT_NAME = "default";
    private static Map<String,ConnectionPool> pools = new HashMap<>();


    private ConnectionPool(){}

    public static ConnectionPool get(String name) {
        if (name != null || name.length()>0) {
            return pools.get(name);
        }
        return null;
    }

}
