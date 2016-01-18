package bd.com.robinapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

    public class MainActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            SendDataTask task = new SendDataTask();
            task.execute();

        }

    }

    class SendDataTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            try {
                URL url = new URL("http://7d58ba52.ngrok.io");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // output an ack
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("User-Agent", "ROBIN");
                connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                connection.setRequestProperty("Content-Type", "text/html");

                PrintWriter out = new PrintWriter(connection.getOutputStream());

                out.println("do you hear me?");
                out.println("break"); // to tell it to stop reading here

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

            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }




