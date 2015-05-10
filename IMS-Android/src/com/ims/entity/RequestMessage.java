package com.ims.entity;

import java.io.InputStream;

import com.ims.utils.XMLFactory;

/**
 * 封装用户的请求信息，以后可以扩展
 * <p>
 * 请求类型有：提问，下载会议资料
 * </p>
 * 如果想增加请求类型，需要修改1,增加属性and set get，请求类型 2，修改attribute,RequestMessage(String[]
 * attribute)，getXML()
 * 
 * @author Administrator
 * 
 */
public class RequestMessage {

	// 请求类型
	public static int REQUEST_TYPE_DOWNLOAD_MEETING_DATA = 0;
	public static int REQUEST_TYPE_PROBLEM = 1;

	private int requestType = -1;
	private String username;
	private String problemCtn;

	// 封装全部属性名
	public static String[] attribute = { "requestType", "username",
			"problemCtn" };

	public RequestMessage(String[] attribute) {
		requestType = Integer.parseInt(attribute[0]);// 排序和attribute 一样的
		username = attribute[1];
		problemCtn = attribute[2];
	}

	public RequestMessage() {
	}

	public int getRequestType() {
		return requestType;
	}

	public void setRequestType(int requestType) {
		this.requestType = requestType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProblemCtn() {
		return problemCtn;
	}

	public void setProblemCtn(String problemCtn) {
		this.problemCtn = problemCtn;
	}

	/**
	 * 由in 的数据来构造一个请求对象
	 * 
	 * @param in
	 * @return
	 */
	public static RequestMessage parse(InputStream in) {
		RequestMessage rqmg = null;
		XMLFactory xmlFactory = new XMLFactory();
		rqmg = (RequestMessage) xmlFactory.getMessageObject(in);
		System.out.println("parse-->>>req" + rqmg.toString());
		return rqmg;
	}

	/**
	 * 开头节点表示是什么类型的xml
	 * 
	 * @return
	 */
	public String getXML() {

		String xml = "";

		xml += "<RequestMessage><requestType>" + requestType + "</requestType>";

		if (username != null) {
			xml += "<username>" + username + "</username>";
		}
		if (problemCtn != null) {
			xml += "<problemCtn>" + problemCtn + "</problemCtn>";
		}

		xml += "</RequestMessage>";
		return xml;
	}

	@Override
	public String toString() {
		return "RequestMessage [requestType=" + requestType + ", username="
				+ username + ", problemCtn=" + problemCtn + "]";
	}

}
