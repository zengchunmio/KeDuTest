package kedu_interface.sale;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

public class SaleDate {
    public static JSONObject getDate(){
        JSONObject json = new JSONObject();
        Date date = new Date();

        json.put("paymentAmount",100);
        json.put("paymentDate",date);
        json.put("periodsNumber",1);

        return json;
    }
}
