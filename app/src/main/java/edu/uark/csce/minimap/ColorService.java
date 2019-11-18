package edu.uark.csce.minimap;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ColorService extends Service {
    int R;
    int G;
    int B;
    private Timer timer = new Timer();
    public static final String MY_PREFS_NAME = "RGB";
    final Context c = this;
    public static final String ACTION = "colorGrabber";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("Rest", "Testing the service out");
        getJsonRequest();


        final Handler handler = new Handler();
        //Get Colors from JSON every minute
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                getJsonRequest();
                sendMessage();
			handler.postDelayed(this, 10000);

            }
        },0);  //the time is in miliseconds
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void getJsonRequest()
    {
        String url = "http://10.5.54.42/db.json";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Rest Response" , response.toString());
                        ArrayList<Integer> values = parseRGB(response.toString());
                        //heatmap = new Heatmap(values);
                        R = values.get(0);
                        G = values.get(1);
                        B = values.get(2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response" , error.toString());
                    }
                }
        );

        requestQueue.add(objectRequest);
    }

    public static ArrayList<Integer> parseRGB(String RGB)
    {
        ArrayList<Integer> values = new ArrayList<Integer>();
        String[] split = RGB.split(",");
        for (String s : split)
        {
            StringBuilder number = new StringBuilder("");
            char[] charsplit = s.toCharArray();
            for (char c : charsplit)
            {
                if (Character.isDigit(c))
                    number.append(c);
            }
            String S = number.toString();
            values.add(Integer.valueOf(S));
        }
        return values;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void sendMessage() {

        Intent intent = new Intent(ACTION);
        // You can also include some extra data.
        intent.putExtra("RED", R);
        intent.putExtra("GREEN", G);
        intent.putExtra("BLUE", B);
        Log.d("sender", "Broadcasting message: " + R + " " + G + " " + B);
        LocalBroadcastManager.getInstance(c).sendBroadcast(intent);
    }
}

