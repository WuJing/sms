package com.race604.sms;

import java.util.ArrayList;
import java.util.List;

import com.race604.sms.MainActivityAdapter.ViewHolder;
import com.race604.sms.model.SmsThread;
import com.race604.sms.model.Utility;

import android.app.ListActivity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity implements OnGesturePerformedListener{
    
	private ListView mThreadLv;
	private GestureLibrary mGestureLib;
	private View mCurrentView;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mThreadLv = getListView();
        List<SmsThread> smsList = Utility.getThreadALL(this);
        setListAdapter(new MainActivityAdapter(this, smsList));   
        
        GestureOverlayView gestureView = (GestureOverlayView) findViewById(R.id.gestures);
        gestureView.addOnGesturePerformedListener(this);
        gestureView.setGestureColor(Color.TRANSPARENT);
        gestureView.setUncertainGestureColor(Color.TRANSPARENT);
        mGestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!mGestureLib.load()) {
			finish();
		}
        
		//mThreadLv.setOnTouchListener(this);
    }

	@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = mGestureLib.recognize(gesture);
		
		if (predictions.size() > 0 && predictions.get(0).score > 1.0) {
	        String action = predictions.get(0).name;
	        RectF rect = gesture.getBoundingBox();
	        int x, y;
	        x = (int) ((rect.left + rect.right) / 2);
	        y = (int) ((rect.top + rect.bottom) / 2);
	        int pos = mThreadLv.pointToPosition(x, y);
	        mCurrentView = mThreadLv.getChildAt(pos);
	        
	        if (mCurrentView == null) {
	        	return;
	        }
	        
	        MainActivityAdapter.ViewHolder holder = (ViewHolder) mCurrentView.getTag();
	        
	        if (holder == null) {
	        	return;
	        }
	        
	        if ("right".equals(action)) {
	            Toast.makeText(this, "Delet the SMS: " + holder.body.getText(), Toast.LENGTH_SHORT).show();
	        } else if ("left".equals(action)) {
	            Toast.makeText(this, "Left: " + holder.body.getText(), Toast.LENGTH_SHORT).show();
	        } else if ("up".equals(action)) {
	            Toast.makeText(this, "Up: " + holder.body.getText(), Toast.LENGTH_SHORT).show();
	        } else if ("down".equals(action)) {
	        	Toast.makeText(this, "Down: " + holder.body.getText(), Toast.LENGTH_SHORT).show();
	        }
	    }
	}

}