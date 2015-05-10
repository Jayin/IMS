package com.ims.handler;

import com.ims.entity.ResponseMessage;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class DownloadHandler extends Handler {
	
	TextView title,content;
	
	
	public TextView getTitle() {
		return title;
	}


	public void setTitle(TextView title) {
		this.title = title;
	}


	public TextView getContent() {
		return content;
	}


	public void setContent(TextView content) {
		this.content = content;
	}


	public DownloadHandler(TextView title, TextView content) {
		super();
		this.title = title;
		this.content = content;
	}


	@Override
	public void handleMessage(Message msg) {
		ResponseMessage response = (ResponseMessage) msg.obj;
		if(msg==null||msg.obj==null||response.getState()==ResponseMessage.RESPONSE_STATE_FAIL){
			title.setText("下载失败！！");
			return;
		}
		title.setText(response.getTitle());
		content.setText(response.getMeetingData());
	}
}
