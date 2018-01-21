package com.cdsxt.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.SimpleType;

import java.io.IOException;
import java.util.Map;

public class JsonUtil {

    /**
     * 解析器：线程安全
     */
    private static ObjectMapper jsonParse = new ObjectMapper();


    /**
     * 字符串转数组
     * <p>
     * jsonStr 被转换的json字符串
     * EleCls 数组的元素类型（javabean）
     */
    public static <T> T[] jsonStrToArr(String jsonStr, Class<T> EleCls) {

        try {
            ArrayType arrType = ArrayType.construct(
                    SimpleType.construct(EleCls), null, null);
            T[] arr;
            arr = jsonParse.readValue(jsonStr,
                    arrType);
            return arr;
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对象转json字符串
     */
    public static String objToJsonStr(Object obj) {
        try {
            String jsonStr = jsonParse.writeValueAsString(obj);
            return jsonStr;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对象转json字符串
     */
    public static Map<String, Object> jsonStrToMap(String jsonStr) {
        try {
            Map<String, Object> map = jsonParse.readValue(jsonStr, Map.class);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
