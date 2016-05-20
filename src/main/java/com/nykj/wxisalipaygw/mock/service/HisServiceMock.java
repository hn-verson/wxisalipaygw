package com.nykj.wxisalipaygw.mock.service;

import com.nykj.wxisalipaygw.mock.model.HisModelMock;
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
public class HisServiceMock {
    private static final Logger LOGGER = Logger.getLogger(HisServiceMock.class);

    @Autowired
    private HisModelMock hisModelMock;

    /**
     * 门诊待缴费记录列表查询
     * @param bizBody
     * @return
     * @throws Exception
     */
    public String mzFeeListQuery(JSONObject bizBody) throws Exception{
        return hisModelMock.mzFeeListQuery(bizBody);
    }

    /**
     * 门诊待缴费记录明细查询
     * @param bizBody
     * @return
     * @throws Exception
     */
    public String mzFeeDetailQuery(JSONObject bizBody) throws Exception{
        return hisModelMock.mzFeeDetailQuery(null);
    }
}