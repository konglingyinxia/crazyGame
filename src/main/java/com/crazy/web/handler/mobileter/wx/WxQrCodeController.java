package com.crazy.web.handler.mobileter.wx;

import com.crazy.util.twm.QRCodeUtil;
import com.crazy.util.wx.ConfigUtil;
import com.crazy.util.wx.HttpUtil;
import com.crazy.util.wx.PayCommonUtil;
import com.crazy.util.wx.XMLUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 类描述：微信授权登录 <br>
 * 作者：田帅 <br>
 * 创建时间：2017-09-16 <br>
 * 版本：V1.0
 */
@Controller
@RequestMapping(value = "/wxQrCodeController")
public class WxQrCodeController {
    private static final Logger logger = Logger.getLogger(WxQrCodeController.class);


    /**
     * 微信扫码支付二维码
     * wxQrCodeController/getWxPayQrCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getWxPayQrCode")
    public void getWxPayQrCode(HttpServletRequest request,HttpServletResponse response){
// 账号信息
        String appid = ConfigUtil.APPIDH5;  // appid
        //String appsecret = PayConfigUtil.APP_SECRET; // appsecret
        String mch_id = ConfigUtil.MCH_IDH5; // 商业号
        String key = ConfigUtil.API_KEYH5; // key
        String nonce_str = PayCommonUtil.CreateNoncestr();
        String order_price = 1+ ""; // 价格   注意：价格的单位是分
        String body = "测试";   // 商品名称
        // 生成32位订单号
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHHmmssSSS");
        String out_trade_no = "YX" + formatter.format(new Date());// 充值订单号时间戳
        out_trade_no += formatter.format(new Date());// 充值订单号时间戳

        // 订单生成的机器 IP
        String spbill_create_ip = PayCommonUtil.getIpAddress(request);
        spbill_create_ip = (spbill_create_ip.split(","))[0];
        spbill_create_ip = "127.0.0.1";
        // 回调接口
        String notify_url = ConfigUtil.QR_CODE_NOTIFY_URL;
        String trade_type = "NATIVE";

        SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();
        packageParams.put("appid", appid);
        packageParams.put("mch_id", mch_id);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", body);
        packageParams.put("out_trade_no", out_trade_no);
        packageParams.put("total_fee", order_price);
        packageParams.put("spbill_create_ip", spbill_create_ip);
        packageParams.put("notify_url", notify_url);
        packageParams.put("trade_type", trade_type);

        String sign = PayCommonUtil.createSign("UTF-8", packageParams);// 生成签名
        packageParams.put("sign", sign);

        String requestXML = PayCommonUtil.getRequestXml(packageParams);
        System.out.println(requestXML);

        String resXml = HttpUtil.postData(ConfigUtil.UFDODER_URL, requestXML);
        Map map = null;
        try {
            map = XMLUtil.doXMLParse(resXml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String return_code = (String) map.get("return_code");
        String prepay_id = (String) map.get("prepay_id");
        String urlCode = (String) map.get("code_url");
        // 生成二维码
        try {
            BufferedImage img = QRCodeUtil.getImage(urlCode);
            ImageIO.write(img, "JPG", response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 微信扫码支付异步回调
     * wxQrCodeController/weixin_notify
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/weixin_notify")
    public void weixin_notify(HttpServletRequest request,HttpServletResponse response) throws Exception{

        //读取参数
        InputStream inputStream ;
        StringBuffer sb = new StringBuffer();
        inputStream = request.getInputStream();
        String s ;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null){
            sb.append(s);
        }
        in.close();
        inputStream.close();

        //解析xml成map
        Map<String, String> m = new HashMap<String, String>();
        m = XMLUtil.doXMLParse(sb.toString());

        //过滤空 设置 TreeMap
        SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();
        Iterator it = m.keySet().iterator();
        while (it.hasNext()) {
            String parameter = (String) it.next();
            String parameterValue = m.get(parameter);

            String v = "";
            if(null != parameterValue) {
                v = parameterValue.trim();
            }
            packageParams.put(parameter, v);
        }

        // 账号信息
        String key = ConfigUtil.API_KEYH5; // key

        logger.info(packageParams);
        //判断签名是否正确
        if(PayCommonUtil.isTenpaySign("UTF-8", packageParams,key)) {
            //------------------------------
            //处理业务开始
            //------------------------------
            String resXml = "";
            if("SUCCESS".equals((String)packageParams.get("result_code"))){
                // 这里是支付成功
                //////////执行自己的业务逻辑////////////////
                String mch_id = (String)packageParams.get("mch_id");
                String openid = (String)packageParams.get("openid");
                String is_subscribe = (String)packageParams.get("is_subscribe");
                String out_trade_no = (String)packageParams.get("out_trade_no");

                String total_fee = (String)packageParams.get("total_fee");

                logger.info("mch_id:"+mch_id);
                logger.info("openid:"+openid);
                logger.info("is_subscribe:"+is_subscribe);
                logger.info("out_trade_no:"+out_trade_no);
                logger.info("total_fee:"+total_fee);

                //////////执行自己的业务逻辑////////////////

                logger.info("支付成功");
                //通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

            } else {
                logger.info("支付失败,错误信息：" + packageParams.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
            //------------------------------
            //处理业务完毕
            //------------------------------
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            out.write(resXml.getBytes());
            out.flush();
            out.close();
        } else{
            logger.info("通知签名验证失败");
        }

    }
}
