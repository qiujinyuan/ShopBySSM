import com.cdsxt.util.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class TestLogistics {


    public static void main(String[] args) {

        String str = "{\n" +
                "\t\"status\": \"0\",\n" +
                "\t\"msg\": \"ok\",\n" +
                "\t\"result\": {\n" +
                "\t\t\"number\": \"1202516745301\",\n" +
                "\t\t\"type\": \"yunda\",\n" +
                "\t\t\"list\": [{\n" +
                "\t\t\t\"time\": \"2017-01-07 16:05:38\",\n" +
                "\t\t\t\"status\": \"湖南省炎陵县公司快件已被 已签收 签收\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"time\": \"2017-01-07 16:02:43\",\n" +
                "\t\t\t\"status\": \"湖南省炎陵县公司快件已被 已签收 签收\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"time\": \"2017-01-07 15:43:42\",\n" +
                "\t\t\t\"status\": \"湖南省炎陵县公司进行派件扫描；派送业务员：陈晓东；联系电话：18173377752\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"time\": \"2017-01-06 18:26:08\",\n" +
                "\t\t\t\"status\": \"湖南长沙分拨中心从站点发出，本次转运目的地：湖南省炎陵县公司\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"time\": \"2017-01-06 17:06:52\",\n" +
                "\t\t\t\"status\": \"湖南长沙分拨中心在分拨中心进行卸车扫描\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"time\": \"2017-01-05 23:48:08\",\n" +
                "\t\t\t\"status\": \"浙江杭州分拨中心进行装车扫描，即将发往：湖南长沙分拨中心\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"time\": \"2017-01-05 23:44:03\",\n" +
                "\t\t\t\"status\": \"浙江杭州分拨中心进行中转集包扫描，将发往：湖南长沙分拨中心\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"time\": \"2017-01-05 23:35:40\",\n" +
                "\t\t\t\"status\": \"浙江杭州分拨中心在分拨中心进行称重扫描\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"time\": \"2017-01-05 20:01:03\",\n" +
                "\t\t\t\"status\": \"浙江主城区公司杭州拱墅区祥符桥服务部进行揽件扫描\"\n" +
                "\t\t}],\n" +
                "\t\t\"deliverystatus\": \"3\",\n" +
                "\t\t\"issign\": \"1\"\n" +
                "\t}\n" +
                "}";

        // System.out.println(JsonUtil.jsonStrToMap(JsonUtil.jsonStrToMap(str).get("result") + ""));
        System.out.println(JsonUtil.jsonStrToMap(str).get("result"));
        System.out.println(((LinkedHashMap)JsonUtil.jsonStrToMap(str).get("result")).get("list"));
        System.out.println(((ArrayList)((HashMap)JsonUtil.jsonStrToMap(str).get("result")).get("list")).get(0));
    }
}
