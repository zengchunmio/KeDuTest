package kedu_interface.sale;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import kedu_interface.crm.Customer;
import kedu_interface.goods.AllGoods;
import kedu_interface.goods.SupplierGoods;
import kedu_interface.init.Init;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.Reporter;
import utils.HttpUtil;
import utils.ReadTxt;

public class CreateSaleOrder {
    /**
     * 创建销采一体订单
     * 1.生成客户及联系人、付款信息及日期、供应商及商品信息、收货人信息、销售模板
     * 2.生成总的销售参数
     * 3.发送请求
     */
    public static JSONObject createSaleOrder(String customeName, String SupplierName, String orderNum) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-scm/sale/contract/audit";

        JSONObject sale = new JSONObject();

        //增加客户及联系人信息
        JSONObject saleContractReqDTO = Customer.customerParam(customeName,true);
        sale.put("saleContractReqDTO", saleContractReqDTO);

        //增加付款信息及日期
        JSONObject date = SaleDate.getDate();
        JSONArray array = new JSONArray();
        array.add(date);
        sale.put("saleContractDateReqDTO", array);

        //增加供应商及商品信息
        JSONObject goods = SupplierGoods.getGoods(SupplierName, orderNum);
        JSONArray goodsArray = new JSONArray();
        goodsArray.add(goods);
        sale.put("saleContractGoodsReqDTO", goodsArray);

        //增加收货人信息
        JSONObject saleContractReceiverDTO = Customer.getReceiverDetail(Customer.getCompany(customeName).getString("customerId"));
        sale.put("saleContractReceiverDTO", saleContractReceiverDTO);

        //增加销售合同模版
        JSONObject saleContractTemplateReqDTO = kedu_interface.sale.SaleTemplate.getTemplateId();
        sale.put("saleContractTemplateReqDTO", saleContractTemplateReqDTO);

        String param = sale.toJSONString();

        HttpResponse response = HttpUtil.post(url, param, token);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String rs = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(rs);

        String code = json.getJSONObject("body").getString("code");
        String orderId = json.getJSONObject("body").getString("id");
        String goodsName = goods.getString("goodsName");

        JSONObject order = new JSONObject();
        order.put("code",code);
        order.put("orderId",orderId);

        Reporter.log("创建销采一体合同成功，销售合同号为："+code+".商品名称为："+goodsName);

        return order;
    }

    /**
     * 创建销售订单
     */
    public static JSONObject createSaleOrder(String customeName, String orderNum,String goodName,double price) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-scm/sale/contract/audit";

        JSONObject sale = new JSONObject();

        //增加客户及联系人信息
        JSONObject saleContractReqDTO = Customer.customerParam(customeName,false);
        sale.put("saleContractReqDTO", saleContractReqDTO);

        //增加付款信息及日期
        JSONObject date = SaleDate.getDate();
        JSONArray array = new JSONArray();
        array.add(date);
        sale.put("saleContractDateReqDTO", array);

        //增加商品信息
        JSONObject goods = AllGoods.goods(goodName,orderNum,price);
        JSONArray goodsArray = new JSONArray();
        goodsArray.add(goods);
        sale.put("saleContractGoodsReqDTO", goodsArray);

        //增加收货人信息
        JSONObject saleContractReceiverDTO = Customer.getReceiverDetail(Customer.getCompany(customeName).getString("customerId"));
        sale.put("saleContractReceiverDTO", saleContractReceiverDTO);

        //增加销售合同模版
        JSONObject saleContractTemplateReqDTO = SaleTemplate.getTemplateId();
        sale.put("saleContractTemplateReqDTO", saleContractTemplateReqDTO);

        String param = sale.toJSONString();

        HttpResponse response = HttpUtil.post(url, param, token);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String rs = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(rs);

        String code = json.getJSONObject("body").getString("code");
        String orderId = json.getJSONObject("body").getString("id");
        String goodsName = goods.getString("goodsName");

        JSONObject order = new JSONObject();
        order.put("code",code);
        order.put("orderId",orderId);

        Reporter.log("创建销售合同成功，销售合同号为："+code+".商品名称为："+goodsName);

        return order;
    }
}
