package kedu_interface.tms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import kedu_interface.init.Init;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.Reporter;
import utils.GetJson;
import utils.HttpUtil;
import utils.ReadTxt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckTransport {
    /**
     * 验收物流单
     * 1.查询物流单
     * 2.查询物流单详情
     * 3.验收物流单
     * @return
     */
    public static JSONObject checkTransport(String transportBoxId,JSONObject info) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "xiaozi-tms/transport/box/check";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new Date());
        long checkTime = 0;
        try {
            checkTime = new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("transportBoxId",transportBoxId);
        jsonObject.put("checkTime",checkTime);

        JSONArray serialCheckList = infoTo(info);

        jsonObject.put("serialCheckList",serialCheckList);

        String param = jsonObject.toJSONString();

        HttpResponse response = HttpUtil.post(url, param, token);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String rs = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(rs);

        String message = (String) GetJson.getValByKey(json, "message");
        if (message.equals("success")){
            Reporter.log("运单验收成功");
        }

        return json;
    }

    /**
     * 生成验收通过的json
     */
    public static JSONArray infoTo(JSONObject info){
        JSONArray serialCheckList = (JSONArray) GetJson.getValByKey(info,"serialCheckList");
        JSONObject jsonObject = serialCheckList.getJSONObject(0);

        Integer appearanceIsQualified = jsonObject.getInteger("appearanceIsQualified");
        if (appearanceIsQualified==3){
            jsonObject.put("appearanceIsQualified",1);
        }

        Integer packingIsQualified = jsonObject.getInteger("packingIsQualified");
        if (packingIsQualified==3){
            jsonObject.put("packingIsQualified",1);
        }

        Integer labelIsQualified = jsonObject.getInteger("labelIsQualified");
        if (labelIsQualified==3){
            jsonObject.put("labelIsQualified",1);
        }

        Integer certificateIsQualified = jsonObject.getInteger("certificateIsQualified");
        if (certificateIsQualified==3){
            jsonObject.put("certificateIsQualified",1);
        }

        Integer allIsQualified = jsonObject.getInteger("allIsQualified");
        if (allIsQualified==3){
            jsonObject.put("allIsQualified",1);
        }


        serialCheckList.clear();
        serialCheckList.add(jsonObject);
        return serialCheckList;
    }
}
