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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PreferenceController extends AppCompatActivity {

    private ToggleButton button_casual, button_sporty, button_formal, imhot, imcold, idontknow;
    private boolean isCheck_casual,isCheck_formal,isCheck_sporty,isCheck_hot,isCheck_cold,isCheck_know = false;
    private int CodiStyle = 10;
    private int temp = 10;
    private Button bt_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setpreferenceform);

        setbutton();
        setbutton2();

        Intent intent = getIntent();
        User singleuser = (User) intent.getSerializableExtra("User");
        final String Uid = FirebaseAuth.getInstance().getUid();

        Preference preference = getPreference(Uid);
        singleuser.addPreference(preference);

        bt_save = (Button) findViewById(R.id.savebutton);

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPreference(Uid, CodiStyle, temp);

            }
        });


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
                    CodiStyle = 1;
                    button_formal.setChecked(false);
                    button_sporty.setChecked(false);
                    isCheck_casual = true;
                } else {
                    isCheck_casual = false;
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
                    CodiStyle = 2;
                    button_casual.setChecked(false);
                    button_formal.setChecked(false);
                    isCheck_sporty = true;

                } else {
                    isCheck_sporty = false;

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
                    CodiStyle = 0;
                    button_casual.setChecked(false);
                    button_sporty.setChecked(false);
                    isCheck_formal = true;

                } else {
                    isCheck_formal = false;

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
                    temp = 2;
                    imcold.setChecked(false);
                    idontknow.setChecked(false);
                    isCheck_hot = true;
                } else {
                    isCheck_hot = false;
                }
            }
        });
        imcold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    temp = -2;
                    imhot.setChecked(false);
                    idontknow.setChecked(false);
                    isCheck_cold = true;
                } else {
                    isCheck_cold = false;
                }
            }
        });
        idontknow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    temp = 0;
                    imhot.setChecked(false);
                    imcold.setChecked(false);
                    isCheck_know = true;
                } else {
                    isCheck_know = false;
                }
            }
        });
    }


    public void setPreference(String Uid, int CodiStyle, int temp) {
        if (!((isCheck_casual||isCheck_formal||isCheck_sporty)&&(isCheck_cold||isCheck_hot||isCheck_know))) {
            Toast.makeText(PreferenceController.this, "모두 선택해주세요", Toast.LENGTH_SHORT).show();
        } else {

            Intent intent = getIntent();

            Preference preference = new Preference(CodiStyle, temp); //CreatePreference (생성자를 사용)
            User singleuser = (User) intent.getSerializableExtra("User");
            singleuser.addPreference(preference);
            singleuser.getPreference().savePreference(Uid);

            Toast.makeText(PreferenceController.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();


            Intent intent2 = new Intent(PreferenceController.this, getLatLng.class);

            Bundle bundle = new Bundle();
            bundle.putSerializable("User", singleuser);
            intent2.putExtras(bundle);

            startActivity(intent2);
            finish();
        }
    }

    public Preference getPreference(String Uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("user_final").document(Uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.getString("tempFeed") != null) {

                        temp = Integer.valueOf(document.getString("tempFeed"));
                        if (temp > 0) {
                            imhot.setChecked(true);
                        } else if (temp < 0) {
                            imcold.setChecked(true);
                        } else if (temp == 0) {
                            idontknow.setChecked(true);
                        }
                    }
                } else {

                }
            }
        });

        DocumentReference docRef2 = db.collection("user_final").document(Uid);
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.getString("codiPref") != null) {
                        CodiStyle = Integer.valueOf(document.getString("codiPref"));
                        if (CodiStyle == 0) {
                            button_formal.setChecked(true);
                        } else if (CodiStyle == 1) {
                            button_casual.setChecked(true);
                        } else if (CodiStyle == 2) {
                            button_sporty.setChecked(true);
                        }
                    }
                } else {

                }
            }
        });

        Preference preference = new Preference(CodiStyle, temp);
        return preference;

    }
}