package com.example.roundprogressbar;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {
	
	private RoundProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		progressBar=(RoundProgressBar)findViewById(R.id.progressBar);
		progressBar.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				progressBar.setProgress(80);
			}
		});
	}


}
