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

public class SpuList {
    /**
     * 查询商品明细
     *
     * @param goodName
     * @return
     */
    public static JSONObject getSpuList(String goodName) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-pms/spu/spuList";

        String param = "{\n" +
                "    \"name\":\"" + goodName + "\",\n" +
                "    \"status\":null,\n" +
                "    \"medicalFlag\":null,\n" +
                "    \"pageNum\":1,\n" +
                "    \"pageSize\":1,\n" +
                "    \"orderBy\":[\n" +
                "        {\n" +
                "            \"orderByProperty\":\"createTime\",\n" +
                "            \"orderByType\":\"DESC\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        HttpResponse response = HttpUtil.post(url, param, token);

        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String res = HttpUtil.getResponse(response);
        com.alibaba.fastjson.JSONObject json = JSON.parseObject(res);

        int total = (int) getValByKey(json, "total");

        if (statusCode == 200 && total != 0) {
            Reporter.log("查询商品成功，商品名称为：" + (GetJson.getValByKey(json, "name")));
        }

        if (total == 0) {
            Reporter.log("没有此商品" + goodName + ",查询系统第一条商品");
            param = "{\"name\":null,\"status\":null,\"medicalFlag\":null,\"pageNum\":1,\"pageSize\":1,\"orderBy\":[{\"orderByProperty\":\"createTime\",\"orderByType\":\"DESC\"}]}";
            response = HttpUtil.post(url, param, token);
            String updateRes = HttpUtil.getResponse(response);
            json = JSON.parseObject(updateRes);
        }

        JSONObject goodParam = new JSONObject();

        String goodsCode = (String) getValByKey(json, "goodsCode");
        String id = (String) json.getJSONObject("body").getJSONArray("list").getJSONObject(0).get("id");
        String name = (String) GetJson.getValByKey(json, "name");
        String spec = (String) GetJson.getValByKey(json, "spec");
        String brandName = (String) GetJson.getValByKey(json, "brandName");
        boolean medicalFlag = (boolean) GetJson.getValByKey(json, "medicalFlag");
        String enterpriseName = (String) GetJson.getValByKey(json, "enterpriseName");

        goodParam.put("goodsCode", goodsCode);
        goodParam.put("id", id);
        goodParam.put("name", name);
        goodParam.put("spec", spec);
        goodParam.put("brandName", brandName);
        goodParam.put("medicalFlag", medicalFlag);
        goodParam.put("enterpriseName", enterpriseName);

        return goodParam;
    }
}