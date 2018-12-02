package org.csw.narsi2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Feedback {
    private int tempFeedback ;
    private int styleFeedback;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    final String uid = user.getUid();

    public Feedback() {
    this.tempFeedback = 2020202020;
    }

    public void getTempFeedbackFromDB() {
        DocumentReference docRef = db.collection("User").document("EETLkwTqY");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

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

    public void setDb(int value) {
        if (user != null) {
            // Name, email address, and profile photo Url
            //String name = user.getDisplayName();

            // Check if user's email is verified
            // boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();


            // 아래에 user1.put("이름", 객체) 순으로 넣으면 firebase DB에 쓰기 가능

            Map<String, Object> user1 = new HashMap<>();
            user1.put("tempFeed", String.valueOf(value));


            db.collection("User").document("EETLkwTqY")
                    .set(user1, SetOptions.merge())
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


