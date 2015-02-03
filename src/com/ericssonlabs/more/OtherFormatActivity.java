package com.ericssonlabs.more;

import com.ericssonlabs.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class OtherFormatActivity extends Activity {

	// private TextView view;
	private Spinner spinner;
	private ArrayAdapter<?> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other_format);

		spinner = (Spinner) findViewById(R.id.spinner);
		// view = (TextView) findViewById(R.id.spinnerText);

		// 将可选内容与ArrayAdapter连接起来
		adapter = ArrayAdapter.createFromResource(this, R.array.wifis,
				android.R.layout.simple_spinner_item);

		// 设置下拉列表的风格
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// 将adapter2 添加到spinner中
		spinner.setAdapter(adapter);

		// 添加事件Spinner事件监听
		spinner.setOnItemSelectedListener(new SpinnerXMLSelectedListener());

		// 设置默认值
		spinner.setVisibility(View.VISIBLE);

	}

	// 使用XML形式操作
	class SpinnerXMLSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// view.setText("Type：" + adapter.getItem(arg2));
			switch (arg2) {
			case 0:// text
				// setContentView(R.layout.other_format);
				break;
			case 1:// wifi
				setContentView(R.layout.other_format_wifi);
				break;
			case 2:

				break;
			case 3:

				break;

			default:
				break;
			}
		}

		public void onNothingSelected(AdapterView<?> arg0) {

		}

	}
}
