package org.csw.narsi2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import java.util.Random;


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

    private String temperature, city, gu, nowWeather, highestTemp, lowestTemp, wspd, type, precipitation;
    private String pm10Value, pm25Value;
    private String wspd4hour, wspd7hour, wspd10hour, wspd13hour;
    private String type4hour, type7hour, type10hour, type13hour;
    private String temp4hour, temp7hour, temp10hour, temp13hour;
    private int feed_21 = -1;
    private int feed_14 = -1;
    private int feed_7 = -1;
    private int feed_3 = -1;
    private int feed_2 = -1;
    private int feed_1 = -1;
    private Weather weather = new Weather();


    private TextView tv_info, tv_weather, tv_others;
    private ImageView iv_top, iv_bottom, iv_umbrella, iv_mask;
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
    private String targetStyle = "-1";
    private int feedbackSum = 0;
    private int recommendCodi_byFeedback;
    private String avgwspd, avgTemp;
    private String UserTemp;
    private HashMap<String, Top> topMap = new HashMap<>();
    private HashMap<String, Bottom> bottomMap = new HashMap<>();
    private HashMap<String, Top> topMapWeighted = new HashMap<>();
    private HashMap<String, Bottom> bottomMapWeighted = new HashMap<>();
    private HashMap<String, Top> topMapWeighted2 = new HashMap<>();
    private HashMap<String, Bottom> bottomMapWeighted2 = new HashMap<>();
    private Others others_mask, others_umbrella;
    private Codi recommendCodi = new Codi() {
    };
    private int formalWeight, casualWeight, sportyWeight = 0;

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

        iv_top = (ImageView) rootView.findViewById(R.id.imageView_top);
        iv_bottom = (ImageView) rootView.findViewById(R.id.imageView_bottom);
        iv_umbrella = (ImageView) rootView.findViewById(R.id.imageView_umbrella);
        iv_mask = (ImageView) rootView.findViewById(R.id.imageView_mask);

        iv_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTopImageView();
            }
        });
        iv_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBottomImageView();
            }
        });

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
                if (city == null) {
                    city = "서울";
                }
                targetURL2 = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty?sidoName=" + city + "&pageNo=1&numOfRows=1&ServiceKey=" + apiKey2 + "&ver=1.3&_returnType=json";
                gatherTogether();

            }
        }, 500);


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
                        highestTemp = Long.toString(tmaxRound);
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


    public void gatherTogether() {
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, targetURL2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONArray("list").getJSONObject(0);
                    pm10Value = main_object.getString("pm10Value");
                    pm25Value = main_object.getString("pm25Value");


                    weather.setTemperature(temperature);
                    weather.setPm10value(pm10Value);
                    weather.setPm25value(pm25Value);
                    weather.setHighestTemp(highestTemp);
                    weather.setLowestTemp(lowestTemp);
                    weather.setWspd(wspd);
                    weather.setPrecipitation(precipitation);
                    weather.setWaterType(type);

                    avgTemp = String.valueOf((Double.parseDouble(temp4hour) + Double.parseDouble(temp7hour) + Double.parseDouble(temp10hour) + Double.parseDouble(temp13hour)) / 4.0);
                    weather.setAvgTemperatrue(avgTemp);


                    avgwspd = String.valueOf((Double.parseDouble(wspd4hour) + Double.parseDouble(wspd7hour) + Double.parseDouble(wspd10hour) + Double.parseDouble(wspd13hour)) / 4.0);
                    weather.setAvgWindspeed(avgwspd);

                    calculateTemp(preferTemp, temperature, Uid);


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
        getPreference(Uid);
        getFeedback(Uid);

    }

    public void getPreference(String Uid) {
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
                                            Log.d("diffdays", String.valueOf(diffdays));

                                            if (diffdays == 21) {
                                                if (document.getString("codiFeed") != null)
                                                    feed_21 = Integer.parseInt(document.getString("codiFeed"));

                                            } else if (diffdays == 14) {
                                                if (document.getString("codiFeed") != null)

                                                    feed_14 = Integer.parseInt(document.getString("codiFeed"));

                                            } else if (diffdays == 7) {
                                                if (document.getString("codiFeed") != null)

                                                    feed_7 = Integer.parseInt(document.getString("codiFeed"));

                                            } else if (diffdays == 3) {
                                                if (document.getString("codiFeed") != null)
                                                    Log.d("value", document.getString("codiFeed"));
                                                feed_3 = Integer.parseInt(document.getString("codiFeed"));

                                            } else if (diffdays == 2) {
                                                if (document.getString("codiFeed") != null)

                                                    feed_2 = Integer.parseInt(document.getString("codiFeed"));

                                            } else if (diffdays == 1) {
                                                if (document.getString("codiFeed") != null)

                                                    feed_1 = Integer.parseInt(document.getString("codiFeed"));

                                            }

                                        } catch (Exception e) {
                                            Toast.makeText(getContext(), "피드백 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    Log.d("feed_1", String.valueOf(feed_1));
                                    Log.d("feed_2", String.valueOf(feed_2));
                                    Log.d("feed_3", String.valueOf(feed_3));
                                    Log.d("feed_7", String.valueOf(feed_7));
                                    Log.d("feed_14", String.valueOf(feed_14));
                                    Log.d("feed_21", String.valueOf(feed_21));

                                    while(CodiStyle==null){}
                                    analysisFeedback(CodiStyle);
                                    Log.d("casualweight", String.valueOf(casualWeight));
                                    Log.d("formalWeight", String.valueOf(formalWeight));
                                    Log.d("sportyWeight", String.valueOf(sportyWeight));
                                } else {

                                }
                            }
                        });


            }
        }, 100);
    }

    public void calculateTemp(final String preferTemp, String temperature, String Uid) {

        final long tempTemp = Math.round((13.12 + 0.6215 * Double.parseDouble(temperature) - 11.37 * Math.pow((Double.parseDouble(avgwspd) * 3.6), 0.16) + 0.3965 * Math.pow((Double.parseDouble(avgwspd) * 3.6), 0.16) * Double.parseDouble(avgTemp)) * 100) / 100;

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

                        final String preferTemp2 = String.valueOf((Integer.parseInt(preferTemp) + feedbackSum));
                        UserTemp = String.valueOf((int) (tempTemp + Double.parseDouble(preferTemp2)));

                        Log.d("UserTemp : ", UserTemp);

                        getOptimums(UserTemp);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (Integer.parseInt(highestTemp) - Integer.parseInt(lowestTemp) > 10) {
                                    selectOuter();
                                }
                                Log.d("temp_diff", String.valueOf(Integer.parseInt(highestTemp) - Integer.parseInt(lowestTemp)));
                                calculateRecommends();

                                for (Map.Entry<String, Top> elem : topMapWeighted2.entrySet()) {
                                    Log.d("selected top", elem.getKey());
                                }
                                for (Map.Entry<String, Bottom> elem : bottomMapWeighted2.entrySet()) {
                                    Log.d("selected top", elem.getKey());
                                }

                                Log.d("topmapweighted", String.valueOf(topMapWeighted2.size()));
                                Log.d("bottommapweighted", String.valueOf(bottomMapWeighted2.size()));

                                display();
                                displayCodi();


                                layout_waiting.setVisibility(View.GONE);


                            }
                        }, 1000);

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
                                Top clothes_top = document.toObject(Top.class);
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
                            Log.d("topMapsize", String.valueOf(topMap.size()));
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
                                Bottom clothes_bottom = document.toObject(Bottom.class);
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
                            Log.d("bottomMapsize", String.valueOf(bottomMap.size()));
                        } else {
                        }
                    }
                });
        recommendCodi.setTop(topMap);
        recommendCodi.setBottom(bottomMap);

    }

    public void displayCodi() {
        if (Integer.parseInt(pm10Value) > 80 || Integer.parseInt(pm25Value) > 35) {
            others_mask = new Others("mask");
            iv_mask.setImageResource(R.drawable.mask);
        } else {
            iv_mask.setVisibility(View.GONE);
        }

        if (!type4hour.equals("0") || !type7hour.equals("0") || !type10hour.equals("0") || !type13hour.equals("0") || !type.equals("0")) {
            others_umbrella = new Others("umbrella");
            iv_umbrella.setImageResource(R.drawable.umbrella);
        } else {
            iv_umbrella.setVisibility(View.GONE);
        }

        setTopImageView();
        setBottomImageView();

    }

    public void analysisFeedback(String CodiStyle) {

        if (feed_1 == 0) {
            formalWeight += 3;
        } else if (feed_1 == 1) {
            casualWeight += 3;
        } else if (feed_1 == 2) {
            sportyWeight += 3;
        } else if (feed_1 == -1) {
            if (CodiStyle.equals("0")) {
                formalWeight += 3;
            } else if (CodiStyle.equals("1")) {
                casualWeight += 3;
            } else if (CodiStyle.equals("2")) {
                sportyWeight += 3;
            }
        }

        if (feed_2 == 0) {
            formalWeight += 2;
        } else if (feed_2 == 1) {
            casualWeight += 2;
        } else if (feed_2 == 2) {
            sportyWeight += 2;
        } else if (feed_2 == -1) {
            if (CodiStyle.equals("0")) {
                formalWeight += 2;
            } else if (CodiStyle.equals("1")) {
                casualWeight += 2;
            } else if (CodiStyle.equals("2")) {
                sportyWeight += 2;
            }
        }

        if (feed_3 == 0) {
            formalWeight += 1;
        } else if (feed_3 == 1) {
            casualWeight += 1;
        } else if (feed_3 == 2) {
            sportyWeight += 1;
        } else if (feed_3 == -1) {
            if (CodiStyle.equals("0")) {
                formalWeight += 1;
            } else if (CodiStyle.equals("1")) {
                casualWeight += 1;
            } else if (CodiStyle.equals("2")) {
                sportyWeight += 1;
            }
        }

        if (feed_7 == 0) {
            formalWeight += 4;
        } else if (feed_7 == 1) {
            casualWeight += 4;
        } else if (feed_7 == 2) {
            sportyWeight += 4;
        } else if (feed_7 == -1) {
            if (CodiStyle.equals("0")) {
                formalWeight += 4;
            } else if (CodiStyle.equals("1")) {
                casualWeight += 4;
            } else if (CodiStyle.equals("2")) {
                sportyWeight += 4;
            }
        }

        if (feed_14 == 0) {
            formalWeight += 3;
        } else if (feed_14 == 1) {
            casualWeight += 3;
        } else if (feed_14 == 2) {
            sportyWeight += 3;
        } else if (feed_14 == -1) {
            if (CodiStyle.equals("0")) {
                formalWeight += 3;
            } else if (CodiStyle.equals("1")) {
                casualWeight += 3;
            } else if (CodiStyle.equals("2")) {
                sportyWeight += 3;
            }
        }

        if (feed_21 == 0) {
            formalWeight += 2;
        } else if (feed_21 == 1) {
            casualWeight += 2;
        } else if (feed_21 == 2) {
            sportyWeight += 2;
        } else if (feed_21 == -1) {
            if (CodiStyle.equals("0")) {
                formalWeight += 2;
            } else if (CodiStyle.equals("1")) {
                casualWeight += 2;
            } else if (CodiStyle.equals("2")) {
                sportyWeight += 2;
            }
        }

    }

    public void selectTop() {
        int targetMax = Math.max(casualWeight, Math.max(formalWeight, sportyWeight));
        if (targetMax == formalWeight) {
            targetStyle = "0";
        } else if (targetMax == casualWeight) {
            targetStyle = "1";
        } else if (targetMax == sportyWeight) {
            targetStyle = "2";
        }
        while (targetStyle.equals("-1")) ;

        Log.d("targetStyle", targetStyle);

        for (Map.Entry<String, Top> elem : topMap.entrySet()) {
            Log.d("maploop", "processing");
            if (targetStyle.equals("0")) {
                if (elem.getValue().getFormal().equals("1")) {
                    topMapWeighted.put(elem.getKey(), topMap.get(elem.getKey()));
                    Log.d("processing", elem.getKey());
                }
            } else if (targetStyle.equals("1")) {
                if (elem.getValue().getCasual().equals("1")) {
                    topMapWeighted.put(elem.getKey(), topMap.get(elem.getKey()));
                }
            } else if (targetStyle.equals("2")) {
                if (elem.getValue().getSporty().equals("1")) {
                    topMapWeighted.put(elem.getKey(), topMap.get(elem.getKey()));
                }
            }
        }


    }

    public void selectBottom() {
        Log.d("selectBottom", "processing");

        while (targetStyle.equals("-1")) ;
        for (Map.Entry<String, Bottom> elem : bottomMap.entrySet()) {
            Log.d("maploop", "processing");
            if (targetStyle.equals("0")) {
                if (elem.getValue().getFormal().equals("1")) {
                    bottomMapWeighted.put(elem.getKey(), bottomMap.get(elem.getKey()));
                }
            } else if (targetStyle.equals("1")) {
                if (elem.getValue().getCasual().equals("1")) {
                    bottomMapWeighted.put(elem.getKey(), bottomMap.get(elem.getKey()));
                }
            } else if (targetStyle.equals("2")) {
                if (elem.getValue().getSporty().equals("1")) {
                    bottomMapWeighted.put(elem.getKey(), bottomMap.get(elem.getKey()));
                }
            }
        }
    }

    public void selectOuter() {
        for (Map.Entry<String, Top> elem : topMap.entrySet()) {
            if (elem.getValue().getIsOuter().equals("0")) {
                topMap.remove(elem.getKey());
            }
        }
    }

    public void checkSex() {
        Log.d("checkSex", "processing");
        for (Map.Entry<String, Top> elem : topMapWeighted.entrySet()) {
            if (singleuser.getGender().equals("남")) {
                if (!elem.getValue().getSex().equals("2")) {
                    topMapWeighted2.put(elem.getKey(), topMapWeighted.get(elem.getKey()));
                }
            } else if (singleuser.getGender().equals("여")) {
                topMapWeighted2 = topMapWeighted;
            }
        }
        for (Map.Entry<String, Bottom> elem : bottomMapWeighted.entrySet()) {
            if (singleuser.getGender().equals("남")) {
                if (!elem.getValue().getSex().equals("2")) {
                    bottomMapWeighted2.put(elem.getKey(), bottomMapWeighted.get(elem.getKey()));
                }
            } else if (singleuser.getGender().equals("여")) {
                bottomMapWeighted2 = bottomMapWeighted;
            }
        }
    }

    public void setTopImageView() {
        try {
            Random random = new Random();
            int index = random.nextInt(topMapWeighted2.size());
            List keys = new ArrayList<>(topMapWeighted2.keySet());
            String key = keys.get(index).toString();

            Resources res = getResources();
            String mDrawableName = key;
            int resID = res.getIdentifier(mDrawableName, "drawable", getContext().getPackageName());
            Drawable drawable = res.getDrawable(resID);
            iv_top.setImageDrawable(drawable);
        } catch
                (Exception e) {
            Toast.makeText(getContext(), "의류 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void setBottomImageView() {
        try {
            Random random2 = new Random();
            int index2 = random2.nextInt(bottomMapWeighted2.size());
            List keys2 = new ArrayList<>(bottomMapWeighted2.keySet());
            String key2 = keys2.get(index2).toString();

            Resources res2 = getResources();
            String mDrawableName2 = key2;
            int resID2 = res2.getIdentifier(mDrawableName2, "drawable", getContext().getPackageName());
            Drawable drawable2 = res2.getDrawable(resID2);
            iv_bottom.setImageDrawable(drawable2);
        } catch (Exception e) {
            Toast.makeText(getContext(), "의류 정보를 불러올 수 없습니다", Toast.LENGTH_SHORT).show();
        }
    }

    public void calculateRecommends() {

        selectTop();
        selectBottom();
        checkSex();
    }
}
