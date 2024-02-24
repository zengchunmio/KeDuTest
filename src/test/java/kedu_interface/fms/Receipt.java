package kedu_interface.fms;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import kedu_interface.crm.Customer;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.Reporter;
import kedu_interface.init.Init;
import kedu_interface.sale.Query;
import utils.GetJson;
import utils.HttpUtil;
import utils.ReadTxt;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 收款流程
 * 1.新建收款单
 * 2.结算收款单
 */
public class Receipt {
    /**
     * 新建收款单
     *
     * @param customer
     * @return
     */
    public static JSONObject saveReceipt(String customer,String contact) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-fms/cmp/receipt/settle/save";

        if (customer==null){
            customer = "cc测试";
        }


        JSONObject param = new JSONObject();
        JSONObject paymentMsg = getPaymentMsg(customer,contact);

        JSONArray receiptSettlementItemReqDTOList = new JSONArray();
        JSONObject object2 = getPayAmout(contact);
        receiptSettlementItemReqDTOList.add(object2);

        param.put("receiptSettlementReqDTO",paymentMsg);
        param.put("receiptSettlementItemReqDTOList",receiptSettlementItemReqDTOList);

        String paramter = param.toJSONString();

        HttpResponse response = HttpUtil.post(url, paramter, token);

        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String res = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(res);

        String message = (String) GetJson.getValByKey(json, "message");

        if (message.equals("success")){
            Reporter.log("创建收款单成功");
        }

        return json;
    }

    /**
     * 根据结算单id,结算收款单
     *
     * @param
     * @return
     */
    public static void settlement(String ids) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-fms/cmp/receipt/settle/settlement";

        String param = "{\n" +
                "    \"ids\":[\n" +
                "        \""+ids+"\"\n" +
                "    ]\n" +
                "}";

        HttpResponse response = HttpUtil.post(url, param, token);

        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String res = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(res);

        String message = (String) GetJson.getValByKey(json, "message");
        if (message.equals("success")){
            Reporter.log("结算收款单成功");
        }
    }

    /**
     * 生成收款人付款人信息
     */
    public static JSONObject getPaymentMsg(String customer,String contact){
        //获取小紫医疗核算单位详情
        JSONObject unit = QueryFms.queryUnitAccount();
        String clearingFormName = (String) GetJson.getValByKey(unit, "clearingFormName");//新建收款参数
        String accountingUnitId = (String) GetJson.getValByKey(unit, "accountingUnitId");//新建收款参数
        String accountingUnit = (String) GetJson.getValByKey(unit, "accountingUnit");//新建收款参数
        String capitalType = (String) GetJson.getValByKey(unit, "capitalType");//新建收款参数
        String settlementTypeId = (String) GetJson.getValByKey(unit, "id");//新建收款参数

        //获取客户id
        JSONObject company = Customer.getCompany(customer);
        String customerId = (String) GetJson.getValByKey(company, "customerId");//新建收款参数
        //查询客户详情
        JSONObject customerDetail = Customer.getCustomerDetail(customerId);
        String bankAccount = (String) GetJson.getValByKey(customerDetail, "bankAccount");//新建收款参数
        String bankName = (String) GetJson.getValByKey(customerDetail, "bankName");//新建收款参数
        Integer accountType = 0;

        if (capitalType.equals("1")) {
            accountType = 2;
        } else if (capitalType.equals("2")) {
            accountType = 1;
        }

        //本方账号
        JSONObject jsonObject = QueryFms.queryProceeds(accountType, accountingUnit, accountingUnitId);
        String receiptAccountName = (String) GetJson.getValByKey(jsonObject, "accountName");//新建收款参数
        String receiptAccountId = (String) GetJson.getValByKey(jsonObject, "id");//新建收款参数

        JSONObject paymnet = new JSONObject();
        paymnet.put("settlementCode","");
        paymnet.put("contactsType","CUSTOMER");
        paymnet.put("payableId",customerId);
        paymnet.put("payableName",customer);
        paymnet.put("payableAccount",bankAccount);
        paymnet.put("payableBank",bankName);
        paymnet.put("settlementTypeId",settlementTypeId);
        paymnet.put("settlementType",clearingFormName);

        //创建结算日期
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new Date());

        paymnet.put("settlementDate",date);
        //获取合同金额
        JSONObject object = Query.queryOrder(contact);
        Long total = object.getJSONObject("body").getJSONArray("list").getJSONObject(0).getLong("total");
        paymnet.put("settlementAmount",total);
        paymnet.put("receiptAccountId",receiptAccountId);
        paymnet.put("receiptAccount","");
        paymnet.put("receiptAccountName",receiptAccountName);
        paymnet.put("receiptBank","");
        paymnet.put("pendedTotal","");
        paymnet.put("pendingTotal","");
        paymnet.put("remark","");
        paymnet.put("accountingUnitId",accountingUnitId);
        paymnet.put("accountingUnit",accountingUnit);
        paymnet.put("capitalType",capitalType);

        return paymnet;
    }

    /**
     * 生成收款金额信息
     */
    public static JSONObject getPayAmout(String contact){
        //获取合同金额
        JSONObject object = Query.queryOrder(contact);
        Long total = object.getJSONObject("body").getJSONArray("list").getJSONObject(0).getLong("total");

        JSONObject payAmout = new JSONObject();

        //创建收款银行流水号
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = format.format(new Date());
        String billCode = "LS"+date;

        //创建收款摘要
        String digest = "合同"+contact+"收款摘要";

        payAmout.put("digest",digest);
        payAmout.put("billCode",billCode);
        payAmout.put("settlementAmount",total);
        payAmout.put("detailId","");

        return payAmout;
    }
}
