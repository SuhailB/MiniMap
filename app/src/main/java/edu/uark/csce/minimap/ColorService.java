package edu.uark.csce.minimap;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

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

public class ColorService extends Service {
    int primeR, axciomR;
    int primeG, axciomG;
    int primeB, axciomB;
    int primeCount, axciomCount;
    private Timer timer = new Timer();
    public static final String MY_PREFS_NAME = "RGB";
    final Context c = this;
    public static final String PRIME = "prime";
    public static final String AXCIOM = "axciom";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("Rest", "Testing the service out");
        getPrimeRequest();

        final Handler handler = new Handler();
        //Get Colors from JSON every minute
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                getPrimeRequest();
                getAxciomRequest();
                sendPrimeMessage();
                sendAxciomMessage();
                handler.postDelayed(this, 3000);

            }
        },0);  //the time is in miliseconds
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void getPrimeRequest()
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
                        primeR = values.get(0);
                        primeG = values.get(1);
                        primeB = values.get(2);
                        primeCount = values.get(3);
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

    public void getAxciomRequest()
    {
        String url = "http://10.5.54.42/axciom.json";
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
                        axciomR = values.get(0);
                        axciomG = values.get(1);
                        axciomB = values.get(2);
                        axciomCount = values.get(3);
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

    private void sendPrimeMessage() {

        Intent primeIntent = new Intent(PRIME);
        // You can also include some extra data.
        primeIntent.putExtra("RED", primeR);
        primeIntent.putExtra("GREEN", primeG);
        primeIntent.putExtra("BLUE", primeB);
        primeIntent.putExtra("PRIMECOUNT", primeCount);
        Log.d("sender", "Broadcasting message: " + primeR + " " + primeG + " " + primeB);
        LocalBroadcastManager.getInstance(c).sendBroadcast(primeIntent);
    }

    private void sendAxciomMessage()
    {
        Intent axciomIntent = new Intent(AXCIOM);
        // You can also include some extra data.
        axciomIntent.putExtra("RED", axciomR);
        axciomIntent.putExtra("GREEN", axciomG);
        axciomIntent.putExtra("BLUE", axciomB);
        axciomIntent.putExtra("AXCIOMCOUNT", axciomCount);
        Log.d("sender", "Broadcasting message: " + axciomR + " " + axciomG + " " + axciomB);
        LocalBroadcastManager.getInstance(c).sendBroadcast(axciomIntent);

    }


}

