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

public class AllGoods {
    public static JSONObject goods(String goodName,String salesVolume,double unitPrice) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-scm/sale/goods/goodsStock";

        String param = "{\"pageSize\":10,\"pageNum\":1,\"goodsName\":\"" + goodName + "\"}";

        HttpResponse post = HttpUtil.post(url, param, token);
        int statusCode = post.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String rs = HttpUtil.getResponse(post);
        JSONObject json = JSON.parseObject(rs);
        int total = (int) getValByKey(json, "total");

        if (total == 0) {
            param = "{\"pageSize\":1,\"pageNum\":1}";
            post = HttpUtil.post(url, param, token);
            String updateRes = HttpUtil.getResponse(post);
            json = JSON.parseObject(updateRes);
            String goodsName = (String) GetJson.getValByKey(json, "goodsName");
            Reporter.log("没有此商品(" + goodName + "),使用系统默认商品:（"+goodsName+")");
        }

        JSONObject goodParam = new JSONObject();

        String goodsCode = String.valueOf(getValByKey(json,"goodsCode"));
        String goodsName = String.valueOf(getValByKey(json,"goodsName"));
        String spec = String.valueOf(getValByKey(json,"spec"));
        String brandName = String.valueOf(getValByKey(json,"brandName"));
        String brandId = String.valueOf(getValByKey(json,"brandId"));
        String skuId = String.valueOf(getValByKey(json,"skuId"));
        String manufacturer = String.valueOf(getValByKey(json,"enterpriseName"));
        Boolean medical = (Boolean) getValByKey(json,"medicalFlag");
        String productUnit = String.valueOf(getValByKey(json,"unitName"));
        String saleableInventory = String.valueOf(getValByKey(json,"saleStockNum"));
        String productUnitId = String.valueOf(getValByKey(json,"unitId"));
        String certificateNumber = String.valueOf(getValByKey(json,"registerCode"));
        String spuId = String.valueOf(getValByKey(json,"spuId"));

        int isMedical = -1;

        if (medical){
            isMedical = 1;
        }else {
            isMedical = 0;
        }

        goodParam.put("goodsCode",goodsCode);
        goodParam.put("goodsName",goodsName);
        goodParam.put("spec",spec);
        goodParam.put("brandName",brandName);
        goodParam.put("brandId",brandId);
        goodParam.put("skuId",skuId);
        goodParam.put("manufacturer",manufacturer);
        goodParam.put("isMedical",isMedical);
        goodParam.put("productUnit",productUnit);
        goodParam.put("saleableInventory",saleableInventory);
        goodParam.put("unitPrice",unitPrice);
        goodParam.put("productUnitId",productUnitId);
        goodParam.put("certificateNumber",certificateNumber);
        goodParam.put("spuId",spuId);
        goodParam.put("salesVolume",salesVolume);

        return goodParam;
    }
}
