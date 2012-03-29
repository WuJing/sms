package com.race604.sms.model;
/**
 * @author Wu Jing wujing@jike.com
 * @version Create at：2012-3-29 下午4:59:02
 * 
 **/
public class Thread {
	public long id; // thread id
	public long latest; // 最近更新时间
	public int count; 	// 本thread包含的短信数量
	public String address;	// 对方用户的电话号码
}
