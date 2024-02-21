package kedu_interface.fms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.Reporter;
import kedu_interface.init.Init;
import kedu_interface.sale.Query;
import utils.GetJson;
import utils.HttpUtil;
import utils.ReadTxt;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * 发票流程
 * 1.创建发票申请
 * 2.提交发票申请
 * 3.审批发票
 * 4.开票
 */
public class CreateInvoice {
    /**
     * 创建发票申请
     * @param contact
     * @return
     */
    public static String createInvoice(String contact){
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-fms/iv/apply/save";


        JSONObject jsonObject = invoiceParam(contact);
        String param = jsonObject.toString();

        HttpResponse response = HttpUtil.post(url, param, token);

        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String res = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(res);

        String message = (String) GetJson.getValByKey(json, "message");
        if (message.equals("success")){
            Reporter.log("开票申请成功");
        }

        String invoiceId = (String) GetJson.getValByKey(json, "body");

        return invoiceId;
    }

    /**
     * 提交发票申请
     * @param ivId
     * @return
     */
    public static void submitIv(String ivId){
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-fms/iv/apply/submit";


        String param = "{\"id\":\""+ivId+"\"}";

        HttpResponse response = HttpUtil.post(url, param, token);

        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String res = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(res);

        String message = (String) GetJson.getValByKey(json, "message");
        if (message.equals("success")){
            Reporter.log("提交成功");
        }
    }

    /**
        查询销售订单待开票的数据
     */
    public static JSONObject getIvDelivery(String contact){
        //查询订单中的核算单位
        JSONObject contactJson = Query.queryContract(contact);
        String accountingUnit = (String) GetJson.getValByKey(contactJson, "accountingUnit");

        //查询核算单位，把与订单匹配的核算单位参数取出
        JSONObject json = QueryFms.queryUnit();
        JSONArray jsonArray = json.getJSONObject("body").getJSONArray("list");
        Iterator<Object> iterator = jsonArray.iterator();
        JSONObject ivParam = new JSONObject();
        while (iterator.hasNext()){
            JSONObject jsonObject = (JSONObject) iterator.next();
            String accountName = (String) GetJson.getValByKey(jsonObject, "accountName");

            if (accountName.equals(accountingUnit)){
                String id = (String) GetJson.getValByKey(jsonObject, "id");
                ivParam.put("accountingUnit",accountName);
                ivParam.put("accountingUnitId",id);
            }
        }

        //查询订单详情，把订单放入参数中
        JSONObject orderJson = Query.queryOrder(contact);
        String orderCode = (String) GetJson.getValByKey(orderJson, "code");
        String customerName = (String) GetJson.getValByKey(orderJson, "customerHead");
        String customerId = (String) GetJson.getValByKey(orderJson, "customerId");
        ivParam.put("customerName",customerName);
        ivParam.put("customerId",customerId);
        ivParam.put("orderCode",orderCode);

        //查询订单待开票库存单
        JSONObject jsonObject = QueryFms.queryIvDelivery(ivParam);

        return jsonObject;
    }

    /**
    生成开票参数
    */
    public static JSONObject invoiceParam(String contact){
        JSONObject object = getIvDelivery(contact);
        JSONObject ivDelivery = object.getJSONObject("body").getJSONArray("list").getJSONObject(0);

        BigDecimal purePrice = (BigDecimal) GetJson.getValByKey(ivDelivery, "purePrice");
        Integer quantity = (Integer) GetJson.getValByKey(ivDelivery, "waitInvoiceQuantity");
        BigDecimal totalAmount = (BigDecimal) GetJson.getValByKey(ivDelivery, "taxPrice");
        BigDecimal tariffInit = (BigDecimal) GetJson.getValByKey(ivDelivery, "tariff");
        BigDecimal taxAmount = (BigDecimal) GetJson.getValByKey(ivDelivery, "taxAmount");


        String customerId = (String) GetJson.getValByKey(ivDelivery, "customerId");
        String customerName = (String) GetJson.getValByKey(ivDelivery, "customerName");
        String accountingUnit = (String) GetJson.getValByKey(ivDelivery, "accountingUnit");
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String time = format.format(date);

        ivDelivery.put("initPurePrice",purePrice);
        ivDelivery.put("quantity",quantity);
        ivDelivery.put("totalAmount",totalAmount);
        ivDelivery.put("tariffInit",tariffInit);
        ivDelivery.put("stockInQuantity",null);

        JSONObject jsonObject = new JSONObject();
        JSONArray attachList= new JSONArray();
        JSONArray itemList = new JSONArray();

        itemList.add(ivDelivery);

        jsonObject.put("attachList",attachList);
        jsonObject.put("itemList",itemList);

        jsonObject.put("applySource",1);
        jsonObject.put("buyerName",customerName);
        jsonObject.put("buyerId",customerId);
        jsonObject.put("buyerTaxNumber","");
        jsonObject.put("buyerAddressMobile","");
        jsonObject.put("buyerBankAccount","");
        jsonObject.put("invoiceType",2);
        jsonObject.put("expectInvoiceDate",time);
        jsonObject.put("pureAmount",purePrice);
        jsonObject.put("taxAmount",taxAmount);
        jsonObject.put("totalAmount",totalAmount);
        jsonObject.put("operator","陈晨");
        jsonObject.put("department","测试组");
        jsonObject.put("invoiceRemark","");
        jsonObject.put("applyRemark","");
        jsonObject.put("accountingUnit",accountingUnit);
        jsonObject.put("accountingUnitId","");

        return jsonObject;
    }

    /**
     * 开发票
     * @param ivId
     * @return
     */
    public static void openIv(String ivId){
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-fms/iv/apply/open/invoice";

        //生成开票日期
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String invoiceDate = format.format(date);

        //生成开票号码
        Date date1 = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("MMddhhmm");
        String invoiceCode = format1.format(date1);


        String param = "{\n" +
                "    \"invoiceNumber\":\"\",\n" +
                "    \"invoiceCode\":\""+invoiceCode+"\",\n" +
                "    \"invoiceDate\":\""+invoiceDate+"\",\n" +
                "    \"remark\":\"\",\n" +
                "    \"id\":\""+ivId+"\"\n" +
                "}";

        HttpResponse response = HttpUtil.post(url, param, token);

        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String res = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(res);

        String message = (String) GetJson.getValByKey(json, "message");
        if (message.equals("success")){
            Reporter.log("开票成功，发票号码为："+invoiceCode);
        }
    }
}
