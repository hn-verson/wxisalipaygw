package com.nykj.wxisalipaygw.service.alipay;

import com.nykj.wxisalipaygw.model.HisModel;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by Verson on 2016/5/18.
 */
@Service
@Scope("prototype")
public class HisService {
    private static final Logger LOGGER = Logger.getLogger(HisService.class);

    @Autowired
    private HisModel hisModel;

    /**
     * 门诊待缴费记录列表查询
     * @param bizBody
     * @return
     * @throws Exception
     */
    public String mzFeeListQuery(JSONObject bizBody) throws Exception{
        if(bizBody == null){
            throw new Exception("门诊待缴费记录列表查询业务参数为空");
        }
        JSONObject paramsJson = new JSONObject();
        paramsJson.put("card_no",bizBody.getString("card_no"));
        paramsJson.put("begin_time",bizBody.getString("begin_time"));
        paramsJson.put("end_time",bizBody.getString("end_time"));
        if(bizBody.has("branch_code")){
            paramsJson.put("branch_code",bizBody.getString("branch_code"));
        }
        return hisModel.mzFeeListQuery(paramsJson);
    }

    /**
     * 门诊待缴费记录明细查询
     * @param bizBody
     * @return
     * @throws Exception
     */
    public String mzFeeDetailQuery(JSONObject bizBody) throws Exception{
        if(bizBody == null){
            throw new Exception("门诊待缴费记录明细查询业务参数为空");
        }
        JSONObject paramsJson = new JSONObject();
        paramsJson.put("card_no",bizBody.getString("card_no"));
        paramsJson.put("begin_time",bizBody.getString("begin_time"));
        paramsJson.put("end_time",bizBody.getString("end_time"));
        if(bizBody.has("branch_code")){
            paramsJson.put("branch_code",bizBody.getString("branch_code"));
        }
        return hisModel.mzFeeDetailQuery(paramsJson);
    }

    /**
     * 门诊缴费状态查询
     * @param bizBody
     * @return
     * @throws Exception
     */
    public String mzFeeStateQuery(JSONObject bizBody) throws Exception{
        if(bizBody == null){
            throw new Exception("门诊待缴费记录明细查询业务参数为空");
        }
        JSONObject paramsJson = new JSONObject();
        //TODO 请求接口参数的组装
        return hisModel.mzFeeStateQuery(paramsJson);
    }
}