package kedu_interface.crm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import kedu_interface.init.Init;
import kedu_interface.sale.Reviewer;
import kedu_interface.sale.SaleTemplate;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.Reporter;
import utils.GetJson;
import utils.HttpUtil;
import utils.JsonArraySort;
import utils.ReadTxt;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import static utils.GetJson.getValByKey;

public class Customer {
    /**
     * 获取客户名字，code，id
     *
     * @param companyName
     * @return
     */
    public static JSONObject getCompany(String companyName) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xkedu-crm/wireless/customer/page";

//        if (companyName==null){
//            companyName = "嘉兴市妇幼保健院";
//        }

        String param = "{\n" +
                "    \"pageNum\":1,\n" +
                "    \"pageSize\":1,\n" +
                "    \"status\":\"\",\n" +
                "    \"customerType\":\"\",\n" +
                "    \"finalUserName\":\"\",\n" +
                "    \"nameOrNameEn\":\"" + companyName + "\",\n" +
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

        String res = HttpUtil.getResponse(response);

        JSONObject json = JSON.parseObject(res);

//        int total = (int) getValByKey(json, "total");
//
//        if (total == 0) {
//            param = "{\"pageNum\":1,\"pageSize\":1,\"status\":\"\",\"type\":\"\",\"nameOrNameEn\":\"\",\"orderBy\":[{\"orderByProperty\":\"createTime\",\"orderByType\":\"DESC\"}]}";
//            response = HttpUtil.post(url, param, token);
//            String updateRes = HttpUtil.getResponse(response);
//            json = JSON.parseObject(updateRes);
//            String clientName = (String) GetJson.getValByKey(json, "name");
//            Reporter.log("没有此客户(" + companyName + "),使用系统默认客户:（"+clientName+")");
//        }

        JSONObject companyParam = new JSONObject();
        String clientID = (String) getValByKey(json, "id");
        String clientName = (String) GetJson.getValByKey(json, "name");
        String clientCode = (String) GetJson.getValByKey(json, "code");

        companyParam.put("customerId", clientID);
        companyParam.put("customerHead", clientName);
        companyParam.put("customerCode", clientCode);

