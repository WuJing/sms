package com.race604.sms.model;

public class SmsInfo {
	// 所有的短信
	public static final String SMS_URI_ALL = "content://sms/";
	// 收件箱短信
	public static final String SMS_URI_INBOX = "content://sms/inbox";
	// 发件箱短信
	public static final String SMS_URI_SEND = "content://sms/sent";
	// 草稿箱短信
	public static final String SMS_URI_DRAFT = "content://sms/draft";
	
	public int id; 			// 短信id
	public int thread_id; 	// 对话的序号，与同一个手机号互发的短信，其序号是相同的
	public String address; 	// 发件人地址，即电话号码
	public String person; 	// 发件人的名字，如果发件人在通讯录中则为具体姓名，陌生人为null
	public String body; 	// 短信内容
	public long   date; 	// 发件时间
	public int protocol; 	// 协议0 SMS_RPOTO短信，1 MMS_PROTO彩信
	public int read; 		// 是否阅读0未读，1已读
	public int status; 		// 短信状态-1 接收，0 complete,64 pending,128 failed
	public int type; 		// 短信类型1是接收到的，2是已发出
	//private String service_center; // 短信服务中心号码编号，如+8613800755500
}