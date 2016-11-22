package com.nykj.wxisalipaygw.model;

import com.nykj.wxisalipaygw.constants.HisConstants;
import com.nykj.wxisalipaygw.util.HttpUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created by Verson on 2016/5/13.
 */
@Repository
public class HisModel {
    private static final Logger LOGGER = Logger.getLogger(HisModel.class);

    @Value("#{config['his.api.mz.fee.list.query']}")
    private String mzFeeListApi;

    @Value("#{config['his.api.mz.fee.detail.query']}")
    private String mzFeeDetailApi;

    @Value("#{config['his.api.mz.fee.state.query']}")
    private String mzFeeStateApi;

    /**
     * 门诊待缴费记录列表查询
     * @param paramsJson
     * @return
     */
    public String mzFeeListQuery(JSONObject paramsJson) throws Exception{
        return HttpUtil.httpPost(mzFeeListApi,paramsJson, HisConstants.REQUEST_CONTENT_TYPE);
    }

    /**
     * 门诊待缴费记录明细查询
     * @param paramsJson
     * @return
     */
    public String mzFeeDetailQuery(JSONObject paramsJson) throws Exception{
        return HttpUtil.httpPost(mzFeeDetailApi,paramsJson, HisConstants.REQUEST_CONTENT_TYPE);
    }

    public String mzFeeStateQuery(JSONObject paramsJson) throws Exception{
        return HttpUtil.httpPost(mzFeeStateApi,paramsJson, HisConstants.REQUEST_CONTENT_TYPE);
    }
}