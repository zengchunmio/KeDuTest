package kedu_interface.tms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.Reporter;
import kedu_interface.init.Init;
import utils.GetJson;
import utils.HttpUtil;
import utils.ReadTxt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignTransport {
    /**
     * 签收物流单
     * 1.查询物流单
     * 2.查询物流单详情
     * 3.签收物流单
     * @return
     */
    public static JSONObject signTransport(String transportBoxId,JSONObject json) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-tms/transport/record/manual/sign";

        String address = (String) GetJson.getValByKey(json,"receiveAddress");
        String areaId = (String) GetJson.getValByKey(json,"receiveAreaId");
        String areaName = (String) GetJson.getValByKey(json,"receiveAreaName");
        String cityId = (String) GetJson.getValByKey(json,"receiveCityId");
        String cityName = (String) GetJson.getValByKey(json,"receiveCityName");
        String provinceId = (String) GetJson.getValByKey(json,"receiveProvinceId");
        String provinceName = (String) GetJson.getValByKey(json,"receiveProvinceName");
        String signMobile = (String) GetJson.getValByKey(json,"receiverMobile");
        String signName = (String) GetJson.getValByKey(json,"receiverName");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new Date());
        long signTime = 0;
        try {
            signTime = new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String param = "{\n" +
                "    \"address\":\""+address+"\",\n" +
                "    \"areaId\":\""+areaId+"\",\n" +
                "    \"areaName\":\""+areaName+"\",\n" +
                "    \"cityId\":\""+cityId+"\",\n" +
                "    \"cityName\":\""+cityName+"\",\n" +
                "    \"provinceId\":\""+provinceId+"\",\n" +
                "    \"provinceName\":\""+provinceName+"\",\n" +
                "    \"signAttachment\":\"\",\n" +
                "    \"signMobile\":\""+signMobile+"\",\n" +
                "    \"signName\":\""+signName+"\",\n" +
                "    \"signTime\":"+signTime+"," +
                "    \"transportBoxId\":\""+transportBoxId+"\"\n" +
                "}";

        HttpResponse response = HttpUtil.post(url, param, token);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String rs = HttpUtil.getResponse(response);
        JSONObject jsonObject = JSON.parseObject(rs);

        String message = (String) GetJson.getValByKey(jsonObject, "message");
        if (message.equals("success")){
            Reporter.log("物流单签收成功");
        }

        return jsonObject;
    }
}
