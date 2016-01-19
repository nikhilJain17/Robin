package bd.com.robinapp;

import android.content.Context;
import android.gesture.Gesture;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

    public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

        private float x,y;
        private Socket mSocket;

        private GestureDetector gestureDetector;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // set up the scrolling thing
            gestureDetector = new GestureDetector(this);
            View.OnTouchListener onTouchListener = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            };


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

            Button leftClick, rightClick;
            leftClick = (Button) findViewById(R.id.leftClk);
            rightClick = (Button) findViewById(R.id.rightClk);

            leftClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSocket.emit("left_click");
                }
            });

            //TODO ADD A DRAG OPTION (BUTTON DOWN)

            rightClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSocket.emit("right_click");
                }
            });




        }// end of oncreate

        private void getAccelData() {

            SensorManager mSensorManager;
            Sensor mSensor;

            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            mSensorManager.registerListener(new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {

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


        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            Log.d("Scroll", "Did scroll");

            mSocket.emit("scroll", distanceY);

            return true;
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            gestureDetector.onTouchEvent(event);
            return super.dispatchTouchEvent(event);
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return true;
        }

        // needed
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return true;
        }

    }


