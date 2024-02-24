package kedu_interface.third;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import kedu_interface.init.Init;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import utils.HttpUtil;
import utils.ReadTxt;

import static utils.GetJson.getValByKey;

public class Third {


    /**
     * 查询销售合同并获取客户名称，最终客户（医院）
     *
     * @param
     * @return
     */
    public static JSONObject getSaleContract(String unitname,String ctCode) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        url += "kedu-third/wireless/sale/contract/pageList";

        String param = "{\"pageNum\":1,\"pageSize\":10,\"ctCode\":\""+ctCode+"\",\"custname\":\"\",\"enablePage\":true,\"unitname\":\""+unitname+"\",\"orderBy\":[{\"orderByProperty\":\"lastUpdateTime\",\"orderByType\":\"DESC\"}]}";
        HttpResponse response = HttpUtil.post(url, param, token);


        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String res = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(res);
        String custname = (String) getValByKey(json, "custname");
        String finalUserName = (String) getValByKey(json, "finalUserName");

        JSONObject saleContractparam = new JSONObject();
        saleContractparam.put("custname", custname);
        saleContractparam.put("finalUserName", finalUserName);

        return saleContractparam;

    }
}
