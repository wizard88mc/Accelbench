/**
 * Created by Nicola Beghin on 05/06/13.
 */

package org.unipd.nbeghin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.unipd.nbeghin.listeners.AccelerometerSamplingRateDetect;
import org.unipd.nbeghin.models.ClassifierCircularBuffer;
import org.unipd.nbeghin.services.SamplingClassifyService;
import org.unipd.nbeghin.services.SamplingRateDetectorService;
import org.unipd.nbeghin.services.SamplingStoreService;
import org.unipd.nbeghin.utils.DbAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends FragmentActivity {
    public static final String AppName = "AccelBench";
    public static final String SAMPLING_TYPE = "ACTION_SAMPLING";
    public static final String SAMPLING_TYPE_STAIR_UPSTAIRS = "STAIR_UPSTAIRS";
    public static final String SAMPLING_TYPE_STAIR_DOWNSTAIRS = "STAIR_DOWNSTAIRS";
    public static final String SAMPLING_TYPE_NON_STAIR = "NON_STAIR";
    public static final String SAMPLING_DELAY="DELAY";
	public static final String	ACCELEROMETER_POSITION_ACTION	= "org.unipd.nbeghin.accelerometer.position";
	public static final String	ACCELEROMETER_MIN_DIFF	= "org.unipd.nbeghin.accelerometer.min_diff";
    private boolean samplingEnabled=false;
    private double detectedSamplingRate=0;
    private double minimumSamplingRate=13;
    private Intent backgroundStoreSampler;
    private IntentFilter classifierFilter =new IntentFilter(ClassifierCircularBuffer.CLASSIFIER_ACTION);
    private IntentFilter samplingRateDetectorFilter=new IntentFilter(AccelerometerSamplingRateDetect.SAMPLING_RATE_ACTION);
    private BroadcastReceiver classifierReceiver=new ClassifierReceiver();
    private int num_steps=0;

    public class ClassifierReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String result=intent.getExtras().getString(ClassifierCircularBuffer.CLASSIFIER_NOTIFICATION_STATUS);
            Log.i(MainActivity.AppName, result);
            if (result.equals("STAIR")) {
                num_steps++;
                ((TextView) findViewById(R.id.lblNumSteps)).setText(Integer.toString(num_steps));
            }
            ((TextView) findViewById(R.id.lblClassifierOutput)).setText(result);
        }
    }

    public void onBtnStartSamplingAltro(View v) {
        ((Button) findViewById(R.id.btnStartSampling)).setEnabled(false);
        v.setEnabled(false); // disable start button
        ((Button) findViewById(R.id.btnStopSamplingAltro)).setEnabled(true); // enable stop button
        backgroundStoreSampler.putExtra(SAMPLING_TYPE, SAMPLING_TYPE_NON_STAIR);
        setGeneralSamplingParams();
        startSamplingService();
    }

    private void setGeneralSamplingParams() {
    	// accelerometer position
        int selectedId = ((RadioGroup) findViewById(R.id.radioAccelerometerPosition)).getCheckedRadioButtonId();
        switch(selectedId) {
        	case R.id.radioManoMode: backgroundStoreSampler.putExtra(ACCELEROMETER_POSITION_ACTION, "MANO"); break;
        	case R.id.radioTascaMode: backgroundStoreSampler.putExtra(ACCELEROMETER_POSITION_ACTION, "TASCA"); break;
        }
        // accelerometer min diff
        try {
        	float minDiff=Float.parseFloat(((EditText) findViewById(R.id.minDiff)).getText().toString());
        	backgroundStoreSampler.putExtra(ACCELEROMETER_MIN_DIFF, minDiff);
        } catch(Exception e) {
        	Log.e(AppName, "Unable to set min diff: "+e.getMessage());
        }
    }
    
    public void onBtnStopSamplingAltro(View v) {
        stopService(backgroundStoreSampler); // stop background server
        samplingEnabled=false;
        v.setEnabled(false); // disable stop button
        ((Button) findViewById(R.id.btnStartSamplingAltro)).setEnabled(true); // enable start button
        ((Button) findViewById(R.id.btnStartSampling)).setEnabled(true); // enable start button
    }

    public void onBtnStartSampling() {
        
        ((Button) findViewById(R.id.btnStopSampling)).setEnabled(true); // enable stop button
        int selectedId = ((RadioGroup) findViewById(R.id.radioStairsType)).getCheckedRadioButtonId();
        switch(selectedId) {
            case R.id.radioDownstairs: backgroundStoreSampler.putExtra(SAMPLING_TYPE, SAMPLING_TYPE_STAIR_DOWNSTAIRS); break;
            case R.id.radioUpstairs: backgroundStoreSampler.putExtra(SAMPLING_TYPE, SAMPLING_TYPE_STAIR_UPSTAIRS); break;
        }
        setGeneralSamplingParams();
        startSamplingService();
    }

    public void onBtnStopSampling(View v) {
        stopService(backgroundStoreSampler); // stop background server
        samplingEnabled=false;
        v.setEnabled(false); // disable stop button
        ((Button) findViewById(R.id.btnStartSamplingAltro)).setEnabled(true); // enable start button
        ((Button) findViewById(R.id.btnStartSampling)).setEnabled(true); // enable start button
    }

    public void startSamplingService() {
        int selectedId = ((RadioGroup) findViewById(R.id.radioSamplingGroup)).getCheckedRadioButtonId();
        switch(selectedId) {
            case R.id.radioSamplingFastest: backgroundStoreSampler.putExtra(SAMPLING_DELAY, 8000); break;
            case R.id.radioSamplingGame: backgroundStoreSampler.putExtra(SAMPLING_DELAY, 16000); break;
            case R.id.radioSamplingUI: backgroundStoreSampler.putExtra(SAMPLING_DELAY, 32000); break;
            default: backgroundStoreSampler.putExtra(SAMPLING_DELAY, 65000);
        }
        startService(backgroundStoreSampler); // start background service
        samplingEnabled=true;
    }

    private void stopAllServices() {
        try {
            stopService(backgroundStoreSampler);
        } catch (Exception e) {
            Log.e(MainActivity.AppName, "Unable to stop background store");
            e.printStackTrace();
        }
    }

    public void onBtnClearDb(MenuItem v) {
        clearDb();
    }

    public void onBtnStopAllServices(MenuItem v) {
        stopAllServices();
    }

    public void onBtnUploadDb(MenuItem v) {
        shareDb();
    }

    private void clearDb() {
        DbAdapter dbAdapter = new DbAdapter(this);
        dbAdapter.open();
        dbAdapter.cleanDb();
        Log.i(AppName, "Database cleared");
        Toast.makeText(getApplicationContext(), "Database cleared", Toast.LENGTH_SHORT).show();
        dbAdapter.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        backgroundStoreSampler = new Intent(this, SamplingStoreService.class); // instance (without starting) background sampler
        
        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(new MainFragment(getSupportFragmentManager()));
    }

    @Override
    protected void onResume() {
        Log.i(AppName, "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i(AppName, "onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.i(AppName, "onDestroy");
        stopAllServices();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (samplingEnabled==false) super.onBackPressed();
        else Toast.makeText(getApplicationContext(), "Sampling running - Stop it before exiting", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    private void shareDb() {
        SimpleDateFormat df=new SimpleDateFormat("yyyyMMddHHmmss");
        String output_name="accelbench_"+df.format(new Date())+".db";
        try {
            DbAdapter dbAdapter = new DbAdapter(this); // get reference to db connection
            dbAdapter.open();
            File file=new File(dbAdapter.getDbPath()); // get private db reference
            dbAdapter.close();
            if (file.exists()==false || file.length()==0) throw new Exception("Empty DB");
            this.copyFile(new FileInputStream(file), this.openFileOutput(output_name, MODE_WORLD_READABLE));
            file=this.getFileStreamPath(output_name);
            Intent i=new Intent(Intent.ACTION_SEND);
            i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            i.setType("*/*");
            startActivity(Intent.createChooser(i, "Share to"));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Unable to export db: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(AppName, e.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (samplingEnabled==false) {
            if (item.getItemId() == R.id.action_cleardb) {
                clearDb();
                return (true);
            }
            if (item.getItemId() == R.id.action_upload_db) {
                shareDb();
                return true;
            }
            if (item.getItemId()==R.id.action_stop_all_services) {
                stopAllServices();
                return true;
            }
        }
        return (super.onOptionsItemSelected(item));
    }
}
