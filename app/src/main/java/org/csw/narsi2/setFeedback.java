package org.csw.narsi2;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class setFeedback extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DocumentReference docRef = db.collection("users").document("user");
    //현 유저의 document를 참조해야 하므로 document( )에서 ( )안에는 현 유저가 들어가야함


    // 기존의 날씨 피드백을 읽어온다. 유저의 아이디 통해서!
    // 들어온 피드백인 hot인지 cold인지 구별해야 함 구별해서 연산해야 함...?
    // 날씨 피드백을 연산한다(더하기 1을 하든, 빼기 1을 하든!)
    // 연산한 피드백을 다시 저장한다.
    // 스타일 피드백의 경우에는 읽어올 필요 없이 저장 - 그럼 덮어쓰기 되는 거 아닌가? 이 문제 어떻게 해결하지?

  //  Map<String, Object> User = new HashMap<>();
   // User.put("codiPref", );
   // User.put("tempFeed", );
}
