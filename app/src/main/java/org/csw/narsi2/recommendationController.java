package org.csw.narsi2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.csw.narsi2.loginActivity.loginActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    private ProgressBar progressBar;
    private ConstraintLayout layout_waiting, first, second;

    private String temperature, city, gu, humidity, nowWeather, airPollution, higestTemp, lowestTemp, wspd, wctIndex, type, precipitation;
    private String pm10Value, pm25Value;
    private String wspd4hour, wspd7hour, wspd10hour, wspd13hour;
    private String type4hour, type7hour, type10hour, type13hour;
    private String temp4hour, temp7hour, temp10hour, temp13hour;
    private int feed_21, feed_14, feed_7, feed_3, feed_2, feed_1;
    private Weather weather = new Weather();


    private TextView tv_info, tv_weather, tv_others;
    private String Uid;
    private double lat, lng;
    private ImageView iv_dialog;
    private String targetURL;
    private String targetURL2;
    private String targetURL3;
    private static String apiKey;
    private String apiKey2;
    private User singleuser;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private ImageView iv_left, iv_right;
    private ImageView iv_mimiroom;
    private String CodiStyle, preferTemp;
    private int feedbackSum = 0;
    private int recommendCodi_byFeedback;
    private String avgwspd, avgTemp;
    private String UserTemp;
    private HashMap<String, Clothes> topMap = new HashMap<>();
    private HashMap<String, Clothes> bottomMap = new HashMap<>();


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

        first = (ConstraintLayout) rootView.findViewById(R.id.layout_1);
        second = (ConstraintLayout) rootView.findViewById(R.id.layout_2);
        iv_dialog = (ImageView) rootView.findViewById(R.id.imageView3);
        iv_left = (ImageView) rootView.findViewById(R.id.imageView10);
        iv_right = (ImageView) rootView.findViewById(R.id.imageView11);
        iv_mimiroom = (ImageView) rootView.findViewById(R.id.mimiroom);

        tv_weather = (TextView) rootView.findViewById(R.id.textView5);
        tv_info = (TextView) rootView.findViewById(R.id.textView4);
        tv_others = (TextView) rootView.findViewById(R.id.textView6);

        Intent i = getActivity().getIntent();

        singleuser = (User) i.getSerializableExtra("User");

        iv_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });


        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        layout_waiting = (ConstraintLayout) rootView.findViewById(R.id.layout_waiting);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFF0000FF, PorterDuff.Mode.MULTIPLY);

        final Intent intent = getActivity().getIntent();
        lat = intent.getExtras().getDouble("lat");
        lng = intent.getExtras().getDouble("lng");
        Log.d("HEll", Double.toString(lat) + Double.toString(lng));
        apiKey = "0d4f48e5-5c2c-4bb5-931a-bca3f92b47d0";
        targetURL = "http://api2.sktelecom.com/weather/current/hourly?version=1&lat=" + lat + "&lon=" + lng + "&appkey=" + apiKey;
        targetURL3 = "http://api2.sktelecom.com/weather/forecast/3days?version=1&lat=" + lat + "&lon=" + lng + "&appkey=" + apiKey;
        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();
        Uid = user.getUid();
        db = FirebaseFirestore.getInstance();
        getRecommend(Uid);
        readWeather();
        readWeather2();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                apiKey2 = "k4EQ4%2F5OWx0mMD0gkif7DSIBL4Wr2atojsRiHmY21vl1FcPsUIUusSXeL1xyVGcNAmciyWc3OUQmgmqaH1kPlg%3D%3D";
                targetURL2 = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty?sidoName=" + city + "&pageNo=1&numOfRows=1&ServiceKey=" + apiKey2 + "&ver=1.3&_returnType=json";
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


    public void readWeather() {
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, targetURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONObject("weather").getJSONArray("hourly").getJSONObject(0);
                    // 메인오브젝트 -> weather node의 hourly node

                    // temperature node
                    JSONObject temp = main_object.getJSONObject("temperature");
                    try {
                        String tempCurrent = temp.getString("tc");
                        long tempCurrentRound = Math.round(Double.parseDouble(tempCurrent));
                        temperature = Long.toString(tempCurrentRound);
                        long tmaxRound = Math.round(Double.parseDouble(temp.getString("tmax")));
                        higestTemp = Long.toString(tmaxRound);
                        long tminRound = Math.round(Double.parseDouble(temp.getString("tmin")));
                        lowestTemp = Long.toString(tminRound);

                        JSONObject watertype = main_object.getJSONObject("precipitation");
                        type = watertype.getString("type");
                        precipitation = watertype.getString("sinceOntime");

                        // 바람
                        JSONObject wind = main_object.getJSONObject("wind");
                        wspd = wind.getString("wspd");

                        // 도, 시/구
                        JSONObject whereGu = main_object.getJSONObject("grid");
                        gu = whereGu.getString("county");
                        city = (whereGu.getString("city"));
                        //습도

                        humidity = Long.toString(Math.round(Double.parseDouble(main_object.getString("humidity"))));

                        nowWeather = main_object.getJSONObject("sky").getString("name");

                    } catch (Exception e) {
                        Toast.makeText(getContext(), "날씨 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                    //하늘 상태

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


    public void readWeather2() {
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, targetURL3, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONObject("weather").getJSONArray("forecast3days").getJSONObject(0);
                    // 메인오브젝트 -> weather node의 forecast3days node
                    JSONObject fcst3hour = main_object.getJSONObject("fcst3hour");
                    JSONObject wspdJSON = fcst3hour.getJSONObject("wind");
                    wspd4hour = wspdJSON.getString("wspd4hour");
                    wspd7hour = wspdJSON.getString("wspd7hour");
                    wspd10hour = wspdJSON.getString("wspd10hour");
                    wspd13hour = wspdJSON.getString("wspd13hour");

                    JSONObject precipitationJSON = fcst3hour.getJSONObject("precipitation");
                    type4hour = precipitationJSON.getString("type4hour");
                    type7hour = precipitationJSON.getString("type7hour");
                    type10hour = precipitationJSON.getString("type10hour");
                    type13hour = precipitationJSON.getString("type13hour");
                    Log.d("type4hour", type4hour);
                    Log.d("type7hour", type7hour);
                    Log.d("type10hour", type10hour);
                    Log.d("type13hour", type13hour);


                    JSONObject temperatureJSON = fcst3hour.getJSONObject("temperature");
                    temp4hour = temperatureJSON.getString("temp4hour");
                    temp7hour = temperatureJSON.getString("temp7hour");
                    temp10hour = temperatureJSON.getString("temp10hour");
                    temp13hour = temperatureJSON.getString("temp13hour");


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
                    pm10Value = main_object.getString("pm10Value");
                    pm25Value = main_object.getString("pm25Value");


                    display();
                    weather.setTemperature(temperature);
                    weather.setPm10value(pm10Value);
                    weather.setPm25value(pm25Value);
                    weather.setHighestTemp(higestTemp);
                    weather.setLowestTemp(lowestTemp);
                    weather.setWspd(wspd);
                    weather.setPrecipitation(precipitation);
                    weather.setWaterType(type);

                    avgTemp = String.valueOf((Double.parseDouble(temp4hour) + Double.parseDouble(temp7hour) + Double.parseDouble(temp10hour) + Double.parseDouble(temp13hour)) / 4.0);
                    weather.setAvgTemperatrue(avgTemp);


                    avgwspd = String.valueOf(Double.parseDouble(wspd4hour) + Double.parseDouble(wspd7hour) + Double.parseDouble(wspd10hour) + Double.parseDouble(wspd13hour) / 4.0);
                    weather.setAvgWindspeed(avgwspd);

                    getFeedback(Uid);
                    calculateTemp(Uid);

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


    void show() {
        final List<String> ListItems = new ArrayList<>();
        ListItems.add("선호도 수정");
        ListItems.add("로그아웃");
        final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("메뉴 선택");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                String selectedText = items[pos].toString();
                if (selectedText.equals("선호도 수정")) {
                    Intent intent = new Intent(getContext(), PreferenceController.class);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("User", singleuser);
                    intent.putExtras(bundle);

                    startActivity(intent);
                } else if (selectedText.equals("로그아웃")) {
                    mAuth.signOut();
                    Toast.makeText(getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), loginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        builder.show();
    }


    public void display() {

        first.setBackgroundResource(R.drawable.layout_bg);
        second.setBackgroundResource(R.drawable.layout_bg);
        iv_mimiroom.setImageResource(R.drawable.output);

        if (Integer.parseInt(pm10Value) > 80 || Integer.parseInt(pm25Value) > 35) {
            tv_others.setText("미세먼지가 많아요. 주의하세요");
        } else if (!type4hour.equals("0") || !type7hour.equals("0") || !type10hour.equals("0") || !type13hour.equals("0") || !type.equals("0")) {
            tv_others.setText("우산 챙기세요");
        }

        tv_info.setText(gu + " " + temperature + "°C");

        if (nowWeather.equals("맑음")) {
            tv_weather.setText("오늘은 맑아요");
            iv_left.setImageResource(R.drawable.sunny);
            iv_right.setImageResource(R.drawable.sunny);
        }
        if (nowWeather.equals("구름조금") || nowWeather.equals("구름많음")) {
            tv_weather.setText("오늘은 구름이 많아요");
            iv_left.setImageResource(R.drawable.cloudy);
            iv_right.setImageResource(R.drawable.cloudy);
        }
        if (nowWeather.equals("구름많고 비")) {
            tv_weather.setText("오늘은 구름많고 비가와요");
            iv_left.setImageResource(R.drawable.rainy);
            iv_right.setImageResource(R.drawable.rainy);
        }
        if (nowWeather.equals("구름많고 눈")) {
            tv_weather.setText("오늘은 구름많고 눈이와요");
            iv_left.setImageResource(R.drawable.snowy);
            iv_right.setImageResource(R.drawable.snowy);
        }
        if (nowWeather.equals("구름많고 비 또는 눈")) {
            tv_weather.setText("오늘은 구름많고 짓눈깨비가 와요");
            iv_left.setImageResource(R.drawable.rainy);
            iv_right.setImageResource(R.drawable.rainy);
        }
        if (nowWeather.equals("흐림")) {
            tv_weather.setText("오늘은 흐려요");
            iv_left.setImageResource(R.drawable.cloudy);
            iv_right.setImageResource(R.drawable.cloudy);
        }
        if (nowWeather.equals("흐리고 비")) {
            tv_weather.setText("오늘은 흐리고 비가와요");
            iv_left.setImageResource(R.drawable.rainy);
            iv_right.setImageResource(R.drawable.rainy);
        }
        if (nowWeather.equals("흐리고 눈")) {
            tv_weather.setText("오늘은 흐리고 눈이와요");
            iv_left.setImageResource(R.drawable.snowy);
            iv_right.setImageResource(R.drawable.snowy);
        }
        if (nowWeather.equals("흐리고 비 또는 눈")) {
            tv_weather.setText("오늘은 흐리고 짓눈깨비가 와요");
            iv_left.setImageResource(R.drawable.rainy);
            iv_right.setImageResource(R.drawable.rainy);
        }
        if (nowWeather.equals("흐리고 낙뢰")) {
            tv_weather.setText("오늘은 흐리고 천둥번개가 쳐요");
            iv_left.setImageResource(R.drawable.thunder);
            iv_right.setImageResource(R.drawable.thunder);
        }
        if (nowWeather.equals("뇌우/비")) {
            tv_weather.setText("오늘은 비바람이 불어요");
            iv_left.setImageResource(R.drawable.rainy);
            iv_right.setImageResource(R.drawable.rainy);

        }
        if (nowWeather.equals("뇌우/눈")) {
            tv_weather.setText("오늘은 절대 나가지 마세요");
            iv_left.setImageResource(R.drawable.thunder);
            iv_right.setImageResource(R.drawable.snowy);
        }
        if (nowWeather.equals("뇌우/비 또는 눈")) {
            tv_weather.setText("오늘은 절대 나가지 마세요");
            iv_left.setImageResource(R.drawable.thunder);
            iv_right.setImageResource(R.drawable.snowy);
        }


    }


    public void getRecommend(final String Uid) {
        DocumentReference docRef = db.collection("user_final").document(Uid);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.getString("tempFeed") != null) {

                        preferTemp = document.getString("tempFeed");
                        Log.d("preferTemp", preferTemp);

                    }
                } else {

                }
            }
        });

        DocumentReference docRef2 = db.collection("user_final").document(Uid);
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.getString("codiPref") != null) {
                        CodiStyle = document.getString("codiPref");

                    }
                } else {

                }
            }
        });
    }

    public void getFeedback(final String Uid) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                long now = System.currentTimeMillis();
                final Date date = new Date(now);
                String getTimeYMD = new SimpleDateFormat("yyyy-MM-dd").format(date);

                db.collection("user_final").document(Uid).collection("Feedback")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        try {
                                            String targetDate = document.getId();
                                            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");

                                            Date date2 = transFormat.parse(targetDate);
                                            long diff = date.getTime() - date2.getTime();
                                            long diffdays = diff / (24 * 60 * 60 * 1000);
                                            Log.d("diffdays",String.valueOf(diffdays));

                                            if (diffdays == 21) {
                                                feed_21 = Integer.parseInt(document.getString("codiFeed"));

                                            } else if (diffdays == 14) {
                                                feed_14 = Integer.parseInt(document.getString("codiFeed"));

                                            } else if (diffdays == 7) {
                                                feed_7 = Integer.parseInt(document.getString("codiFeed"));

                                            } else if (diffdays == 3) {
                                                feed_3 = Integer.parseInt(document.getString("codiFeed"));

                                            } else if (diffdays == 2) {
                                                feed_2 = Integer.parseInt(document.getString("codiFeed"));

                                            } else if (diffdays == 1) {
                                                feed_1 = Integer.parseInt(document.getString("codiFeed"));

                                            }

                                        } catch (Exception e) {
                                            Toast.makeText(getContext(),"피드백 정보를 불러올 수 없습니다.",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {

                                }
                            }
                        });


            }
        }, 0);
    }

    public void calculateTemp(String Uid) {

        final long tempTemp = Math.round((13.12 + 0.6215 * Double.parseDouble(temperature) - 11.37 * Math.pow((Double.parseDouble(avgwspd) * 3.6),0.16) + 0.3965 * Math.pow((Double.parseDouble(avgwspd) * 3.6),0.16) * Double.parseDouble(avgTemp))*100)/100;

        Log.d("avgwspd", avgwspd);
        Log.d("avgtemp", avgTemp);
        Log.d("tempTemp", String.valueOf(tempTemp));

        db.collection("user_final").document(Uid).collection("Feedback")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                feedbackSum += Integer.parseInt(document.getString("tempFeed"));

                            }
                        } else {
                            Toast.makeText(getContext(), "cant open feedback", Toast.LENGTH_SHORT).show();
                        }
                        Log.d("feedbacksum", String.valueOf(feedbackSum));

                        preferTemp = String.valueOf((Integer.parseInt(preferTemp) + feedbackSum));
                        UserTemp = String.valueOf((int)(tempTemp + Double.parseDouble(preferTemp)));

                        Log.d("UserTemp : ", UserTemp);
                        getOptimums(UserTemp);
                    }
                });


    }

    public void getOptimums(final String temp) {
        db.collection("Clothes_top")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Clothes clothes_top = document.toObject(Clothes.class);
                                if (!clothes_top.getTemp().get(0).equals("") && clothes_top.getTemp().get(1).equals("")) {
                                    if (Integer.parseInt(clothes_top.getTemp().get(0)) <= Integer.parseInt(temp)) {
                                        topMap.put(document.getId(), clothes_top);

                                    }
                                } else if (!clothes_top.getTemp().get(1).equals("") && clothes_top.getTemp().get(0).equals("")) {
                                    if (Integer.parseInt(clothes_top.getTemp().get(1)) >= Integer.parseInt(temp)) {
                                        topMap.put(document.getId(), clothes_top);
                                    }
                                } else if (!clothes_top.getTemp().get(0).equals("") && !clothes_top.getTemp().get(1).equals("")) {
                                    if (Integer.parseInt(clothes_top.getTemp().get(1)) >= Integer.parseInt(temp) && Integer.parseInt(clothes_top.getTemp().get(0)) <= Integer.parseInt(temp)) {
                                        topMap.put(document.getId(), clothes_top);
                                    }
                                }

                            }
                        } else {
                        }
                    }
                });
        db.collection("Clothes_bottom")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Clothes clothes_bottom = document.toObject(Clothes.class);
                                if (!clothes_bottom.getTemp().get(0).equals("") && clothes_bottom.getTemp().get(1).equals("")) {
                                    if (Integer.parseInt(clothes_bottom.getTemp().get(0)) <= Integer.parseInt(temp)) {
                                        bottomMap.put(document.getId(), clothes_bottom);
                                    }
                                } else if (!clothes_bottom.getTemp().get(1).equals("") && clothes_bottom.getTemp().get(0).equals("")) {
                                    if (Integer.parseInt(clothes_bottom.getTemp().get(1)) >= Integer.parseInt(temp)) {
                                        bottomMap.put(document.getId(), clothes_bottom);
                                    }
                                } else if (!clothes_bottom.getTemp().get(0).equals("") && !clothes_bottom.getTemp().get(1).equals("")) {
                                    if (Integer.parseInt(clothes_bottom.getTemp().get(1)) >= Integer.parseInt(temp) && Integer.parseInt(clothes_bottom.getTemp().get(0)) <= Integer.parseInt(temp)) {
                                        bottomMap.put(document.getId(), clothes_bottom);
                                    }
                                }

                            }
                        } else {
                        }
                    }
                });
    }

}
