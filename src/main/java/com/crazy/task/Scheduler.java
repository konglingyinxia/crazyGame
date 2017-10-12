package com.crazy.task;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.crazy.util.wx.ConfigUtil;
import com.crazy.util.wx.HttpKit;
import com.crazy.web.service.repository.jpa.WxConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时器
 * @author 田帅
 *
 */
@Component
public class Scheduler {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private WxConfigRepository wxConfigRepository;

    @Scheduled(fixedRate=7200000)
    public void testTasks() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+ ConfigUtil.APPIDH5+"&secret="+ConfigUtil.APP_SECRECTH5;
        String result = null;
        try{
            //请求获取token值
            result = HttpKit.get(url);
            JSONObject tokenJson = (JSONObject) JSON.parse(result);
            if (null != (tokenJson.get("access_token"))) {
                //更新数据库token值
                wxConfigRepository.setTypevalueById("1",String.valueOf(tokenJson.get("access_token")));
                //请求获取ticket值
                url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token="+tokenJson.get("access_token");
                result = HttpKit.get(url);
                JSONObject ticket = (JSONObject) JSON.parse(result);
                if (null != (ticket.get("ticket"))) {
                    //更新数据库ticket值
                    wxConfigRepository.setTypevalueById("2",String.valueOf(ticket.get("ticket")));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}