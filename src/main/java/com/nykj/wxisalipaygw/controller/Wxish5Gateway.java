package com.nykj.wxisalipaygw.controller;

import com.nykj.wxisalipaygw.constants.Wxish5Constants;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Verson on 2016/5/4.
 */
@Controller
@RequestMapping("/gateway")
public class Wxish5Gateway {

    @RequestMapping(value = "/wxish5", method = RequestMethod.POST)
    @ResponseBody
    public String handler(@RequestParam("request_time") String requestTime,
                          @RequestParam("token") String token,
                          @RequestParam("data") String data,
                          @RequestParam("service") String service,
                          HttpServletRequest request){
        JSONObject bizBodyJson = new JSONObject();
        JSONObject responseMsgJson = new JSONObject();
        if(StringUtils.isEmpty(service)){
            responseMsgJson.put("code",1);
            responseMsgJson.put("msg","服务参数为空");
            return responseMsgJson.toString();
        }
        if(StringUtils.isEmpty(data)){
            responseMsgJson.put("code",1);
            responseMsgJson.put("msg","业务参数为空");
            return responseMsgJson.toString();
        }
        JSONObject dataJson = JSONObject.fromObject(data);
        String unitId = dataJson.getString("unit_id");
        String channel = dataJson.getString("channel");

        if(Wxish5Constants.ALIPAY_USER_INFO_SERVICE.equals(service)){

        }else if(Wxish5Constants.ALIPAY_USER_GIS_SERVICE.equals(service)){

        }else{
            responseMsgJson.put("code",1);
            responseMsgJson.put("msg","未知的服务参数【" + service + "】");
        }
        return responseMsgJson.toString();
    }

}