package com.wynne.timeshaft;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class HorizontalScrollViewShaft extends HorizontalScrollView {

	private StopListener mStopListener;
	private Runnable mStopRunnable;
	private int mLastX;
	private int DELAY = 10;

	public HorizontalScrollViewShaft(Context context, AttributeSet attrs) {
		super(context, attrs);
		mStopRunnable = new Runnable() {
			@Override
			public void run() {
				int currentX = getScrollX();
				if (mLastX - currentX == 0) {
					// TODO 通知滑动时停止的
					if (mStopListener != null) {
						mStopListener.stop(true);
					}
				} else {
					if (mStopListener != null) {
						mStopListener.stop(false);
					}
					mLastX = currentX;
					postDelayed(mStopRunnable, DELAY);
				}
			}
		};
	}

	public void setStopListener(StopListener listenter) {
		mStopListener = listenter;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_UP) {
			// TODO 启动一个线程
			postDelayed(mStopRunnable, DELAY);
		}
		return super.onTouchEvent(ev);
	}

}
