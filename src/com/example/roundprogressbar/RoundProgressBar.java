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
		
	//����
	private int progress;
	//������
	private int max;
	//����С��ĸ���
	private int count;
	//С��Ŀ�ȣ�ʵ��Ӧ�ýи߶ȲŶԣ����widthʵ���ǻ����ߵ�strokeWidth����
	private int countWidth;
	//С����ռ�ĽǶ�
	private float countAngle;
	//С�����ɫ���൱��progressColor
	private int countColor;
	//С��ĵڶ���ɫ����������ɫ���൱��secondProgressColor
	private int secondCountColor;
	//���ȵ���ʼ�Ƕȣ�����progress��0��ʼ�ĽǶ�
	private float startAngle;
	//�Ƿ��ڿղ��ֽ���
	private boolean isSpacing;
	//�ڿյĿ�ʼ�Ƕ�
	private float spacingFromAngle;
	//�ڿյĽ����Ƕ�
	private float spacingToAngle;
	//�Ƿ�ʹ�ö�������progress
	private boolean animation;
	//�����ĳ���ʱ��
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
		
		//��Ҫ��ʹview�Ŀ��һ�����Լ���wrap_contentʱĬ��Ϊ100dp
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
		//ÿ��С��֮��ļ���Ƕ�
		float countSplitAngle=0;
		
		if(isSpacing){
			//360�ȼ�ȥ����С����ռ�ǶȺ��ڿյ��ܽǶȣ�������count-1,��1����Ϊ���ֻ��count-1����������βС��λ�òŶԳ�
			countSplitAngle=(360-count*countAngle-(spacingToAngle-spacingFromAngle))/(count-1);
		}else{
			//360�ȼ�ȥ����С����ռ�Ƕȣ�������count��������360����Ϊ����
			countSplitAngle=(360-count*countAngle)/count;
		}
		
		//���ǻ�������Ҫ�Ĳ��������ڸþ����ڻ�����
		RectF oval=new RectF(0+countWidth/2, 0+countWidth/2, getWidth()-countWidth/2, getHeight()-countWidth/2);
		
		//����С��ĸ߶ȣ������ߵ�strokeWidth
		paint.setStrokeWidth(countWidth);
		paint.setStyle(Style.STROKE);
		
		//�Ȼ����ȣ�����ѭ�����������С�黭��,Ҫ�ѽ���0�����ʼ�Ƕȼ���
		paint.setColor(secondCountColor);				
		for(int i=0; i<count*progress/max; i++){
			float sweepStartAngle=startAngle+(countAngle+countSplitAngle)*i;	
			canvas.drawArc(oval, sweepStartAngle, countAngle, false, paint);
		}
		
		//��һ����ɫ��ʣ�µķǽ���С�黭��
		paint.setColor(countColor);
		for(int i=count*progress/max; i<count; i++){
			float sweepStartAngle=startAngle+(countAngle+countSplitAngle)*i;
			canvas.drawArc(oval, sweepStartAngle, countAngle, false, paint);
		}
		
		//��������ָ�룬������㻭��һ��ɫָ��
		paint.setColor(Color.RED);
		if(isSpacing){
			canvas.drawArc(oval, startAngle+(360-(spacingToAngle-spacingFromAngle))*progress/max, countAngle, false, paint);
		}else{
			canvas.drawArc(oval, startAngle+360*progress/max, countAngle, false, paint);
		}
	}
	

	public void setProgress(int progress){
		//��ʹ�ö�����������ValueAnimator���в���һ�����ȼ��صĻ���Ч��
		if(animation){
			ValueAnimator valueAnimator=ValueAnimator.ofInt(this.progress, progress);
			valueAnimator.setDuration(animationDuration);			
			valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
				
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					// TODO Auto-generated method stub
					RoundProgressBar.this.progress=Integer.valueOf(animation.getAnimatedValue().toString());
					//����ÿһ����ÿ֡�ı�progress/max,Ȼ�����UI
					invalidate();
				}
			});
			valueAnimator.start();
		}else{
			this.progress=progress;
			invalidate();
		}		
	}

	//dpתpx��λ
	private int dp2px(int dp){
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}
}
