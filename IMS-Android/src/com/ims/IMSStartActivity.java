package com.ims;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class IMSStartActivity extends BaseUIActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_start);
		initData();
		initLayout();
		Handler h = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				openActivity(IMSMainActivity.class);
				closeActivity();
			}
		};
		h.sendMessageDelayed(h.obtainMessage(), 1500);
	}

	@Override
	protected void initLayout() {

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

}
