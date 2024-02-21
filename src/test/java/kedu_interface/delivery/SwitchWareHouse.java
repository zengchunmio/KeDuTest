package kedu_interface.delivery;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.Reporter;
import kedu_interface.init.Init;
import utils.GetJson;
import utils.HttpUtil;
import utils.ReadTxt;

public class SwitchWareHouse {
    /**
     * 根据仓库id，切换仓库
     */
    public static void switchWareHouse(String contractCode) {
        //根据销售合同，查询发货通知单
        JSONObject notifyObject = QueryDeliveryNotify.queryDeliveryNotify(contractCode);
        String warehouseId = (String) GetJson.getValByKey(notifyObject,"warehouseId");
        String warehouseName = (String) GetJson.getValByKey(notifyObject,"warehouseName");

        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-rbac/switchWarehouse";

        String param = "{\"warehouseId\":\""+warehouseId+"\"}";

        HttpResponse response = HttpUtil.post(url, param, token);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String rs = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(rs);
        String message = (String) GetJson.getValByKey(json, "message");

        if (message.equals("success")){
            Reporter.log("切换仓库成功，仓库名称为："+warehouseName);
        }
    }
}
