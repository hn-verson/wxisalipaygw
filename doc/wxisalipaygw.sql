#支付宝服务窗配置表
CREATE TABLE "alipay_hosp_link" (
  "id" varchar(64) NOT NULL COMMENT '主键ID',
  "app_id" varchar(64) DEFAULT NULL COMMENT '服务窗id',
  "app_name" varchar(64) DEFAULT NULL COMMENT '服务窗名称',
  "alipay_gateway" varchar(128) DEFAULT NULL COMMENT '支付宝网关',
  "alipay_public_key" varchar(2048) DEFAULT NULL COMMENT '支付宝公钥',
  "private_key" varchar(2048) DEFAULT NULL COMMENT '应用私钥',
  "public_key" varchar(2048) DEFAULT NULL COMMENT '应用公钥',
  "partner" varchar(32) DEFAULT NULL COMMENT '合作伙伴身份（PID）',
  "unit_id" varchar(20) DEFAULT NULL COMMENT '医院ID',
  "is_active" int(1) DEFAULT NULL COMMENT '是否激活(0-未激活 1-正常激活)',
  "is_delete" int(1) DEFAULT NULL COMMENT '是否删除(0-正常     1-已删除)',
  "create_time" datetime DEFAULT NULL COMMENT '创建时间',
  "creator" varchar(32) DEFAULT NULL COMMENT '创建者',
  "modify_time" datetime DEFAULT NULL COMMENT '修改时间',
  "modifor" varchar(32) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY ("id")
);

ALTER TABLE weixin_channel_scene ADD channel_id TINYINT NOT NULL DEFAULT 1 COMMENT '1-微信，2-支付宝，3-app，....';
ALTER TABLE weixin_channel_count ADD channel_id TINYINT NOT NULL DEFAULT 1 COMMENT '1-微信，2-支付宝，3-app，....';