package com.example.okarepro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
    private ImageButton back;
    private RequestQueue queue;
    private TextView test1,test2,test3,test4;
    private myReceiver myreceiver,m1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud);


        Intent intent=new Intent();
        intent.setClass(cloud.this,ifinite_cloud.class);
        startService(intent);

        test1=(TextView)findViewById(R.id.testmsg);
        test2=(TextView)findViewById(R.id.testhumid);
        test3=(TextView)findViewById(R.id.text3);
        test4=(TextView)findViewById(R.id.text4);
        queue= Volley.newRequestQueue(this);

        back=(ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(cloud.this,ifinite_cloud.class);
                stopService(intent);
                Intent intent1 = new Intent();
                intent1.setClass(cloud.this,MainActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_left,R.anim.slide_left);

            }
        });



    }

    public void onStart(){
        super.onStart();
        myreceiver=new myReceiver();
        IntentFilter filter=new IntentFilter("fromService");
        registerReceiver(myreceiver,filter);

    }
    private class myReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        String result=intent.getStringExtra("result");
        String result1=intent.getStringExtra("result1");
        String result2=intent.getStringExtra("result2");
        String result3=intent.getStringExtra("result3");

            test1.setText(result+"度");
            test2.setText(result1+"%");
            test4.setText(result2+"%");
            test3.setText(result3+"度");

        }
    }

}

//中央氣象台url
//https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-D0047-061?Authorization=CWB-48D21A76-4D45-4BBB-AFB5-E7DAEA8E9714&format=JSON