package com.race604.sms.model;
/**
 * @author Wu Jing wujing@jike.com
 * @version Create at：2012-3-29 下午4:59:02
 * 
 **/
public class SmsThread {
	public int count; 	// 本thread包含的短信数量
	public SmsInfo latest; // 最新的一条短信
	public boolean unread; // 是否包含未读消息
}
