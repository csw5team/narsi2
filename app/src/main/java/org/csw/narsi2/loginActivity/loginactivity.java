package org.csw.narsi2.loginActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.csw.narsi2.MainActivity;
import org.csw.narsi2.R;

public class loginactivity extends AppCompatActivity {

    private Button bt_create_user, bt_user_signin;
    private EditText userId, userPassword;
    private FirebaseAuth mAuth;

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

        mAuth = FirebaseAuth.getInstance();
        bt_user_signin = (Button) findViewById(R.id.user_signIn);


        bt_user_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId.getText().toString().equals("") || userPassword.getText().toString().equals(""))
                    Toast.makeText(loginactivity.this, "아이디, 비밀번호가 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
                else
                    signIn(userId.getText().toString(), userPassword.getText().toString());

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(loginactivity.this, MainActivity.class));
        }
    }


    private void signIn(String userId, String userPassword) {
        mAuth.signInWithEmailAndPassword(userId, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                Toast.makeText(loginactivity.this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(loginactivity.this, MainActivity.class));
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(loginactivity.this, "아이디, 비밀번호를 확인해 주세요.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }
}
