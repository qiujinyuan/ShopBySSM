package com.cdsxt.vo;

import java.util.List;
import java.util.Map;

/**
 * 物流信息
 */


/*
{
	"status": "0",
	"msg": "ok",
	"result": {
		"number": "1202516745301",
		"type": "yunda",
		"list": [{
			"time": "2017-01-07 16:05:38",
			"status": "湖南省炎陵县公司快件已被 已签收 签收"
		}, {
			"time": "2017-01-07 16:02:43",
			"status": "湖南省炎陵县公司快件已被 已签收 签收"
		}, {
			"time": "2017-01-07 15:43:42",
			"status": "湖南省炎陵县公司进行派件扫描；派送业务员：陈晓东；联系电话：18173377752"
		}, {
			"time": "2017-01-06 18:26:08",
			"status": "湖南长沙分拨中心从站点发出，本次转运目的地：湖南省炎陵县公司"
		}, {
			"time": "2017-01-06 17:06:52",
			"status": "湖南长沙分拨中心在分拨中心进行卸车扫描"
		}, {
			"time": "2017-01-05 23:48:08",
			"status": "浙江杭州分拨中心进行装车扫描，即将发往：湖南长沙分拨中心"
		}, {
			"time": "2017-01-05 23:44:03",
			"status": "浙江杭州分拨中心进行中转集包扫描，将发往：湖南长沙分拨中心"
		}, {
			"time": "2017-01-05 23:35:40",
			"status": "浙江杭州分拨中心在分拨中心进行称重扫描"
		}, {
			"time": "2017-01-05 20:01:03",
			"status": "浙江主城区公司杭州拱墅区祥符桥服务部进行揽件扫描"
		}],
		"deliverystatus": "3",
		"issign": "1"
	}
}
 */
public class LogisticsInfo {
    private String number;
    private String type;
    private List<Map<String, String>> results;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Map<String, String>> getResults() {
        return results;
    }

    public void setResults(List<Map<String, String>> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "LogisticsInfo{" +
                "number='" + number + '\'' +
                ", type='" + type + '\'' +
                ", results=" + results +
                '}';
    }
}
