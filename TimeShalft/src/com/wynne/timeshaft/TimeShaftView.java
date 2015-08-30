package com.wynne.timeshaft;

import com.wynne.timeshalft.R;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class TimeShaftView extends FrameLayout {

	public interface OnTimeChangeListener {
		void onValueChanged(boolean isSlipStop, int topValue, int bottomValue);
	}

	private OnTimeChangeListener mOnTimeChangeListener;

	private HorizontalScrollViewShaft mHorizontalScrollView;
	private LinearLayout mContainer;
	private ImageView mMoveMark;
	private TimeShaftMark mMarkView;

	private int mScreenWidth;
	private int mLineGap;
	private int mHalfLineGap;
	/**
	 * 当前的时间值
	 */
	private int mCurrentYear;
	private int mCurrentMonth;
	private int mMinYear;

	private int deltaGap = 0;

	private float mDensity;

	private LinearLayout.LayoutParams normalLayoutParams;
	private LinearLayout.LayoutParams bigLayoutParams;

	public TimeShaftView(Context context) {
		super(context);
	}

	public TimeShaftView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mDensity = getContext().getResources().getDisplayMetrics().density;
		mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
		mLineGap = (int) (mScreenWidth / 12.5);
		mHalfLineGap = (int) (0.5 * mLineGap);

		deltaGap = mScreenWidth - 12 * mLineGap - mHalfLineGap;

		normalLayoutParams = new LinearLayout.LayoutParams(mScreenWidth
				- mHalfLineGap - deltaGap,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		bigLayoutParams = new LinearLayout.LayoutParams(mScreenWidth,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		initTime();
		initWidget(context);
	}

	/**
	 * 初始化时间轴的各个值
	 */
	@SuppressWarnings("deprecation")
	private void initTime() {
		Time time = new Time("GTM+8");
		time.setToNow();
		mCurrentYear = time.year;
		mCurrentMonth = time.month + 1;
		mMinYear = mCurrentYear - 2;

	}

	/**
	 * 初始化组件
	 * 
	 * @param context
	 */
	private void initWidget(Context context) {
		LayoutInflater.from(context).inflate(R.layout.time_shaft_view, this,
				true);

		mMarkView = new TimeShaftMark(context, null);
		int halfWidth = mMarkView.getHalfViewWidth();
		FrameLayout.LayoutParams layoutParams = new LayoutParams(2 * halfWidth,
				(int) (20 * mDensity + 0.5f));
		layoutParams.leftMargin = mHalfLineGap - halfWidth;
		addView(mMarkView, layoutParams);

		mMoveMark = (ImageView) findViewById(R.id.move_mark);
		mHorizontalScrollView = (HorizontalScrollViewShaft) findViewById(R.id.time_line_scroll);
		mContainer = (LinearLayout) findViewById(R.id.time_line_container);
		for (int i = 0; i < 5; i++) {
			TimeShaftLine timeLine = new TimeShaftLine(context, null);
			timeLine.setCurrentTopValue(mCurrentYear + i - 2);
			if (i < 4) {
				mContainer.addView(timeLine, normalLayoutParams);
			} else {
				mContainer.addView(timeLine, bigLayoutParams);
			}
		}

		mHorizontalScrollView.setStopListener(new StopListener() {
			@Override
			public void stop(boolean isStop) {
				int scrollX = mHorizontalScrollView.getScrollX();
				int deltay = (int) (scrollX / mLineGap);
				int deltayValue = Math.abs(scrollX % mLineGap);
				mCurrentYear = deltay / 12 + mMinYear;
				mCurrentMonth = deltay % 12 + 1;

				if (isStop) {
					handleStop(deltayValue);
				}
				if (mOnTimeChangeListener != null) {
					mOnTimeChangeListener.onValueChanged(isStop, mCurrentYear,
							mCurrentMonth);
				}
			}
		});

		post(new Runnable() {
			@Override
			public void run() {
				scrollTimerShaftBack();
			}
		});
	}

	/**
	 * 停止滑动的时候，有可能不是在整个刻度，需要回弹到整刻度
	 */
	private void handleStop(int deltaDay) {
		if (deltaDay == 0) {
			return;
		}
		int deltaMove = -deltaDay;
		if (deltaDay >= mHalfLineGap) {
			mCurrentMonth += 1;
			deltaMove = mLineGap - deltaDay;
		}
		mHorizontalScrollView.smoothScrollBy(deltaMove, 0);
	}

	/**
	 * 滑动是时间轴到指定的位置
	 */
	@SuppressWarnings("deprecation")
	public void scrollTimerShaftBack() {
		// 1.当前的值的月份 2.计算出其距离，然后smoothScroll指定的距离
		Time time = new Time("GTM+8");
		time.setToNow();
		int year = time.year;
		int month = time.month + 1;
		int lenth = (12 * (year - mMinYear) + month - 1) * mLineGap;
		mHorizontalScrollView.smoothScrollTo(lenth, 0);
		if (mOnTimeChangeListener != null) {
			mOnTimeChangeListener.onValueChanged(true, year, month);
		}
	}

	/**
	 * 小标志物的移动
	 * 
	 * @param year
	 * @param month
	 * @param day
	 */
	public void setMarkDestion(int year, int month, int day) {
		float markMoveLenth = Math
				.round(((year - mMinYear) * 12 + (month - 1) + day / 30.0)
						* mLineGap + mHalfLineGap);
		if (markMoveLenth == 0) {
			return;
		}

		mMoveMark.clearAnimation();

		ObjectAnimator animator = ObjectAnimator.ofFloat(mMoveMark, "x",
				markMoveLenth);
		animator.setDuration(800);
		animator.start();
	}

	public void setOnValueChangeListener(OnTimeChangeListener changeListener) {
		mOnTimeChangeListener = changeListener;
	}

}
