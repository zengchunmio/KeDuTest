package testNg.fms;

import com.alibaba.fastjson.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import kedu_interface.fms.CreateInvoice;
import kedu_interface.fms.Receipt;
import kedu_interface.init.CheckToken;
import kedu_interface.sale.ReceiptMoney;
import utils.GetJson;

public class FmsTest {
    @Test(description = "测试环境初始化", priority = 1)
    @BeforeTest
    public void checkToken() {
        CheckToken.check();
    }

    @Test(description = "创建结算收款单",priority = 2)
    @Parameters({"customerName","contractCode"})
    @DataProvider(name = "receipt")
    public static Object[][] createReceipt(String customerName,String contractCode) {
        JSONObject jsonObject = Receipt.saveReceipt(customerName,contractCode);
        String ids = (String) GetJson.getValByKey(jsonObject, "body");
        Receipt.settlement(ids);
        return new Object[][] {
                new Object[] { ids }
        };
    }

    @Test(dataProvider = "receipt",description = "销售订单收款认领",priority = 3)
    @Parameters({"contractCode"})
    public static void getMoney(String ids,String contractCode) {
        ReceiptMoney.getMoney(contractCode,ids);
    }

    @Test
    public void queryIvDeliery(){
//        String code = CreateInvoice.createInvoice("XSHT2206140001");
//        CreateInvoice.submitIv(code);
//        System.out.println(code);

//        JSONObject jsonObject = QueryFms.queryIvDetail("986308523069673472");
//        System.out.println(jsonObject);

//        Approve.approveOrder("986308523069673472");
        CreateInvoice.openIv("986940098685833216");
    }
}
