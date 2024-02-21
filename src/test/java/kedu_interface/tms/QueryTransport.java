package kedu_interface.tms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import kedu_interface.delivery.QueryDeliveryNotify;
import kedu_interface.init.Init;
import org.testng.Reporter;
import utils.GetJson;
import utils.HttpUtil;
import utils.ReadTxt;

public class QueryTransport {
    /**
     * 根据发货通知单，查询运单号和运单详情
     * @return
     */
    public static JSONObject queryTransport(String contractCode) {
        //根据销售合同，查询发货通知单
        JSONObject notifyObject = QueryDeliveryNotify.queryDeliveryNotify(contractCode);
        String logisticsOrderCode = (String) GetJson.getValByKey(notifyObject,"logisticsOrderCode");
        Reporter.log("物流单号为："+logisticsOrderCode);

        //查询物流单
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-tms/transport/list";

        String param = "{\n" +
                "    \"transportCode\":\""+logisticsOrderCode+"\",\n" +
                "    \"handoverCode\":\"\",\n" +
                "    \"combinedboxCode\":\"\",\n" +
                "    \"customerCode\":\"\",\n" +
                "    \"carrierCode\":\"\",\n" +
                "    \"status\":\"\",\n" +
                "    \"pageNum\":1,\n" +
                "    \"pageSize\":10,\n" +
                "    \"waybillType\":\"\",\n" +
                "    \"orderBy\":[\n" +
                "        {\n" +
                "            \"orderByProperty\":\"createTime\",\n" +
                "            \"orderByType\":\"DESC\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        HttpResponse response = HttpUtil.post(url, param, token);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(200, statusCode);

        String rs = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(rs);

        return json;
    }

    /**
     * 根据物流单id，查询运单号详情
     * @return
     */
    public static JSONObject transportDetail(String transportId) {
        //查询物流单详情
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-tms/transport/detail";

        String param = "{\"id\":\""+transportId+"\"}";

        HttpResponse response = HttpUtil.post(url, param, token);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode,200);

        String rs = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(rs);

        return json;
    }

    /**
     * 根据物流单id，查询运单号详情
     * @return
     */
    public static JSONObject transportInfo(String transportBoxId) {
        //查询物流单详情
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-tms/transport/box/check/info";

        String param = "{\"transportBoxId\":\""+transportBoxId+"\"}";

        HttpResponse response = HttpUtil.post(url, param, token);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode,200);

        String rs = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(rs);

        return json;
    }
}
