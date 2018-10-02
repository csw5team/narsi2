package org.csw.narsi2;


import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.csw.narsi2.loginActivity.loginActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private double lat;
    private double lng;
    private Button logOut;
    private String targetURL;
    private String apiKey;
    private TextView city, personalizing, tempNow, wetRatio, airPollution;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        lat = intent.getExtras().getDouble("lat");
        lng = intent.getExtras().getDouble("lng");

        apiKey = "6acd62f11cfc61efca9748dc828fb78d";
        targetURL = "http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&" + "lon=" + lng + "&appid=" + apiKey;

        city = (TextView) findViewById(R.id.city);
        personalizing = (TextView) findViewById(R.id.personalizing);
        tempNow = (TextView) findViewById(R.id.temp);
        wetRatio = (TextView) findViewById(R.id.wetRatio);
        airPollution = (TextView) findViewById(R.id.airPollution);
        logOut = (Button) findViewById(R.id.logOut);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent = new Intent(v.getContext(), loginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        find_weather(targetURL);

    }

    public void find_weather(String url) {
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONObject("main");
                    String temp = String.valueOf((int)(main_object.getDouble("temp")-273));
                    String humidity = String.valueOf(main_object.getInt("humidity"));
                    tempNow.setText("현재온도 : "+temp+"ºC");
                    wetRatio.setText("현재 습도 : "+humidity+"%");

                    JSONArray array = response.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);
                    String description = object.getString("description");
                    personalizing.setText("현재 날씨 : "+description);

                    String temp3 = response.getString("name");
                    city.setText(temp3);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);
    }
}
