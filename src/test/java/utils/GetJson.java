package utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class GetJson {
    /**
     * 主函数
     *
     * @param args
     */
    public static void main(String[] args) {
        String str = "{\"HeWeather6\":[{\"basic\":{\"cid\":\"CN101010100\",\"location\":\"北京\",\"parent_city\":\"北京\",\"admin_area\":\"北京\",\"cnty\":\"中国\",\"lat\":\"39.90498734\",\"lon\":\"116.40528870\",\"tz\":\"8.0\"},\"daily_forecast\":[{\"cond_code_d\":\"103\",\"cond_code_n\":\"101\",\"cond_txt_d\":\"晴间多云\",\"cond_txt_n\":\"多云\",\"date\":\"2017-10-26\",\"hum\":\"57\",\"pcpn\":\"0.0\",\"pop\":\"0\",\"pres\":\"1020\",\"tmp_max\":\"16\",\"tmp_min\":\"8\",\"uv_index\":\"3\",\"vis\":\"16\",\"wind_deg\":\"0\",\"wind_dir\":\"无持续风向\",\"wind_sc\":\"微风\",\"wind_spd\":\"5\"},{\"cond_code_d\":\"101\",\"cond_code_n\":\"501\",\"cond_txt_d\":\"多云\",\"cond_txt_n\":\"雾\",\"date\":\"2017-10-27\",\"hum\":\"56\",\"pcpn\":\"0.0\",\"pop\":\"0\",\"pres\":\"1018\",\"tmp_max\":\"18\",\"tmp_min\":\"9\",\"uv_index\":\"3\",\"vis\":\"20\",\"wind_deg\":\"187\",\"wind_dir\":\"南风\",\"wind_sc\":\"微风\",\"wind_spd\":\"6\"},{\"cond_code_d\":\"101\",\"cond_code_n\":\"101\",\"cond_txt_d\":\"多云\",\"cond_txt_n\":\"多云\",\"date\":\"2017-10-28\",\"hum\":\"26\",\"pcpn\":\"0.0\",\"pop\":\"0\",\"pres\":\"1029\",\"tmp_max\":\"17\",\"tmp_min\":\"5\",\"uv_index\":\"2\",\"vis\":\"20\",\"wind_deg\":\"2\",\"wind_dir\":\"北风\",\"wind_sc\":\"3-4\",\"wind_spd\":\"19\"}],\"status\":\"ok\",\"update\":{\"loc\":\"2017-10-26 23:09\",\"utc\":\"2017-10-26 15:09\"}}]}";
        JSONObject jo = JSON.parseObject(str);
        Object loc = getValByKey(jo, "loc");
        System.out.println(loc);
    }

    /**
     * 通过KEY查询VALUE
     *
     * @param obj
     * @param name
     * @return
     */
    public static Object getValByKey(Object obj, String name) {
        Map<String, Object> map = new HashMap<>();
        getValForObj(obj, name, map);
        return map.get(name);
    }

    /**
     * 查询值
     *
     * @param obj
     * @param name
     * @param map
     */
    public static void getValForObj(Object obj, String name, Map<String, Object> map) {
        if (obj instanceof JSONObject) {
            getValForJObj((JSONObject) obj, name, map);
        }
        if (obj instanceof JSONArray) {
            getValForJArr((JSONArray) obj, name, map);
        }
    }

    /**
     * 处理JsonObject 对象查值
     *
     * @param jobj
     * @param name
     * @param map
     */
    public static void getValForJObj(JSONObject jobj, String name, Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : jobj.entrySet()) {
//			System.out.println("key:"+entry.getKey()+"|val:"+entry.getValue()+"|cls:"+entry.getValue().getClass());
            if (StringUtils.equals(entry.getKey(), name)) {
                map.put(entry.getKey(), entry.getValue());
            }
            getValForObj(entry.getValue(), name, map);
        }
    }

    /**
     * 处理JsonAarray 对象查值
     *
     * @param arr
     * @param name
     * @param map
     */
    public static void getValForJArr(JSONArray arr, String name, Map<String, Object> map) {
        Iterator<Object> iner = arr.iterator();
        while (iner.hasNext()) {
            getValForObj(iner.next(), name, map);
        }
    }
}
