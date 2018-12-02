package org.csw.narsi2;


import android.content.Intent;

import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;


import org.csw.narsi2.loginActivity.loginActivity;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private Button logOut, dbcheck, pageChange;
    private TextView tv_city, tv_personalizing, tv_tempNow, tv_wetRatio, tv_airPollution;
    private ProgressBar progressBar;
    private ConstraintLayout layout_waiting;
    private Button goJisu;

    private String temperature, city, gu, humidity, nowWeather, airPollution, tmax, tmin, wspd, wctIndex;
    private Weather weather = Weather.getInstance();

    private double lat, lng;
    private String targetURL, targetURL2, targetURL3;
    private String apiKey, apiKey2;

    private User currentUser;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logOut = (Button) findViewById(R.id.logOut);
        dbcheck = (Button) findViewById(R.id.dbcheck);
        pageChange = (Button) findViewById(R.id.pagechangebutton);
        tv_city = (TextView) findViewById(R.id.city);
        tv_personalizing = (TextView) findViewById(R.id.personalizing);
        tv_tempNow = (TextView) findViewById(R.id.temp);
        tv_wetRatio = (TextView) findViewById(R.id.wetRatio);
        tv_airPollution = (TextView) findViewById(R.id.airPollution);

        pageChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), personalizingInfo.class);
                startActivity(intent);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(MainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), loginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dbcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DBcheck.class);
                startActivity(intent);
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        layout_waiting = (ConstraintLayout) findViewById(R.id.layout_waiting);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFF0000FF, PorterDuff.Mode.MULTIPLY);

        final Intent intent = getIntent();
        lat = intent.getExtras().getDouble("lat");
        lng = intent.getExtras().getDouble("lng");
        apiKey = "0d4f48e5-5c2c-4bb5-931a-bca3f92b47d0";
        targetURL = "http://api2.sktelecom.com/weather/current/hourly?version=1&lat=" + lat + "&lon=" + lng + "&appkey=" + apiKey;

        mAuth = FirebaseAuth.getInstance();

        goJisu = (Button) findViewById(R.id.gojisu);
        goJisu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), FeedbackActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        find_weather();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                apiKey2 = "k4EQ4%2F5OWx0mMD0gkif7DSIBL4Wr2atojsRiHmY21vl1FcPsUIUusSXeL1xyVGcNAmciyWc3OUQmgmqaH1kPlg%3D%3D";
                targetURL2 = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty?sidoName=" + city + "&pageNo=1&numOfRows=1&ServiceKey=" + apiKey2 + "&ver=1.3&_returnType=json";find_airPollution();
                find_airPollution();

                setDB();
            }
        }, 1000);
    }

    public void find_weather() {
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, targetURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONObject("weather").getJSONArray("hourly").getJSONObject(0);
                    // 메인오브젝트 -> weather node의 hourly node

                    // temperature node
                    JSONObject temp = main_object.getJSONObject("temperature");
                    String tempCurrent = temp.getString("tc");
                    long tempCurrentRound = Math.round(Double.parseDouble(tempCurrent));
                    temperature = Long.toString(tempCurrentRound);
                    long tmaxRound = Math.round(Double.parseDouble(temp.getString("tmax")));
                    tmax = Long.toString(tmaxRound);
                    long tminRound = Math.round(Double.parseDouble(temp.getString("tmin")));
                    tmin = Long.toString(tminRound);

                    // 바람
                    JSONObject wind = main_object.getJSONObject("wind");
                    wspd = wind.getString("wspd");

                    // 도, 시/구
                    JSONObject whereGu = main_object.getJSONObject("grid");
                    gu = whereGu.getString("county");
                    city = (whereGu.getString("city"));

                    //습도
                    humidity = Long.toString(Math.round(Double.parseDouble(main_object.getString("humidity"))));

                    //하늘 상태
                    nowWeather = main_object.getJSONObject("sky").getString("name");

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
    public void find_airPollution() {
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, targetURL2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONArray("list").getJSONObject(0);
                    String pm10Grade = main_object.getString("pm10Grade");

                    setAirPollution(pm10Grade);

                    layout_waiting.setVisibility(View.GONE);
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

    public void setAirPollution(String value) {
        switch (value) {
            case "1":
                this.airPollution = "좋음";
                break;
            case "2":
                this.airPollution = "보통";
                break;
            case "3":
                this.airPollution = "나쁨";
                break;
            case "4":
                this.airPollution = "매우 나쁨";
        }

        tv_airPollution.setText("미세먼지 : " + airPollution);
        tv_city.setText(gu);
        tv_personalizing.setText(nowWeather);
        tv_tempNow.setText("현재온도 : " + temperature + "ºC");
        tv_wetRatio.setText("현재습도 : " + humidity + "%");
    }

    public void setDB() { // firebase DB 데이터 쓰기 부분

        if (user != null) {
            // Name, email address, and profile photo Url
            //String name = user.getDisplayName();

            // Check if user's email is verified
            // boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();


            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
            String getTime = sdf.format(date);
            String getTimeYMD = new SimpleDateFormat("yyyy-MM-dd kk").format(date);


            // 아래에 user1.put("이름", 객체) 순으로 넣으면 firebase DB에 쓰기 가능

            Map<String, Object> user1 = new HashMap<>();
            user1.put("TempNow", temperature);
            user1.put("NowWeather", nowWeather);
            user1.put("AirPollution", airPollution);
            user1.put("tmax", tmax);
            user1.put("tmin", tmin);
            user1.put("humidity", humidity);
            user1.put("wspd", wspd);
            user1.put("wctIndex", wctIndex);
            user1.put("timestamp", FieldValue.serverTimestamp());


            db.collection("users").document(uid).collection("WeatherInfo").document(getTimeYMD)
                    .set(user1)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });

        }
    }


}
