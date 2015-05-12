package com.ims.acty;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
		client = client.getExistInstance();
		
	}

	@Override public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sync:
			File p = Environment.getExternalStorageDirectory();
			Toast.makeText(this, p.getAbsolutePath(), 1).show();
			break;
		case R.id.btn_pre:
			File p1 = Environment.getRootDirectory();
			Toast.makeText(this, p1.getAbsolutePath(), 1).show();
			break;
		case R.id.btn_next:
			
			break;
		case R.id.btn_save:
			new SaveImageTask().execute();
			break;
		default:
			break;
		}
	}
	
	class SaveImageTask extends AsyncTask<Void, Void, Boolean>{

		@Override protected Boolean doInBackground(Void... params) {
			Bitmap bmp = ((BitmapDrawable)iv.getDrawable()).getBitmap();
			 
			FileOutputStream out = null;
			try {
				String _fileName = ImageStorageUtil.getRoolFile() + "/" +System.currentTimeMillis() + ".png";
			    out = new FileOutputStream(_fileName);
			    bmp.compress(Bitmap.CompressFormat.PNG, 40, out); // bmp is your Bitmap instance
			    // PNG is a lossless format, the compression factor (100) is ignored
			    ImageStorageUtil.clean();
			    return true;
			} catch (Exception e) {
			    e.printStackTrace();
			    return false;
			} finally {
			    try {
			        if (out != null) {
			            out.close();
			        }
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			}
		}

		@Override protected void onPostExecute(Boolean result) {
			if(result){
				Toast.makeText(Display.this, "已保存", 1).show();
			}else{
				Toast.makeText(Display.this, "保存失败", 1).show();
			}
		}
		
		
	}

}
