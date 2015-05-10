package com.ims.entity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.ims.common.XMLFactory;

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
	public static int REQUEST_TYPE_IDENTITY= 2; 
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
		try {
			
			File file = new File(getTmpRequestFile());
			BufferedWriter writer;
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file)));

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			String line = "";
			while ((line = reader.readLine()) != null) {
				writer.write(line);
			}
			writer.flush();
			writer.close();

			XMLFactory xmlFactory = new XMLFactory();

			rqmg = (RequestMessage) xmlFactory.getMessageObject(getTmpRequestFile());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rqmg;
	}

	/**
	 * 根据操作系统获取request.xml路径
	 * @return
	 */
	public static String getTmpRequestFile(){
		String os_name = System.getProperty("os.name").toLowerCase();
		if(os_name.contains("windows")){
			return "c:/request.xml";
		}
		return "/tmp/request.xml";
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
