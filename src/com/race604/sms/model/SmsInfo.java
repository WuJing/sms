package com.race604.sms.model;

public class SmsInfo {
	// ���еĶ���
	public static final String SMS_URI_ALL = "content://sms/";
	// �ռ������
	public static final String SMS_URI_INBOX = "content://sms/inbox";
	// ���������
	public static final String SMS_URI_SEND = "content://sms/sent";
	// �ݸ������
	public static final String SMS_URI_DRAFT = "content://sms/draft";
	
	public int id; 			// ����id
	public int thread_id; 	// �Ի�����ţ���ͬһ���ֻ��Ż����Ķ��ţ����������ͬ��
	public String address; 	// �����˵�ַ�����绰����
	public String person; 	// �����˵����֣������������ͨѶ¼����Ϊ����������İ����Ϊnull
	public String body; 	// ��������
	public long   date; 	// ����ʱ��
	public int protocol; 	// Э��0 SMS_RPOTO���ţ�1 MMS_PROTO����
	public int read; 		// �Ƿ��Ķ�0δ����1�Ѷ�
	public int status; 		// ����״̬-1 ���գ�0 complete,64 pending,128 failed
	public int type; 		// ��������1�ǽ��յ��ģ�2���ѷ���
	//private String service_center; // ���ŷ������ĺ����ţ���+8613800755500
	
	

}
