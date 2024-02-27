package testNg.kedu;

import kedu_interface.login.KeDuLogin;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TestParameter {
//    @Parameters({"userName"})
//    @Test
//    public void test(String tt){
//        System.out.println("登录名："+tt);
//    }
    @Parameters({"userName"})
    @Test
    public void testLogin(String name){
        KeDuLogin.login(name,"96e79218965eb72c92a549dd5a330112");
        System.out.println("登录名：");
    }

    @Test
    public void testco(){
        String accountName = "上海凯渡商贸有限公司";





    }


}
