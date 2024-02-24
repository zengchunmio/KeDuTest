package kedu_interface.scm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import kedu_interface.init.Init;
import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.testng.Assert;
import utils.GetJson;
import utils.HttpUtil;
import utils.ReadTxt;

public class WorkOrder {
    /**
     * 创建需求申请单
     *
     * @param
     * @return
     */
    public static JSONObject addAdministrationWorkOrder(String requirementType,String businessType,String type,String draftFlag)  {
        String token = ReadTxt.readFile();
        String url = Init.url;
        //新增行政办公商品类需求单
        url += "kedu-scm/wireless/purch/workOrder/add";

        String param = "{\"pageNum\":1,\"pageSize\":10,\"nowStatus\":2,\"requirementType\":\"" + requirementType + "\",\"keyword\":\"\",\"page\":true}";
        HttpResponse response = HttpUtil.post(url, param, token);

        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String res = HttpUtil.getResponse(response);

        com.alibaba.fastjson.JSONObject json = JSON.parseObject(res);
        String accountingUnitId = (String) GetJson.getValByKey(json, "accountingUnitId");
        String accountingUnitName = (String) GetJson.getValByKey(json, "accountingUnitName");

        JSONObject accountParam = new JSONObject();
        accountParam.put("userId", accountingUnitId);
        accountParam.put("userEmail", accountingUnitName);

        return accountParam;

    }




}
