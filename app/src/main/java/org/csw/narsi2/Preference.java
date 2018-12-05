package org.csw.narsi2;

import android.os.Handler;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class Preference implements Serializable {

    private int CodiStyle;
    private int temp;

    public Preference() {
    }

    public Preference(int CodiStyle, int temp) {
        this.CodiStyle = CodiStyle;
        this.temp = temp;
    }

    public int getCodiStyle() {
        return CodiStyle;
    }

    public void setCodiStyle(int codiStyle) {
        CodiStyle = codiStyle;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public void savePreference() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();


        Map<String, Object> data = new HashMap<>();

        data.put("tempFeed", String.valueOf(temp));
        data.put("codiPref", String.valueOf(CodiStyle));

        data.put("checked", true);

        if (user != null) {

            DocumentReference Ref = db.collection("user_final").document(uid);

            Ref
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

    public void getPreference() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("user_final").document(uid);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            setTemp(Integer.valueOf(document.getString("tempFeed")));
                        } else {

                        }
                    }
                });

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                String getTimeYMD = new SimpleDateFormat("yyyy-MM-dd").format(date);

                DocumentReference docRef2 = db.collection("user_final").document(uid).collection("codiFeedback").document(getTimeYMD);
                docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.getString("codiFeed") != null) {
                                setCodiStyle(Integer.valueOf(document.getString("codiFeed")));
                            }
                        } else {

                        }
                    }
                });


            }
        }, 0);

    }
}
