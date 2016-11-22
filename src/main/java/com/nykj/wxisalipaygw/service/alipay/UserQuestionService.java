package com.nykj.wxisalipaygw.service.alipay;

import com.nykj.wxisalipaygw.constants.AlipayEnvConstants;
import com.nykj.wxisalipaygw.model.alipay.UserQuestionMapper;
import com.nykj.wxisalipaygw.entity.alipay.AutoReply;
import com.nykj.wxisalipaygw.entity.alipay.UserQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

/**
 * Created by Verson on 2016/4/27.
 */
@Service
@Scope("prototype")
public class UserQuestionService {
    @Autowired
    private UserQuestionMapper userQuestionMapper;

    /**
     * 持久化用户提问信息
     * @param unitId
     * @param content
     * @return
     */
    public Integer insertUserQuestion(String unitId,String content){
        UserQuestion userQuestion = new UserQuestion();
        userQuestion.setUnitId(unitId);
        userQuestion.setUserQuestion(content);
        userQuestion.setCreateTime(new Date());
        return userQuestionMapper.insertUserQuestion(userQuestion);
    }

    /**
     * 自动回复：（按优先级排序）
     * 	常见问题自助查询
     *  关键词触发
     *  常见问题列表
     */
    public String getAutoReplyContentByUnitIdAndContent(String unitId,String content){
        String result = null;
        List<AutoReply> replys = userQuestionMapper.getAutoReplyListByUnitId(unitId);
        if(replys!=null && replys.size()>0){
            for (int i = 0; i < replys.size(); i++) {
                AutoReply reply = replys.get(i);
                if(reply.getTrigger_type()== AlipayEnvConstants.AUTO_REPLY_TRIGGER_TYPE_QUESTIONNUM){
                    //常见问题自助查询
                    if(content.equals(reply.getTrigger_word())){
                        result = reply.getReply_content();
                        break;
                    }
                }else if(reply.getTrigger_type()== AlipayEnvConstants.AUTO_REPLY_TRIGGER_TYPE_KEY){
                    //关键词触发
                    if(content.indexOf(reply.getTrigger_word())!=-1){
                        result = reply.getReply_content();
                        break;
                    }
                }else if(reply.getTrigger_type()== AlipayEnvConstants.AUTO_REPLY_TRIGGER_TYPE_DEFAULT){
                    //常见问题列表
                    result = reply.getReply_content();
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 根据医院编号、触发类型，查询回复内容
     * @param unitId
     * @param trigger_type
     * @return
     */
    public String getAutoReplyContent(String unitId, int trigger_type) throws Exception {
        String result = null;
        AutoReply autoReply = new AutoReply();
        autoReply.setUnit_id(unitId);
        autoReply.setTrigger_type(trigger_type);
        List<AutoReply> replys = userQuestionMapper.getAutoReply(autoReply);
        if(replys!=null && replys.size()>0){
            result = replys.get(0).getReply_content();
        }
        return result;
    }

}