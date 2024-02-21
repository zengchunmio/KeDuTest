package kedu_interface.sale;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.Reporter;
import kedu_interface.init.Init;
import utils.GetJson;
import utils.HttpUtil;
import utils.ReadTxt;

import static utils.GetJson.getValByKey;

public class Reviewer {
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

        userParam.put("salesmanId", reviewerId);
        userParam.put("salesmanMail", email);
        userParam.put("salesmanName", realName);

        return userParam;
    }


    /**
     * 获取员工用户及密码
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
}