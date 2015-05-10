package com.ims.common;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPUtils {
	/**
	 * ��ñ���IP ����
	 * @return ����ip����InetAddress 
	 *         ��ȡʧ�ܷ���null
	 */
	public static InetAddress getLocalInetAddress() {
		InetAddress ip = null;
		try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			System.out.println("δ֪����");
			e1.printStackTrace();
			return null;
		}
		return ip;
	}
	/**
	 * ��ñ���ip��ַ
	 * @return ��ȡʧ��ʱ����null
	 */
	public static String getLocalIP(){
		return getLocalInetAddress().getHostAddress();
	}
}
