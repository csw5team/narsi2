package org.csw.narsi2;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static java.sql.Types.NULL;

public class FeedbackActivity extends AppCompatActivity {

    private int update_temp = -1;
    private int recent_temp;
    private int update_codi = -1;
    private User user;
    //private Feedback update_feedback = user.getFeedback();
    private ToggleButton btn_hot, btn_cold, btn_good, btn_casual, btn_sporty, btn_formal;
    private Feedback feedback;
    private Button btn_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedback = new Feedback();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("User").document("EETLkwTqY");
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            recent_temp = Integer.valueOf(document.getString("tempFeed"));
                        } else {

                        }
                    }
                });


            }
        }, 500);

        while (feedback == null) {
        }


        btn_hot = (ToggleButton) findViewById(R.id.button_hot);
        btn_cold = (ToggleButton) findViewById(R.id.button_cold);
        btn_good = (ToggleButton) findViewById(R.id.button_good);
        btn_casual = (ToggleButton) findViewById(R.id.button_casual);
        btn_sporty = (ToggleButton) findViewById(R.id.button_sporty);
        btn_formal = (ToggleButton) findViewById(R.id.button_formal);
        btn_finish = (Button) findViewById(R.id.finished);

        //Activity 들어와서 class로 넘어가도록

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (update_temp == -1 && update_codi == -1) {
                    Toast.makeText(getApplicationContext(), "한 개 이상의 피드백을 제출하셔야 합니다", Toast.LENGTH_LONG).show();

                } else {
                    recent_temp += update_temp;
                    feedback.setDb(recent_temp);
                }
            }
        });

        btn_hot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_cold.setChecked(false);
                    btn_good.setChecked(false);
                    update_temp = 1;
                }
            }
        });

        btn_cold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_hot.setChecked(false);
                    btn_good.setChecked(false);
                    update_temp = -1;
                }
            }
        });

        btn_good.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_hot.setChecked(false);
                    btn_cold.setChecked(false);
                    update_temp = 0;
                }
            }
        });

        btn_casual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_sporty.setChecked(false);
                    btn_formal.setChecked(false);
                    update_codi = 1;
                }
            }
        });


        btn_formal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_sporty.setChecked(false);
                    btn_casual.setChecked(false);
                    update_codi = 0;
                }
            }
        });


        btn_sporty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_casual.setChecked(false);
                    btn_formal.setChecked(false);
                    update_codi = 2;
                }
            }
        });


    }

}
