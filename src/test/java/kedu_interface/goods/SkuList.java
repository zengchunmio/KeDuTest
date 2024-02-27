package kedu_interface.goods;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.Reporter;
import kedu_interface.init.Init;
import utils.GetJson;
import utils.HttpUtil;
import utils.JsonArraySort;
import utils.ReadTxt;

import java.util.Iterator;

import static utils.GetJson.getValByKey;

public class SkuList {
    public static JSONObject getSkuList(String categoryId, String name, String spec, String model, String code) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "kedu-pms/sku/skuList";

        String param = "{\"name\":\"" + name + "\",\"spec\":\"" + spec + "\",\"model\":\"" + model + "\",\"categoryId\":\"" + categoryId + "\",\"code\":\"" + code + "\",\"pageNum\":1,\"pageSize\":10,\"pageFlag\":true,\"spuId\":\"\",\"findCategoryAllNameFlag\":true,\"findSkuDeviceFlag\":true,\"findSkuImageFlag\":true,\"findSupplierFlag\":true,\"findSkuModelAttributeFlag\":true,\"status\":\"PASS\",\"orderBy\":[{\"orderByProperty\":\"lastUpdateTime\",\"orderByType\":\"DESC\"}]}";

        HttpResponse response = HttpUtil.post(url, param, token);

        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode, 200);

        String res = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(res);

        int total = (int) getValByKey(json, "total");

        if (statusCode == 200 && total != 0) {
            Reporter.log("查询商品成功");
        }

        if (total == 0) {
            Reporter.log("没有此商品");
        }


        JSONArray list = (JSONArray)getValByKey(json,"list");
        Iterator<Object> iterator = list.iterator();
        while (iterator.hasNext()) {
            JSONObject object = (JSONObject) iterator.next();
            JSONArray imageList = (JSONArray)object.get("imageList");
            for (int i = 0;i<imageList.size();i++){
                JSONObject image = (JSONObject) imageList.get(i);
                String imageUrl = (String) image.get("url");
            }
            String skuCategoryId = (String) getValByKey(json, "categoryId");
            String categoryName = (String) GetJson.getValByKey(json, "categoryName");
            String categoryAllName = (String) GetJson.getValByKey(json, "categoryAllName");
            String skuCode = (String) GetJson.getValByKey(json, "code");
            String skuModel = (String) GetJson.getValByKey(json, "model");
            String brandName = (String) GetJson.getValByKey(json, "brandName");
            String skuSpec = (String) GetJson.getValByKey(json, "spec");
            String unitName = (String) GetJson.getValByKey(json, "unitName");

            JSONObject goodParam = new JSONObject();
            goodParam.put("categoryId", skuCategoryId);
            goodParam.put("categoryName", categoryName);
            goodParam.put("categoryAllName", categoryAllName);
            goodParam.put("code", skuCode);
            goodParam.put("model", skuModel);
            goodParam.put("brandName", brandName);
            goodParam.put("spec", skuSpec);
            goodParam.put("unitName", unitName);


            return goodParam;

        }

        return null;
    }
}