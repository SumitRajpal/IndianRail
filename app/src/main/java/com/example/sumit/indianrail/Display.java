package com.example.sumit.indianrail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Display extends AppCompatActivity {

    final String TAG = "check";
    private String train;
    TextView days,runst,trainnameT,trainnumberT;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
         pd = new ProgressDialog(Display.this);
         pd.setMessage("Loading . . .");

         days=(TextView)findViewById(R.id.days);
         runst=(TextView)findViewById(R.id.runs);
         trainnameT=(TextView)findViewById(R.id.trainname);
         trainnumberT=(TextView)findViewById(R.id.trainnumber);

         train=getIntent().getStringExtra("train");


        String url = "http://api.railwayapi.com/name_number/train/"+train+"/apikey/p3g4szhd/";
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                       // Log.d(TAG, response);
                        JSONObject jObject  = null;
                        try {
                            jObject = new JSONObject(response);
                            JSONObject jsonObject = jObject.getJSONObject("train");
                           // String traintname = jsonObject.getString("name");
                            trainnameT.setText(jsonObject.getString("name").toString());
                            trainnumberT.setText(jsonObject.getString("number").toString());
                            JSONArray jsonarray = new JSONArray(jsonObject.getString("days"));
                            for(int i=0; i < jsonarray.length(); i++) {
                                
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String runs       = jsonobject.getString("runs");
                                String day       = jsonobject.getString("day-code");


                                days.append("  "+day.substring(0,1).trim()+"  ");
                                runst.append("  "+runs.trim()+"  ");
                                pd.hide();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            pd.hide();
                        }




                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){
                            Log.d(TAG, error.toString());
                            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                        }
                    }
                }

        );

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

}
