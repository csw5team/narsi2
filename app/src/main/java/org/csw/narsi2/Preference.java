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
    private boolean check = false;

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

    public void savePreference(final String Uid) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        Map<String, Object> data = new HashMap<>();

        data.put("tempFeed", String.valueOf(temp));
        data.put("codiPref", String.valueOf(CodiStyle));

        data.put("checked", true);

        if (user != null) {

            DocumentReference Ref = db.collection("user_final").document(Uid);

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
        check = true;

    }

    public Preference getPreference(final String Uid) {

        if (check) {
            return this;
        } else {
            return null;

        }

    }
}
