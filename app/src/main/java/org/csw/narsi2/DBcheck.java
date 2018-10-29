package org.csw.narsi2;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class DBcheck extends AppCompatActivity {
    private ArrayList<String> ArrayListView = new ArrayList<>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbcheck);

        findDB();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ArrayAdapter<String> adapter = new ArrayAdapter(DBcheck.this, android.R.layout.simple_list_item_1, ArrayListView);
                listView = (ListView) findViewById(R.id.listview1);
                listView.setAdapter(adapter);

            }
        },1000);
        //파이어베이스에서 데이터를 로드하는 데에 걸리는 시간을 어댑터 하기 전에 기다려준다.


    }

    public void findDB() {

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
            final String uid = user.getUid();

            db.collection("users").document(uid).collection("WeatherInfo")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String weather = document.getString("NowWeather");
                                    String date = document.getId();
                                    ArrayListView.add(date +"시 "+weather);
                                    //컬렉션 아이디와 문서 내용 중 NowWeather를 불러와서 ArrayList에 저장한다.
                                }
                            } else {
                            }
                        }
                    });
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
}
