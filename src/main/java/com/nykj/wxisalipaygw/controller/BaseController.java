package com.nykj.wxisalipaygw.controller;

import com.nykj.wxisalipaygw.constants.ParamNameConstants;
import com.nykj.wxisalipaygw.constants.StatusCodeConstants;
import com.nykj.wxisalipaygw.constants.Wxish5Constants;
import com.nykj.wxisalipaygw.entity.alipay.UnitLink;
import com.nykj.wxisalipaygw.exception.ArgumentException;
import com.nykj.wxisalipaygw.exception.BaseException;
import com.nykj.wxisalipaygw.service.alipay.UnitInfoService;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Verson on 2016/4/29.
 */
@Component
public class BaseController {
    private final static Logger LOGGER = Logger.getLogger(BaseController.class);

    @Autowired
    private UnitInfoService unitInfoService;

    /**
     * 获取项目应用目录
     * @param request   请求对象
     * @return
     */
    public String getProjectBasePath(HttpServletRequest request) throws Exception{
        if(request == null){
            return null;
        }
        return request.getScheme() + "://" + request.getServerName() + request.getContextPath();
    }

    /**
     * 获取所有request请求参数key-value
     * @param request   请求对象
     * @return
     */
    public Map<String, String> getRequestParams(HttpServletRequest request) throws Exception{

        Map<String, String> params = new HashMap<String, String>();
        if(null != request){
            Set<String> paramsKey = request.getParameterMap().keySet();
            for(String key : paramsKey){
                params.put(key, request.getParameter(key));
            }
        }
        return params;
    }

    /**
     * 请求参数验证
     * @param paramMap  请求参数
     * @return
     */
    public void paramsVerify(Map<String,String> paramMap,String[] requireValidBusiParams) throws Exception{
        String[] requireValidCommonParams = Wxish5Constants.REQUEST_COMMON_PARAMS;
        //公共参数验证
        if(requireValidCommonParams != null && requireValidCommonParams.length > 0){
            for(int i=0,len=requireValidCommonParams.length; i < len ; i++){
                String paramName = requireValidCommonParams[i];
                if(StringUtils.isEmpty(paramMap.get(paramName)))
                    throw new ArgumentException(StatusCodeConstants.ARGUMENT_EXCEPTION,"参数异常:" + paramName);
            }
        }
        //业务参数验证
        if(requireValidBusiParams != null && requireValidBusiParams.length > 0){
            String data = paramMap.get("data");
            JSONObject dataJson = JSONObject.fromObject(data);
            for(int i=0,len=requireValidBusiParams.length; i < len ; i++){
                String paramName = requireValidBusiParams[i];
                if(!dataJson.has(paramName))
                    throw new ArgumentException(StatusCodeConstants.ARGUMENT_EXCEPTION,"参数异常:" + paramName);
            }
        }
    }

    /**
     * 构建业务体
     * @param paramMap      请求参数
     * @param mustParams    必要参数
     * @param optParams     可选参数
     * @return
     */
    public JSONObject buildBizBody(Map<String,String> paramMap,String[] mustParams,String[] optParams) throws Exception{
        JSONObject bizBody = new JSONObject();
        String data = paramMap.get(ParamNameConstants.DATA);
        JSONObject dataJson = JSONObject.fromObject(data);

        //公共业务参数组装
        String[] busiCommonParams = Wxish5Constants.BUSI_COMMON_PARAMS;
        if(busiCommonParams != null && busiCommonParams.length > 0){
            for(int i = 0,len = busiCommonParams.length ; i < len ; i++ ){
                String paramName = busiCommonParams[i];
                bizBody.put(paramName,dataJson.getString(paramName));
            }
        }

        //必填业务参数组装
        if(mustParams != null && mustParams.length > 0){
            for(int i = 0,len = mustParams.length ; i < len ; i++){
                String paramName = mustParams[i];
                bizBody.put(paramName,dataJson.getString(paramName));
            }
        }

        //可选业务参数组装
        if(optParams != null && optParams.length > 0){
            for(int i = 0,len = optParams.length ; i < len ; i++){
                String paramName = optParams[i];
                bizBody.put(paramName,dataJson.has(paramName) ? dataJson.getString(paramName) : null);
            }
        }

        return bizBody;
    }

    /**
     * 包装成功响应体
     * @param dataJson 数据对象
     */
    public String wrapSuccessResponseMsg(Object dataJson){
        JSONObject responseMsgJson = new JSONObject();
        responseMsgJson.put(ParamNameConstants.DATA,dataJson);
        responseMsgJson.put(ParamNameConstants.CODE, StatusCodeConstants.SUCCESS);
        responseMsgJson.put(ParamNameConstants.MESSAGE,"SUCCESS");
        responseMsgJson.put(ParamNameConstants.TIME,System.currentTimeMillis());
        return responseMsgJson.toString();
    }

    /**
     * 包装失败响应体
     * @param e 异常对象
     */
    public String wrapFailerResponseMsg(Exception e){
        JSONObject responseMsgJson = new JSONObject();
        if(e instanceof BaseException){
            responseMsgJson.put(ParamNameConstants.CODE,((BaseException) e).getCode());
            responseMsgJson.put(ParamNameConstants.MESSAGE,e.getMessage());
        }else{
            responseMsgJson.put(ParamNameConstants.CODE, StatusCodeConstants.INTERNEL_SERVER_EXCEPTION);
            responseMsgJson.put(ParamNameConstants.MESSAGE,"内部服务异常");
        }
        responseMsgJson.put(ParamNameConstants.TIME,System.currentTimeMillis());
        return responseMsgJson.toString();
    }

    /**
     * 获取医院服务窗配置对象
     * @param bizBodyJson   业务对象
     * @return
     */
    public UnitLink fetchUnitLinkFromBizBody(JSONObject bizBodyJson){
        return unitInfoService.findUnitLinkByUnitId(bizBodyJson.getString(ParamNameConstants.UNIT_ID));
    }

}