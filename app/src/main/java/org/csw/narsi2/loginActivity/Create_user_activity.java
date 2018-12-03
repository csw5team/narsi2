package org.csw.narsi2.loginActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.csw.narsi2.R;
import org.csw.narsi2.User;

import java.util.HashMap;

public class Create_user_activity extends AppCompatActivity {

    private EditText et_email, et_password, et_age, et_name;
    private Button createUser;
    private Spinner spinner_sex;
    private String sex = "무";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_activity);

        et_email = (EditText) findViewById(R.id.user_id2);
        et_password = (EditText) findViewById(R.id.user_password2);
        createUser = (Button) findViewById(R.id.user_signup);
        et_age = (EditText) findViewById(R.id.user_age);
        et_name = (EditText) findViewById(R.id.user_name);
        spinner_sex = (Spinner) findViewById(R.id.user_sex);

        String[] items = new String[]{"남", "여"};

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, items);
        spinner_sex.setAdapter(adapter);
        spinner_sex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sex = spinner_sex.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(et_email.getText().toString().equals("") || et_password.getText().toString().equals("") || et_age.getText().toString().equals("") || et_name.getText().toString().equals("")||sex.equals("무"))) {
                    createAccount(et_email.getText().toString().trim(), et_password.getText().toString().trim());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else
                    Toast.makeText(Create_user_activity.this, "값이 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createAccount(String email, String password) {

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Create_user_activity.this, "회원가입 성공", Toast.LENGTH_LONG).show();

                            if (user != null) {

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                final String uid = user.getUid();
                                HashMap<String, Object> user1 = new HashMap<>();
                                user1.put("age", et_age.getText().toString());
                                user1.put("sex", sex);
                                user1.put("UserName", et_name.getText().toString());

                                db.collection("user_final").document(uid)
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


                            startActivity(new Intent(Create_user_activity.this, loginActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Create_user_activity.this, "회원가입 실패",
                                    Toast.LENGTH_LONG).show();
                        }


                    }

                });
        // [END create_user_with_email]
    }


}
