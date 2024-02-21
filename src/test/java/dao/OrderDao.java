package dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import utils.JDBCUtils;

import java.util.List;
import java.util.Map;

public class OrderDao {
    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 根据用户名查询最后一条验证码
     * @param username
     * @return
     */
    public Map getSmsCode(String username) {
        //1.定义sql
        String sql = "select * from V_TBL_USER_MSM where MSM_PHONE = ? ORDER BY MSM_ID desc LIMIT 1";
        //2.执行sql
        Map<String, Object> map = template.queryForMap(sql, username);
        return map;
    }

    /**
     * 根据手机号查询摄影师code
     * @param phone
     * @return
     */
    public String getPhotographer(String phone) {
        String sql = "SELECT user_code FROM `t_photographer` where user_id = (select id from t_user where phone = ?)";
        String id = template.queryForObject(sql, String.class, phone);
        return id;
    }

    /**
     * 根据手机号查询数码师id
     * @param phone
     * @return
     */
    public String getDigitalId(String phone) {
        String sql = "SELECT user_code FROM `t_digital` where user_id = (select id from t_user where phone = ?)";
        String id = template.queryForObject(sql, String.class, phone);
        return id;
    }

    /**
     * 根据订单号查orderId
     * @param orderNumber
     * @return
     */
    public String getOrderId(String orderNumber) {
        String sql = "select id from t_order where order_no = ?";
        String id = template.queryForObject(sql, String.class, orderNumber);
        return id;
    }

    /**
     * 根据订单号查orderInterval
     * @param orderId
     * @return
     */
    public String getInterval(String orderId) {
        String sql = "SELECT id FROM `V_TBL_ORDER_INTERVAL` where ORDER_ID = ?";
        String id = template.queryForObject(sql, String.class, orderId);
        return id;
    }

    /**
     * 根据订单号查ticketId
     * @param orderNumber
     * @return
     */
    public String getTicketId(String orderNumber) {
        String sql = "select id from t_ticket where service_order_no = ?";
        String id = template.queryForObject(sql, String.class, orderNumber);
        return id;
    }


    /**
     * 根据公司名称查商业用户id和创建日期
     * @param Customername
     * @return
     */
    public List<Map<String, Object>> getCustomerId(String Customername) {
        //1.定义sql
        String sql = "select c.id,c.cr_date,u.company from t_customer c left join t_user u on c.user_id = u.id where u.company=?";

        List<Map<String, Object>> list = null;
        //2.执行sql
        list = template.queryForList(sql, Customername);
        return list;
    }

    /**
     * 查询所有对账单客户id
     * @return
     */
    public List<Map<String, Object>> getCustomerId() {
        //1.定义sql
        String sql = "select f.id,f.cr_date,u.company from t_user u,(select * from t_customer where user_id in (select user_id from t_customer_hob)) f where u.id = f.user_id limit 10";

        //2.执行sql
        List<Map<String, Object>> list = template.queryForList(sql);
        return list;
    }

    /**
     * 更新商业用户销售id
     * @param salesman_id
     * @param user_code
     */
    public void updataSaleManId(String salesman_id, String user_code) {
        //1.定义sql
        String sql = "update t_customer set salesman_id = ? where 1=1 and user_code = ?";

        //2.执行sql
        template.update(sql, salesman_id, user_code);
    }

    /**
     * 根据用户名查询user_id
     * @param name
     * @return
     */
    public int getUserid(String name) {
        //1.定义sql
        String sql = "select id from t_user where name = ? and state=1 and company is not null";
        int id=-1;

        //2.执行sql
        try {
            id = template.queryForInt(sql, name);
        }catch (EmptyResultDataAccessException e) {
            System.out.println("空的数据");
        } catch (IncorrectResultSizeDataAccessException e) {
            System.out.println("多条数据");
        } finally {
            return id;
        }
    }
}
