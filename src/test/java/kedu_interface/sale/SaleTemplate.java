package kedu_interface.sale;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.Reporter;
import kedu_interface.init.Init;
import utils.HttpUtil;
import utils.ReadTxt;

import static utils.GetJson.getValByKey;

public class SaleTemplate {
    /**
     * 获取销售模板
     *
     * @param
     * @return
     */
    public static JSONObject getTemplateId() {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-scm/sale/template/list";

        String param = "{\"name\":\"\",\"pageSize\":1,\"pageNum\":1,\"status\":\"ENABLE\"}";

        HttpResponse response = HttpUtil.post(url, param, token);

        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String res = HttpUtil.getResponse(response);

        JSONObject json = JSON.parseObject(res);

        int total = (int) getValByKey(json, "total");

        if (total == 0) {
            Reporter.log("没有销售模板");
        }

        JSONObject templateParam = new JSONObject();
        String id = (String) getValByKey(json, "id");
        templateParam.put("id",id);

        return templateParam;
    }
}
