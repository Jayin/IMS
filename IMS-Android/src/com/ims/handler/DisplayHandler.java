package com.ims.handler;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
/**
 * 同步屏幕处理器
 * @author Jayin Ton
 *
 */
public class DisplayHandler extends Handler {
	private ImageView iv;

	

	public DisplayHandler(ImageView iv) {
		this.iv = iv;
	}

	@Override
	public void handleMessage(Message msg) {
		if (msg != null && msg.obj != null ) {
			if(iv != null){
				iv.setImageBitmap((Bitmap) msg.obj);
			}
		} else {
			Log.e("debug", "handlerMessage():::msg or msg.obj is null");
		}

	}

	/**
	 * 切换imageview
	 * 
	 * @param _iv
	 */
	public void setHolding(ImageView _iv) {
		iv = _iv;
	}
    /**
     * 获得正在hold 的ImageView
     * @return
     */
	public ImageView getHolding() {
		return iv;
	}
}
