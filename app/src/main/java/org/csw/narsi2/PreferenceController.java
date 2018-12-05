package org.csw.narsi2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class PreferenceController extends AppCompatActivity {

    private ToggleButton button_casual, button_sporty, button_formal, imhot, imcold, idontknow;
    private int CodiStyle = 10;
    private int temp = 10;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setpreferenceform);

        setbutton();
        setbutton2();


        saveButton = (Button) findViewById(R.id.savebutton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CodiStyle == 10 || temp == 10) {
                    Toast.makeText(PreferenceController.this, "모두 선택해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = getIntent();



                    Preference preference = new Preference(CodiStyle, temp); //CreatePreference (생성자를 사용)
                    User singleuser = (User) intent.getSerializableExtra("User");
                    singleuser.setPreference(preference);
                    preference.savePreference();

                    Toast.makeText(PreferenceController.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();


                    Intent intent2 = new Intent(PreferenceController.this, getLatLng.class);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("User", singleuser);
                    intent2.putExtras(bundle);

                    startActivity(intent2);
                    finish();
                }
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
                } else {
                    CodiStyle = 10;
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

                } else {
                    CodiStyle = 10;
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

                } else {
                    CodiStyle = 10;
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
                    temp = 1;
                    imcold.setChecked(false);
                    idontknow.setChecked(false);
                } else {
                    temp = 10;
                }
            }
        });
        imcold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    temp = -1;
                    imhot.setChecked(false);
                    idontknow.setChecked(false);
                } else {
                    temp = 10;

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
                } else {
                    temp = 10;

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