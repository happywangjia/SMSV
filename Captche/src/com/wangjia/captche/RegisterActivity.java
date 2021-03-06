package com.wangjia.captche;

import com.thinkland.sdk.sms.SMSCaptcha;
import com.thinkland.sdk.util.BaseData.ResultCallBack;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends BaseActivity implements OnClickListener,TextWatcher{

	private EditText etPhoneNum;
	private TextView tvCountryNum;
	private ImageView ivClear;
	private Button btnNext;
	
	private SMSCaptcha captcha;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		captcha=SMSCaptcha.getInstance();
		initView();
	}
	private void initView(){
		TextView tv=(TextView) findViewById(R.id.tv_title);
		tv.setText(R.string.smssdk_register);
		
		btnNext=(Button) findViewById(R.id.btn_next);
		tvCountryNum=(TextView) findViewById(R.id.tv_country_num);
		ivClear=(ImageView) findViewById(R.id.iv_clear);
		etPhoneNum=(EditText) findViewById(R.id.et_write_phone);
		etPhoneNum.addTextChangedListener(this);
		etPhoneNum.setText("");
		etPhoneNum.requestFocus();
		if(etPhoneNum.getText().length()>0){
			btnNext.setEnabled(true);
			ivClear.setVisibility(View.VISIBLE);
			btnNext.setBackgroundResource(R.drawable.smssdk_btn_enable);
		}
		btnNext.setOnClickListener(this);
		ivClear.setOnClickListener(this);
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(s.length()>0){
			btnNext.setEnabled(true);
			ivClear.setVisibility(View.VISIBLE);
			btnNext.setBackgroundResource(R.drawable.smssdk_btn_enable);
		}
		else{
			btnNext.setEnabled(false);
			ivClear.setVisibility(View.GONE);
			btnNext.setBackgroundResource(R.drawable.smssdk_btn_disenable);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		
	}
	@Override
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.btn_next:
			String phone=etPhoneNum.getText().toString().trim().replaceAll("\\s*","");
			String code=tvCountryNum.getText().toString().trim();
			checkPhoneNum(phone,code);
			break;
		case R.id.iv_clear:
			etPhoneNum.getText().clear();
			break;
		default:
			break;
		}
	}
	private void checkPhoneNum(String phone,String code){
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(this, R.string.smssdk_write_mobile_phone, Toast.LENGTH_LONG).show();
			return;
		}
		showDialog(phone,code);
		
		
	}
	public void showDialog(final String phone,String code){
		String phoneNum=code+" "+splitPhoneNum(phone);
		String strContent=getResources().getString(R.string.smssdk_make_sure_mobile_detail)+phoneNum;
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		
		builder.setTitle("Captcha")
			.setIcon(R.drawable.ic_launcher)
			.setCancelable(false)
			.setMessage(strContent)
			.setPositiveButton(R.string.smssdk_ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					showDialog(getResources().getString(R.string.smssdk_get_verification_code_content));
					captcha.sendCaptcha(phone, new ResultCallBack() {
						
						@Override
						public void onResult(int arg0, String arg1, String arg2) {
							closeDialog();
							if(arg0==0){
								afterCaptchaRequested();
							}
						}

					});
					
					
				}
			})
			.setNegativeButton(R.string.smssdk_cancel, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
		AlertDialog dlg=builder.create();
		dlg.show();
		
	}
	private void afterCaptchaRequested(){
		String phone=etPhoneNum.getText().toString().trim().replaceAll("\\s*", "");
		String code=tvCountryNum.getText().toString().trim();
		String fomatedPhone=code+" "+splitPhoneNum(phone);
		
		Toast.makeText(this, "�ɹ���", Toast.LENGTH_SHORT).show();
		Intent intent=new Intent(RegisterActivity.this,CaptcharActivity.class);
		intent.putExtra("fomatedPhone", fomatedPhone);
		intent.putExtra("phone", phone);
		startActivity(intent);
	}
	
	private String splitPhoneNum(String phone) {
		StringBuilder builder=new StringBuilder(phone);
		builder.reverse();
		for(int i=4,len=builder.length();i<len;i+=5){
			builder.insert(i, ' ');
		}
		builder.reverse();
		return builder.toString();
	}

	
	
	
}
