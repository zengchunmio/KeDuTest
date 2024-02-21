package kedu_interface.sale;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.Reporter;
import kedu_interface.init.Init;
import utils.GetJson;
import utils.HttpUtil;
import utils.ReadTxt;

import java.util.Iterator;

/**
 * 销售订单发起收款
 */
public class ReceiptMoney {
    /**
     * 销售订单发起收款
     */
    public static void getMoney(String contract,String settlementId){
        String url = Init.url;
        String token = ReadTxt.readFile();
        url += "xiaozi-scm/sale/order/receipt/money";

        //获取销售订单id
        JSONObject jsonObject = Query.queryOrder(contract);
        String saleOrderId = (String) GetJson.getValByKey(jsonObject, "id");
        String customerId = (String) GetJson.getValByKey(jsonObject, "customerId");
        String customer = (String) GetJson.getValByKey(jsonObject, "customerHead");
        //获取销售订单待付款金额
        JSONObject object = Query.receiptInfo(saleOrderId);
        Object amount = GetJson.getValByKey(object, "waitReceiptAmount");
        String waitReceiptAmount = amount.toString();
        //获取收款单号
        String settlementCode = getSettlementCode(settlementId,customer,customerId);

        String param = "{\n" +
                "    \"saleOrderId\":\""+saleOrderId+"\",\n" +
                "    \"itemList\":[\n" +
                "        {\n" +
                "            \"amount\":\""+waitReceiptAmount+"\",\n" +
                "            \"settlementCode\":\""+settlementCode+"\",\n" +
                "            \"settlementId\":\""+settlementId+"\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        HttpResponse post = HttpUtil.post(url,param,token);
        int statusCode = post.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String response = HttpUtil.getResponse(post);
        JSONObject json = JSONObject.parseObject(response);

        String message = (String) GetJson.getValByKey(json, "message");
        if (message.equals("成功")){
            Reporter.log("收款成功,收款单号为："+settlementCode);
        }
    }

    /**
     * 获取跟结算单号匹配的收款单单号
     */
    public static String getSettlementCode(String settlementId,String customer,String customerId){
        JSONObject claimList = Query.findClaimList(customer,customerId);
        JSONArray array = claimList.getJSONArray("body");
        Iterator<Object> iterator = array.iterator();
        String settlementCode= "";
        while (iterator.hasNext()){
            JSONObject json = (JSONObject) iterator.next();
            String id = (String) GetJson.getValByKey(json, "id");
            if (id.equals(settlementId)){
                settlementCode = (String) GetJson.getValByKey(json,"settlementCode");
                return settlementCode;
            }
        }
        return "收款单已被认领";
    }
}
