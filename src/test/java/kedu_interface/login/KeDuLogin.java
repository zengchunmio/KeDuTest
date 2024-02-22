package kedu_interface.login;

import kedu_interface.init.Init;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.Reporter;
import top.jfunc.json.JsonObject;
import top.jfunc.json.impl.JSONObject;
import utils.HttpUtil;
import utils.ReadTxt;

public class KeDuLogin {
    /**
     * 登陆
     *
     * @return
     */
    public static String login(String userName,String passWord) {
        String url = Init.url;

        //获取验证码
//        List<String> list = GetCaptcha.getCaptcha();
//        String captchaKey = list.get(0);
//        String captcha = list.get(1);

        url += "kedu-rbac/login";
        String param = "{\"userName\":\"" + userName + "\",\"password\":\"" + passWord + "\",\"byType\":\"2\",\"captchaKey\":\"\",\"captcha\":\"\"}";
        System.out.println("url："+url);
        HttpResponse response = HttpUtil.post(url, param);

        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);
        if (statusCode == 200) {
            Reporter.log("登录成功，登录账号为：" + userName);
            JSONObject object = new JSONObject(HttpUtil.getResponse(response));
            System.out.println("登录成功，登录账号为："+userName);
            JsonObject body = object.getJsonObject("body");
            String access_token = body.getString("access_token");
            access_token = "bearer " + access_token;
            return access_token;
        }else{
            return  "";
        }


    }

    /**
     * 更新token
     */
    public static void updateToken() {
        String userName = Init.userName;
        String passWord = Init.passWord;
        String token = login(userName,passWord);
        Reporter.log("更新登录，登录账号为：" + userName);
        ReadTxt.writeFile(token);
    }
}