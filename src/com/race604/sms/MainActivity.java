package com.race604.sms;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.race604.sms.model.SmsThread;
import com.race604.sms.model.Utility;

import android.app.ListActivity;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends SherlockListActivity implements OnGesturePerformedListener, OnItemClickListener{
    
	public static int THEME = 0;
	
	private ListView mThreadLv;
	private GestureLibrary mGestureLib;
	private View mCurrentView;
	private ArrayAdapter<SmsThread> mListAdapter;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mThreadLv = getListView();
        List<SmsThread> smsList = Utility.getThreadALL(this);
        mListAdapter = new MainActivityAdapter(this, smsList);
        setListAdapter(mListAdapter);   
        
        GestureOverlayView gestureView = (GestureOverlayView) findViewById(R.id.gestures);
        gestureView.addOnGesturePerformedListener(this);
        gestureView.setGestureColor(Color.TRANSPARENT);
        gestureView.setUncertainGestureColor(Color.TRANSPARENT);
        mGestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!mGestureLib.load()) {
			finish();
		}
		
		mThreadLv.setOnItemClickListener(this);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	return showAllMenu(menu);
    	
    }
    
    private boolean showAllMenu(Menu menu) {
    	boolean isLight = false;
    	menu.clear();
    	menu.add(0, R.string.save, 0, R.string.save)
            .setIcon(isLight ? R.drawable.ic_compose_inverse : R.drawable.ic_compose)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        menu.add(0, R.string.search, 0, R.string.search)
        	.setActionView(R.layout.action_search)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
        case R.string.save:
        	break;
        case R.string.search:
        	// mActionMode = startActionMode(new MainActivityActionMode());
        }
        return true;
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
	        
	        MainActivityAdapter.ViewHolder holder = (MainActivityAdapter.ViewHolder) mCurrentView.getTag();
	        
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
	
	class MainActivityActionMode implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            //Used to put dark icons on light action bar
            boolean isLight = false;

            menu.add("Save")
                .setIcon(isLight ? R.drawable.ic_compose_inverse : R.drawable.ic_compose)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            menu.add("Search")
                .setIcon(isLight ? R.drawable.ic_search_inverse : R.drawable.ic_search)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            menu.add("Refresh")
                .setIcon(isLight ? R.drawable.ic_refresh_inverse : R.drawable.ic_refresh)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            menu.add("Save")
                .setIcon(isLight ? R.drawable.ic_compose_inverse : R.drawable.ic_compose)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            menu.add("Search")
                .setIcon(isLight ? R.drawable.ic_search_inverse : R.drawable.ic_search)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            menu.add("Refresh")
                .setIcon(isLight ? R.drawable.ic_refresh_inverse : R.drawable.ic_refresh)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Toast.makeText(MainActivity.this, "Got click: " + item, Toast.LENGTH_SHORT).show();
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }
    }

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		Intent intent = new Intent(MainActivity.this, ThreadActivity.class);
		SmsThread thread = mListAdapter.getItem(position);
		intent.putExtra("id", thread.latest.thread_id);
		startActivity(intent);
	}

}