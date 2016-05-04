package com.nykj.wxisalipaygw.service;

import com.nykj.wxisalipaygw.model.ChannelSceneMapper;
import com.nykj.wxisalipaygw.model.ChannelStatisticsMapper;
import com.nykj.wxisalipaygw.entity.ChannelScene;
import com.nykj.wxisalipaygw.entity.ChannelStatistics;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Verson on 2016/4/28.
 */
@Service
public class ChannelService {
    private final static Logger LOGGER = Logger.getLogger(ChannelService.class);

    @Autowired
    private ChannelStatisticsMapper channelStatisticsMapper;

    @Autowired
    private ChannelSceneMapper channelSceneMapper;

    public Integer insertChannelStatistics(JSONObject alipayBizBody) throws Exception{
        String unitId = alipayBizBody.getString("unit_id");
        JSONObject bizContentJson = (JSONObject) alipayBizBody.get("biz_content");
        JSONObject actionParam = bizContentJson.has("ActionParam") ? (JSONObject)bizContentJson.get("ActionParam") : null;
        JSONObject scene = (actionParam == null ? null : (JSONObject)actionParam.get("scene"));
        String sceneId = (scene == null ? null : scene.getString("sceneId"));

        //判断医院对应的场景值是否存在
        ChannelScene channelScene = new ChannelScene();
        channelScene.setUnit_id(unitId);
        channelScene.setScene_code(sceneId);

        int isExists = channelSceneMapper.isExistsChannelScene(channelScene);
        if(isExists < 1){
            LOGGER.info("医院【"+ unitId +"】没有对应的场景【"+ sceneId +"】配置");
            return -1 ;
        }

        ChannelStatistics channelStatistics = new ChannelStatistics();
        channelStatistics.setId(UUID.randomUUID().toString());
        channelStatistics.setFromUserName(bizContentJson.getString("FromAlipayUserId"));
        channelStatistics.setEvent(bizContentJson.getString("EventType"));
        channelStatistics.setCreateTime(bizContentJson.getString("CreateTime"));
        channelStatistics.setToUserName(bizContentJson.getString("AppId"));
        channelStatistics.setMsgType(bizContentJson.getString("MsgType"));
        channelStatistics.setScene_id(sceneId);
        channelStatistics.setCreate_time(new Date());

        return channelStatisticsMapper.insertChannelStatistics(channelStatistics);
    }

    public Integer isExistsChannelScene(ChannelScene channelScene){
        return channelSceneMapper.isExistsChannelScene(channelScene);
    }
}