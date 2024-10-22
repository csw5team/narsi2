package org.csw.narsi2;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class Feedback implements Serializable {
    private int tempFeedback ;
    private int styleFeedback;

    public Feedback() {
    }

    public void getTempFeedbackFromDB() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String uid = user.getUid();


        DocumentReference docRef = db.collection("user_final").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    int temp = Integer.parseInt(document.getString("tempFeed"));
                    setTempFeedback(tempFeedback);
                } else {

                }
            }
        });

    }

    public int getTempFeedback() {

        return this.tempFeedback;
    }

    public int getStyleFeedback() {
        return styleFeedback;
    }

    public void setTempFeedback(int tempFeedback) {

        this.tempFeedback = tempFeedback;
    }

    public void setStyleFeedback(int styleFeedback) {
        this.styleFeedback = styleFeedback;
    }

    public void setStatus(int styleFeedback, int tempFeedback) {
        this.tempFeedback = tempFeedback;
        this.styleFeedback = styleFeedback;
    }

    public void setFeedback(String Uid, int CodiFeedback, int update_temp) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (user != null) {
            // Name, email address, and profile photo Url
            //String name = user.getDisplayName();

            // Check if user's email is verified
            // boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.


            // 아래에 user1.put("이름", 객체) 순으로 넣으면 firebase DB에 쓰기 가능

            Map<String, Object> data = new HashMap<>();
            data.put("codiFeed", String.valueOf(CodiFeedback));
            data.put("tempFeed",String.valueOf(update_temp));


            long now = System.currentTimeMillis();
            Date date = new Date(now);
            String getTimeYMD = new SimpleDateFormat("yyyy-MM-dd").format(date);

            db.collection("user_final").document(Uid).collection("Feedback").document(getTimeYMD)
                    .set(data, SetOptions.merge())
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
    }


}


