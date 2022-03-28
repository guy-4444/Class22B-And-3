package com.guy.class22b_and_3;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import com.google.android.material.textview.MaterialTextView;

import android.view.Menu;
import android.view.MenuItem;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Activity_Sensor extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private ExtendedFloatingActionButton sensor_FAB_action;
    private MaterialTextView sensor_LBL_acc;
    private MaterialTextView sensor_LBL_light;

    private SensorManager sensorManager;
    private Sensor accSensor;
    private Sensor lightSensor;

    private boolean open = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        toolbar = findViewById(R.id.toolbar);
        sensor_FAB_action = findViewById(R.id.sensor_FAB_action);
        sensor_LBL_acc = findViewById(R.id.sensor_LBL_acc);
        sensor_LBL_light = findViewById(R.id.sensor_LBL_light);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

    }

    /**
     * This function opens the Vibration activity
     * @param data the last light value
     * @return returns if the session was successes
     */
    private boolean openVibrateActivity(String data) {
        Intent intent = new Intent(this, Activity_Vibrate.class);
        intent.putExtra(Activity_Vibrate.EXTRA_LIGHT, data);
        startActivity(intent);
        finish();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        toolbar.setTitle("Guy Isakov");
        toolbar.setSubtitle("typing...");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private SensorEventListener lightSensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            NumberFormat formatter = new DecimalFormat("#0.00");
            float lux = sensorEvent.values[0];
            sensor_LBL_light.setText(formatter.format(lux));

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            Log.d("pttt", "onAccuracyChanged");
        }
    };

    private SensorEventListener accSensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            NumberFormat formatter = new DecimalFormat("#0.00");
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            sensor_LBL_acc.setText(formatter.format(x) + "\n" + formatter.format(y) + "\n" + formatter.format(z));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            Log.d("pttt", "onAccuracyChanged");
        }
    };

    private SensorEventListener multipleSensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            NumberFormat formatter = new DecimalFormat("#0.00");

            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];
                sensor_LBL_acc.setText(formatter.format(x) + "\n" + formatter.format(y) + "\n" + formatter.format(z));
            } else if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
                float lux = sensorEvent.values[0];
                sensor_LBL_light.setText(formatter.format(lux));

                if (!open  &&  lux > 1000) {
                    open = true;
                    openVibrateActivity("" + lux);
                }
            } else if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                float cm = sensorEvent.values[0];
                sensor_LBL_light.setText(formatter.format(cm));
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            Log.d("pttt", "onAccuracyChanged");
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //sensorManager.registerListener(lightSensorEventListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        //sensorManager.registerListener(accSensorEventListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);


        sensorManager.registerListener(multipleSensorEventListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(multipleSensorEventListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(multipleSensorEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        open = false;
    }
}