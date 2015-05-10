package com.ims.handler;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

/**
 * 用户验证处理器
 * 
 * @author Jayin Ton
 * 
 */
public class IdentifyHandler extends Handler {
	private TextView tv;

	public IdentifyHandler(TextView tv) {
		this.tv = tv;
	}

	@Override
	public void handleMessage(Message msg) {
		if (msg != null && msg.obj != null) {
			tv.setText((String) msg.obj);
		}

	}

	public void setHolding(TextView tv) {
		this.tv = tv;
	}
}
