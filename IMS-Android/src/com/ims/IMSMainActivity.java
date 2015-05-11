package com.ims;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ims.adapter.AskAdapter;
import com.ims.entity.AbstractClient.CallBack;
import com.ims.entity.Ask;
import com.ims.entity.IMSClient;
import com.ims.handler.DisplayHandler;
import com.ims.handler.DownloadHandler;
import com.ims.handler.IdentifyHandler;
import com.ims.utils.Constant;
import com.zxing.activity.CaptureActivity;

/**
 * 主界面。用来拿数据显示全部信息，和用户交互
 * 
 * @author Administrator
 * 
 */
public class IMSMainActivity extends BaseUIActivity implements OnClickListener {

	private Button btn_ask_submit;
	private EditText et_ask_content;
	private View switchBtn, qrcode;
	private RadioButton display, identify, ask, download, setting,
			currentPressed;
	private RelativeLayout frame_display, frame_ask, frame_download,
			frame_identify, frame_setting, frame_current;
	private ImageView main_display;
	private IMSClient client = null;
	private String ip = null;
	private TextView tv_title, tv_identify_info,tv_download_title,tv_download_content;
	private DisplayHandler displayHandler;
	private IdentifyHandler identifyHandler;
	private DownloadHandler downloadHandler;
	private EditText ed_identify_username;
	
