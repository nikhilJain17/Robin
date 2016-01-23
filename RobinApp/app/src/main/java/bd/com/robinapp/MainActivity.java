package bd.com.robinapp;

import android.app.ActionBar;
import android.app.Activity;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Key;

public class MainActivity extends Activity implements GestureDetector.OnGestureListener, KeyEvent.Callback {

        private float x,y;
        private Socket mSocket;

        private GestureDetector gestureDetector;
        private ToggleButton lockMouseBtn;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            lockMouseBtn = (ToggleButton) findViewById(R.id.lockMouseBtn);

//            showMenuButtons();

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
                mSocket = IO.socket("http://47da2053.ngrok.io");
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


            // setup keyboard toggle stuff
            ImageView toggleKeyBoard = (ImageView) findViewById(R.id.keyboardBtn);

            toggleKeyBoard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    if (inputMethodManager != null) {
                        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }

                }
            });

//
//            final ToggleButton altBtn, ctrlBtn, shiftBtn;
//
//            altBtn = (ToggleButton) findViewById(R.id.altBtn);
//            ctrlBtn = (ToggleButton) findViewById(R.id.ctrlBtn);
//            shiftBtn = (ToggleButton) findViewById(R.id.altBtn);
//
//            altBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    if (altBtn.isChecked())
//                        mSocket.emit("alt", "yes");
//
//                }
//            });



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

                    // if mouse is NOT locked
                    if (!lockMouseBtn.isChecked()) {
                        if (x > 0.75 || x < -0.75 || y > 0.75 || y < -0.75) {
                            try {
                                mSocket.emit("mouse_move", x, y);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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

        // // TODO: 1/19/16  add shift, control, alt buttons as well
        // TODO Ngrok it up!

        @Override
        public boolean onKeyUp(int keyCode, KeyEvent event) {

            boolean capslock = false;

            if (event.isCapsLockOn() || event.isShiftPressed())
                capslock = true;


            // do not emit the capslock, shift
            if (!(keyCode == KeyEvent.KEYCODE_CAPS_LOCK) || !(event.isShiftPressed() )) {

                // emit a special case for space
                if (keyCode == KeyEvent.KEYCODE_SPACE) {
                    mSocket.emit("key", -1, capslock);
                    return true;
                }

                // emit a special case for backspace
                else if (keyCode == KeyEvent.KEYCODE_DEL) {
                    mSocket.emit("key", -2, capslock);
                    return true;
                }

                else
                    mSocket.emit("key", keyCode, capslock);
            }

            return true;
        } // end of onKeyUp


            // NOT IMPLEMENTED
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


