package org.unipd.nbeghin;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class MainActivityFragment extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(new MainFragment(getSupportFragmentManager()));
	}
	
	public void startRecordStairData() {
		Log.d("MAIN_ACTIVITY_FRAGMENT", "Sono qui");
	}
}
