package org.csw.narsi2;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Feedback {
    private double tempFeedback;
    private int styleFeedback;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String uid = user.getUid();
    double temp;

    public Feedback() {
    }

    public Feedback(double tempFeedback, int styleFeedback) {
        this.styleFeedback = styleFeedback;
        this.tempFeedback = tempFeedback;
    }
    public void getTempFeedbackFromDB(){
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        temp = document.getDouble("tempFeed");
                        setTempFeedback(temp);
                    }
                } else {
                }
            }
        });
    }
    public double getTempFeedback() {

        return this.tempFeedback;
    }

    public int getStyleFeedback() {
        return styleFeedback;
    }

    public void setTempFeedback(double tempFeedback) {

        this.tempFeedback = tempFeedback;
    }

    public void setStyleFeedback(int styleFeedback) {
        this.styleFeedback = styleFeedback;
    }

    public void setStatus(int styleFeedback, double tempFeedback) {
        this.tempFeedback = tempFeedback;
        this.styleFeedback = styleFeedback;
    }


}
