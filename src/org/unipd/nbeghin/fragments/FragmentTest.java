package org.unipd.nbeghin.fragments;

import java.util.Arrays;

import org.unipd.nbeghin.MainActivity;
import org.unipd.nbeghin.MainFragment;
import org.unipd.nbeghin.R;
import org.unipd.nbeghin.listeners.AccelerometerStoreListener;
import org.unipd.nbeghin.models.Settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

public class FragmentTest extends Fragment implements View.OnClickListener {
	
	public static String[] optionsSex = null;
	public static String[] optionsAge = null;
	public static String[] optionsHeight = null;
	public static String[] optionsShoes = null;
	public static String[] optionsLocation = null;
	
	private String currentStairMode = MainActivity.SAMPLING_TYPE_STAIR_UPSTAIRS;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		SharedPreferences preferences = getActivity().getPreferences(0);
		
		View result = inflater.inflate(R.layout.test_layout, container, false);
		
		result.findViewById(R.id.btnStartStairs).setOnClickListener(this);
		
		result.findViewById(R.id.btnStartAltro).setOnClickListener(this);
		
		result.findViewById(R.id.btnStopDataAcquisition).setOnClickListener(this);
		result.findViewById(R.id.btnStopDataAcquisition).setEnabled(false);
		/**
		 * Setting the adapter for the SEX spinner
		 */
		MainFragment.setAdapterForSpinner((Spinner)result.findViewById(R.id.sex), R.array.sex, getActivity());
		if (optionsSex == null) {
			optionsSex = getResources().getStringArray(R.array.sex);
			((Spinner) result.findViewById(R.id.sex)).setSelection(Arrays.asList(optionsSex).indexOf(preferences.getString("SEX", "")));
		}
		/**
		 * Setting the adapter for the AGE Spinner
		 */
		MainFragment.setAdapterForSpinner((Spinner)result.findViewById(R.id.age), R.array.age, getActivity());
		if (optionsAge == null) {
			optionsAge = getResources().getStringArray(R.array.age);
			((Spinner) result.findViewById(R.id.age)).setSelection(Arrays.asList(optionsAge).indexOf(preferences.getString("AGE", "")));
		}
		/**
		 * Setting the adapter for the HEIGHT Spinner
		 */
		MainFragment.setAdapterForSpinner((Spinner)result.findViewById(R.id.height), R.array.height, getActivity());
		if (optionsHeight == null) {
			optionsHeight = getResources().getStringArray(R.array.height);
			((Spinner) result.findViewById(R.id.height)).setSelection(Arrays.asList(optionsHeight).indexOf(preferences.getString("HEIGHT", "")));
		}
		/**
		 * Setting the adapter for the SHOES Spinner
		 */
		MainFragment.setAdapterForSpinner((Spinner)result.findViewById(R.id.shoes_type), R.array.shoes, getActivity());
		if (optionsShoes == null) {
			optionsShoes = getResources().getStringArray(R.array.shoes);
			((Spinner) result.findViewById(R.id.shoes_type)).setSelection(Arrays.asList(optionsShoes).indexOf(preferences.getString("SHOES", "")));
		}
		/**
		 * Setting the adapter for the ACCELEROMETER_POSITION Spinner
		 */
		MainFragment.setAdapterForSpinner((Spinner)result.findViewById(R.id.accelerometer_position), R.array.location, getActivity());
		if (optionsLocation == null) {
			optionsLocation = getResources().getStringArray(R.array.location);
			((Spinner) result.findViewById(R.id.accelerometer_position)).setSelection(Arrays.asList(optionsLocation).indexOf(preferences.getString("LOCATION", "")));
		}
		
		return result;
	}
	
	@Override
	public void onClick(View v) {
		
		AccelerometerStoreListener.settings.setTestData(1);
		v.setEnabled(false);
		
		if (v.getId() == getView().findViewById(R.id.btnStartStairs).getId()) {
			getView().findViewById(R.id.btnStopDataAcquisition).setEnabled(true);
			
			AccelerometerStoreListener.settings.setAction(currentStairMode);
			
			((MainActivity)getActivity()).onBtnStartSampling();
		}
		else if (v.getId() == getView().findViewById(R.id.btnStartAltro).getId()) {
			getView().findViewById(R.id.btnStopDataAcquisition).setEnabled(true);
		}
		else if (v.getId() == getView().findViewById(R.id.btnStopDataAcquisition).getId()) {
			
			
		}
		
	}
	
private void storePreferences() {
		
		String sex = ((Spinner) getView().findViewById(R.id.sex)).getSelectedItem().toString();
		String age = ((Spinner) getView().findViewById(R.id.age)).getSelectedItem().toString();
		String height = ((Spinner) getView().findViewById(R.id.height)).getSelectedItem().toString();
		String shoes = ((Spinner) getView().findViewById(R.id.shoes_type)).getSelectedItem().toString();
		String location = ((Spinner) getView().findViewById(R.id.accelerometer_position)).getSelectedItem().toString();
		
		/**
		 * Storing preferences
		 */
		SharedPreferences settings = getActivity().getPreferences(0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("SEX", sex); editor.putString("AGE", age);
		editor.putString("HEIGHT", height); editor.putString("SHOES", shoes);
		editor.putString("LOCATION", location);
		editor.commit();
		
		AccelerometerStoreListener.settings = new Settings(sex, age, height, shoes, location);
	}

}
