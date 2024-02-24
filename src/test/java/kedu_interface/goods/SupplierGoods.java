package kedu_interface.goods;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.Reporter;
import kedu_interface.init.Init;
import utils.HttpUtil;
import utils.ReadTxt;

import java.util.Iterator;

import static utils.GetJson.getValByKey;

public class SupplierGoods {
    public static JSONObject getGoods(String customerName,String salesVolume) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-pms/sku/supplier/goodsPageList";

        JSONObject company = Customer.getCompany(customerName);
        String id = company.getString("id");


        String param = "{\"pageSize\":10,\"pageNum\":1,\"customerName\":\"" + customerName + "\",\"customerId\":\"" + id + "\",\"status\":\"UP\"}";

        HttpResponse response = HttpUtil.post(url, param, token);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String rs = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(rs);
        int total = (int) getValByKey(json, "total");

        //没有商品的客户，需要再加一个逻辑
        if (total == 0) {
            Reporter.log("此客户没有供应商品");
            String name = getSupplier();

            company = Customer.getCompany(name);
            id = company.getString("id");


            param = "{\"pageSize\":10,\"pageNum\":1,\"customerName\":\"" + name + "\",\"customerId\":\"" + id + "\",\"status\":\"UP\"}";

            response = HttpUtil.post(url, param, token);
            statusCode = response.getStatusLine().getStatusCode();
            Assert.assertEquals(200, statusCode);

            rs = HttpUtil.getResponse(response);
            json = JSON.parseObject(rs);
        }

        JSONObject jsonObject = new JSONObject();
        JSONObject good = new JSONObject();

        JSONArray jsonArray = json.getJSONObject("body").getJSONArray("list");
        Iterator<Object> iterator = jsonArray.iterator();
        while (iterator.hasNext()){
            Object object = iterator.next();
            jsonObject = (JSONObject) JSONObject.toJSON(object);
            String salePrice = jsonObject.getString("salePrice");
            if (salePrice!=null){
                String goodsCode = jsonObject.getString("goodsCode");
                String goodsName = jsonObject.getString("name");
                String spec = jsonObject.getString("spec");
                String brandName = jsonObject.getString("brandName");
                String brandId = jsonObject.getString("brandId");
                String skuId = jsonObject.getString("skuId");
                String manufacturer = jsonObject.getString("enterpriseName");
                Boolean medical = jsonObject.getBoolean("medicalFlag");
                String productUnit = jsonObject.getString("unitName");
                Double unitPrice = jsonObject.getDouble("salePrice");
                String productUnitId = jsonObject.getString("unitId");
                String certificateNumber = jsonObject.getString("registerCode");
                String spuId = jsonObject.getString("spuId");
                String supplierId = jsonObject.getString("customerId");
                String supplierName = jsonObject.getString("customerName");
                Double purchasePrice = jsonObject.getDouble("purchasePrice");

                int isMedical = -1;

                if (medical){
                    isMedical = 1;
                }else {
                    isMedical = 0;
                }

                good.put("goodsCode",goodsCode);
                good.put("goodsName",goodsName);
                good.put("spec",spec);
                good.put("brandName",brandName);
                good.put("brandId",brandId);
                good.put("skuId",skuId);
                good.put("manufacturer",manufacturer);
                good.put("isMedical",isMedical);
                good.put("productUnit",productUnit);
                good.put("unitPrice",unitPrice);
                good.put("productUnitId",productUnitId);
                good.put("certificateNumber",certificateNumber);
                good.put("spuId",spuId);
                good.put("supplierId",supplierId);
                good.put("supplierName",supplierName);
                good.put("purchasePrice",purchasePrice);
                break;
            }
        }

        good.put("salesVolume",salesVolume);
        return good;
    }

    /**
     * 获取有商品的供应商
     * @return
     */
    public static String getSupplier(){
        String token = ReadTxt.readFile();
        String url = Init.url;
        url+="xiaozi-crm/customer/page";
        String param = "{\"pageNum\":1,\"pageSize\":10,\"isEnable\":1,\"findContactPersonFlag\":true,\"type\":\"SUPPLIER\",\"status\":\"EFFECTIVE\"}";
        HttpResponse response = HttpUtil.post(url, param, token);
        String updateRes = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(updateRes);

        JSONArray jsonArray = json.getJSONObject("body").getJSONArray("list");

        JSONObject supplier = new JSONObject();

        Iterator<Object> iterator = jsonArray.iterator();

        while (iterator.hasNext()) {
            Object o = iterator.next();
            supplier = (JSONObject) JSONObject.toJSON(o);
            String suppliername = supplier.getString("name");
            String id = supplier.getString("id");

            String url_goods = Init.url;
            url_goods += "xiaozi-pms/sku/supplier/goodsPageList";

            String param_goods = "{\"pageSize\":10,\"pageNum\":1,\"customerName\":\"" + suppliername + "\",\"customerId\":\"" + id + "\",\"status\":\"UP\"}";

            HttpResponse response_goods = HttpUtil.post(url_goods, param_goods, token);
            int statusCode = response_goods.getStatusLine().getStatusCode();
            Assert.assertEquals( statusCode,200);

            String rs = HttpUtil.getResponse(response_goods);
            JSONObject json_goods = JSON.parseObject(rs);
            int total = (int) getValByKey(json_goods, "total");

            //没有商品的客户，需要再加一个逻辑
            if (total != 0) {
                Reporter.log("使用系统默认供应商:（"+suppliername+")");
                return suppliername;
            }
            break;
        }
        return null;
    }
}
