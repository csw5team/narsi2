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

    private Button logOut;
    private TextView city, personalizing, tempNow, wetRatio, airPollution;
    private String temp, whereGu, humidity, nowWeather, airPollutionNow;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        temp = intent.getExtras().getString("temp");
        whereGu = intent.getExtras().getString("whereGu");
        humidity = intent.getExtras().getString("humidity");
        nowWeather = intent.getExtras().getString("nowWeather");
        airPollutionNow = intent.getExtras().getString("airPollution");

        city = (TextView) findViewById(R.id.city);
        personalizing = (TextView) findViewById(R.id.personalizing);
        tempNow = (TextView) findViewById(R.id.temp);
        wetRatio = (TextView) findViewById(R.id.wetRatio);
        airPollution = (TextView) findViewById(R.id.airPollution);
        logOut = (Button) findViewById(R.id.logOut);

        city.setText(whereGu);
        personalizing.setText(nowWeather);
        tempNow.setText("현재온도 : "+temp+"ºC");
        wetRatio.setText("현재습도 : "+humidity+"%");
        airPollution.setText(airPollutionNow);



        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Toast.makeText(MainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), loginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


}
