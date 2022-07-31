package com.example.okarepro;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class healthractivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.healthy_bind);

        TextView img = (TextView) findViewById(R.id.diet_view1);
        img.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.hpa.gov.tw/Pages/List.aspx?nodeid=40"));
                startActivity(intent);
            }
        });
        TextView img1 = (TextView) findViewById(R.id.textView19);
        img1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.cdc.gov.tw/Disease/SubIndex/N6XvFa1YP9CXYdB0kNSA9A"));
                startActivity(intent);
            }
        });
        TextView img2 = (TextView) findViewById(R.id.textView20);
        img2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.mohw.gov.tw/cp-88-212-1.html"));
                startActivity(intent);
            }
        });
        TextView img3 = (TextView) findViewById(R.id.textView21);
        img3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://health.mdic.ncku.edu.tw/%E9%86%AB%E8%AD%B7%E5%88%86%E4%BA%AB/42.html"));
                startActivity(intent);
            }
        });
        TextView back1 = (TextView) findViewById(R.id.back1);
        back1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setClass(healthractivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left,R.anim.slide_left);
            }
        });


    }



}
