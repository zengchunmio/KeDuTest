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

public class SpuDetail {
    /**
     * 查询商品详情及获取商品详情参数
     * @param goodId
     * @return
     */
    public static JSONObject getSpuDetail(String goodId) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-pms/spu/detail";

        String param = "{\"id\":\"" + goodId + "\"}";

        HttpResponse response = HttpUtil.post(url, param, token);

        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String res = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(res);

        int code = (int) GetJson.getValByKey(json,"code");

        if (code == 200) {
            Reporter.log("查询商品明细成功");
        }

        if (code != 200) {
            Reporter.log("没有此商品");
        }

        JSONObject goodDetail = new JSONObject();
        String registerCode = (String) getValByKey(json, "registerCode");
        String brandId = (String) GetJson.getValByKey(json, "brandId");

        goodDetail.put("registerCode", registerCode);
        goodDetail.put("brandId", brandId);

        return goodDetail;
    }
}