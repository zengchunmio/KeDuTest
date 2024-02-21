package testNg.kedu;

import com.alibaba.fastjson.JSONObject;
import kedu_interface.tms.CheckTransport;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import kedu_interface.OA.Approve;
import kedu_interface.delivery.CreateDeliveryNotify;
import kedu_interface.delivery.SwitchWareHouse;
import kedu_interface.fms.CreateInvoice;
import kedu_interface.fms.Receipt;
import kedu_interface.init.CheckToken;
import kedu_interface.sale.CreateSaleOrder;
import kedu_interface.sale.GetSaleContractStatus;
import kedu_interface.sale.ReceiptMoney;
import kedu_interface.tms.QueryTransport;
import kedu_interface.tms.SignTransport;
import utils.GetJson;
import ztest.ZTestReport;

/**
 * 销售订单全流程
 * 1.登陆
 * 2.创建销售订单
 * 3.审批销售订单
 * 4.获取合同状态
 * 5.创建发货通知单
 * 6.签收物流单
 * 7.创建财务收款单
 * 8.销售开票
 */
@Listeners({ZTestReport.class})
public class SalesAllTest {
    public static String contractId = "";
    public static String contractCode = "";
    public static String ids = "";
    public static String customerName = "cc测试";
    public static String supplierName = "上海";

    @Test(description = "测试环境初始化", priority = 1)
//    @BeforeTest
    public void checkToken() {
        CheckToken.check();
    }

    @Test(description = "创建销采一体合同", priority = 2)
    public static void createSaleAndPurshTest() {
        JSONObject saleOrder = CreateSaleOrder.createSaleOrder(customerName, supplierName, "1");
        contractId = saleOrder.getString("orderId");
        contractCode = saleOrder.getString("code");
    }

//    @Test(description = "创建销售订单", priority = 1)
    public static void createSaleTest() {
        JSONObject saleOrder = CreateSaleOrder.createSaleOrder("广东宝莱特医用科技股份有限公司", "1", "内", 20);
        contractId = saleOrder.getString("orderId");
        contractCode = saleOrder.getString("code");
    }

    @Test(description = "审批销售合同", priority = 3)
    public static void approveSaleTest() {
        Approve.approveOther(contractId);
    }

    @Test(description = "获取合同状态", priority = 4)
    public static void getOrderStatus() {
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        GetSaleContractStatus.getStatus(contractCode);
    }

    @Test(description = "创建发货通知单",priority = 5)
    public static void createDeliveryNotify() {
        CreateDeliveryNotify.createSaleOrder(contractCode, customerName, "1");
        SwitchWareHouse.switchWareHouse(contractCode);
    }

    @Test(description = "签收物流单",priority = 6)
    public static void queryTransport(){
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

    @Test(description = "创建结算收款单",priority = 7)
    public static void createReceipt() {
        JSONObject jsonObject = Receipt.saveReceipt(customerName,contractCode);
        ids = (String) GetJson.getValByKey(jsonObject, "body");
        Receipt.settlement(ids);
    }

    @Test(description = "销售订单收款认领",priority = 8)
    public static void getMoney() {
        ReceiptMoney.getMoney(contractCode,ids);
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(description = "销售发票",priority = 9)
    public static void createIv() {
        //发票申请
        String code = CreateInvoice.createInvoice(contractCode);
        //发票提交
        CreateInvoice.submitIv(code);
        //审批发票
        Approve.approveOther(code);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //开发票
        CreateInvoice.openIv(code);
    }
}
