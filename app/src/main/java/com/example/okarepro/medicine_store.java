package com.example.okarepro;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

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

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!isGpsAble(locationManager)) {
                    Toast.makeText(medicine_store.this, "請打開GPS", Toast.LENGTH_SHORT).show();
                }
                if (ContextCompat.checkSelfPermission(medicine_store.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {//未開啟定位權限
                    //開啟定位權限,200是標識碼
                    ActivityCompat.requestPermissions(medicine_store.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
                } else {
                    startLocation();
                    Toast.makeText(medicine_store.this, "已開啟定位權限", Toast.LENGTH_LONG).show();
                }

                double myla=Double.parseDouble(startLocation().split(",")[1]);
                double mylo=Double.parseDouble(startLocation().split(",")[3]);
                double stla=121.570639840024;
                double stlo=25.001390217006;

                //System.out.print(String.valueOf(gps2m(myla,mylo,stla,stlo))+"m\n");
            }

            //定義一個更新顯示的方法
            private String updateShow(Location location) {
                if (location != null) {
                    String sb = new String();
                    sb+=("經度：" +","+ location.getLongitude() + ","+"緯度：" +","+ location.getLatitude());
                    //System.out.println(sb);
                    return sb;
                } else {System.out.print("bye bye");
                return "bye bye";
                }

            }

            @SuppressLint("MissingPermission")
            private String startLocation() {
                //從GPS獲取最近的定位資訊
                Location lc = getLastKnownLocation();
                String aa=updateShow(lc);
                //設定間隔兩秒獲得一次GPS定位資訊
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 8, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        // 當GPS定位資訊發生改變時，更新定位
                        updateShow(location);
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        // 當GPS LocationProvider可用時，更新定位
                        updateShow(locationManager.getLastKnownLocation(provider));
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        updateShow(null);
                    }
                });
                return aa;
            }

            private Location getLastKnownLocation() {

                locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                List<String> providers = locationManager.getProviders(true);
                Location bestLocation = null;
                for (String provider : providers) {
                    if (ActivityCompat.checkSelfPermission(medicine_store.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(medicine_store.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    }
                    Location l = locationManager.getLastKnownLocation(provider);
                    if (l == null) {
                        continue;
                    }
                    if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                        // Found best last known location: %s", l);
                        bestLocation = l;
                    }
                }
                return bestLocation;
            }

            private boolean isGpsAble(LocationManager lm) {
                return lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) ? true : false;
            }

            private final double EARTH_RADIUS = 6378137.0;
            private double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
                double radLat1 = (lat_a * Math.PI / 180.0);
                double radLat2 = (lat_b * Math.PI / 180.0);
                double a = radLat1-radLat2;
                double b = (lng_a-lng_b) * Math.PI / 180.0;
                double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)+
                        Math.cos(radLat1) * Math.cos(radLat2)
                                * Math.pow(Math.sin(b / 2), 2)));
                s = s * EARTH_RADIUS;
                s = Math.round(s * 10000) / 10000;
                return s;
            }







            public String readTextFile(InputStreamReader inputStream) throws IOException {
                    BufferedReader br=new BufferedReader(inputStream);
                   // BufferedReader br1=new BufferedReader(inputStream);
                    String aa="";
                    while ((aa=br.readLine())!=null){
                        String item[] = aa.split(",");
                        /** 讀取 **/
                        String  data1= item[1];
                        String  data2= item[4];
                        String  data3= item[5];
                        if(gps2m(Double.parseDouble(startLocation().split(",")[1]),Double.parseDouble(startLocation().split(",")[3]),Double.parseDouble(data2),Double.parseDouble(data3))<1800){
                        System.out.print(data1+"\t"+ data2+"\t"+ data3+"\n");}
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



}