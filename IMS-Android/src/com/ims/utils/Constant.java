package com.ims.utils;

/**
 * 常量定义类
 * 
 * @author Jayin Ton
 * 
 */
public class Constant {
	//状态
	public static final int Status_Start = 0xF00001;
	public static final int Status_Doing = 0xF00002;
	public static final int Status_Finish = 0xF00003;
	public static final int Status_Faild = 0xF00004;
	public static final int Status_Display = 0xF00005;
	public static final int Status_Asking = 0xF00006;
	public static final int Status_Downloading = 0xF00007;
	public static final int Status_Identifying = 0xF00008;
	public static final int Status_Waitting = 0xF00009;
	 
    //请求码：
	public static final int ReqCode_Main2Display = 0xE00001;
	public static final int ReqCode_main2Qrcode = 0xE00002;
}
