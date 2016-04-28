package com.nykj.wxisalipaygw.dao.alipay;

import com.nykj.wxisalipaygw.entity.alipay.AutoReply;
import com.nykj.wxisalipaygw.entity.alipay.UserQuestion;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>项目名称:saleshelp</p>
 * <p>文件名称:UserMapper.java</p>
 * <p>文件描述:TODO</p>
 * <p>版权所有:深圳市宁远科技股份有限公司版权所有(C)2015</p>
 * <p>内容摘要:简要描述本文件的内容，包括主要模块、函数及能的说明</p>
 * <p>其他说明:其它内容的说明</p>
 * <p>创建日期:2015年12月9日 下午4:03:45</p>
 * <p>创建用户： Administrator</p>
 */

@Repository
public interface UserQuestionMapper {
    Integer insertUserQuestion(UserQuestion userQuestion);
    List<AutoReply> getAutoReplyListByUnitId(@Param("unit_id") String unitId);
    List<AutoReply> getAutoReply(AutoReply autoReply);
}
