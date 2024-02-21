package testNg.kedu;

import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import kedu_interface.delivery.CreateDeliveryNotify;
import kedu_interface.delivery.SwitchWareHouse;
import kedu_interface.init.CheckToken;
import ztest.ZTestReport;

/**
 * 创建发货通知单，参数合同号、客户名称从xml传入
 * 1.登陆
 * 2.创建销售订单
 * 3.审批销售订单
 * 4.获取合同状态
 */
@Listeners({ZTestReport.class})
public class CreateDeliveryTest {
    @Test(description = "测试环境初始化", priority = 1)
    public void checkToken() {
        CheckToken.check();
    }

    @Test(description = "创建发货通知单",priority = 2)
    @Parameters({"contractCode","customerName"})
    public static void createDeliveryNotify(String contractCode,String customerName) {
        CreateDeliveryNotify.createSaleOrder(contractCode, customerName, "1");
        SwitchWareHouse.switchWareHouse(contractCode);
    }
}
