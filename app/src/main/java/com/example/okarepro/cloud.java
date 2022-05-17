package com.example.okarepro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class cloud extends AppCompatActivity {
    private ImageButton getdata,newgetdata;
    private RequestQueue queue;
    private TextView test1,test2,test3,test4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud);
//        Intent intent = new Intent();
//        intent.setClass(cloud.this,ifinite_cloud.class);
//        startService(intent);
        test1=(TextView)findViewById(R.id.testmsg);
        test2=(TextView)findViewById(R.id.testhumid);
        test3=(TextView)findViewById(R.id.text3);
        test4=(TextView)findViewById(R.id.text4);
        queue= Volley.newRequestQueue(this);
        getdata=(ImageButton) findViewById(R.id.aa);
        getdata.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
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
                }
                private void parsejson(String aa){
                try {
                    JSONObject json=new JSONObject(aa);
                    String result=json.getString("field1");
                    String result1=json.getString("field2");
                    test1.setText(result+"度");
                    test2.setText(result1+"%");

                }catch (Exception e){
                  System.out.print(e);
                }
                }

            }
        );


        newgetdata=(ImageButton) findViewById(R.id.bb);
        newgetdata.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                StringRequest stringRequest=new StringRequest(
                        Request.Method.GET,
                        "https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-D0047-061?Authorization=CWB-48D21A76-4D45-4BBB-AFB5-E7DAEA8E9714&format=JSON",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println(response);
                                parsejson(response);
                            }
                            },null
                );
                queue.add(stringRequest);
            }
            private void parsejson(String aa){
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
                    String result=newc2.getString("value");

                    JSONObject newd=weatherElement.getJSONObject(3);
                    JSONArray time1=newd.getJSONArray("time");
                    JSONObject newd1=time1.getJSONObject(0);
                    JSONArray elementValue1=newd1.getJSONArray("elementValue");
                    JSONObject newd2=elementValue1.getJSONObject(0);
                    String result1=newd2.getString("value");
                    test3.setText(result1+"度");
                    test4.setText(result+"%");

                }catch (Exception e){
                    System.out.print(e);
                }
            }

        }
        );

    }
}

//中央氣象台url
//https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-D0047-061?Authorization=CWB-48D21A76-4D45-4BBB-AFB5-E7DAEA8E9714&format=JSON