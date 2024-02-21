package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FormatDate {

//    public static Map getDate() {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        String date = format.format(new Date());
//        HashMap<String, String> map = new HashMap<>();
//
//        long starTime = 0;
//        long endTime = 0;
//
//        String minite_morning = "9:00:00";
//        String minite_afternoon = "18:00:00";
//        try {
//            long timestamp = new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime();
//            long minite_mor = new SimpleDateFormat("HH:mm:ss").parse(minite_morning).getTime();
//            long minite_nig = new SimpleDateFormat("HH:mm:ss").parse(minite_afternoon).getTime();
//            starTime = timestamp + minite_mor + 28800000;
//            endTime = timestamp + minite_nig + 28800000;
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        map.put("startTime", String.valueOf(starTime));
//        map.put("endTime", String.valueOf(endTime));
//        return map;
//    }

    public static Map getDate() {
        HashMap<String, String> map = new HashMap<>();

        int l = 86400000;
        long l1 = System.currentTimeMillis();
        long now = l1-l;
        String today = String.valueOf(now);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR,1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(calendar.getTime());

        Date date = null;
        try {
            date = simpleDateFormat.parse(format);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String nextYear = String.valueOf(date.getTime());

        map.put("today",today);
        map.put("nextYear",nextYear);
        return map;
    }
}
