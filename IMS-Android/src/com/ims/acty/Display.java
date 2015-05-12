package com.ims.acty;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ims.BaseUIActivity;
import com.ims.R;
import com.ims.entity.IMSClient;
import com.ims.utils.ImageStorageUtil;

public class Display extends BaseUIActivity implements View.OnClickListener{
	private View menu;
	private ImageView iv;
	private IMSClient client = null;
	
	private View btn_sync,btn_pre,btn_next,btn_save;
	private int image_total = -1, cur = -1;
 
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

		btn_sync = this.findViewById(R.id.btn_sync);
		btn_sync.setOnClickListener(this);
		
		btn_pre = this.findViewById(R.id.btn_pre);
		btn_pre.setOnClickListener(this);
		
		btn_next = this.findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);
		
		btn_save = this.findViewById(R.id.btn_save);
		btn_save.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		client = IMSClient.getExistInstance();
		updateImageCount();
	}

	@Override public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btn_sync:
			//同步
			client.setSyncingImage(true);
			break;
		case R.id.btn_pre:
			//浏览图片不同步
			client.setSyncingImage(false);
			cur--;
			if(cur < 0){
				cur = image_total - 1;
			}
			updateImageView();
			break;
		case R.id.btn_next:
			//浏览图片不同步
			client.setSyncingImage(false);
			cur++;
			if(cur >= image_total){
				cur = 0;
			}
			updateImageView();
			break;
		case R.id.btn_save:
			new SaveImageTask().execute();
			break;
		default:
			break;
		}
	}
	//更新图片总数
	private void updateImageCount(){
		image_total = ImageStorageUtil.getFileCount();
		cur = image_total - 1;
	}
	
	private void updateImageView(){
		Bitmap bitmap = ImageStorageUtil.getBitmap(cur);
		if(bitmap != null){
			iv.setImageBitmap(bitmap);
		}
	}
	
	class SaveImageTask extends AsyncTask<Void, Void, Boolean>{

		@Override protected Boolean doInBackground(Void... params) {
			return ImageStorageUtil.saveBitmap(((BitmapDrawable)iv.getDrawable()).getBitmap());
		}

		@Override protected void onPostExecute(Boolean result) {
			if(result){
				updateImageCount();
				Toast.makeText(Display.this, "已保存", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(Display.this, "保存失败", Toast.LENGTH_SHORT).show();
			}
		}
	}

}
