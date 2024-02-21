package kedu_interface.init;

import utils.GetProperites;
import java.util.Map;

public abstract class Init {
    public static Map<Object, Object> map = GetProperites.getProp("/user.properties");
    public static String userName = (String) map.get("username");
    public static String passWord = (String) map.get("password");
    public static String url = (String) map.get("url");
    public static String redis = (String) map.get("redisNodes");
    public static String redisPassword = (String) map.get("redisPassword");
    public static String goodName = (String) map.get("goodName");
    public static String company = (String) map.get("company");
    public static String sellerUrl = (String) map.get("sellerUrl");
}
