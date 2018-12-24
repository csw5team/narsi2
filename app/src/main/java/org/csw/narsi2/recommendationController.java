package org.csw.narsi2;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.csw.narsi2.loginActivity.loginActivity;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link recommendationController.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link recommendationController#newInstance} factory method to
 * create an instance of this fragment.
 */
public class recommendationController extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button logOut, dbcheck, pageChange;
    private TextView tv_city, tv_personalizing, tv_tempNow, tv_wetRatio, tv_airPollution;
    private ProgressBar progressBar;
    private ConstraintLayout layout_waiting;

    private String temperature, city, gu, humidity, nowWeather, airPollution, tmax, tmin, wspd, wctIndex;
    private Weather weather = Weather.getInstance();

    private double lat, lng;
    private String targetURL;
    private String targetURL2;
    private static String apiKey;
    private String apiKey2;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public recommendationController() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CodiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static recommendationController newInstance(String param1, String param2) {
        recommendationController fragment = new recommendationController();
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
        View rootView = inflater.inflate(R.layout.recommendstyleform, container, false);
        logOut = (Button) rootView.findViewById(R.id.logOut);
        dbcheck = (Button) rootView.findViewById(R.id.dbcheck);
        pageChange = (Button) rootView.findViewById(R.id.pagechangebutton);
        tv_city = (TextView) rootView.findViewById(R.id.city);
        tv_personalizing = (TextView) rootView.findViewById(R.id.personalizing);
        tv_tempNow = (TextView) rootView.findViewById(R.id.temp);
        tv_wetRatio = (TextView) rootView.findViewById(R.id.wetRatio);
        tv_airPollution = (TextView) rootView.findViewById(R.id.airPollution);

        Intent i = getActivity().getIntent();

        final User singleuser = (User)i.getSerializableExtra("User");


        pageChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PreferenceController.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("User",singleuser);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(v.getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), loginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        dbcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DBcheck.class);
                startActivity(intent);
            }
        });

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        layout_waiting = (ConstraintLayout) rootView.findViewById(R.id.layout_waiting);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFF0000FF, PorterDuff.Mode.MULTIPLY);

        final Intent intent = getActivity().getIntent();
        lat = intent.getExtras().getDouble("lat");
        lng = intent.getExtras().getDouble("lng");
        Log.d("HEll", Double.toString(lat)+ Double.toString(lng));
        apiKey = "0d4f48e5-5c2c-4bb5-931a-bca3f92b47d0";
        targetURL = "http://api2.sktelecom.com/weather/current/hourly?version=1&lat=" + lat + "&lon=" + lng + "&appkey=" + apiKey;

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        find_weather();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                apiKey2 = "k4EQ4%2F5OWx0mMD0gkif7DSIBL4Wr2atojsRiHmY21vl1FcPsUIUusSXeL1xyVGcNAmciyWc3OUQmgmqaH1kPlg%3D%3D";
                targetURL2 = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty?sidoName=" + city + "&pageNo=1&numOfRows=1&ServiceKey=" + apiKey2 + "&ver=1.3&_returnType=json";find_airPollution();
                find_airPollution();

            }
        }, 1000);


        return rootView;
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

    public void find_weather() {
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, targetURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONObject("weather").getJSONArray("hourly").getJSONObject(0);
                    // 메인오브젝트 -> weather node의 hourly node

                    // temperature node
                    JSONObject temp = main_object.getJSONObject("temperature");
                    String tempCurrent = temp.getString("tc");
                    long tempCurrentRound = Math.round(Double.parseDouble(tempCurrent));
                    temperature = Long.toString(tempCurrentRound);
                    long tmaxRound = Math.round(Double.parseDouble(temp.getString("tmax")));
                    tmax = Long.toString(tmaxRound);
                    long tminRound = Math.round(Double.parseDouble(temp.getString("tmin")));
                    tmin = Long.toString(tminRound);

                    // 바람
                    JSONObject wind = main_object.getJSONObject("wind");
                    wspd = wind.getString("wspd");

                    // 도, 시/구
                    JSONObject whereGu = main_object.getJSONObject("grid");
                    gu = whereGu.getString("county");
                    city = (whereGu.getString("city"));
                    //습도
                    try {
                        humidity = Long.toString(Math.round(Double.parseDouble(main_object.getString("humidity"))));
                    }
                    catch (Exception e){
                        humidity = "0";
                    }
                        //하늘 상태
                    nowWeather = main_object.getJSONObject("sky").getString("name");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(jor);
    }
    public void find_airPollution() {
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, targetURL2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONArray("list").getJSONObject(0);
                    String pm10Grade = main_object.getString("pm10Grade");

                    setAirPollution(pm10Grade);

                    layout_waiting.setVisibility(View.GONE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(jor);

    }

    public void setAirPollution(String value) {
        switch (value) {
            case "1":
                this.airPollution = "좋음";
                break;
            case "2":
                this.airPollution = "보통";
                break;
            case "3":
                this.airPollution = "나쁨";
                break;
            case "4":
                this.airPollution = "매우 나쁨";
        }

        tv_airPollution.setText("미세먼지 : " + airPollution);
        tv_city.setText(gu);
        tv_personalizing.setText(nowWeather);
        tv_tempNow.setText("현재온도 : " + temperature + "ºC");
        tv_wetRatio.setText("현재습도 : " + humidity + "%");

    }

}
