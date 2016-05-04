package com.nykj.wxisalipaygw.model;

import com.nykj.wxisalipaygw.entity.ChannelStatistics;
import org.springframework.stereotype.Repository;

/**
 * Created by Verson on 2016/4/28.
 */
@Repository
public interface ChannelStatisticsMapper {
    Integer insertChannelStatistics(ChannelStatistics channelStatistics);
}
