package com.example.stevens.vibcomm;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends Activity implements SensorEventListener{
    SensorManager sensorManager;
    private Sensor mAccelerometer,mGyroscope;
    EditText etAcc, etGyro;
    Button btn_start;
    private Boolean SENSE_FLAG = false;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
    private String currentDateandTime;
    // for recording wav audio
    private boolean isCompressed = false;
    ExtAudioRecorder extAudioRecorder = ExtAudioRecorder.getInstanse(isCompressed);
    private long[] pattern={ //vibration pattern
            1000,
            10,400,10,400,10,400,10,400,10,1000,
            20,400,20,400,20,400,20,400,20,1000,
            40,400,40,400,40,400,40,400,40,1000,
            80,400,80,400,80,400,80,400,80,1000,
            160,400,160,400,160,400,160,400,160,1000,
            5000,1000,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etAcc = (EditText) findViewById(R.id.etAccelerometer);
        etGyro = (EditText) findViewById(R.id.etGyroscope);
        btn_start = (Button) findViewById(R.id.btnStart);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        btn_start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.btnStart){
                    if(btn_start.getText().equals("Start Vibration Sensing")){
                        currentDateandTime = sdf.format(new Date());
                        if(SENSE_FLAG == false){
                            SENSE_FLAG = true;
                        }
                        btn_start.setText("Stop Vibration Sensing");
                        // audio recording configuration
                        if (!Environment.getExternalStorageState().equals(
                                android.os.Environment.MEDIA_MOUNTED))
                        {
                            Toast.makeText(MainActivity.this, "No SD card",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        extAudioRecorder.setOutputFile(FileStoreTools.getSDPath() + File.separator + "AUDIO_" + currentDateandTime + ".wav");
                        extAudioRecorder.prepare();
                        extAudioRecorder.start();
                        Log.d(this.toString(), "audio recording start");
                        // vibration configuration
                        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vib.vibrate(pattern,-1);
                        CharSequence text = "Start Vibration!";
                        Context context = getApplicationContext();
                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                    }
                    else if(btn_start.getText().equals("Stop Vibration Sensing")){
                        btn_start.setText("Start Vibration Sensing");
                        if(SENSE_FLAG == true){
                            SENSE_FLAG = false;
                        }
                        extAudioRecorder.stop();
                        extAudioRecorder.release();
                        Log.d(this.toString(), "audio recording end");
                        CharSequence text = "Sensor Data Restored!";
                        Context context = getApplicationContext();
                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        //Configure Accelerometer
       // mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //sensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_FASTEST);
        //Configure Gyroscope
        //mGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        //sensorManager.registerListener(this, mGyroscope , SensorManager.SENSOR_DELAY_FASTEST);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        int sensorType = event.sensor.getType();
        StringBuilder sb = null;
        String str_acc,str_gyro;
        switch (sensorType)
        {
            case Sensor.TYPE_LINEAR_ACCELERATION:
//                sb = new StringBuilder();
//                sb.append("x-Axis: ");
//                sb.append(values[0]);
//                sb.append("\ny-Axis: ");
//                sb.append(values[1]);
//                sb.append("\nz-Axis: ");
//                sb.append(values[2]);
//                etAcc.setText(sb.toString());
                if (SENSE_FLAG == true){
                    str_acc = Long.toString(event.timestamp)+"\t"+Float.toString(values[0])+"\t"+Float.toString(values[1])+"\t"+Float.toString(values[2])+"\t\n";
                    FileStoreTools.saveFile(str_acc, FileStoreTools.getSDPath()  + File.separator +"ACC_" + currentDateandTime + ".txt");
                }
                break;
            case Sensor.TYPE_GYROSCOPE:
//                sb = new StringBuilder();
//                sb.append("x-Axis: ");
//                sb.append(values[0]);
//                sb.append("\ny-Axis: ");
//                sb.append(values[1]);
//                sb.append("\nz-Axis: ");
//                sb.append(values[2]);
//                etGyro.setText(sb.toString());
                if (SENSE_FLAG == true){
                    str_gyro = Long.toString(event.timestamp)+"\t"+Float.toString(values[0])+"\t"+Float.toString(values[1])+"\t"+Float.toString(values[2])+"\t\n";
                    FileStoreTools.saveFile(str_gyro, FileStoreTools.getSDPath() + File.separator +"GYRO_" + currentDateandTime + ".txt");
                }
                break;
            default:
                break;
        }
    }


}
