package felixserrano.example.org.sensores;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements  SensorEventListener{
    private TextView salida;
    private float[] mGravedad;
    private float[] mGeomagnetismo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        salida = (TextView)findViewById(R.id.salida);
        SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        List<Sensor> listaSensores = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : listaSensores)
            log(sensor.getName());

        /*listaSensores = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
        if(!listaSensores.isEmpty())
        {
            Sensor orientationSensor = listaSensores.get(0);
            sensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }*/
        listaSensores = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if(!listaSensores.isEmpty())
        {
            Sensor acelerometerSensor = listaSensores.get(0);
            sensorManager.registerListener(this,acelerometerSensor,SensorManager.SENSOR_DELAY_UI);
        }
        listaSensores = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
        if(!listaSensores.isEmpty())
        {
            Sensor magneticSensor = listaSensores.get(0);
            sensorManager.registerListener(this,magneticSensor,SensorManager.SENSOR_DELAY_UI);
        }
        listaSensores = sensorManager.getSensorList(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if(!listaSensores.isEmpty())
        {
            Sensor temperatureSensor = listaSensores.get(0);
            sensorManager.registerListener(this,temperatureSensor,SensorManager.SENSOR_DELAY_UI);
        }

    }

    private void log(String string) {
        salida.append(string + "\n");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this){
            switch (event.sensor.getType())
            {
                case Sensor.TYPE_ACCELEROMETER:
                    mGravedad = event.values;
                    for (int i = 0; i < mGravedad.length;i++)
                        log("Acelerómetro "+i+": "+event.values[i]);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    mGeomagnetismo = event.values;
                    for (int i = 0; i < mGeomagnetismo.length;i++)
                        log("Magnetismo "+i+": "+event.values[i]);
                    break;
                default:
                    for(int i=0; i<event.values.length; i++)
                        log("Temperatura "+i+": "+event.values[i]);
            }
            if (mGravedad !=null && mGeomagnetismo != null)
            {
                float R[] = new float[9];
                float I[] = new float[9];
                boolean exito = SensorManager.getRotationMatrix(R,I,mGravedad,mGeomagnetismo);
                if(exito){
                    float orientacion[] = new float[3];
                    SensorManager.getOrientation(R,orientacion);
                    log("ORIENTACION Acimut " + orientacion[0]) ;
                    log("ORIENTACION Pitch " + orientacion[1]) ;
                    log("ORIENTACION Roll " + orientacion[2]) ;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
