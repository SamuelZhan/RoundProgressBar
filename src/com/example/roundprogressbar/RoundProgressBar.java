package com.example.roundprogressbar;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class RoundProgressBar extends View {
		
	//进度
	private int progress;
	//最大进度
	private int max;
	//矩形小块的个数
	private int count;
	//小块的宽度，实际应该叫高度才对，这个width实际是画弧线的strokeWidth而已
	private int countWidth;
	//小块所占的角度
	private float countAngle;
	//小块的颜色，相当于progressColor
	private int countColor;
	//小块的第二颜色，即进度颜色，相当于secondProgressColor
	private int secondCountColor;
	//进度的起始角度，即从progress从0开始的角度
	private float startAngle;
	//是否挖空部分进度
	private boolean isSpacing;
	//挖空的开始角度
	private float spacingFromAngle;
	//挖空的结束角度
	private float spacingToAngle;
	//是否使用动画更新progress
	private boolean animation;
	//动画的持续时间
	private long animationDuration;
	
	private Paint paint;

	public RoundProgressBar(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public RoundProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);
		progress=array.getInt(R.styleable.RoundProgressBar_progress, 0);
		max=array.getInt(R.styleable.RoundProgressBar_max, 100);
		count=array.getInt(R.styleable.RoundProgressBar_count, 100);
		countWidth=array.getDimensionPixelSize(R.styleable.RoundProgressBar_countWidth, dp2px(10));
		countAngle=array.getFloat(R.styleable.RoundProgressBar_countAngle, 1f);
		countColor=array.getColor(R.styleable.RoundProgressBar_countColor, Color.GRAY);
		secondCountColor=array.getColor(R.styleable.RoundProgressBar_secondCountColor, Color.GREEN);
		startAngle=array.getFloat(R.styleable.RoundProgressBar_startAngle, 90f); 
		isSpacing=array.getBoolean(R.styleable.RoundProgressBar_isSpacing, false);
		spacingFromAngle=array.getFloat(R.styleable.RoundProgressBar_spacingFromAngle, 45f);
		spacingToAngle=array.getFloat(R.styleable.RoundProgressBar_spacingToAngle, 135f);
		animation=array.getBoolean(R.styleable.RoundProgressBar_animation, false);
		animationDuration=array.getInteger(R.styleable.RoundProgressBar_animationDuration, 2000);
				
		array.recycle();
		
		paint=new Paint();
		paint.setAntiAlias(true);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		
		//主要是使view的宽高一样，以及当wrap_content时默认为100dp
		int widthMode=MeasureSpec.getMode(widthMeasureSpec);
		int width=MeasureSpec.getSize(widthMeasureSpec);
		if(widthMode==MeasureSpec.AT_MOST){
			width=dp2px(100);
		}
		
		int heightMode=MeasureSpec.getMode(heightMeasureSpec);
		int height=MeasureSpec.getSize(heightMeasureSpec);
		if(heightMode==MeasureSpec.AT_MOST){
			height=dp2px(100);
		}
		
		setMeasuredDimension(Math.min(width, height), Math.min(width, height));
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		//每个小块之间的间隔角度
		float countSplitAngle=0;
		
		if(isSpacing){
			//360度减去所有小块所占角度和挖空的总角度，并除以count-1,减1是因为间隔只有count-1个，这样首尾小块位置才对称
			countSplitAngle=(360-count*countAngle-(spacingToAngle-spacingFromAngle))/(count-1);
		}else{
			//360度减去所有小块所占角度，并除以count，以整个360度作为进度
			countSplitAngle=(360-count*countAngle)/count;
		}
		
		//这是画弧线需要的参数，即在该矩形内画弧线
		RectF oval=new RectF(0+countWidth/2, 0+countWidth/2, getWidth()-countWidth/2, getHeight()-countWidth/2);
		
		//设置小块的高度，即弧线的strokeWidth
		paint.setStrokeWidth(countWidth);
		paint.setStyle(Style.STROKE);
		
		//先画进度，利用循环画弧把逐个小块画好,要把进度0点的起始角度加上
		paint.setColor(secondCountColor);				
		for(int i=0; i<count*progress/max; i++){
			float sweepStartAngle=startAngle+(countAngle+countSplitAngle)*i;	
			canvas.drawArc(oval, sweepStartAngle, countAngle, false, paint);
		}
		
		//换一种颜色把剩下的非进度小块画完
		paint.setColor(countColor);
		for(int i=count*progress/max; i<count; i++){
			float sweepStartAngle=startAngle+(countAngle+countSplitAngle)*i;
			canvas.drawArc(oval, sweepStartAngle, countAngle, false, paint);
		}
		
		//画个进度指针，这里随便画了一红色指针
		paint.setColor(Color.RED);
		if(isSpacing){
			canvas.drawArc(oval, startAngle+(360-(spacingToAngle-spacingFromAngle))*progress/max, countAngle, false, paint);
		}else{
			canvas.drawArc(oval, startAngle+360*progress/max, countAngle, false, paint);
		}
	}
	

	public void setProgress(int progress){
		//若使用动画，则利用ValueAnimator进行产生一个进度加载的缓冲效果
		if(animation){
			ValueAnimator valueAnimator=ValueAnimator.ofInt(this.progress, progress);
			valueAnimator.setDuration(animationDuration);			
			valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
				
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					// TODO Auto-generated method stub
					RoundProgressBar.this.progress=Integer.valueOf(animation.getAnimatedValue().toString());
					//利用每一动画每帧改变progress/max,然后更新UI
					invalidate();
				}
			});
			valueAnimator.start();
		}else{
			this.progress=progress;
			invalidate();
		}		
	}

	//dp转px单位
	private int dp2px(int dp){
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}
}
