package kedu_interface.init;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Reporter;
import kedu_interface.login.KeDuLogin;
import utils.GetProperites;
import utils.HttpUtil;
import utils.ReadTxt;

import java.util.Map;

public class CheckToken {

    public static Map<Object, Object> map = GetProperites.getProp("/user.properties");
    public static String username = (String) map.get("username");

    public static void check() {
        String url = (String) map.get("url");
        String token = ReadTxt.readFile();
        url += "kedu-rbac/api/menu/treeMenuListById";

        HttpResponse post = HttpUtil.post(url, "", token);
        try {
            JSONObject json = new JSONObject(HttpUtil.getResponse(post));
            int code = json.getJSONObject("heads").getInt("code");
            String message = json.getJSONObject("heads").getString("message");
            if (code == 506 && message.equals("无效token")) {
                System.out.println("token过期");
                KeDuLogin.updateToken();
                getTree();
            } else {
                System.out.println("获取token成功");
                Reporter.log("登陆成功，用户为：" + username);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getTree() {
        String url = (String) map.get("url");
        String token = ReadTxt.readFile();
        url += "kedu-rbac/api/menu/treeMenuListById";

        HttpUtil.post(url, "", token);
    }
}