package kedu_interface.OA;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import kedu_interface.init.Init;
import kedu_interface.login.KeDuLogin;
import kedu_interface.sale.Reviewer;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.Reporter;
import utils.GetJson;
import utils.HttpUtil;
import utils.JsonArraySort;
import utils.ReadTxt;

import java.util.Iterator;

import static utils.GetJson.getValByKey;

public class Approve {
    /**
     * 审批：
     * 1.查询审批人员及编号
     * 2.审批人员登陆获取token
     * 3.查询审批taskid
     * 4.审核人员审批task
     */

    /**
     * 需求单审批：
     */
    public static void approveOrder(String applyid,String emergencyLevel){
        JSONObject jsonObject = getApplyIdAndApprovePerson(applyid);
        JSONArray list = jsonObject.getJSONArray("list");

        list = JsonArraySort.arraySort(list);

        Iterator<Object> iterator = list.iterator();
        while (iterator.hasNext()){
            JSONObject object = (JSONObject) iterator.next();
            String auditStatus = (String) object.get("auditStatus");
            String businessId = (String) object.get("businessId");
            String businessNo = (String) object.get("businessNo");
            String userRealName = (String) object.get("userRealName");
            String userName = (String) object.get("userName");


            if (auditStatus.equals("1")){
                String token = getApproveToken(userRealName,userName);
                String taskId = queryApproveOrder(token, businessNo);
                approveOrder(token,applyid,taskId,emergencyLevel,businessNo,businessId);
            }else {
                System.out.println("已审核");
            }
        }
    }

    /**
     * 非需求单审批：
     */
    public static void approveOther(String applyid){
        JSONObject jsonObject = getApplyIdAndApprovePerson(applyid);
        JSONArray list = jsonObject.getJSONArray("list");

        list = JsonArraySort.arraySort(list);

        Iterator<Object> iterator = list.iterator();
        while (iterator.hasNext()){
            JSONObject object = (JSONObject) iterator.next();
            String auditStatus = (String) object.get("auditStatus");
            String businessNo = (String) object.get("businessNo");
            String userRealName = (String) object.get("userRealName");
            String userName = (String) object.get("userName");

            if (auditStatus.equals("1")){
                String token = getApproveToken(userRealName,userName);
                String taskId = queryApproveOrder(token, businessNo);
                approveOther(token,applyid,taskId);
            }else {
                System.out.println("已审核");
            }
        }
    }

    /**
     * 查询审批人员及审批编号
     *
     * @param applyId
     * @return
     */
    public static JSONObject getApplyIdAndApprovePerson(String applyId) {
        String token = ReadTxt.readFile();
        String url = Init.url;
        //查询单据审核人
        url += "kedu-oa/oa/process/queryPageList";

        String param = "{\"applyId\":\"" + applyId + "\"}";

        HttpResponse response = HttpUtil.post(url, param, token);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String rs = HttpUtil.getResponse(response);

        System.out.println("rs:"+rs);

        JSONObject json = JSON.parseObject(rs);

        JSONObject jsonObject = json.getJSONObject("body");

        return jsonObject;

    }

    /**
     * 审批人员登陆获取token
     */
    public static String getApproveToken(String realName,String userName){
        JSONObject json = Reviewer.getPassword(realName,userName);
        String phoneNo = String.valueOf(GetJson.getValByKey(json, "phoneNo"));
        String password = String.valueOf(GetJson.getValByKey(json, "password"));
        String token = KeDuLogin.login(phoneNo, password);
        return token;
    }

    /**
     * 查询审批taskId
     */
    public static String queryApproveOrder(String token, String businessNo) {
        String url = Init.url;
        //查询审核列表
        url += "kedu-oa/oa/apply/queryPageList";

        String param = "{\"businessNo\":\"" + businessNo + "\",\"auditStatus\":\"1\",\"pageNum\":1,\"pageSize\":10}";

        HttpResponse response = HttpUtil.post(url, param, token);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String rs = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(rs);
        System.out.println("rs:"+rs);
        String taskId = (String) getValByKey(json, "taskId");
        return taskId;
    }

    /**
     * 审批需求单任务
     */
    public static void approveOrder(String token, String applyId, String taskId,String emergencyLevel,String businessNo,String businessId) {
        String url = Init.url;
        //  审核
        url += "kedu-oa/oa/task/completeTask";

        String param = "{\"taskId\":\"" + taskId + "\",\"applyId\":\"" + applyId + "\",\"auditResult\":1,\"auditOpinion\":\"\"," +
                "\"emergencyLevel\":\""+ emergencyLevel + "\",\"businessNo\":\""+businessNo+"\",\"businessId\":\""+businessId+"\"}";
        System.out.println("param:"+param);
        HttpResponse response = HttpUtil.post(url, param, token);
        //System.out.println("response:"+response);
//        int statusCode = response.getStatusLine().getStatusCode();
//        System.out.println("code:"+statusCode);
//


        String rs = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(rs);
        System.out.println("rs:"+rs);
        int statusCode = ((JSONObject)getValByKey(json, "heads")).getInteger("code");
        Assert.assertEquals( statusCode,200);

        System.out.println(rs);


//        boolean result = (boolean) getValByKey(json, "body");
//        if (result) {
//            Reporter.log("审批成功,id为：" + applyId);
//        }
    }


    /**
     * 审批其他任务
     */
    public static void approveOther(String token, String applyId, String taskId) {
        String url = Init.url;
        url += "kedu-oa/oa/task/completeTask";

        String param = "{\"taskId\":\"" + taskId + "\",\"applyId\":\"" + applyId + "\",\"auditResult\":1,\"auditOpinion\":\"\"}";

        HttpResponse response = HttpUtil.post(url, param, token);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals( statusCode,200);

        String rs = HttpUtil.getResponse(response);
        JSONObject json = JSON.parseObject(rs);

        boolean result = (boolean) getValByKey(json, "body");
        if (result) {
            Reporter.log("审批成功,id为：" + applyId);
        }
    }


}
