package com.ericssonlabs.more;

import java.util.logging.Logger;

import com.ericssonlabs.BarCodeTestActivity;
import com.ericssonlabs.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class MoreFormatActivity extends Activity {

	private int type;
	private String spinnerStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.other_format);// default text

		type = Const.TEXT;

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.more_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_text:
			setContentView(R.layout.other_format);
			type = Const.TEXT;
			return true;
		case R.id.action_wifi:
			initWifiView();
			type = Const.WIFI;
			return true;
		case R.id.action_messages:
			setContentView(R.layout.other_format_messages);
			type = Const.MESSAGES;
			return true;
		case R.id.action_contacts:
			setContentView(R.layout.other_format_contacts);
			type = Const.CONSTANT;
			return true;
		default:
			Log.i("zhty", "up default");
			Intent it = new Intent(MoreFormatActivity.this, BarCodeTestActivity.class);
			it.putExtra("content", getContentStr());
			Log.i("zhty text", getContentStr());
			setResult(Activity.RESULT_OK, it);
		    this.finish();
//			return super.onOptionsItemSelected(item);
			return true;
		}
	}

	public String getContentStr() {

		String str;
		switch (type) {
		case Const.TEXT:
			EditText et = (EditText) findViewById(R.id.et_text);
			str = et.getText().toString();
			Log.i("zhty text", str);
			return str;
		case Const.WIFI:
			EditText et2 = (EditText) findViewById(R.id.et_wifi_name);
			EditText et3 = (EditText) findViewById(R.id.et_wifi_pwd);
			str = "WIFI:T:" + spinnerStr + ";P:" + et3.getText().toString()
					+ ";S:" + et2.getText().toString() + ";";
			return str;
		case Const.MESSAGES:
			EditText et4 = (EditText) findViewById(R.id.et_messages_recipient);
			EditText et5 = (EditText) findViewById(R.id.et_messages_content);
			str = "SMSTO:" + et4.getText().toString() + ":"
					+ et5.getText().toString();
			return str;
		case Const.CONSTANT:
			EditText cet1 = (EditText) findViewById(R.id.et_cantacts_name);
			EditText cet2 = (EditText) findViewById(R.id.et_contacts_address);
			EditText cet3 = (EditText) findViewById(R.id.et_contacts_company);
			EditText cet4 = (EditText) findViewById(R.id.et_contacts_email);
			EditText cet5 = (EditText) findViewById(R.id.et_contacts_qq);
			EditText cet6 = (EditText) findViewById(R.id.et_contacts_tel);

			str = "MECARD:N:" + cet1.getText().toString() + ";TEL:"
					+ cet6.getText().toString() + ";ORG:"
					+ cet3.getText().toString() + ";EMAIL:"
					+ cet4.getText().toString() + ";ADR:"
					+ cet2.getText().toString() + ";NOTE:"
					+ cet5.getText().toString() + ";";
			return str;
		default:
			break;
		}

		return null;

	}

	/**
	 * init wifi view, include spinner
	 */
	public void initWifiView() {
		setContentView(R.layout.other_format_wifi);
		Spinner spinner;
		final ArrayAdapter<?> adapter;
		spinner = (Spinner) findViewById(R.id.spinner);
		// 将可选内容与ArrayAdapter连接起来
		adapter = ArrayAdapter.createFromResource(this, R.array.wifis,
				android.R.layout.simple_spinner_item);
		// 设置下拉列表的风格
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter2 添加到spinner中
		spinner.setAdapter(adapter);
		// 添加事件Spinner事件监听
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				spinnerStr = (String) adapter.getItem(arg2);
				Log.i("zhty", spinnerStr);
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});
		// 设置默认值
		spinner.setVisibility(View.VISIBLE);
	}

}
