package kedu_interface.delivery;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import kedu_interface.init.Init;
import utils.GetJson;
import utils.HttpUtil;
import utils.ReadTxt;

import java.text.SimpleDateFormat;
import java.util.Date;

import static utils.GetJson.getValByKey;

public class Logistics {
    /**
     * 获取物流供应商信息
     * 1.查询物流供应商list
     * 2.判断物流信息是否完整，是否启用，是否有承运方式，运费计算方式是否填写，是否有承运联系人(暂未 判断）
     * 3.获取有效的物流商信息，保存至json内，给创建发货单时调用
     * @return
     */
    public static JSONObject getCarrier() {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-crm/customer/page";

        String param = "{\n" +
                "    \"status\":\"EFFECTIVE\",\n" +
                "    \"type\":\"CARRIER\",\n" +
                "    \"pageSize\":1,\n" +
                "    \"nameOrNameEn\":\"\",\n" +
                "    \"pageNum\":1\n" +
                "}";

        HttpResponse response = HttpUtil.post(url, param, token);

        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode,200 );

        String res = HttpUtil.getResponse(response);

        JSONObject json = JSON.parseObject(res);

        JSONObject carrier = new JSONObject();
        String code = (String) getValByKey(json, "code");
        String id = (String) GetJson.getValByKey(json, "id");
        String name = (String) GetJson.getValByKey(json, "name");

        carrier.put("logisticsCode", code);
        carrier.put("logisticsId", id);
        carrier.put("logisticsName", name);

        //生成物流单号
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String logisticNum ="WL"+ dateFormat.format(date);

        carrier.put("logisticsOrderCode", logisticNum);

        return carrier;
    }
}
