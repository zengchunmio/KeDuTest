package kedu_interface.sale;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import kedu_interface.init.Init;
import utils.HttpUtil;
import utils.ReadTxt;

public class Query {
    /**
     * 根据销售合同号查询销售合同列表
     * @param contractCode
     */
    public static JSONObject queryContract(String contractCode) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-scm/sale/contract/page";

        String param = "{\"pageSize\":10,\"pageNum\":1,\"executionStatus\":[],\"code\":\"" + contractCode + "\",\"orderBy\":[{\"orderByProperty\":\"lastUpdateTime\",\"orderByType\":\"DESC\"}]}";

        HttpResponse response = HttpUtil.post(url, param, token);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String rs = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(rs);

        return json;
    }

    /**
     * 根据销售合同号查询销售订单信息
     *
     * @param contractCode
     * @return
     */
    public static JSONObject queryOrder(String contractCode) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-scm/sale/order/pageList";

        String param = "{\"pageSize\":10,\"pageNum\":1,\"status\":\"\",\"code\":\"\",\"contractCode\":\"" + contractCode + "\",\"orderBy\":[{\"orderByProperty\":\"lastUpdateTime\",\"orderByType\":\"DESC\"}]}";

        HttpResponse response = HttpUtil.post(url, param, token);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String rs = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(rs);

        return json;
    }

    /**
     * 查询采购合同
     */
    public static JSONObject queryPurchesContract(String contractCode) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-scm/purch/contract/queryPage";

        String param = "{\"flag\":1,\"supSettledFlag\":null,\"changeState\":\"\",\"agreementCode\":\"\",\"isFile\":\"\",\"billingState\":\"\",\"bindSaleCode\":\"\",\"collectState\":\"\",\"createEndTime\":\"\",\"createStartTime\":\"\",\"createUserName\":\"\",\"dueEndDate\":\"\",\"dueStartDate\":\"\",\"effectiveEndDate\":\"\",\"effectiveStartDate\":\"\",\"pageSize\":10,\"pageNum\":1,\"payState\":\"\",\"payWay\":\"\",\"pcCode\":\"" + contractCode + "\",\"pcType\":\"\",\"signEndDate\":\"\",\"signStartDate\":\"\",\"state\":\"\",\"supName\":\"\",\"orderBy\":[{\"orderByProperty\":\"lastUpdateTime\",\"orderByType\":\"DESC\"}]}";

        HttpResponse response = HttpUtil.post(url, param, token);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String rs = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(rs);

        return json;
    }

    /**
     * 根据销售订单id查询订单详情
     */
    public static JSONObject queryOrderDetail(String orderId) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-scm/sale/order/detail";

        String param = "{\"id\":\"" + orderId + "\"}";

        HttpResponse response = HttpUtil.post(url, param, token);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String rs = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(rs);

        return json;
    }

    /**
     * 查询采购订单
     */
    public static JSONObject queryPurchesOrder(String contractCode) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-scm/purch/order/queryPage";

        String param = "{\n" +
                "    \"orderCode\":\"\",\n" +
                "    \"supSettledFlag\":null,\n" +
                "    \"supName\":\"\",\n" +
                "    \"canApplyPaySts\":\"\",\n" +
                "    \"orderSts\":\"\",\n" +
                "    \"createUserName\":\"\",\n" +
                "    \"saleOrderCode\":\"\",\n" +
                "    \"endCreateTime\":\"\",\n" +
                "    \"beginCreateTime\":\"\",\n" +
                "    \"payType\":\"\",\n" +
                "    \"pcCode\":\""+contractCode+"\",\n" +
                "    \"pageSize\":10,\n" +
                "    \"pageNum\":1,\n" +
                "    \"payState\":\"\",\n" +
                "    \"billingState\":\"\",\n" +
                "    \"collectState\":\"\",\n" +
                "    \"agreementCode\":\"\",\n" +
                "    \"decision\":\"\"\n" +
                "}";

        HttpResponse response = HttpUtil.post(url, param, token);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String rs = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(rs);

        return json;
    }

    /**
     * 查询采购spu
     */
    public static JSONObject queryMaterialList(String contractId) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-scm/purch/agreement/queryMaterialList";

        String param = "{\n" +
                "    \"id\":\""+contractId+"\",\n" +
                "    \"flag\":2,\n" +
                "    \"enable\":false\n" +
                "}";

        HttpResponse response = HttpUtil.post(url, param, token);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String rs = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(rs);

        return json;
    }

    /**
     * 根据销售订单id查询付款信息
     *
     * @param orderId
     * @return
     */
    public static JSONObject receiptInfo(String orderId) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-scm/sale/order/receipt/money/info";

        String param = "{\"id\":\""+orderId+"\"}";

        HttpResponse response = HttpUtil.post(url, param, token);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String rs = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(rs);

        return json;
    }

    /**
     * 根据客户id查询客户的所有未认领完的收款单
     *
     * @param customerId
     * @return
     */
    public static JSONObject findClaimList(String customer,String customerId) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-fms/cmp/receipt/claim/findClaimList";

        String param = "{\n" +
                "    \"contactsType\":\"CUSTOMER\",\n" +
                "    \"receiptPaymentTypeEnum\":\"RECEIPT\",\n" +
                "    \"payableId\":\""+customerId+"\",\n" +
                "    \"payableName\":\""+customer+"\",\n" +
                "    \"type\":1\n" +
                "}";

        HttpResponse response = HttpUtil.post(url, param, token);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String rs = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(rs);

        return json;
    }

}
