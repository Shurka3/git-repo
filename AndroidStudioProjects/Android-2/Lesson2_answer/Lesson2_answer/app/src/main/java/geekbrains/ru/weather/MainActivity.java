package geekbrains.ru.weather;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    SensorManager manager;
    WeatherView viewTem;
    WeatherView viewHum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor sensorTem = manager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        Sensor sensorHum = manager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

        viewTem = findViewById(R.id.temperature_block);
        if(sensorTem == null)
        {
            viewTem.setVisibility(View.GONE);
        }
        else {
            manager.registerListener(viewTem, sensorTem, SensorManager.SENSOR_DELAY_NORMAL);
        }

        viewHum = findViewById(R.id.humidity_block);
        if(sensorHum == null)
        {
            viewHum.setVisibility(View.GONE);
        }
        else {
            manager.registerListener(viewHum, sensorHum, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(viewHum.getVisibility() != View.GONE)
            manager.unregisterListener(viewHum);
        if(viewTem.getVisibility() != View.GONE)
            manager.unregisterListener(viewTem);
    }
}
