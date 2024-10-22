package org.csw.narsi2.loginActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.csw.narsi2.User;
import org.csw.narsi2.PreferenceController;
import org.csw.narsi2.R;
import org.csw.narsi2.getLatLng;

import java.util.HashMap;
import java.util.Map;

public class loginActivity extends AppCompatActivity {

    private Button bt_create_user, bt_user_signin;
    private EditText userId, userPassword;
    private User singleUser;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private String uid;
    Boolean checked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);
        userId = (EditText) findViewById(R.id.user_id1);
        userPassword = (EditText) findViewById(R.id.user_password1);
        bt_create_user = (Button) findViewById(R.id.button_create_user);

        bt_create_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(v.getContext(), Create_user_activity.class));

            }
        });

        bt_user_signin = (Button) findViewById(R.id.user_signIn);


        bt_user_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId.getText().toString().equals("") || userPassword.getText().toString().equals(""))
                    Toast.makeText(loginActivity.this, "아이디, 비밀번호가 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
                else
                    signIn(userId.getText().toString(), userPassword.getText().toString());

            }
        });

        mAuth = FirebaseAuth.getInstance();
        singleUser = new User();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this, "앱 실행을 위해 위치정보 제공 동의가 필요합니다.", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user != null) {
                    Greeting();
                }
            }
        }, 0);

    }


    private void signIn(String userId, String userPassword) {
        mAuth.signInWithEmailAndPassword(userId, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            /*
                            Toast.makeText(loginActivity.this, "반갑습니다!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(loginActivity.this, getLatLng.class);

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("User", singleUser);
                            i.putExtras(bundle);

                            startActivity(i);
                            finish();
                            */
                            user = mAuth.getCurrentUser();
                            db = FirebaseFirestore.getInstance();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (user != null) {
                                        Greeting();
                                    }
                                }
                            }, 0);



                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(loginActivity.this, "아이디, 비밀번호를 확인해 주세요.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }


    public void Greeting() {

        if (user != null) {
            // Name, email address, and profile photo Url
            //String name = user.getDisplayName();

            // Check if user's email is verified
            // boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            uid = user.getUid();
            DocumentReference docRef = db.collection("user_final").document(uid);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String name = "";
                            String gender = document.getString("sex");
                            singleUser.setGender(gender);
                            singleUser.setUserID(uid);
                            name = document.getString("UserName");
                            if (name != null) {
                                if (!name.equals("")) {
                                    Toast.makeText(loginActivity.this, name + "님 어서오세요.", Toast.LENGTH_SHORT).show();
                                    singleUser.setName(name);
                                } else {
                                    Toast.makeText(loginActivity.this, "반갑습니다.", Toast.LENGTH_SHORT).show();

                                    Map<String, Object> data = new HashMap<>();
                                    data.put("UserName", "");
                                    db.collection("user_final").document(uid).set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });

                                }
                            }
                        } else {
                            Map<String, Object> data = new HashMap<>();
                            data.put("UserName", "");
                            db.collection("user_final").document(uid).set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(loginActivity.this, "오류 발생", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                    }
                }
            });
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            checked = document.getBoolean("checked");
                            if (checked != null) {
                                Intent intent = new Intent(loginActivity.this, getLatLng.class);

                                Bundle bundle = new Bundle();
                                bundle.putSerializable("User", singleUser);
                                intent.putExtras(bundle);

                                startActivity(intent);
                                finish();
                            } else {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(loginActivity.this, PreferenceController.class);

                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("User", singleUser);
                                        intent.putExtras(bundle);

                                        startActivity(intent);
                                    }
                                }, 0);

                            }
                        } else {
                            Map<String, Object> data = new HashMap<>();

                            db.collection("user_final").document(uid).set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    /*Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);*/
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(loginActivity.this, "오류 발생", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


        }
    }
}
