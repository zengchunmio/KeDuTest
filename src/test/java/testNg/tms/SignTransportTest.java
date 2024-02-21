package testNg.tms;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import kedu_interface.tms.CheckTransport;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import kedu_interface.init.CheckToken;
import kedu_interface.tms.QueryTransport;
import kedu_interface.tms.SignTransport;
import utils.GetJson;
import ztest.ZTestReport;

/**
 * 签收物流单，参数合同号从xml传入
 * 1.登陆
 * 2.创建销售订单
 * 3.审批销售订单
 * 4.获取合同状态
 */
@Listeners({ZTestReport.class})
public class SignTransportTest {
    @Test(description = "测试环境初始化", priority = 1)
    public void checkToken() {
        CheckToken.check();
    }

    @Test(description = "签收物流单",priority = 2)
    @Parameters({"contractCode"})
    public static void queryTransport(String contractCode){
        //查询物流单
        JSONObject jsonObject = QueryTransport.queryTransport(contractCode);
        String transportId = (String) GetJson.getValByKey(jsonObject, "transportId");
        //查询物流单详情
        JSONObject json = QueryTransport.transportDetail(transportId);
        //获取物流单详情中的transportBoxId（签收用）
        String transportBoxId = (String) GetJson.getValByKey(json, "transportBoxId");

        //获取详情中的状态
        String statusStr = (String) GetJson.getValByKey(json, "statusStr");
        if (statusStr.equals("未验收")){

            JSONObject info = QueryTransport.transportInfo(transportBoxId);
            CheckTransport.checkTransport(transportBoxId,info);
        }
        //签收物流单
        SignTransport.signTransport(transportBoxId, json);
    }

    @Test
    public void test(){
        JSONObject info = QueryTransport.transportInfo("987390464888016896");
        JSONArray jsonObject = CheckTransport.infoTo(info);
        System.out.println(jsonObject);
    }
}
