package kedu_interface.delivery;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import kedu_interface.init.Init;
import utils.HttpUtil;
import utils.ReadTxt;

public class QueryDeliveryNotify {
    /**
     * 查询发货通知单
     */
    public static JSONObject queryDeliveryNotify(String contractCode) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-scm/sale/deliveryNotify/pageList";

        String param = "{\n" +
                "    \"status\":\"\",\n" +
                "    \"code\":\"\",\n" +
                "    \"orderCode\":\"\",\n" +
                "    \"customerHead\":\"\",\n" +
                "    \"warehouseId\":\"\",\n" +
                "    \"contractCode\":\""+contractCode+"\",\n" +
                "    \"pageNum\":1,\n" +
                "    \"pageSize\":10,\n" +
                "    \"createTimeFrom\":\"\",\n" +
                "    \"createTimeTo\":\"\",\n" +
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

        String rs = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(rs);

        return json;
    }
}
