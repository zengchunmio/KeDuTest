package utils;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Iterator;

/**
 * @program: xiaozi-oa
 * @description:
 * @packagename: com.xiaozi.oa.controller.internal
 * @author: xuedongdong
 * @date: 2022-02-28 11:14
 **/
public class JsonOther {
    public String getJsonStr3() {
        String jsonStr = "{\"heads\":{\"code\":200,\"message\":\"success\"},\"body\":{\"startRow\":1,\"navigatepageNums\":[1],\"lastPage\":1,\"prePage\":0,\"hasNextPage\":false,\"nextPage\":0,\"pageSize\":10,\"endRow\":2,\"list\":[{\"brandName\":\"飞利浦PHILIPS\",\"skuList\":[{\"id\":\"944615234218364928\",\"goodsCode\":\"080202022021944521\",\"spec\":\"XING\"}],\"createTime\":1645255421000,\"name\":\"工具\",\"id\":\"944615234151256064\",\"modelType\":\"REPLACE\",\"enterpriseName\":\"正安（北京）医疗设备有限公司\",\"status\":\"PASS\",\"medicalFlag\":false,\"lastUpdateTime\":1645255694000},{\"brandName\":\"韶关市泰宏医疗器械有限公司\",\"skuList\":[{\"id\":\"942831550821699584\",\"goodsCode\":\"080202022021488658\",\"spec\":\"2222\"}],\"createTime\":1644830158000,\"name\":\"测试工具商品同步\",\"id\":\"942831550737813504\",\"modelType\":\"REPLACE\",\"enterpriseName\":\"常州雅思医疗器械有限公司\",\"status\":\"PASS\",\"medicalFlag\":false,\"lastUpdateTime\":1644830158000}],\"pageNum\":1,\"navigatePages\":8,\"total\":2,\"navigateFirstPage\":1,\"pages\":1,\"size\":2,\"firstPage\":1,\"isLastPage\":true,\"hasPreviousPage\":false,\"navigateLastPage\":1,\"isFirstPage\":true}}\n";
        return jsonStr;
    }

    @SuppressWarnings("rawtypes")
    public static void analysisJson(Object objJson){
        //如果obj为json数组
        if(objJson instanceof JSONArray){
            JSONArray objArray = (JSONArray)objJson;
            for (int i = 0; i < objArray.size(); i++) {
                analysisJson(objArray.get(i));
            }
        }
        //如果为json对象
        else if(objJson instanceof JSONObject){
            JSONObject jsonObject = (JSONObject)objJson;
            Iterator it = jsonObject.keys();
            while(it.hasNext()){
                String key = it.next().toString();
                Object object = jsonObject.get(key);
                //如果得到的是数组
                if(object instanceof JSONArray){
                    JSONArray objArray = (JSONArray)object;
                    analysisJson(objArray);
                }
                //如果key中是一个json对象
                else if(object instanceof JSONObject){
                    analysisJson((JSONObject)object);
                }
                //如果key中是其他
                else{
                    System.out.println("["+key+"]:"+object.toString()+" ");
                }
            }
        }
    }
    public static void main(String[] args) {
        JsonOther hw = new JsonOther();
        JSONObject jsonObject = JSONObject.fromObject(hw.getJsonStr3());
        hw.analysisJson(jsonObject);
    }
}

