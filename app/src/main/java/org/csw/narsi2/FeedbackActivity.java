package org.csw.narsi2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a;
                //setFeedback(); 피드백을 firebase에 입력하는 부분
            }
        };

        Button btn_hot =(Button) findViewById(R.id.button_hot);
        Button btn_cold=(Button) findViewById(R.id. button_cold);
        Button btn_good=(Button) findViewById(R.id.button_good);
    }

}
