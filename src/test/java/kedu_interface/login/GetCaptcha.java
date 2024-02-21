package kedu_interface.login;

import kedu_interface.init.Init;
import utils.JedisUtil;

import java.util.ArrayList;
import java.util.List;

public class GetCaptcha {
    public static List<String> getCaptcha(){
        //Redis信息
        String nodes = Init.redis;
        String password = Init.redisPassword;
        String key = GetCaptchaKey.getCaptchaKey();

        //连接Redis
        JedisUtil jedisUtil = new JedisUtil();
        try {
            jedisUtil.getRedisCluster(nodes, password);
        } catch (Throwable e) {
            throw e;
        }

        //获取数据
        String captcha = jedisUtil.getValue(key);
        ArrayList<String> list = new ArrayList<>();
        list.add(key);
        list.add(captcha);

        return list;
    }
}
