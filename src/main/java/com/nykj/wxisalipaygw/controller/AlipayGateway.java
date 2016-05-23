package com.nykj.wxisalipaygw.controller;

import com.alipay.api.internal.util.StringUtils;
import com.nykj.wxisalipaygw.constants.StatusCode;
import com.nykj.wxisalipaygw.entity.alipay.UnitLink;
import com.nykj.wxisalipaygw.exception.ArgumentException;
import com.nykj.wxisalipaygw.service.alipay.AlipayService;
import com.nykj.wxisalipaygw.service.alipay.UnitInfoService;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * 开发者网关，支付宝所有主动和开发者的交互会经过此网关进入开发者系统
 *
 * @author Verson
 */
@Controller
@Scope("prototype")
@RequestMapping("/gateway")
public class AlipayGateway extends BaseController {

    private static final long serialVersionUID = 1210436705188940602L;

    private final static Logger LOGGER = Logger.getLogger(AlipayGateway.class);

    @Autowired
    private AlipayService alipayService;

    @Autowired
    private UnitInfoService unitInfoService;

    /**
     * 服务窗事件回调处理
     * @param channel   渠道
     * @param unitId    医院
     * @param request   请求
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/{channel}/{unit_id}/window", method = RequestMethod.POST)
    @ResponseBody
    public String alipayWindowEventCallBack(@PathVariable(value = "channel") String channel,
                                 @PathVariable(value = "unit_id") String unitId,
                                 HttpServletRequest request) {
        Map<String,String> params = null;
        JSONObject alipayBizBody = null;
        String responseMsg = null;
        try {
            //将原生参数解析成Map
            params = getRequestParams(request);
            LOGGER.info("支付宝窗口事件回调参数:"+ params);

            //1. 根据请求信息组装业务体
            alipayBizBody = buildAlipayBizBody(request);
            alipayBizBody.put("unit_id",unitId);
            alipayBizBody.put("channel",channel);

            //将医院服务窗配置信息整合至业务消息体
            UnitLink unitLink = unitInfoService.findUnitLinkByUnitId(unitId);
            alipayBizBody.put("unit_link",unitLink);

            //2. 验证签名
            alipayService.verifySign(alipayBizBody);

            //3. 执行业务逻辑
            responseMsg = alipayService.handlerCallBack(alipayBizBody);

            alipayBizBody.put("response_msg",responseMsg);

        } catch (Exception e) {
            LOGGER.error("支付宝回调网关执行异常:" + e);
        } finally {
            //4. 响应结果加签及返回
            try {
                responseMsg = alipayService.encryptAndSign(alipayBizBody);
                LOGGER.info("响应结果加签内容:" + responseMsg);
            } catch (Exception e) {
                LOGGER.error("响应结果加签失败:" + e);
            }
        }
        return responseMsg;
    }

    /**
     * 支付宝业务处理回调处理
     * @param channel   渠道
     * @param unitId    医院
     * @param request   请求
     */
    @RequestMapping(value = "/{channel}/{unit_id}/biz", method = RequestMethod.GET)
    @ResponseBody
    public String alipayBizHandlerCallBack(@PathVariable(value = "channel") String channel,
                                         @PathVariable(value = "unit_id") String unitId,
                                         HttpServletRequest request){
        //TODO 后续可能会涉及到支付宝业务回调网关的情况
        Map<String,String> params = null;
        JSONObject alipayBizBody = null;
        try {
            params = getRequestParams(request);
            LOGGER.info("支付宝业务处理回调参数:"+ params);

        }catch (Exception e){
            LOGGER.error("支付宝业务处理回调处理异常:" + e);
        }
        return channel + unitId + "biz";
    }

    /**
     * 构建支付宝窗口事件回调请求体，方便简化下层逻辑的处理
     * @param request
     * @return
     */
    public JSONObject buildAlipayBizBody(HttpServletRequest request) throws Exception{
        if(request == null){
            throw new NullPointerException("请求体【request】为空，无法构建业务体");
        }
        Map<String,String> params = getRequestParams(request);

        //获取服务信息
        String service = params.get("service");
        if (StringUtils.isEmpty(service)) {
            throw new ArgumentException(StatusCode.ARGUMENT_EXCEPTION,"无法取得服务名");
        }

        //获取内容信息
        String bizContent = params.get("biz_content");
        if (StringUtils.isEmpty(bizContent)) {
            throw new ArgumentException(StatusCode.ARGUMENT_EXCEPTION,"无法取得业务内容信息");
        }

        JSONObject bizContentJson = (JSONObject) new XMLSerializer().read(bizContent);
        JSONObject alipayBizBody = new JSONObject();
        alipayBizBody.put("params",params);
        alipayBizBody.put("biz_content",bizContentJson);
        alipayBizBody.put("service",service);

        return alipayBizBody;
    }

}