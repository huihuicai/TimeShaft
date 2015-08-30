package com.wynne.timeshaftdemo;

import com.wynne.timeshaft.TimeShaftView;
import com.wynne.timeshaft.TimeShaftView.OnTimeChangeListener;
import com.wynne.timeshalftdemo.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity implements OnTimeChangeListener {

	private TimeShaftView mShaftView;
	private TextView mTip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mShaftView = (TimeShaftView) findViewById(R.id.time_shalft);
		mTip = (TextView) findViewById(R.id.text);
		mShaftView.setOnValueChangeListener(this);
	}

	@Override
	public void onValueChanged(boolean isSlipStop, int topValue, int bottomValue) {
		//参数中的topValue和bottomValue完全可以去控制业务中的一些需求。比如按照这连个值去万罗获取此时间的数据
		//此处标志物的移动要根据业务中时间来定。比如是一个列表，列表中的第一条数据的时间，可以来控制这个标志物的移动；
		mShaftView.setMarkDestion(topValue, bottomValue, 15);
		mTip.setText(topValue+"年"+bottomValue+"月"+15);
	}

}
