package id.yusufrizalh.shakesensor;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    // instance variable
    private SensorManager sensorManager;
    private boolean isColor = false;
    private View view;
    private long lastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.txt_view);
        view.setBackgroundColor(Color.GREEN);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }
    }

    private void getAccelerometer(SensorEvent event) {
        // mendeteksi movement
        float[] values = event.values;
        float x = values[0];
        float y = values[1];
        float z = values[2];
        float accelationSqrtRoot = (x * x * y * y * z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = System.currentTimeMillis();
        Toast.makeText(this,
                String.valueOf(accelationSqrtRoot) + " " +
                SensorManager.GRAVITY_EARTH, Toast.LENGTH_LONG).show();

        if (accelationSqrtRoot >= 2) {
            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;    // update waktu utk next shake
            if (isColor) {
                view.setBackgroundColor(Color.GREEN);
            } else {
                view.setBackgroundColor(Color.RED);
            }
            isColor = !isColor;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}