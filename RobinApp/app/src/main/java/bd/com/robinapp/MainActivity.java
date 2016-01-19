package bd.com.robinapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

    public class MainActivity extends AppCompatActivity {

        private float x,y;

        Runnable sendRunnable;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            getAccelData();

            sendRunnable = new Runnable() {
                // send to server
                @Override
                public void run() {

                    Log.d("Sending data", "Called");

                    try {
                        URL url = new URL("http://e8b3e82d.ngrok.io");

                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        // output an ack
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("User-Agent", "ROBIN");
                        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                        connection.setRequestProperty("Accept-Type", "text/html");
                        connection.setRequestProperty("Content-Type", "text/html");

                        PrintWriter out = new PrintWriter(connection.getOutputStream());

                        out.write("x: " + x + "\n");
                        out.write("y: " + y + "\n");
                        out.write("break"); // to tell it to stop reading here

                        out.flush();

                        Log.d("Response", connection.getResponseMessage());

//                BufferedReader in = new BufferedReader(connection.getInputStream());
//
//                String inputLine = " ";
//
//                // get the response
//                while (!in.readLine().equals(null))
//                    inputLine += in.readLine();
//
//                Log.d("More Response", inputLine);

                        // send accelerometer data!

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            };



            }// end of oncreate


        private void getAccelData() {

            final TextView tv = (TextView) findViewById(R.id.latlng);

            SensorManager mSensorManager;
            Sensor mSensor;

            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

            mSensorManager.registerListener(new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    tv.setText(Float.toString(sensorEvent.values[0])
                            + "\n" + Float.toString(sensorEvent.values[1])
                            + "\n" + Float.toString(sensorEvent.values[2]));

                    x = sensorEvent.values[0];
                    y = sensorEvent.values[1];

                    if (x > 2 || y > 2 || y < 2 || x < 2)
                        new Thread(sendRunnable).start();

                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            }, mSensor, 200000); // delay in microseconds

        }



    class SendDataTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            Log.d("Called", "Asynctask");

//            float x = floats[0];
//            float y = floats[1];

            try {
                URL url = new URL("http://c8339227.ngrok.io");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // output an ack
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("User-Agent", "ROBIN");
                connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                connection.setRequestProperty("Accept-Type", "text/html");
                connection.setRequestProperty("Content-Type", "text/html");

                PrintWriter out = new PrintWriter(connection.getOutputStream());

                out.write("x: " + x);
                out.write("y: " + y);
                out.write("break"); // to tell it to stop reading here

                out.flush();

                Log.d("Response", connection.getResponseMessage());

//                BufferedReader in = new BufferedReader(connection.getInputStream());
//
//                String inputLine = " ";
//
//                // get the response
//                while (!in.readLine().equals(null))
//                    inputLine += in.readLine();
//
//                Log.d("More Response", inputLine);

                // send accelerometer data!

            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}


