package com.example.okarepro;

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

import org.json.JSONObject;

public class cloud extends AppCompatActivity {
    private ImageButton getdata;
    private RequestQueue queue;
    private TextView test1,test2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud);
        test1=(TextView)findViewById(R.id.testmsg);
        test2=(TextView)findViewById(R.id.testhumid);
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
    }
}

//中央氣象台url
//https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-D0047-061?Authorization=CWB-48D21A76-4D45-4BBB-AFB5-E7DAEA8E9714&format=JSON