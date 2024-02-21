package utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class JsonArraySort {
    public static JSONArray arraySort(JSONArray array) {
        List<JSONObject> list = JSONObject.parseArray(array.toJSONString(), JSONObject.class);
        Collections.sort(list, (JSONObject o1, JSONObject o2) -> {
            //转成JSON对象中保存的值类型
            Double a = Double.parseDouble(o1.getString("id"));
            Double b = Double.parseDouble(o2.getString("id"));
            // 如果a, b数据类型为int，可直接 return a - b ;(升序，降序为 return b - a;)
            if (a > b) {  //降序排列，升序改成a>b
                return 1;
            } else if (a == b) {
                return 0;
            } else {
                return -1;
            }
        });
        array = JSONArray.parseArray(JSON.toJSONString(list));
        return array;
    }
}