        return companyParam;
    }

    /**
     * 获取客户联系人信息
     *
     * @param companyId
     * @return
     */
    public static JSONObject getContactPerson(String companyId) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-crm/customer/detail";

        String param = "{\n" +
                "    \"id\":\"" + companyId + "\",\n" +
                "    \"findContactPersonFlag\":true,\n" +
                "    \"findBankFlag\":true,\n" +
                "    \"findReceiverFlag\":true,\n" +
                "    \"findCredentialFlag\":true,\n" +
                "    \"findCarrierFlag\":true,\n" +
                "    \"findCarrierCourierContactFlag\":true\n" +
                "}";

        HttpResponse response = HttpUtil.post(url, param, token);


        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String res = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(res);

        JSONObject contact = new JSONObject();
        JSONObject contactPerson = new JSONObject();
        JSONArray jsonArray = json.getJSONObject("body").getJSONArray("crmContactPersonDetailRespDTOList");

        Iterator<Object> iterator = jsonArray.iterator();

        while (iterator.hasNext()) {
            Object o = iterator.next();
            contact = (JSONObject) JSONObject.toJSON(o);
            String defaultFlag = contact.getString("defaultFlag");

            if (defaultFlag.equals("1")) {
                String customerContactId = (String) getValByKey(contact, "id");
                String customerId = (String) getValByKey(contact, "customerId");
                String customerContact = (String) getValByKey(contact, "name");
                String contactPhone = (String) getValByKey(contact, "mobilePhone");
                String contactEmail = (String) getValByKey(contact, "email");

                contactPerson.put("customerContactId", customerContactId);
                contactPerson.put("customerId", customerId);
                contactPerson.put("customerContact", customerContact);
                contactPerson.put("contactPhone", contactPhone);
                contactPerson.put("contactEmail", contactEmail);
                break;
            } else {
                String customerContactId = (String) getValByKey(contact, "id");
                String customerId = (String) getValByKey(contact, "customerId");
                String customerContact = (String) getValByKey(contact, "name");
                String contactPhone = (String) getValByKey(contact, "mobilePhone");
                String contactEmail = (String) getValByKey(contact, "email");

                contactPerson.put("customerContactId", customerContactId);
                contactPerson.put("customerId", customerId);
                contactPerson.put("customerContact", customerContact);
                contactPerson.put("contactPhone", contactPhone);
                contactPerson.put("contactEmail", contactEmail);
            }
        }
        return contactPerson;
    }

    /**
     * 获取客户收货人信息
     *
     * @param companyId
     * @return
     */
    public static JSONObject getReceiverDetail(String companyId) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-crm/customer/detail";

        String param = "{\n" +
                "    \"id\":\"" + companyId + "\",\n" +
                "    \"findContactPersonFlag\":true,\n" +
                "    \"findBankFlag\":true,\n" +
                "    \"findReceiverFlag\":true,\n" +
                "    \"findCredentialFlag\":true,\n" +
                "    \"findCarrierFlag\":true,\n" +
                "    \"findCarrierCourierContactFlag\":true\n" +
                "}";

        HttpResponse response = HttpUtil.post(url, param, token);


        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String res = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(res);

        JSONObject ReceiverParam = new JSONObject();
        JSONObject recive = new JSONObject();
        JSONArray jsonArray = json.getJSONObject("body").getJSONArray("crmReceiverGoodsDetailRespDTOList");

        Iterator<Object> iterator = jsonArray.iterator();

        while (iterator.hasNext()) {
            Object o = iterator.next();
            ReceiverParam = (JSONObject) JSONObject.toJSON(o);
            String defaultFlag = ReceiverParam.getString("defaultFlag");

            if (defaultFlag.equals("1")) {
                String id = (String) getValByKey(ReceiverParam, "id");
                String detailAddress = (String) getValByKey(ReceiverParam, "detailAddress");
                String areaId = (String) getValByKey(ReceiverParam, "areaId");
                String areaName = (String) getValByKey(ReceiverParam, "areaName");
                String cityId = (String) getValByKey(ReceiverParam, "cityId");
                String cityName = (String) getValByKey(ReceiverParam, "cityName");
                String name = (String) getValByKey(ReceiverParam, "name");
                String mobilePhone = (String) getValByKey(ReceiverParam, "mobilePhone");
                String provinceId = (String) getValByKey(ReceiverParam, "provinceId");
                String provinceName = (String) getValByKey(ReceiverParam, "provinceName");

                recive.put("id", id);
                recive.put("address", detailAddress);
                recive.put("areaId", areaId);
                recive.put("areaName", areaName);
                recive.put("cityId", cityId);
                recive.put("cityName", cityName);
                recive.put("name", name);
                recive.put("phone", mobilePhone);
                recive.put("provinceId", provinceId);
                recive.put("provinceName", provinceName);
                break;
            } else {
                String id = (String) getValByKey(ReceiverParam, "id");
                String detailAddress = (String) getValByKey(ReceiverParam, "detailAddress");
                String areaId = (String) getValByKey(ReceiverParam, "areaId");
                String areaName = (String) getValByKey(ReceiverParam, "areaName");
                String cityId = (String) getValByKey(ReceiverParam, "cityId");
                String cityName = (String) getValByKey(ReceiverParam, "cityName");
                String name = (String) getValByKey(ReceiverParam, "name");
                String mobilePhone = (String) getValByKey(ReceiverParam, "mobilePhone");
                String provinceId = (String) getValByKey(ReceiverParam, "provinceId");
                String provinceName = (String) getValByKey(ReceiverParam, "provinceName");

                recive.put("id", id);
                recive.put("address", detailAddress);
                recive.put("areaId", areaId);
                recive.put("areaName", areaName);
                recive.put("cityId", cityId);
                recive.put("cityName", cityName);
                recive.put("name", name);
                recive.put("phone", mobilePhone);
                recive.put("provinceId", provinceId);
                recive.put("provinceName", provinceName);
            }
        }
        return recive;
    }

    /**
     * 获取企业ID
     *
     * @param companyName
     * @return
     */
    public static JSONObject getEnterprise(String companyName) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-enterprise/wireless/enterprise/list";

        String param = "{\"pageNum\":1,\"pageSize\":1,\"keyWord\":\"" + companyName + "\",\"status\":null,\"sortFields\":[{\"orderByProperty\":\"createTime\",\"orderByType\":\"DESC\"}]}";

        HttpResponse response = HttpUtil.post(url, param, token);

        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String res = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(res);

        int total = (int) getValByKey(json, "total");

        if (total == 0) {
            Reporter.log("没有此客户" + companyName + ",使用系统第一条客户");
            param = "{\"pageNum\":1,\"pageSize\":1,\"keyWord\":\"\",\"status\":null,\"sortFields\":[{\"orderByProperty\":\"createTime\",\"orderByType\":\"DESC\"}]}";
            response = HttpUtil.post(url, param, token);
            String updateRes = HttpUtil.getResponse(response);
            json = JSON.parseObject(updateRes);
        }

        JSONObject enterpriseParam = new JSONObject();
        String id = (String) getValByKey(json, "id");

        enterpriseParam.put("id", id);

        return enterpriseParam;
    }

    /**
     * 生成供应商参数
     * @param companyName  供应商名称
     * @param bindPurchaseFlag 是否创建采购订单
     * @return
     */
    public static JSONObject customerParam(String companyName,boolean bindPurchaseFlag){
        JSONObject json = new JSONObject();

        //客户名称、ID、code
        JSONObject company = getCompany(companyName);
        Set<String> keySet = company.keySet();
        for (String key :keySet){
            json.put(key,company.get(key));
        }

        //员工信息
        JSONObject sale = Reviewer.getUser("陈晨");
        Set<String> keySet1 = sale.keySet();
        for (String key :keySet1){
            json.put(key,sale.get(key));
        }

        String customerId = company.getString("customerId");

        //客户联系人
        JSONObject customerContact = getContactPerson(customerId);
        Set<String> set = customerContact.keySet();
        for (String key :set){
            json.put(key,customerContact.get(key));
        }

        //客户收货人
        JSONObject customerReceiver = getReceiverDetail(customerId);
        String addressId = customerReceiver.getString("id");
        json.put("addressId",addressId);

        json.put("accountingUnit","上海小紫医疗科技有限公司");
        json.put("isIncludeTax",1);
        json.put("isIncludedFare",1);
        json.put("accounts","632790466");
        json.put("signedDate",new Date());
        json.put("tariff",0.13);
        json.put("contractSource",0);
        json.put("contractType",1);
        json.put("deliveryMethod",1);
        json.put("outBusinessNo","");
        json.put("autoOrderFlag",true);
        json.put("bindPurchaseFlag",bindPurchaseFlag);
        json.put("paymentMethod",0);
        json.put("isTemplate",1);
        json.put("templateId", SaleTemplate.getTemplateId().get("id"));

        return json;
    }

    /**
     * 获取客户详情
     *
     * @param companyId
     * @return
     */
    public static JSONObject getCustomerDetail(String companyId) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-crm/customer/detail";

        String param = "{\n" +
                "    \"id\":\"" + companyId + "\",\n" +
                "    \"findContactPersonFlag\":true,\n" +
                "    \"findBankFlag\":true,\n" +
                "    \"findReceiverFlag\":true,\n" +
                "    \"findCredentialFlag\":true,\n" +
                "    \"findCarrierFlag\":true,\n" +
                "    \"findCarrierCourierContactFlag\":true\n" +
                "}";

        HttpResponse response = HttpUtil.post(url, param, token);


        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String res = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(res);


        return json;
    }

    /**
     * 查询客户收货地址
     *
     * @param  customerName
     * @return
     */
    public static JSONObject getCustomerReceiver(String customerName)  {
        String token = ReadTxt.readFile();
        String url = Init.url;
        //查询客户收货地址
        url += "kedu-crm/receiver/resultList";

        String param = "{\n" +
                "    \"pageNum\":1,\n" +
                "    \"pageSize\":1,\n" +
                "    \"customerName\":\"" + customerName + "\",\n" +
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

        String res = HttpUtil.getResponse(response);

        JSONObject json = JSON.parseObject(res);
        JSONArray list = json.getJSONArray("body");

        list = JsonArraySort.arraySort(list);


        if(!list.isEmpty()){
            JSONObject json1= list.getJSONObject(0);
            String name = (String) GetJson.getValByKey(json1, "name");
            String phone = (String) GetJson.getValByKey(json1, "mobilePhone");
            String postcode = (String) GetJson.getValByKey(json1, "postcode");
            String provinceId = (String) GetJson.getValByKey(json1, "provinceId");
            String provinceName = (String) GetJson.getValByKey(json1, "provinceName");
            String cityId = (String) GetJson.getValByKey(json1, "cityId");
            String cityName = (String) GetJson.getValByKey(json1, "cityName");
            String areaId = (String) GetJson.getValByKey(json1, "areaId");
            String areaName = (String) GetJson.getValByKey(json1, "areaName");
            String detailAddress = (String) GetJson.getValByKey(json1, "detailAddress");

            JSONObject recevierParam = new JSONObject();
            recevierParam.put("name", name);
            recevierParam.put("phone", phone);
            recevierParam.put("postcode", postcode);
            recevierParam.put("provinceId", provinceId);
            recevierParam.put("provinceName", provinceName);
            recevierParam.put("cityId", cityId);
            recevierParam.put("cityName", cityName);
            recevierParam.put("areaId", areaId);
            recevierParam.put("areaName", areaName);
            recevierParam.put("detailAddress", detailAddress);
            return recevierParam;
        }

        return null;

    }
}