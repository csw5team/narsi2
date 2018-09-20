package org.csw.narsi2.loginActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.csw.narsi2.R;

public class Create_user_activity extends AppCompatActivity{

    private EditText email, password;
    private Button createUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_activity);

        mAuth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.user_id2);
        password = (EditText) findViewById(R.id.user_password2);
        createUser = (Button) findViewById(R.id.user_signup);


        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(email.getText().toString().equals("") || password.getText().toString().equals(""))) {
                    createAccount(email.getText().toString().trim(), password.getText().toString().trim());
                    startActivity(new Intent(Create_user_activity.this, loginactivity.class));
                    finish();
                }else
                    Toast.makeText(Create_user_activity.this,"아이디, 비밀번호가 유효하지 않습니다.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void createAccount(String email, String password) {

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Create_user_activity.this,"회원가입 성공", Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Create_user_activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }

                });
        // [END create_user_with_email]
    }
    private class isValid{
        private boolean flag;


    }
}
