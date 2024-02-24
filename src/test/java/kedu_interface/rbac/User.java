package kedu_interface.rbac;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import kedu_interface.init.Init;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.Reporter;
import utils.GetJson;
import utils.HttpUtil;
import utils.JsonArraySort;
import utils.ReadTxt;

import static utils.GetJson.getValByKey;

public class User {
    /**
     * 获取员工名字，code，id，email
     *
     * @param userName
     * @return
     */
    public static JSONObject getUser(String userName) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "kedu-rbac/api/user/queryPageList";

        String param = "{\"pageNum\":1,\"pageSize\":10,\"realName\":\"" + userName + "\",\"deptId\":\"\",\"userStatus\":null,\"stationNm\":\"\",\"phoneNo\":\"\"}";

        HttpResponse response = HttpUtil.post(url, param, token);

        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String res = HttpUtil.getResponse(response);

        JSONObject json = JSON.parseObject(res);

        int total = (int) getValByKey(json, "total");

        if (total == 0) {
            Reporter.log("没有此人员" + userName + ",使用默认业务员");
            param = "{\"pageNum\":1,\"pageSize\":1,\"realName\":\"\",\"deptId\":\"\",\"userStatus\":null,\"stationNm\":\"\",\"phoneNo\":\"\"}";
            response = HttpUtil.post(url, param, token);
            String updateRes = HttpUtil.getResponse(response);
            json = JSON.parseObject(updateRes);
        }

        JSONObject userParam = new JSONObject();
        String reviewerId = (String) GetJson.getValByKey(json, "userId");
        String email = (String) GetJson.getValByKey(json, "email");
        String realName = (String) GetJson.getValByKey(json, "realName");

        userParam.put("userId", reviewerId);
        userParam.put("userEmail", email);
        userParam.put("userRealName", realName);

        return userParam;
    }


    /**
     * 获取员工用户名及密码
     *
     * @param realName
     * @return
     */
    public static JSONObject getPassword(String realName) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "kedu-rbac/api/user/queryPageList";

        String param = "{\"pageNum\":1,\"pageSize\":10,\"realName\":\"" + realName + "\",\"deptId\":\"\",\"userStatus\":null,\"stationNm\":\"\",\"phoneNo\":\"\"}";

        HttpResponse response = HttpUtil.post(url, param, token);

        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String res = HttpUtil.getResponse(response);

        JSONObject json = JSON.parseObject(res);

        JSONObject userParam = new JSONObject();
        String password = (String) GetJson.getValByKey(json, "password");
        String phoneNo = (String) GetJson.getValByKey(json, "phoneNo");

        userParam.put("password", password);
        userParam.put("phoneNo", phoneNo);

        return userParam;
    }

    /**
     * 查询个人收货地址
     *
     * @param  userId
     * @return
     */
    public static JSONObject getUserReceiver(String userId)  {
        String token = ReadTxt.readFile();
        String url = Init.url;
        //查询用户地址
        url += "kedu-rbac/api/user/address/list";

        String param = "{\"userId\":\""+userId+"\"}";
        HttpResponse response = HttpUtil.post(url, param, token);

        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String res = HttpUtil.getResponse(response);

        JSONObject json = JSON.parseObject(res);
        JSONArray list = json.getJSONArray("body");

        list = JsonArraySort.arraySort(list);


        if(!list.isEmpty()){
            JSONObject json1= list.getJSONObject(0);
            String name = (String) GetJson.getValByKey(json1, "deliveryName");
            String phone = (String) GetJson.getValByKey(json1, "phoneNo");
//            String postcode = (String) GetJson.getValByKey(json1, "postcode");
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