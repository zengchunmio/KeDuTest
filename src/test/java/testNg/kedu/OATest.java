package testNg.kedu;

import kedu_interface.OA.Approve;
import kedu_interface.init.CheckToken;
import org.testng.annotations.Test;

public class OATest {
/**
 *@描述  OA审批
 *@参数
 *@返回值
 *@创建人 zcm
 *@创建时间 20240222
 *@修改人和其它信息

 */

    @Test(description = "测试环境初始化", priority = 1)
    public void checkToken() {
        CheckToken.check();
    }

//    @Test(description = "审核非需求单", priority = 2)
//public static void approveTest(){
//
//    String applyId = "1209789770817998849";
//    Approve.approveOther(applyId);
//
//}

    @Test(description = "审核需求单", priority = 3)
    public static void approveWorkOrderTest(){


        String applyId = "1210363386920046593";
        String emergencyLevel = "LOW";
        Approve.approveOrder(applyId,emergencyLevel);
    }


}
