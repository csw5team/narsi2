package org.csw.narsi2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import static java.sql.Types.NULL;

public class FeedbackActivity extends AppCompatActivity {

    double update_temp = 0;
    double recent_temp;
    int update_codi;
    Feedback update_feedback = new Feedback();
    private ToggleButton btn_hot,btn_cold,btn_good,btn_casual,btn_sporty,btn_formal,btn_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        btn_hot = (ToggleButton) findViewById(R.id.buttonhot);
        btn_cold = (ToggleButton) findViewById(R.id.button_cold);
        btn_good = (ToggleButton) findViewById(R.id.button_good);
        btn_casual = (ToggleButton) findViewById(R.id.button_casual);
        btn_sporty = (ToggleButton) findViewById(R.id.button_sporty);
        btn_formal = (ToggleButton) findViewById(R.id.button_formal);
        btn_finish = (ToggleButton) findViewById(R.id.finished);

        //Activity 들어와서 class로 넘어가도록

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (update_temp == NULL && update_codi == NULL) {
                    Toast.makeText(getApplicationContext(), "한 개 이상의 피드백을 제출하셔야 합니다", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(FeedbackActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        btn_hot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(FeedbackActivity.this, "Done", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recent_temp = update_feedback.getTempFeedback();
        recent_temp += update_temp;
        update_feedback.setStatus(update_codi, recent_temp);
    }

}
