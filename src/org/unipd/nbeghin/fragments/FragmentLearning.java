package org.unipd.nbeghin.fragments;

import java.util.Arrays;

import org.unipd.nbeghin.MainActivity;
import org.unipd.nbeghin.MainActivityFragment;
import org.unipd.nbeghin.MainFragment;
import org.unipd.nbeghin.R;
import org.unipd.nbeghin.listeners.AccelerometerStoreListener;
import org.unipd.nbeghin.models.Settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class FragmentLearning extends Fragment implements View.OnClickListener {
	
	public static String[] optionsSex = null;
	public static String[] optionsAge = null;
	public static String[] optionsHeight = null;
	public static String[] optionsShoes = null;
	public static String[] optionsLocation = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		SharedPreferences preferences = getActivity().getPreferences(0);
		
		View result = inflater.inflate(R.layout.learning_layout, container, false);
		
		result.findViewById(R.id.btnStartSampling).setOnClickListener(this);
		
		/**
		 * Setting the adapter for the SEX spinner and setting default value
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

		storePreferences();
		
		if (v.getId() == R.id.btnStartSampling) {
			/**
			 * Disabling the button to start acquiring data NON_STAIRS
			 */
			((Button) getView().findViewById(R.id.btnStartSamplingAltro)).setEnabled(false);
			/**
			 * Disabling the current clicked button and enabling the stop button
			 */
			v.setEnabled(false);
			((Button) getView().findViewById(R.id.btnStopSampling)).setEnabled(true);
			
			((RadioGroup) getView().findViewById(R.id.radioStairsType)).setEnabled(false);
			
			if (((RadioGroup)getView().findViewById(R.id.radioStairsType)).getCheckedRadioButtonId() == 
					getView().findViewById(R.id.radioDownstairs).getId()) {
				
				AccelerometerStoreListener.settings.setAction(MainActivity.SAMPLING_TYPE_STAIR_DOWNSTAIRS);
			}
			else {
				AccelerometerStoreListener.settings.setAction(MainActivity.SAMPLING_TYPE_STAIR_UPSTAIRS);
			}
			
			//((MainActivity)getActivity()).onBtnStartSampling();
		}
		else if (v.getId() == R.id.btnStopSampling) {
			/**
			 * Enabling the button to start acquiring data NON_STAIRS
			 */
			((Button) getView().findViewById(R.id.btnStartSamplingAltro)).setEnabled(true);
			/**
			 * Disabling the current button and enabling the start button
			 */
			v.setEnabled(false);
			((Button) getView().findViewById(R.id.btnStartSampling)).setEnabled(true);
			
			((RadioGroup) getView().findViewById(R.id.radioStairsType)).setEnabled(true);
		}
		else if (v.getId() == R.id.btnStartSamplingAltro) {
			/**
			 * Disabling the start button for the STAIR data
			 */
			((Button) getView().findViewById(R.id.btnStartSampling)).setEnabled(false);
			/**
			 * Disabling the current button and enabling the stop button
			 */
			v.setEnabled(false);
			((Button) getView().findViewById(R.id.btnStopSamplingAltro)).setEnabled(false);
			
			AccelerometerStoreListener.settings.setAction(MainActivity.SAMPLING_TYPE_NON_STAIR);
		}
		else if (v.getId() == R.id.btnStopSamplingAltro) {
			/**
			 * Enabling the start button for the STAIR data
			 */
			((Button) getView().findViewById(R.id.btnStartSampling)).setEnabled(true);
			/**
			 * Disabling the current button and enabling the start button for NON_STAIR
			 */
			v.setEnabled(false);
			((Button) getView().findViewById(R.id.btnStartSamplingAltro)).setEnabled(true);
		}
		((MainActivityFragment)getActivity()).startRecordStairData();
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
