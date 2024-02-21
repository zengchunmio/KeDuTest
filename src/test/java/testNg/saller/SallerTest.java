package testNg.saller;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.FormatDate;
import ztest.ZTestReport;

import java.util.Map;

/**
 *
 */
@Listeners({ZTestReport.class})
public class SallerTest {
    @Test(description = "卖家中心登陆")
    public static void getCaptche(){
//        SallerLogin.sendCaptcha("15221888544");
//        String list = SallerLogin.getPassword("15221888544");
//        System.out.println(list);
        Map date = FormatDate.getDate();
        System.out.println(date);
    }
}
