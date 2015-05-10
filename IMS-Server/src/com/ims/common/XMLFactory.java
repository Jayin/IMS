package com.ims.common;

import java.io.InputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.ims.entity.RequestMessage;
import com.ims.entity.ResponseMessage;

/**
 * 解析XML 文件，封装成请求对象，响应对象返回。
 * 
 * @author Administrator
 * 
 */
public class XMLFactory {

	SAXParserFactory factory = null;// 解析xml文件用的
	XMLReader reader = null;

	RequestMessage request = null; // 用来解析后的封装
	ResponseMessage response = null;

	Lock lock = new ReentrantLock();

	public XMLFactory() {
		factory = SAXParserFactory.newInstance();// 1--创建事件处理程序=SAX工厂
		try {
			reader = factory.newSAXParser().getXMLReader();// 2--创建SAX解析器=在工厂里拿到解析器
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 用一个uri 中拿到数据解析封装到请求对象中，如果是同一个XMLFactory调用这个方法，应该锁住
	 * 
	 * @param uri
	 * @return 请求对象
	 */
	public Object getMessageObject(String uri) {
		lock.lock();
		try {
			reader.setContentHandler(new XmlContentHandler());// 设置解析器
			reader.parse(uri);// 加入文档
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}

		if (request != null)
			return request;
		return response;
	}

	/**
	 * 用一个input 中拿到数据解析封装到请求对象中，如果是同一个XMLFactory调用这个方法，应该锁住
	 * 
	 * @param in
	 * @return 请求对象
	 */
	public Object getMessageObject(InputStream in) {
		lock.lock();
		try {
			reader.setContentHandler(new XmlContentHandler());// 设置解析器
			reader.parse(new InputSource(in));// 加入文档
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}
		if (request != null)
			return request;
		return response;
	}

	/**
	 * 
	 * @param uri
	 * @return
	 */

	class XmlContentHandler extends DefaultHandler {

		private String localName_;
		private String charstr;
		private String[] attr;
		private String[] attrValue;
		
		int type = 0; //用来处理第一个节点，判断是请求 1，还是响应 2
		final int requestType = 1;
		final int responseType = 2;

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			
			this.localName_ = qName;
			if(type==0){
				if(localName_.equalsIgnoreCase("RequestMessage")){
					
					attr = RequestMessage.attribute;
					attrValue = new String[attr.length];
					type = requestType;
				}
				if(localName_.equalsIgnoreCase("ResponseMessage")){
					attr = ResponseMessage.attribute;
					attrValue = new String[attr.length];
					type = responseType;
				}
			}
			charstr = "";
			super.startElement(uri, localName, qName, attributes);
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			charstr += new String(ch, start, length);
			charstr = charstr.trim();
			
			if (type==requestType) {
				for (int i = 0; i < attr.length; i++) {
					if (localName_.equalsIgnoreCase(attr[i])) {
						attrValue[i] = charstr;
					}
				}
			}
			
			if (type==responseType) {
				for (int i = 0; i < attr.length; i++) {
					if (localName_.equalsIgnoreCase(attr[i])) {
						attrValue[i] = charstr;
					}
				}
			}
			super.characters(ch, start, length);
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			localName = "";
			super.endElement(uri, localName, qName);
		}

		@Override
		public void endDocument() throws SAXException {
			if(type==requestType){
				request = new RequestMessage(attrValue);
				System.out.println("new 了request");
			}
			if(type==responseType){
				response = new ResponseMessage(attrValue);
				System.out.println("new 了response");
			}
			super.endDocument();

		}

	}

}
