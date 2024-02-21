package kedu_interface.goods;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.Reporter;
import kedu_interface.init.Init;
import utils.GetJson;
import utils.HttpUtil;
import utils.ReadTxt;

import static utils.GetJson.getValByKey;

public class SkuList {
    public static JSONObject getSkuList(String goodId) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "/xiaozi-pms/sku/skuList";

        String param = "{\n" +
                "    \"pageNum\":1,\n" +
                "    \"pageSize\":1,\n" +
                "    \"spuId\":\"" + goodId + "\"" +
                "}";

        HttpResponse response = HttpUtil.post(url, param, token);

        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String res = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(res);

        int total = (int) getValByKey(json, "total");

        if (statusCode == 200 && total != 0) {
            Reporter.log("查询商品单位成功");
        }

        if (total == 0) {
            Reporter.log("没有此商品");
        }

        JSONObject goodUnit = new JSONObject();

        String skuId = (String) getValByKey(json, "skuId");
        String unitName = (String) GetJson.getValByKey(json, "unitName");
        String unitId = (String) GetJson.getValByKey(json, "unitId");
        String spuId = (String) GetJson.getValByKey(json, "spuId");

        goodUnit.put("skuId",skuId);
        goodUnit.put("unitName",unitName);
        goodUnit.put("unitId",unitId);
        goodUnit.put("spuId",spuId);

        return goodUnit;
    }
}