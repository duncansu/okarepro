package com.example.okarepro;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class medicine_store extends AppCompatActivity {
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicinestore);

        ImageButton seekstore = (ImageButton) findViewById(R.id.seekstore);
        seekstore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                InputStream XmlFileInputStream = getResources().openRawResource(R.raw.data);
                InputStreamReader aa = null;
                try {
                    aa = new InputStreamReader(XmlFileInputStream, "MS950");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String sxml = null;
                try {
                    sxml = (readTextFile(aa));
                    System.out.print(sxml);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                //偵測手機經緯度

                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
                    if (ActivityCompat.checkSelfPermission(medicine_store.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(medicine_store.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, medicine_store.this);
                String localProvider = "";
                Location location = locationManager.getLastKnownLocation(localProvider);
                if (location != null){
                    String address = "緯度："+location.getLatitude()+"經度："+location.getLongitude();
                    System.out.print(address);
                }
                else {
                    System.out.print("bye bye");
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, mListener);
                   locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, mListener);
                }

            }


            public String readTextFile(InputStreamReader inputStream) throws IOException {
                    BufferedReader br=new BufferedReader(inputStream);
                    String aa="";
                    while ((aa=br.readLine())!=null){
                        String item[] = aa.split(",");
                        /** 讀取 **/
                        String  data1= item[0].trim();
                        String  data2= item[1].trim();
                        String  data3= item[2].trim();
                        //System.out.print(data1+"\t"+ data2+"\t"+ data3+"\n");
                    }
                    br.close();
                    return aa;
                }

        });

        ImageButton back2 = (ImageButton) findViewById(R.id.back2);
        back2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setClass(medicine_store.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left,R.anim.slide_left);
            }
        });
    }


    LocationListener mListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            String address = "緯度：" + location.getLatitude() + "經度：" + location.getLongitude();
            System.out.print(address);
        }
    };


}