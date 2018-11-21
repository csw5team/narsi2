package org.csw.narsi2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.csw.narsi2.loginActivity.loginActivity;

import java.util.HashMap;
import java.util.Map;

public class personalizingInfo extends AppCompatActivity {

    private ToggleButton button_casual, button_sporty, button_formal, imhot, imcold, idontknow;
    private int casual, sporty, formal = 0;
    private double hot, cold = 0.0;
    private Button saveButton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalizing_info);

        setbutton();
        setbutton2();

        savecondition();


    }

    public void setbutton() {
        button_casual = (ToggleButton) findViewById(R.id.buttoncasual);
        button_formal = (ToggleButton) findViewById(R.id.buttonformal);
        button_sporty = (ToggleButton) findViewById(R.id.buttonsporty);

        button_casual.setText("캐쥬얼");
        button_casual.setTextOn("캐쥬얼");
        button_casual.setTextOff("캐쥬얼");

        button_casual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    casual = 1;
                    button_formal.setChecked(false);
                    button_sporty.setChecked(false);
                } else {
                    casual = 0;

                }
            }
        });
        button_sporty.setText("스포티");
        button_sporty.setTextOn("스포티");
        button_sporty.setTextOff("스포티");
        button_sporty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sporty = 1;
                    button_casual.setChecked(false);
                    button_formal.setChecked(false);

                } else {
                    sporty = 0;

                }
            }
        });
        button_formal.setText("포멀");
        button_formal.setTextOn("포멀");
        button_formal.setTextOff("포멀");
        button_formal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    formal = 1;
                    button_casual.setChecked(false);
                    button_sporty.setChecked(false);

                } else {
                    formal = 0;

                }
            }
        });
    }

    public void setbutton2() {
        imhot = (ToggleButton) findViewById(R.id.imhot);
        imcold = (ToggleButton) findViewById(R.id.imcold);
        idontknow = (ToggleButton) findViewById(R.id.idontknow);

        imhot.setText("더위를 탄다");
        imhot.setTextOff("더위를 탄다");
        imhot.setTextOn("더위를 탄다");
        imcold.setText("추위를 탄다");
        imcold.setTextOn("추위를 탄다");
        imcold.setTextOff("추위를 탄다");
        idontknow.setText("잘 모르겠다");
        idontknow.setTextOff("잘 모르겠다");
        idontknow.setTextOn("잘 모르겠다");

        imhot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    hot = 0.3;
                    imcold.setChecked(false);
                    idontknow.setChecked(false);
                } else {
                    hot = 0.0;
                }
            }
        });
        imcold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cold = 0.3;
                    imhot.setChecked(false);
                    idontknow.setChecked(false);
                } else {
                    cold = 0;
                }
            }
        });
        idontknow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    imhot.setChecked(false);
                    imcold.setChecked(false);
                } else {

                }
            }
        });
    }

    public void savecondition() {

        saveButton = (Button) findViewById(R.id.savebutton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> data = new HashMap<>();

                data.put("casual", casual);
                data.put("sporty", sporty);
                data.put("formal", formal);
                data.put("hot", hot);
                data.put("cold", cold);
                data.put("checked", true);

                if (user != null) {
                    final String uid = user.getUid();

                    DocumentReference Ref = db.collection("users").document(uid);

                    Ref
                            .set(data, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(personalizingInfo.this, "저장되었습니다", Toast.LENGTH_SHORT).show();
                                    personalizingInfo.super.onBackPressed();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(personalizingInfo.this, "저장실패", Toast.LENGTH_SHORT).show();
                                }
                            });

                }


            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
}