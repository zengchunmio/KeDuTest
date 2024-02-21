package kedu_interface.saller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.Reporter;
import kedu_interface.init.Init;
import top.jfunc.json.JsonObject;
import utils.GetJson;
import utils.HttpUtil;
import utils.JedisUtil;
import utils.ReadTxt;

public class SallerLogin {

    /**
     * 卖家中心审批流
     * 1.根据供应商名称查看供应商管理员
     * 2.去数据库拿密码或从redis拿验证码
     * 3.登陆卖家中心，返回token
     * 4.审批卖家中心采购合同
     *
     * @param
     * @return
     */
    public static String approveContract(String purchaseId, String supplierName) {
        //1.根据供应商名称查看供应商管理员
        String phoneNo = getSallerContact(supplierName);
        //2.发送验证码，去redis拿验证码
        sendCaptcha(phoneNo);
        String password = getPassword(phoneNo);

        //3.登陆卖家中心，返回token
        JSONObject login = login(phoneNo, password);
        String token = login.getString("token.txt");
        String name = login.getString("name");
        String userId = login.getString("userId");

        String url = Init.sellerUrl;
        url += "xiaozi-seller/wireless/contract/editContractStateById";

        String param = "{\"id\":\"" + purchaseId + "\",\"supIdea\":\"\",\"updateUserId\":\"" + userId + "\",\"updateUserName\":\"" + name + "\",\"state\":3}";

        //4.审批卖家中心采购合同
        HttpResponse response = HttpUtil.post(url, param, token);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String rs = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(rs);

        String message = (String) GetJson.getValByKey(json, "message");

        if (message.equals("成功")){
            Reporter.log("供应商审批成功");
            return message;
        }
        return message;
    }

    /**
     * 1.根据供应商名称查看供应商管理员
     *
     * @param sallerName
     * @return
     */
    public static String getSallerContact(String sallerName) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-pop/wireless/supplier/page";

        String param = "{\"adminName\":\"\",\"adminPhoneNumber\":\"\",\"name\":\"" + sallerName + "\",\"hasAdminFlag\":\"\",\"settleInTimeFrom\":\"\",\"settleInTimeTo\":\"\",\"pageSize\":10,\"pageNum\":1,\"orderBy\":[{\"orderByProperty\":\"lastUpdateTime\",\"orderByType\":\"DESC\"}]}";

        HttpResponse response = HttpUtil.post(url, param, token);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String rs = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(rs);

        Boolean hasAdminFlag = (Boolean) GetJson.getValByKey(json, "hasAdminFlag");//是否有管理员
        String adminPhoneNumber = "";
        if (hasAdminFlag) {
            adminPhoneNumber = (String) GetJson.getValByKey(json, "adminPhoneNumber");
        }

        return adminPhoneNumber;
    }

    /**
     * 2.卖家中心发送验证码
     * @param phoneNo
     * @return
     */
    public static void sendCaptcha(String phoneNo) {
        String url = Init.sellerUrl;
        url += "xiaozi-seller/wireless/user/sendCaptcha";

        String param = "{\"phone\":\""+phoneNo+"\",\"captchaType\":\"LOGIN\"}";

        HttpResponse response = HttpUtil.post(url, param);
        int statusCode = response.getStatusLine().getStatusCode();
        top.jfunc.json.impl.JSONObject object = new top.jfunc.json.impl.JSONObject(HttpUtil.getResponse(response));

        JsonObject heads = object.getJsonObject("heads");
        String message = heads.getString("message");
        int code = heads.getInteger("code");

        Assert.assertEquals( statusCode,200);
        if (statusCode == 200&&message.equals("成功")) {
            Reporter.log("发送验证码成功");
        }else if (code==300&&message.equals("读秒中，请稍后再获取手机验证码。")){
            Reporter.log("验证码发送频繁"+message);
            try {
                Thread.sleep(60000);
                sendCaptcha(phoneNo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 3.去redis拿验证码
     *
     * @param phoneNo
     * @return
     */
    public static String getPassword(String phoneNo) {
        //Redis信息
        String nodes = Init.redis;
        String password = Init.redisPassword;

        //连接Redis
        JedisUtil jedisUtil = new JedisUtil();
        try {
            jedisUtil.getRedisCluster(nodes, password);
        } catch (Throwable e) {
            throw e;
        }

        //获取数据
        String key = "SELLER_PHONE_CAPTCHA_LOGIN_"+phoneNo;
        String captcha = jedisUtil.getValue(key);


        return captcha;
    }

    /**
     * 4.登陆卖家中心，返回token
     *
     * @param phoneNo
     * @param password
     * @return
     */
    public static JSONObject login(String phoneNo, String password) {
        String url = Init.sellerUrl;
        url += "xiaozi-seller/wireless/user/phoneLogin";

        String param = "{\"phone\":\"" + phoneNo + "\",\"code\":\"" + password + "\",\"type\":\"CAPTCHA\"}";

        HttpResponse response = HttpUtil.post(url, param);
        int statusCode = response.getStatusLine().getStatusCode();
        top.jfunc.json.impl.JSONObject object = new top.jfunc.json.impl.JSONObject(HttpUtil.getResponse(response));

        JsonObject heads = object.getJsonObject("heads");
        String message = heads.getString("message");
        int code = heads.getInteger("code");

        Assert.assertEquals( statusCode,200);
        if (statusCode == 200&&message.equals("success")) {
            Reporter.log("卖家中心登录成功，登录账号为：" + phoneNo);
        }else if (code!=200){
            Reporter.log("登陆失败，"+message);
        }


        JsonObject body = object.getJsonObject("body");
        String token = body.getString("token");
        String name = body.getJsonObject("tokenInfo").getString("name");
        String userId = body.getJsonObject("tokenInfo").getString("userId");

        JSONObject json = new JSONObject();
        json.put("token.txt", token);
        json.put("name", name);
        json.put("userId", userId);

        return json;
    }
}
