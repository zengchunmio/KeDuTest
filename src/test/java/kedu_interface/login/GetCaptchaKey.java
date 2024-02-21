package kedu_interface.login;

import org.apache.http.HttpResponse;
import org.testng.Assert;
import kedu_interface.init.Init;
import top.jfunc.json.impl.JSONObject;
import utils.HttpUtil;

public class GetCaptchaKey {
    public static String getCaptchaKey(){

        String url = Init.url;
        url += "kedu-rbac/getCaptcha";
        HttpResponse response = HttpUtil.post(url, "");
        String body = HttpUtil.getResponse(response);

        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        JSONObject json = new JSONObject(body);
        String codeKey = json.getJsonObject("body").getString("codeKey");
        return codeKey;
    }
}
