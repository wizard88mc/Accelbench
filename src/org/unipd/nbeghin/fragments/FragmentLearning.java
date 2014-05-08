package org.unipd.nbeghin.fragments;

import org.unipd.nbeghin.MainActivity;
import org.unipd.nbeghin.MainActivityFragment;
import org.unipd.nbeghin.MainFragment;
import org.unipd.nbeghin.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class FragmentLearning extends Fragment implements View.OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View result = inflater.inflate(R.layout.learning_layout, container, false);
		
		result.findViewById(R.id.btnStartSampling).setOnClickListener(this);
		
		/**
		 * Setting the adapter for the SEX spinner
		 */
		MainFragment.setAdapterForSpinner((Spinner)result.findViewById(R.id.sex), R.array.sex, getActivity());
		/**
		 * Setting the adapter for the AGE Spinner
		 */
		MainFragment.setAdapterForSpinner((Spinner)result.findViewById(R.id.age), R.array.age, getActivity());
		/**
		 * Setting the adapter for the HEIGHT Spinner
		 */
		MainFragment.setAdapterForSpinner((Spinner)result.findViewById(R.id.height), R.array.height, getActivity());
		/**
		 * Setting the adapter for the SHOES Spinner
		 */
		MainFragment.setAdapterForSpinner((Spinner)result.findViewById(R.id.shoes_type), R.array.shoes, getActivity());
		/**
		 * Setting the adapter for the ACCELEROMETER_POSITION Spinner
		 */
		MainFragment.setAdapterForSpinner((Spinner)result.findViewById(R.id.accelerometer_position), R.array.location, getActivity());
		
		return result;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnStartSampling) {
			/**
			 * Disabling the button to start acquiring data NON_STAIRS
			 */
			((Button) getView().findViewById(R.id.btnStartSamplingAltro)).setEnabled(false);
			/**
			 * Disabling the current clicked button and enabling the stop button
			 */
			v.setEnabled(false);
			((Button) getView().findViewById(R.id.btnStopSampling)).setEnabled(false);
			
			((RadioGroup) getView().findViewById(R.id.radioStairsType)).setEnabled(false);
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
}
