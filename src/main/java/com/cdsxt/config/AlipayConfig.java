package com.cdsxt.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016091300498150";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDCtrnwA2EoQBhng8i0MquM5/kZ+7A4x9hCvm8sh8bTIc8D6rOlmj9Ql8utZkcHNCg8lK4VYohTxMPUqs23aBMdGQPKR+pBqbsErzEEaC4vq8Wp2PnkVK7mNTzePStEKkW6di+xf5GAFseQwFOlrQucjMs8dL2ujJtpm/H8VnCcfTQ+em8m2sZ+rsnX21OHlpPV4oscBAGrE0iWgqVNHCCtYm1ER6Gl+XIU1CuZ1ZJIW1rB2gj42nAYQhG3WRdHRhl+fxr79xCadd+MFfct6Dd8gHAS1T0UIUX5pTH3L817FCojA7vENltvGH0Gdw3S04pD3XRoB07W0ypyuSZrVjlRAgMBAAECggEAS2uNwAr/EiEpksb38dyCmHSuB0N3Mbcu7ENpOp1pBRTeY80AQs/pYGU7rnGkZ4TrZPPOLlhZJ+FaXUC/qB/271lmH28VrNN6rkjRurxDz5FAYha6TJgEmwTaC9czsBaTpbLrrFwWVr/8EvR8MftCOSRMAKHTaw3pkNGnvK8atXIX/VwwdrwgLBn9rQAiR27my2wRJhiTGGgHDwHDdTNyVRQd7wnwfqI3pZEwEg2mT8isE+y4b8hqBN6KQsFdDdaQkUkUxjjTbBoA4ZaVY65zJIW1igdbKd/occD/XGk6L3iqY5T58rvct7bcJW/7Rt9f/ZFBOu4cmxSDZxeZ4R82BQKBgQD2rN4tqnNbD9XOtgDcpAxrF4GCRh5iapSqDkhLqpqDKT19unH5nVjXcbYDjShO1jJa7PBZ0dVXwNIlSP/ixf2I5JDHZBRR1+HVFhWOPda8B3gCDl6HQfUiTMBiqeFw3WEoKyqq3qeAYd0gzlAYK5Tw+emPhr17K9+E9MfB+ddz9wKBgQDKEwPwNtLfftcbg6gb1BFkgyxWEpRRPKauWsxU3XZgJUC6n1i2MmlBpFRisIkoFO/4xsavyF3P6RCNILCkLk94AZhYBHYqtRc7m5e4jcOpEg2aQvrT8Q+lmZO/cdjb5eMSoCXQteZQ0KCKmIpY3jWqRad21X71Ks3nl05QIrPa9wKBgQDhKDE0pZzdxbp3EBIBU4wyRCZmbvJVCsvf3WGID5Uxm3cRSBm4qjLplFV7MGFWdJVKAXPxyGJf0xT+C3/l0qr1RQwRs/wIKHFuOtY6G5/hyT91QQBvnXjPchc4969bgfao3532kX165dkdlLPvG/i7bKIP7AEQaJ+BkJy1JgPPSQKBgQCzQRsFRBzb1ZDL/4Zo6T5kg1gZInIffkX99N29ipcvwtwgsKnpNd7ZNAUHZsLEo44ciXhHBt7xSDY+evk3Kz2jnwadTZflWq85WVTFijw/Bpy6cyA+UmEm7Y17Dmro5o0AQMBK3JFqryc6ywYuvy+r1A4yBEGTCl2NS1NVEvOpEQKBgGGKvCikI11g2NNAgzwTnjyfCZZYsLetXtp4p6O+XLJBKkkSgu5Warl0cfrOC4SqWo31VJA2xdlnbdXFVqNVMpef6Oz6bFRVAdLhS/mG9dASA3qmOwnAByH14egVlyegbB55JNhcfvtjfH/Hde9SVW19Fs3TFulzuipPS3nNTtau";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuvts1D3UmAOfrTXKTzi2kCxMBA98XjNwPKPeiDD7CPt35+WrmBcZQyXm8RpVVHB8gpUQwpzGsvI37yXXRrRUbPIPxu4eZr5VaJUYwBSUetixbdRCxv2Fz8FrknKQ70yZ0z9yahmMcXsgxSIbt5QcpWRP9zeWV+kHo0zJN/n0abNFhhH1zTqv6VRVCFf/IpP4Z5q5qQQRgVY3BaZVwlQ9K6t/cDvBC7lAt3GYa3IddtHRJ1VKc7s6BgA7fvKqFkT54ULezeWJYDh+MOzkJTxh18iaBcVAmHvSDbvTSb+qXrcs2d3o2M7BTS5jct2hvVjfa8wzg9YYQ455cse/a/irDwIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://0a9a0258.ngrok.io/alipay/notifyUrl";
    // public static String notify_url = "http://localhost:8080/alipay/notifyUrl";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://0a9a0258.ngrok.io/alipay/returnUrl";
    // public static String return_url = "http://localhost:8080/alipay/returnUrl";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "D:\\Java\\training\\ShopBySSM\\src\\main\\webapp";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     *
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis() + ".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


