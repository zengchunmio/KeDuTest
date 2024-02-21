package kedu_interface.fms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import kedu_interface.init.Init;
import utils.GetJson;
import utils.HttpUtil;
import utils.ReadTxt;

/**
 * 查询财务模块
 */
public class QueryFms {
    /*
    查询核算单位
     */
    public static JSONObject queryUnit(){
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-rbac/api/unit/queryPageInfo";

        String param = "{\"pageNum\":1,\"pageSize\":10,\"keyword\":\"\"}";

        HttpResponse response = HttpUtil.post(url, param, token);


        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String res = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(res);

        return json;
    }

    /*
    查询核算单位详情:根据名称
    */
//    public static JSONObject queryUnitAccount(String unitName){
//        String token = ReadTxt.readFile();
//        String url = Init.url;
//        url += "xiaozi-fms/clearing/findPageList";
//        String accountingUnit = "上海小紫医疗科技有限公司";
//        String accountingUnitId = "978237183293001735";
//
//        if (unitName.contains("医学")){
//            accountingUnit = "上海小紫医学科技有限公司";
//            accountingUnitId = "978237183293001729";
//        }
//
//        String param = "{\"pageNum\":1,\"pageSize\":10,\"accountingUnit\":\""+accountingUnit+"\",\"accountingUnitId\":\""+accountingUnitId+"\"}";
//
//        HttpResponse response = HttpUtil.post(url, param, token);
//
//
//        int statusCode = response.getStatusLine().getStatusCode();
//        Assert.assertEquals(200, statusCode);
//
//        String res = HttpUtil.getResponse(response);
//        JSONObject json = JSON.parseObject(res);
//
//        return json;
//    }

    /*
      默认查询核算单位：上海小紫医疗科技有限公司详情
    */
    public static JSONObject queryUnitAccount(){
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-fms/clearing/findPageList";

        String param = "{\"pageNum\":1,\"pageSize\":10,\"accountingUnit\":\"上海小紫医疗科技有限公司\",\"accountingUnitId\":\"978237183293001735\"}";

        HttpResponse response = HttpUtil.post(url, param, token);


        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String res = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(res);

        return json;
    }

    /*
    默认本方账户
    */
    public static JSONObject queryProceeds(Integer accountType,String accountingUnit,String accountingUnitId){
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-fms/proceeds/findPageList";

        String param = "{\"accountType\":"+accountType+",\"pageNum\":1,\"pageSize\":10,\"accountingUnit\":\""+accountingUnit+"\",\"accountingUnitId\":\""+accountingUnitId+"\"}";

        HttpResponse response = HttpUtil.post(url, param, token);

        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String res = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(res);

        return json;
    }

    /*
    查询待开票库存订单
    */
    public static JSONObject queryIvDelivery(JSONObject jsonObject){
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-fms/iv/apply/page/sale/delivery";

        String customerName = (String) GetJson.getValByKey(jsonObject, "customerName");
        String customerId = (String) GetJson.getValByKey(jsonObject, "customerId");
        String orderCode = (String) GetJson.getValByKey(jsonObject, "orderCode");
        String accountingUnit = (String) GetJson.getValByKey(jsonObject, "accountingUnit");
        String accountingUnitId = (String) GetJson.getValByKey(jsonObject, "accountingUnitId");

        String param = "{\n" +
                "    \"customerName\":\""+customerName+"\",\n" +
                "    \"customerId\":\""+customerId+"\",\n" +
                "    \"orderCode\":\""+orderCode+"\",\n" +
                "    \"invoiceId\":\"\",\n" +
                "    \"deliveryCode\":\"\",\n" +
                "    \"goodsName\":\"\",\n" +
                "    \"deliveryDate\":[\n" +
                "\n" +
                "    ],\n" +
                "    \"accountingUnit\":\""+accountingUnit+"\",\n" +
                "    \"accountingUnitId\":\""+accountingUnitId+"\",\n" +
                "    \"pageSize\":10,\n" +
                "    \"pageNum\":1\n" +
                "}";

        HttpResponse response = HttpUtil.post(url, param, token);

        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String res = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(res);

        return json;
    }

    /**
     * 查询发票申请详情
     */
    public static JSONObject queryIvDetail(String ivId){
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-fms/iv/apply/detail";


        String param = "{\"id\":\""+ivId+"\"}";

        HttpResponse response = HttpUtil.post(url, param, token);

        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String res = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(res);

        return json;
    }

}
