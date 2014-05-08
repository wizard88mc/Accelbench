package org.unipd.nbeghin;

import org.unipd.nbeghin.fragments.FragmentLearning;
import org.unipd.nbeghin.fragments.FragmentTest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainFragment extends FragmentPagerAdapter {
	
	public MainFragment(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		
		if (arg0 == 0) {
			Fragment fragment = new FragmentLearning();
			return fragment;
		}
		else {
			return new FragmentTest();
		}
	}

	@Override
	public int getCount() {
		return 2;
	}
	
	@Override
	public String getPageTitle(int position) {
		if (position == 0) {
			return "Learning Data";
		}
		else {
			return "Test Data";
		}
	}
	
	public static void setAdapterForSpinner(Spinner spinner, int arrayResource, FragmentActivity activity) {
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity, arrayResource, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

}
