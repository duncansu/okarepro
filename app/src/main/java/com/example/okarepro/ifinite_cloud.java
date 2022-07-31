package com.example.okarepro;

import static java.lang.Thread.sleep;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class ifinite_cloud extends Service {

    private RequestQueue queue;
    Timer timer;
    String result,result1,result2,result3;



    @Override
    public void onCreate() {
        super.onCreate();
        queue= Volley.newRequestQueue(this);
    }
    private class updatetsk extends TimerTask {

        @Override
        public void run() {
            StringRequest stringRequest=new StringRequest(
                    Request.Method.GET,
                    "http://api.thingspeak.com/channels/1679756/feeds/last.json?api_key=CLPA3ECDNZAYXERC&timezone=Asia/Taipei",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println(response);
                            parsejson(response);
                        }
                    },null
            );
            queue.add(stringRequest);
            StringRequest stringRequest1=new StringRequest(
                    Request.Method.GET,
                    //最新
                    "https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-D0047-061?Authorization=CWB-5A6D106A-0F59-4313-BF57-365693C0339A&format=JSON",


//
//                    "https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-D0047-061?Authorization=CWB-1079762D-608A-4B35-B1B2-0D0208F80C47&format=JSON",
                    //"https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-D0047-061?Authorization=CWB-48D21A76-4D45-4BBB-AFB5-E7DAEA8E9714&format=JSON",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println(response);
                            parsejson1(response);
                        }
                    },null
            );
            queue.add(stringRequest1);

            Intent intent =new Intent("fromService");
            intent.putExtra("result",result);
            intent.putExtra("result1",result1);
            intent.putExtra("result2",result2);
            intent.putExtra("result3",result3);
            sendBroadcast(intent);


        }
    }


    public int onStartCommand(Intent intent,int flags,int startID){
        timer=new Timer();
        timer.schedule(new updatetsk(),0,2000);

        return super.onStartCommand(intent,flags,startID);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void parsejson(String aa){
        try {
            JSONObject json=new JSONObject(aa);
           result=json.getString("field1");
           result1=json.getString("field2");


        }catch (Exception e){
            System.out.print(e);
        }
    }

    private void parsejson1(String aa){
        try {
            JSONObject json=new JSONObject(aa);
            JSONObject recods=json.getJSONObject("records");
            JSONArray locations=recods.getJSONArray("locations");
            JSONObject newa=locations.getJSONObject(0);
            JSONArray location=newa.getJSONArray("location");
            JSONObject newb=location.optJSONObject(0);
            JSONArray weatherElement=newb.getJSONArray("weatherElement");

            JSONObject newc=weatherElement.getJSONObject(0);
            JSONArray time=newc.getJSONArray("time");
            JSONObject newc1=time.getJSONObject(0);
            JSONArray elementValue=newc1.getJSONArray("elementValue");
            JSONObject newc2=elementValue.getJSONObject(0);
             result2=newc2.getString("value");

            JSONObject newd=weatherElement.getJSONObject(3);
            JSONArray time1=newd.getJSONArray("time");
            JSONObject newd1=time1.getJSONObject(0);
            JSONArray elementValue1=newd1.getJSONArray("elementValue");
            JSONObject newd2=elementValue1.getJSONObject(0);
             result3=newd2.getString("value");


        }catch (Exception e){
            System.out.print(e);
        }
    }





}