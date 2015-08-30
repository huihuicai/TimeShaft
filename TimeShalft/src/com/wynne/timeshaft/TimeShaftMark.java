package com.wynne.timeshaft;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class TimeShaftMark extends View {

	private float mDensity;
	private final int LINE_SIZE = 20;
	private final int HALF_LINE = 10;

	public TimeShaftMark(Context context, AttributeSet attrs) {
		super(context, attrs);
		mDensity = getContext().getResources().getDisplayMetrics().density;
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Paint linePaint = new Paint();
		linePaint.setStrokeWidth(2);
		linePaint.setColor(Color.BLACK);
		canvas.drawLine(HALF_LINE, 0, HALF_LINE, LINE_SIZE * mDensity, linePaint);

		Paint shapePaint = new Paint();
		shapePaint.setAntiAlias(true);
		shapePaint.setStyle(Paint.Style.FILL);
		linePaint.setColor(Color.BLACK);

		Path path2 = new Path();
		path2.moveTo(0, 0);
		path2.lineTo(LINE_SIZE, 0);
		path2.lineTo(HALF_LINE, 14);
		path2.close();
		canvas.drawPath(path2, shapePaint);

	}
	
	public int getHalfViewWidth(){
		return HALF_LINE;
	}

}
