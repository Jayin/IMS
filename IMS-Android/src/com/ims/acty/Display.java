package com.ims.acty;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.ims.BaseUIActivity;
import com.ims.R;
import com.ims.R.id;
import com.ims.R.layout;
import com.ims.entity.IMSClient;

public class Display extends BaseUIActivity {
	private View menu;
	private ImageView iv;
	private IMSClient client = null;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_display);
		initData();
		initLayout();
	}

	@Override
	protected void initLayout() {
		iv = (ImageView) _getView(R.id.acty_display_iv_display);
		menu = _getView(R.id.acty_display_menu);
		menu.setVisibility(View.GONE);

		final Handler h = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				menu.setVisibility(View.GONE);
			}
		};
		iv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				menu.setVisibility(View.VISIBLE);
				h.sendMessageDelayed(h.obtainMessage(), 2000);

			}
		});
		client.getDisplayHandler().setHolding(iv);
		setResult(RESULT_OK);

	}

	@Override
	protected void initData() {
		client = client.getExistInstance();
		
	}

}
