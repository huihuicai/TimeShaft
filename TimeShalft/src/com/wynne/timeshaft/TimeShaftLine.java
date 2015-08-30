package com.wynne.timeshaft;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class TimeShaftLine extends View {

	private int mLineGap;
	private float mDensity;
	private int mWidth;
	private int mTopValue;
	private final int TEXT_SIZE = 16;
	private final int LINE_SIZE = 10;
	private final int LONG_LINE = 20;
	private final int DELTA_MARGIN = 8;
	private Paint mLinePaint;
	private TextPaint mTextPaint;

	public TimeShaftLine(Context context, AttributeSet attrs) {
		super(context, attrs);
		mDensity = getContext().getResources().getDisplayMetrics().density;
		mWidth = getContext().getResources().getDisplayMetrics().widthPixels;
		mLineGap = (int) (mWidth / 12.5);
		initPaint();
	}

	public void setCurrentTopValue(int topValue) {
		mTopValue = topValue;
		postInvalidate();
	}

	private void initPaint() {
		mLinePaint = new Paint();
		mLinePaint.setStrokeWidth(2);
		mLinePaint.setColor(Color.BLACK);

		mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setTextSize(TEXT_SIZE * mDensity);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		float textWidth = Layout.getDesiredWidth("0", mTextPaint);
		int numberSize = 1;

		canvas.drawLine(0, LONG_LINE * mDensity, mWidth, LONG_LINE * mDensity,
				mLinePaint);

		float marginLeft = 0;
		for (int i = 0; i < 12; i++) {
			numberSize = String.valueOf(i + 1).length();
			marginLeft = (float) ((i + 0.5) * mLineGap);
			if (i == 0) {
				canvas.drawLine(marginLeft, 0, marginLeft,
						(LONG_LINE + LINE_SIZE) * mDensity, mLinePaint);
				canvas.drawText(String.valueOf(mTopValue), marginLeft
						+ DELTA_MARGIN, LONG_LINE * mDensity - DELTA_MARGIN,
						mTextPaint);
			} else {
				canvas.drawLine(marginLeft, LONG_LINE * mDensity, marginLeft,
						(LONG_LINE + LINE_SIZE) * mDensity, mLinePaint);
			}

			canvas.drawText(String.valueOf(i + 1), marginLeft
					- (textWidth * numberSize / 2),
					(LONG_LINE + LINE_SIZE + TEXT_SIZE) * mDensity, mTextPaint);
		}
	}

}
