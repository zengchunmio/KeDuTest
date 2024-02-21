package kedu_interface.sale;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.testng.Reporter;
import kedu_interface.saller.SallerLogin;
import utils.GetJson;

public class GetSaleContractStatus {
    /**
     * 1.查询合同状态、是否自动创建订单、是否自动创建采购合同，查询出关联的采购合同号
     * 2.合同状态为待供应商生效确认——调用供应商审批流
     * 3.合同状态为执行中且自动创建订单，日志输入订单号
     * 4.如自动创建采购合同，日志输入采购合同及采购订单号
     * @param contractCode
     */
    public static void getStatus(String contractCode) {
        //查询销售合同
        JSONObject json = Query.queryContract(contractCode);

        int executionStatus = (int) GetJson.getValByKey(json, "executionStatus");//合同执行状态
        Boolean autoOrderFlag = (Boolean) GetJson.getValByKey(json, "autoOrderFlag");//是否自动创建订单
        Boolean bindPurchaseFlag = (Boolean) GetJson.getValByKey(json, "bindPurchaseFlag");//是否自动创建采购合同
        JSONArray Array = (JSONArray) GetJson.getValByKey(json, "purchaseCodes");
        String purchaseCodes = Array.getString(0);//关联的采购合同号

        //合同状态为待供应商生效确认
        if (executionStatus == 6) {
            String supplierName = (String) GetJson.getValByKey(json, "supplierName");
            String purchesContractId = getPurchesContractId(purchaseCodes);

            SallerLogin.approveContract(purchesContractId, supplierName);
        }

        //获取的销售订单信息json
        JSONObject jsonObject = getOrderCode(contractCode);

        //合同状态为执行中
        if (executionStatus == 3) {
            Reporter.log("销售合同执行中");
            if (autoOrderFlag == true) {
                String orderCode = jsonObject.getString("orderCode");
                Reporter.log("创建销售订单成功，订单号为：" + orderCode);
            }
        }

        if (bindPurchaseFlag == true) {
            String purchaseOrderCode = jsonObject.getString("purchaseOrderCode");
            Reporter.log("创建采购合同成功，合同号为：" + purchaseCodes);
            Reporter.log("创建采购订单成功，订单号为：" + purchaseOrderCode);
        }

    }

    /**
     * 根据销售合同号查询销售订单信息
     *
     * @param contractCode
     * @return
     */
    public static JSONObject getOrderCode(String contractCode) {
        //查询销售订单
        JSONObject json = Query.queryOrder(contractCode);

        JSONArray list = (JSONArray) GetJson.getValByKey(json, "list");

        String orderCode = (String) GetJson.getValByKey(list, "code");

        String purchaseOrderCode = (String) GetJson.getValByKey(json, "purchaseOrderCode");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderCode", orderCode);
        jsonObject.put("purchaseOrderCode", purchaseOrderCode);

        return jsonObject;
    }

    /**
     * 根据采购合同查询采购合同ID
     */
    public static String getPurchesContractId(String contractCode) {
        //查询采购合同
        JSONObject json = Query.queryPurchesContract(contractCode);

        String PurchesContractId = (String) GetJson.getValByKey(json, "id");
        return PurchesContractId;
    }
}