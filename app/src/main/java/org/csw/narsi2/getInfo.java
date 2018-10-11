package org.csw.narsi2;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class getInfo extends AppCompatActivity {

    private double lat, lng;
    private String cityString = "";
    private String targetURL, targetURL2;
    private String apiKey, apiKey2;
    private String temp2, whereGu2, humidity3, nowWeather2, airPollution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getinfo);

        Intent intent = getIntent();
        lat = intent.getExtras().getDouble("lat");
        lng = intent.getExtras().getDouble("lng");

        apiKey = "0d4f48e5-5c2c-4bb5-931a-bca3f92b47d0";
        targetURL = "http://api2.sktelecom.com/weather/current/hourly?version=1&lat=" + lat + "&lon=" + lng + "&appkey=" + apiKey;


        find_weather(targetURL);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent1 = new Intent(getInfo.this, MainActivity.class);
                intent1.putExtra("temp", temp2);
                intent1.putExtra("whereGu", whereGu2);
                intent1.putExtra("humidity", humidity3);
                intent1.putExtra("nowWeather", nowWeather2);
                intent1.putExtra("airPollution", airPollution);
                getInfo.this.startActivity(intent1);
                getInfo.this.finish();
            }
        }, 3000);


    }

    public void find_weather(String url) {
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONObject("weather").getJSONArray("hourly").getJSONObject(0);
                    JSONObject temp = main_object.getJSONObject("temperature");
                    String tempCurrent = temp.getString("tc");
                    long tempCurrentRound = Math.round(Double.parseDouble(tempCurrent));
                    temp2 = Long.toString(tempCurrentRound);

                    JSONObject whereGu = main_object.getJSONObject("grid");
                    whereGu2 = whereGu.getString("county");
                    setCityString(whereGu.getString("city"));

                    String humidity = main_object.getString("humidity");
                    long humidity2 = Math.round(Double.parseDouble(humidity));
                    humidity3 = Long.toString(humidity2);

                    JSONObject nowWeather = main_object.getJSONObject("sky");
                    nowWeather2 = nowWeather.getString("name");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                apiKey2 = "k4EQ4%2F5OWx0mMD0gkif7DSIBL4Wr2atojsRiHmY21vl1FcPsUIUusSXeL1xyVGcNAmciyWc3OUQmgmqaH1kPlg%3D%3D";
                targetURL2 = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty?sidoName=" + cityString + "&pageNo=1&numOfRows=1&ServiceKey=" + apiKey2 + "&ver=1.3&_returnType=json";
                find_airPollution(targetURL2);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);
    }

    public void find_airPollution(String url) {
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONArray("list").getJSONObject(0);
                    String pm10Grade = main_object.getString("pm10Grade");

                    setAirPollution(pm10Grade);


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
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);
    }

    public void setCityString(String value) {
        this.cityString = value;
    }

    public void setAirPollution(String value) {
        switch (value) {
            case "1":
                this.airPollution = "미세먼지 : 좋음";
                break;
            case "2":
                this.airPollution = "미세먼지 : 보통";
                break;
            case "3":
                this.airPollution = "미세먼지 : 나쁨";
                break;
            case "4":
                this.airPollution = "미세먼지 : 매우 나쁨";
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Remove the activity when its off the screen

        finish();
    }
}
