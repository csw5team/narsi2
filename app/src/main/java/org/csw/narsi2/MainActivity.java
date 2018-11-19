package org.csw.narsi2;


import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;


import org.csw.narsi2.loginActivity.loginActivity;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private Button logOut, dbcheck;
    private TextView city, personalizing, tempNow, wetRatio, airPollution;
    private String temp, whereGu, humidity, nowWeather, airPollutionNow, tmax, tmin, wspd, wctIndex;
    private FirebaseAuth mAuth;

    private ArrayList<String> Info = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logOut = (Button) findViewById(R.id.logOut);
        dbcheck = (Button) findViewById(R.id.dbcheck);

        setText();
        setDB();

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
        dbcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DBcheck.class);
                startActivity(intent);
            }
        });
    }

    public void setDB() { // firebase DB 데이터 쓰기 부분


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
            user1.put("TempNow", temp);
            user1.put("NowWeather", nowWeather);
            user1.put("AirPollution", airPollutionNow);
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

    public void setText() { // intent로부터 정보 가져오기 및 Layout에 setText를 해주는 부분.
        Intent intent = getIntent();
        temp = intent.getExtras().getString("temp");
        whereGu = intent.getExtras().getString("whereGu");
        humidity = intent.getExtras().getString("humidity");
        nowWeather = intent.getExtras().getString("nowWeather");
        airPollutionNow = intent.getExtras().getString("airPollution");
        tmax = intent.getExtras().getString("tmax");
        tmin = intent.getExtras().getString("tmin");
        wspd = intent.getExtras().getString("wspd");
        wctIndex = intent.getExtras().getString("wctIndex");

        city = (TextView) findViewById(R.id.city);
        personalizing = (TextView) findViewById(R.id.personalizing);
        tempNow = (TextView) findViewById(R.id.temp);
        wetRatio = (TextView) findViewById(R.id.wetRatio);
        airPollution = (TextView) findViewById(R.id.airPollution);

        airPollution.setText("미세먼지 : " + airPollutionNow);
        city.setText(whereGu);
        personalizing.setText(nowWeather);
        tempNow.setText("현재온도 : " + temp + "ºC");
        wetRatio.setText("현재습도 : " + humidity + "%");


    }


}
