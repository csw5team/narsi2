package org.csw.narsi2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeedbackController.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeedbackController#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedbackController extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int update_temp = 100;
    private int recent_temp;
    private int update_codi = 100;
    //private Feedback update_feedback = user.getFeedback();
    private ToggleButton btn_hot, btn_cold, btn_good, btn_casual, btn_sporty, btn_formal;
    private boolean isCheck_hot, isCheck_cold, isCheck_good, isCheck_casual, isCheck_sporty, isCheck_formal = false;
    private Feedback feedback;
    private TextView tv_weather;
    private Button btn_finish;
    private String Uid = FirebaseAuth.getInstance().getUid();

    private OnFragmentInteractionListener mListener;

    public FeedbackController() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedBackFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedbackController newInstance(String param1, String param2) {
        FeedbackController fragment = new FeedbackController();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.setfeedbackform, container, false);
        feedback = new Feedback();
        tv_weather = (TextView) view.findViewById(R.id.weather_FD_text);

        Intent i = getActivity().getIntent();
        final User singleuser = (User) i.getSerializableExtra("User");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                String getTimeYMD = new SimpleDateFormat("yyyy-MM-dd").format(date);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("user_final").document(Uid).collection("Feedback").document(getTimeYMD);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String name = document.getString("name");
                            tv_weather.setText("오늘 날씨가 " + singleuser.getName() + "님께 어땠나요?");

                            if (document.getString("tempFeed") != null) {
                                recent_temp = Integer.valueOf(document.getString("tempFeed"));
                                if (recent_temp > 0) {
                                    btn_hot.setChecked(true);
                                } else if (recent_temp < 0) {
                                    btn_cold.setChecked(true);
                                } else {
                                    btn_good.setChecked(true);

                                }
                            }
                            if (document.getString("codiFeed") != null) {
                                update_codi = Integer.valueOf(document.getString("codiFeed"));
                                if (update_codi == 0) {
                                    btn_formal.setChecked(true);
                                } else if (update_codi == 1) {
                                    btn_casual.setChecked(true);
                                } else if (update_codi == 2) {
                                    btn_sporty.setChecked(true);
                                }
                            }
                        } else {

                        }


                    }
                });


            }
        }, 0);


        btn_hot = (ToggleButton) view.findViewById(R.id.button_hot);
        btn_cold = (ToggleButton) view.findViewById(R.id.button_cold);
        btn_good = (ToggleButton) view.findViewById(R.id.button_good);
        btn_casual = (ToggleButton) view.findViewById(R.id.button_casual);
        btn_sporty = (ToggleButton) view.findViewById(R.id.button_sporty);
        btn_formal = (ToggleButton) view.findViewById(R.id.button_formal);
        btn_finish = (Button) view.findViewById(R.id.finished);

        //Activity 들어와서 class로 넘어가도록

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFeedback(Uid, update_codi, update_temp);
            }
        });

        btn_hot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_cold.setChecked(false);
                    btn_good.setChecked(false);
                    update_temp = 1;
                    isCheck_hot = true;
                } else {
                    isCheck_hot = false;
                }
            }
        });

        btn_cold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_hot.setChecked(false);
                    btn_good.setChecked(false);
                    update_temp = -1;
                    isCheck_cold = true;
                } else {
                    isCheck_cold = false;
                }
            }
        });

        btn_good.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_hot.setChecked(false);
                    btn_cold.setChecked(false);
                    update_temp = 0;
                    isCheck_good = true;
                } else {
                    isCheck_good = false;
                }
            }
        });

        btn_casual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_sporty.setChecked(false);
                    btn_formal.setChecked(false);
                    update_codi = 1;
                    isCheck_casual = true;
                } else {
                    isCheck_casual = false;
                }
            }
        });


        btn_formal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_sporty.setChecked(false);
                    btn_casual.setChecked(false);
                    update_codi = 0;
                    isCheck_formal = true;
                } else {
                    isCheck_formal = false;
                }
            }
        });


        btn_sporty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_casual.setChecked(false);
                    btn_formal.setChecked(false);
                    update_codi = 2;
                    isCheck_sporty = true;
                } else {
                    isCheck_sporty = false;
                }
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void setFeedback(String Uid, int CodiFeedback, int update_temp) {
        if (!((isCheck_formal || isCheck_casual || isCheck_sporty) || (isCheck_hot || isCheck_cold || isCheck_good))) {
            Toast.makeText(getActivity(), "한 개 이상의 피드백을 제출하셔야 합니다", Toast.LENGTH_LONG).show();

        } else if ((!isCheck_cold && !isCheck_hot && !isCheck_good)) {
            feedback.setFeedback(Uid, CodiFeedback, update_temp);
            Toast.makeText(getActivity(), "저장되었습니다.", Toast.LENGTH_SHORT).show();

        } else {
            recent_temp += update_temp;
            feedback.setFeedback(Uid, CodiFeedback, update_temp);
            Toast.makeText(getActivity(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
