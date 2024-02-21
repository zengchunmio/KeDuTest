package utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FastJsonUtil {
    /**
     * 递归解析JSONObject转换成map
     * 存在的问题：如果json数据中存在一样的key，则后面的值会覆盖前面的key
     *
     * @param jsonObject jsonObject
     * @return Map<String, Object>
     */
    public static Map<String, Object> analysis(JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        Set<String> keys = jsonObject.keySet();
        keys.parallelStream().forEach(key -> {
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                JSONObject valueJsonObject = (JSONObject) value;
                result.putAll(analysis(valueJsonObject));
            } else if (value instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) value;
                if (jsonArray.size() == 0) {
                    return;
                }
                if (jsonArray.get(0) instanceof JSONArray || jsonArray.get(0) instanceof JSONObject) {
                    analysisArray(jsonArray, result);
                } else {
                    result.put(key, value);
                }
            } else {
                result.put(key, value);
            }
        });
        return result;
    }

    /**
     * 递归解析JSONArray
     *
     * @param jsonArray json数组
     * @param map       Map<String, Object>
     */
    private static void analysisArray(JSONArray jsonArray, Map<String, Object> map) {
        jsonArray.parallelStream().forEach(json -> {
            if (json instanceof JSONObject) {
                JSONObject valueJsonObject = (JSONObject) json;
                map.putAll(analysis(valueJsonObject));
            } else if (json instanceof JSONArray) {
                JSONArray tmpJsonArray = (JSONArray) json;
                if (tmpJsonArray.size() == 0) {
                    return;
                }
                analysisArray(tmpJsonArray, map);
            }
        });
    }
}
