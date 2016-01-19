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
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

    public class MainActivity extends AppCompatActivity {

        private float x,y;
        private Socket mSocket;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // connect the socket to the server
            try {
                mSocket = IO.socket("http://48ecd759.ngrok.io");
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            mSocket.connect();

            // register the callback
            getAccelData();


        }// end of oncreate


//        private void


        private void getAccelData() {

            final TextView tv = (TextView) findViewById(R.id.latlng);

            SensorManager mSensorManager;
            Sensor mSensor;

            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            mSensorManager.registerListener(new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    tv.setText(Float.toString(sensorEvent.values[0])
                            + "\n" + Float.toString(sensorEvent.values[1])
                            + "\n" + Float.toString(sensorEvent.values[2]));

                    x = sensorEvent.values[0];
                    y = sensorEvent.values[1];

                    if (x > 1 || x < -1 || y > 1 || y < -1) {
                        try {
                            mSocket.emit("mouse_move", x, y);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }// end of onSensorChanged

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


