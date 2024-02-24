package kedu_interface.rbac;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import kedu_interface.init.Init;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import utils.GetJson;
import utils.HttpUtil;
import utils.JsonArraySort;
import utils.ReadTxt;

import java.util.Iterator;

public class Unit {
    /**
     * 查询核算单位
     *
     * @param  accountUnitName
     * @return
     */
    public static JSONObject getUnitName(String accountUnitName)  {
        String token = ReadTxt.readFile();
        String url = Init.url;
        //查询核算单位
        url += "kedu-rbac/api/unit/queryPageInfo";

        String param = "{\"pageNum\":1,\"pageSize\":10,\"nowStatus\":2,\"accountName\":\"" + accountUnitName + "\",\"keyword\":\"\",\"page\":true}";
        HttpResponse response = HttpUtil.post(url, param, token);

        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String res = HttpUtil.getResponse(response);

        JSONObject json = JSON.parseObject(res);
        String accountingUnitId = (String) GetJson.getValByKey(json, "accountingUnitId");
        String accountingUnitName = (String) GetJson.getValByKey(json, "accountingUnitName");

        JSONObject accountParam = new JSONObject();
        accountParam.put("accountingId", accountingUnitId);
        accountParam.put("accountingName", accountingUnitName);

        return accountParam;

    }

    /**
     * 查询核算单位收货地址
     *
     * @param  accountingId
     * @return
     */
    public static JSONObject getUnitReceiver(String accountingId)  {
        String token = ReadTxt.readFile();
        String url = Init.url;
        //查询核算单位
        url += "kedu-rbac/api/unit/receiver/list";

        String param = "{\"accountingId\":\""+accountingId+"\",\"receiverType\":\"GOODS\"}";
        HttpResponse response = HttpUtil.post(url, param, token);

        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String res = HttpUtil.getResponse(response);

        JSONObject json = JSON.parseObject(res);
        JSONArray list = json.getJSONArray("body");

        list = JsonArraySort.arraySort(list);


        if(!list.isEmpty()){
            JSONObject json1= list.getJSONObject(0);
            String name = (String) GetJson.getValByKey(json1, "receiverName");
            String phone = (String) GetJson.getValByKey(json1, "telephone");
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
//            recevierParam.put("postcode", postcode);
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
