package kedu_interface.delivery;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.Reporter;
import kedu_interface.customer.Customer;
import kedu_interface.init.Init;
import kedu_interface.sale.Query;
import utils.FormatDate;
import utils.GetJson;
import utils.HttpUtil;
import utils.ReadTxt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class CreateDeliveryNotify {
    /**
     * 创建采销一体发货通知单
     * 1.生成收货人信息、商品信息、供应商及sku信息、物流信息
     * 2.生成总的发货通知单参数
     * 3.发送请求
     */
    public static String createSaleOrder(String contractCode, String comanyName, String orderNum) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-scm/sale/deliveryNotify/create";

        JSONObject deliveryNotify = new JSONObject();

        //增加收货人信息
        JSONObject jsonObject = Query.queryOrder(contractCode);
        String id = (String) GetJson.getValByKey(jsonObject,"id");
        deliveryNotify.put("orderId",id);
        JSONObject receive = (JSONObject) GetJson.getValByKey(jsonObject,"receiver");

        Set<String> keySet = receive.keySet();
        for (String key :keySet){
            if (key.equals("address")){
                deliveryNotify.put("detailAddress",receive.get(key));
                continue;
            }
            if (key.equals("phone")){
                deliveryNotify.put("receiverMobilePhone",receive.get(key));
                continue;
            }

            if (key.equals("name")){
                deliveryNotify.put("receiverName",receive.get(key));
                continue;
            }

            deliveryNotify.put(key,receive.get(key));
        }

        //增加是否草稿、计划发货日期、物流信息是否已知、备注、是否自动审核
        deliveryNotify.put("draftFlag",false);
        deliveryNotify.put("expectedDeliveryDate","");
        deliveryNotify.put("logisticsKnownFlag",true);
        deliveryNotify.put("remark","");
        deliveryNotify.put("autoAuditFlag",true);

        //增加客户收货人地址id
        JSONObject company = Customer.getCompany(comanyName);
        String customerId = company.getString("customerId");
        String addressId = (String) GetJson.getValByKey(Customer.getReceiverDetail(customerId), "id");
        deliveryNotify.put("addressId",addressId);

        //增加商品信息
        JSONArray goodsReqDTOS = ordergoods(contractCode, orderNum);
        deliveryNotify.put("goodsReqDTOS",goodsReqDTOS);

        //增加供应商及sku信息
        JSONArray supplierSku = supplierSku(contractCode, orderNum);
        deliveryNotify.put("purchaseReqDTOS", supplierSku);

        //增加物流信息
        JSONObject carrier = Logistics.getCarrier();
        Set<String> keySet1 = carrier.keySet();
        for (String key :keySet1){
            deliveryNotify.put(key,carrier.get(key));
        }

        //增加发货同行单
        JSONArray carriedBill = createCarriedBill();
        deliveryNotify.put("supplyAccompanyBillReqDTOList",carriedBill);

        //发送请求
        String param = deliveryNotify.toJSONString();

        HttpResponse response = HttpUtil.post(url, param, token);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode, 200);

        String rs = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(rs);

        String deliveryId = (String) GetJson.getValByKey(json,"body");

        JSONObject notifyObject = QueryDeliveryNotify.queryDeliveryNotify(contractCode);
        String notify = (String) GetJson.getValByKey(notifyObject,"code");

        Reporter.log("发货通知单创建成功，通知单号为："+notify);

        return notify;
    }

    /**
     * 根据销售合同，查询商品信息
     * 生成商品参数
     * @param contractCode
     * @param notifyNum
     * @return
     */
    public static JSONArray ordergoods(String contractCode, String notifyNum) {
        //根据销售订单，查询订单id
        JSONObject jsonObject = Query.queryOrder(contractCode);
        String orderId = (String) GetJson.getValByKey(jsonObject, "id");
        //根据订单id，查询出订单中商品orderGoodsId
        JSONObject detail = Query.queryOrderDetail(orderId);
        String id = detail.getJSONObject("body").getJSONArray("goodsList").getJSONObject(0).getString("id");

        //增加goodsSerialList的json数组
        JSONArray goodsSerialList = new JSONArray();
        JSONObject list = new JSONObject();
        Map date = FormatDate.getDate();

        Long startTime = Long.valueOf((String) date.get("today") ) ;
        Long endTime = Long.valueOf((String) date.get("nextYear") ) ;

        //生成生产许可证
        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String serialNumber = dateFormat.format(today);

        list.put("expiryDate",endTime);
        list.put("productionBatchNumber","");
        list.put("productionDate",startTime);
        list.put("productionSerialNumber",serialNumber);
        goodsSerialList.add(list);

        //增加数据和订单商品id
        JSONObject json = new JSONObject();
        json.put("notifyNum",Integer.valueOf(notifyNum));
        json.put("orderGoodsId",id);

        json.put("goodsSerialList",goodsSerialList);

        JSONArray goodsReqDTOS = new JSONArray();
        goodsReqDTOS.add(json);

        return goodsReqDTOS;
    }

    /**
     * 根据采购订单，查询商品供应商信息和sku
     * 生成商品参数
     * @param contractCode
     * @return
     */
    public static JSONArray supplierSku(String contractCode, String orderNum) {
        //根据销售合同，查询采购订单
        JSONObject jsonObject = Query.queryContract(contractCode);
        JSONArray Array = (JSONArray) GetJson.getValByKey(jsonObject, "purchaseCodes");
        String purchaseCodes = Array.getString(0);//关联的采购合同号

        //查询采购订单
        JSONObject purchesOrder = Query.queryPurchesOrder(purchaseCodes);
        String contractId = (String) GetJson.getValByKey(purchesOrder, "contractId");

        //查询采购订单spu
        JSONObject materialList = Query.queryMaterialList(contractId);

        String orderCode = (String) GetJson.getValByKey(purchesOrder, "orderCode");
        String id = (String) GetJson.getValByKey(purchesOrder, "id");
        String materialId = (String) GetJson.getValByKey(materialList, "materialId");
        String supCode = (String) GetJson.getValByKey(purchesOrder, "supCode");
        String supId = (String) GetJson.getValByKey(purchesOrder, "supId");
        String supName = (String) GetJson.getValByKey(purchesOrder, "supName");


        //增加数据和订单商品id
        JSONObject json = new JSONObject();
        json.put("consumeNum",orderNum);
        json.put("purchaseOrderCode",orderCode);
        json.put("purchaseOrderId",id);
        json.put("skuId",materialId);
        json.put("supplierCode",supCode);
        json.put("supplierId",supId);
        json.put("supplierName",supName);


        JSONArray purchaseReqDTOS = new JSONArray();
        purchaseReqDTOS.add(json);

        return purchaseReqDTOS;
    }

    /**
     * 生成发货随行单附件
     */
    public static JSONArray createCarriedBill() {
        JSONArray array = new JSONArray();
        JSONObject json = new JSONObject();
        json.put("url","http://test.file.erp.xiaoziyixue.cn/886992572290043904/1655432383400/企业微信截图_20220222151508.png");
        json.put("attachId","FkPhXZS2JYK0aX13ho3XeUAbn_Gp");
        json.put("attachName","企业微信截图_20220222151508.png");
        json.put("uid","1655432383400");
        json.put("size","83832");
        json.put("type","image/png");
        json.put("name","企业微信截图_20220222151508.png");
        array.add(json);

        return array;
    }

}