	private AskAdapter askAdapter;
	private ListView lv_ask_proList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_imsmain);
		initData();
		initLayout();

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {

		super.onPause();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (client != null)
			client.closeClient();
		Log.i("debug", "onDestroy");
		System.out.println();

	}

	@Override
	protected void initLayout() {
		initHeader(); // 头菜单
		initFooter(); // 底部菜单
		initDisplay(); // 同步界面
		initAsk(); // 提问
		initIdentify(); // 个人+验证
		initDownLoad(); // 下载&会议资料
		initSetting(); // 设置
		initContentView();// 初始化中间显示的View
		initHandler();
		initClient();
		initAdapter();
	}

	private void initSetting() {
		frame_setting = (RelativeLayout) _getView(R.id.main_content_setting);

	}

	private void initContentView() {
		frame_current = frame_identify;
		frame_current.setVisibility(View.VISIBLE);

	}

	private void initIdentify() {
		frame_identify = (RelativeLayout) _getView(R.id.main_content_identify);
		tv_identify_info = (TextView) _getView(R.id.frame_identify_info);
		qrcode = _getView(R.id.frame_identify_qrcode);
		ed_identify_username = (EditText) _getView(R.id.frame_identity_ed_name);

		tv_identify_info.setText("尚未验证！\n请扫一扫二维码验证");
		qrcode.setOnClickListener(this);

	}

	private void initDownLoad() {
		frame_download = (RelativeLayout) _getView(R.id.main_content_download);
		tv_download_content = (TextView) _getView(R.id.frame_download_txt_MeetingContent);
		tv_download_title = (TextView) _getView(R.id.frame_download_txt_MeetingTitle);
		
	}

	private void initAsk() {
		frame_ask = (RelativeLayout) _getView(R.id.main_content_ask);
		btn_ask_submit = (Button) _getView(R.id.frame_ask_btn_submit);
		btn_ask_submit.setOnClickListener(this);
		et_ask_content = (EditText) _getView(R.id.frame_ask_et_content);
		lv_ask_proList = (ListView) _getView(R.id.frame_ask_lv);
	}

	private void initDisplay() {
		frame_display = (RelativeLayout) _getView(R.id.main_content_display);
		switchBtn = (View) _getView(R.id.frame_display_btn_switch);
		switchBtn.setOnClickListener(this);

		main_display = (ImageView) _getView(R.id.frame_display_iv_display);

	}

	private void initFooter() {
		display = (RadioButton) _getView(R.id.main_footbar_display);
		ask = (RadioButton) _getView(R.id.main_footbar_ask);
		download = (RadioButton) _getView(R.id.main_footbar_downloadData);
		identify = (RadioButton) _getView(R.id.main_footbar_identify);
		setting = (RadioButton) _getView(R.id.main_footbar_setting);

		currentPressed = identify;

		currentPressed.setChecked(true);
		display.setOnClickListener(this);
		ask.setOnClickListener(this);
		download.setOnClickListener(this);
		identify.setOnClickListener(this);
		setting.setOnClickListener(this);

	}

	private void initHeader() {
		tv_title = (TextView) _getView(R.id.main_header_title);
		tv_title.setText("个人登录");
	}
	
	private void initAdapter() {
		askAdapter = new AskAdapter(this);
	}

	@Override
	protected void initData() {
		// 读入ip and port 在扫二维码之后会覆盖掉的。
		ip = "192.168.1.102";

	}

	protected void initHandler() {
		displayHandler = new DisplayHandler(main_display);
		identifyHandler = new IdentifyHandler(tv_identify_info);
		downloadHandler = new DownloadHandler(tv_download_title, tv_download_content);
	}

	protected void initClient() {
		client = IMSClient.getInstance(new CallBack() {

			@Override
			public void onSwitch() {

			}

			@Override
			public void onError(Exception e, String description) {

			}

			@Override
			public void onClosing() {

			}
		}, ip);

		client.setDisplayHandler(displayHandler);
		client.setIdentifyHandler(identifyHandler);
		client.setDownloadHandler(downloadHandler);
	}

	private void switchPressedState(View pressedView) {
		if ((RadioButton) pressedView != currentPressed) {
			((RadioButton) pressedView).setChecked(true);
			currentPressed.setChecked(false);
			currentPressed = (RadioButton) pressedView;
		}
	}

	private void switchFrame(RelativeLayout switchTo) {
		if (switchTo != frame_current) {
			switchTo.setVisibility(View.VISIBLE);
			frame_current.setVisibility(View.GONE);
			frame_current = switchTo;
		}
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();

		if (!client.isIdentity()) {// 判断有没有确认了IP 地址

			if (viewId == R.id.frame_identify_qrcode) {
				String username = ed_identify_username.getText().toString()
						.trim();
				if (username.equals("")) {
					Toast.makeText(IMSMainActivity.this, "名字不能为空！",
							Toast.LENGTH_LONG).show();
					return;
				}
				client.setUsername(username);
				startActivityForResult(wrapIntent(CaptureActivity.class),
						Constant.ReqCode_main2Qrcode);
				return;
			}

			Toast.makeText(IMSMainActivity.this, "请先确认IP", Toast.LENGTH_LONG)
					.show();
			return;
		}
		switch (v.getId()) {
		case R.id.main_footbar_ask:
			switchPressedState(v);
			switchFrame(frame_ask);
			tv_title.setText("提问");
			break;
		case R.id.login_button:
			break;
		case R.id.main_footbar_display:
			switchPressedState(v);
			switchFrame(frame_display);
			if(!client.isDisplay()){
				client.addTask(Constant.Status_Display);
			}
			tv_title.setText("同步");
			break;
		case R.id.main_footbar_downloadData:
			switchPressedState(v);
			switchFrame(frame_download);
			if(!client.isDownloadMeetingData()){
				client.addTask(Constant.Status_Downloading);
			}
			tv_title.setText("会议资料");
			break;
		case R.id.main_footbar_identify:
			switchPressedState(v);
			switchFrame(frame_identify);
			tv_title.setText("个人登录");
			break;
		case R.id.main_footbar_setting:
			switchPressedState(v);
			tv_title.setText("设置");
			switchFrame(frame_setting);
			break;
		case R.id.frame_display_btn_switch:
			startActivityForResult(new Intent(IMSMainActivity.this,
					com.ims.acty.Display.class), Constant.ReqCode_Main2Display);
			break;
		case R.id.frame_ask_btn_submit:
			String pro = et_ask_content.getText().toString().trim();
			if(pro.equals("")){
				Toast.makeText(IMSMainActivity.this, "请输入你的问题！", Toast.LENGTH_SHORT).show();
				return;
			}
			Ask ask = new Ask();
			ask.setPor(pro);
			askAdapter.addAsk(ask);
			lv_ask_proList.setAdapter(askAdapter);
			client.setAskPro(pro);
			client.addTask(Constant.Status_Asking);
			break;

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 切换回DisplayHandler holding 的ImageView
		if (requestCode == Constant.ReqCode_Main2Display
				&& resultCode == RESULT_OK) {
			client.getDisplayHandler().setHolding(main_display);
			client.setCurrentStatus(Constant.Status_Display);
		}
		if (requestCode == Constant.ReqCode_main2Qrcode
				&& resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			// 获得二维码信息待处理。创建客户端
			String strs[] = scanResult.split("@@@");
			client.setIP(strs[0], Integer.parseInt(strs[1]),
					Integer.parseInt(strs[2]));
			// client.setCurrentStatus(Constant.Status_Identifying);
			client.addTask(Constant.Status_Identifying);
			client.start();
		}
	}
}
