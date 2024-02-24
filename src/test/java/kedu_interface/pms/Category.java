package kedu_interface.pms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import kedu_interface.init.Init;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import utils.GetJson;
import utils.HttpUtil;
import utils.JsonArraySort;
import utils.ReadTxt;

import java.util.Iterator;

import static utils.GetJson.getValByKey;

public class Category {

    /**
     * 根据上级查询下级分类列表
     *
     * @param
     * @return
     */
    public static JSONObject getCategoryList(String pid,String categoryName) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "kedu-pms/category/categoryListByPid";

        String param = "{\"pid\":\""+pid+"\",\"showFlag\":true}";
        HttpResponse response = HttpUtil.post(url, param, token);


        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String res = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(res);
        JSONObject getCategoryIdAndName = new JSONObject();
        JSONArray list = json.getJSONArray("body");
        list = JsonArraySort.arraySort(list);
        Iterator<Object> iterator = list.iterator();
        while (iterator.hasNext()) {
            JSONObject object = (JSONObject) iterator.next();
            String name = (String) object.get("name");
            if (name.equals(categoryName)) {
                String id = (String) GetJson.getValByKey(json, "id");
                getCategoryIdAndName.put("id", id);
                getCategoryIdAndName.put("name", name);
            }
        }

        return getCategoryIdAndName;

    }
}
