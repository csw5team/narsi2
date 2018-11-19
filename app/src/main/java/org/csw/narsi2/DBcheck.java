package org.csw.narsi2;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.csw.narsi2.loginActivity.loginActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBcheck extends AppCompatActivity {
    private ArrayList<String> ArrayListView = new ArrayList<>();
    private ListView listView;
    private Button changeNameButton, findNameButton;

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

                //파이어베이스에서 데이터를 로드하는 데에 걸리는 시간을 어댑터 하기 전에 기다려준다.

                changeNameButton = (Button) findViewById(R.id.changename);
                changeNameButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        popUp();
                    }
                });
                findNameButton = (Button) findViewById(R.id.findname);
                findNameButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        findName();
                    }
                });
            }
        }, 1000);
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
                                    ArrayListView.add(date + "시 " + weather);
                                    //컬렉션 아이디와 문서 내용 중 NowWeather를 불러와서 ArrayList에 저장한다.
                                }
                            } else {
                            }
                        }
                    });
        }

    }

    public void ChangeName(String myName) {

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

            DocumentReference name = db.collection("users").document(uid);
            name
                    .update("UserName", myName)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(DBcheck.this, "이름 변경 성공", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DBcheck.this, "이름 변경 실패", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void findName() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
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

            DocumentReference docRef = db.collection("users").document(uid);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String name = "";
                            name = document.getString("UserName");
                            if (name != "")
                                Toast.makeText(DBcheck.this, "이름 : " + name, Toast.LENGTH_SHORT).show();

                        }
                    } else {
                    }
                }

            });
        }
    }

    public void popUp() {
        final EditText edittext = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("이름 변경");
        builder.setView(edittext);
        builder.setPositiveButton("Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ChangeName(edittext.getText().toString());
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }


    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
}
