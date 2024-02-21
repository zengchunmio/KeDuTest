package utils;

import java.io.*;
import java.util.*;

public class GetProperites {
    /*
    获取properites内的值
     */
    public static Map<Object, Object> getProp(String fileName) {
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(GetProperites.class.getResourceAsStream(fileName), "UTF-8"));
            Enumeration enums = properties.keys();
            while (enums.hasMoreElements()) {
                Object key = enums.nextElement();
                map.put(key, properties.get(key));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /*
    修改properites内的值,未用到
     */
    public static void updata(String fileName, String key, String value) {
        File directory1 = new File("src/test/resources"+fileName);
        String absolutePath = directory1.getAbsolutePath();
        Properties prop = new Properties();
        try {
            prop.load(new InputStreamReader(GetProperites.class.getResourceAsStream(fileName), "UTF-8"));

            prop.setProperty(key, value);

            OutputStream out = new FileOutputStream(absolutePath);
            OutputStreamWriter ow = new OutputStreamWriter(out, "UTF-8");
            prop.store(out, "add");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
