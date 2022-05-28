package com.example.okarepro;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class medicine_store extends AppCompatActivity {
    private LocationManager locationManager;
    private ListView listView;
    private static final int REQUEST_FINE_LOCATION_PERMISSION = 102;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicinestore);

        ImageButton seekstore = (ImageButton) findViewById(R.id.seekstore);
        seekstore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputStreamReader aa = null;
                try {

                    InputStream XmlFileInputStream = getResources().openRawResource(R.raw.data);
                    aa = null;

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


            }

            //定義一個更新顯示的方法
            private String updateShow(Location location) {
                if (location != null) {
                    String sb = new String();
                    sb += ("經度：" + "," + location.getLongitude() + "," + "緯度：" + "," + location.getLatitude());
                    //System.out.println(sb);
                    return sb;
                } else {
                    System.out.print("bye bye");
                    return "bye bye";
                }

            }

            @SuppressLint("MissingPermission")
            private String startLocation() {


                if (ActivityCompat.checkSelfPermission(medicine_store.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(medicine_store.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    requestLocationPermission(); // 詢問使用者開啟權限
                }
                //從GPS獲取最近的定位資訊
                Location lc = getLastKnownLocation();
                String aa = updateShow(lc);
                //設定間隔兩秒獲得一次GPS定位資訊

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, new LocationListener() {
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

            private void requestLocationPermission() {
                // 如果裝置版本是6.0（包含）以上
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 取得授權狀態，參數是請求授權的名稱
                    int hasPermission = checkSelfPermission(
                            Manifest.permission.ACCESS_FINE_LOCATION);

                    // 如果未授權
                    if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                        // 請求授權
                        //     第一個參數是請求授權的名稱
                        //     第二個參數是請求代碼
                        requestPermissions(
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_FINE_LOCATION_PERMISSION);
                    } else {
                        // 啟動地圖與定位元件

                    }
                }
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


            private final double EARTH_RADIUS = 6378137.0;

            private double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
                double radLat1 = (lat_a * Math.PI / 180.0);
                double radLat2 = (lat_b * Math.PI / 180.0);
                double a = radLat1 - radLat2;
                double b = (lng_a - lng_b) * Math.PI / 180.0;
                double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                        Math.cos(radLat1) * Math.cos(radLat2)
                                * Math.pow(Math.sin(b / 2), 2)));
                s = s * EARTH_RADIUS;
                s = Math.round(s * 10000) / 10000;
                return s;
            }


            public String readTextFile(InputStreamReader inputStream) throws IOException {
                ProgressDialog dialog = ProgressDialog.show(medicine_store.this, "讀取中"
                        , "請稍候", true);
//
                new Thread(() -> {

                    Looper.prepare();
                    double a0=Double.parseDouble(startLocation().split(",")[1]);
                    double a1=Double.parseDouble(startLocation().split(",")[3]);
                    BufferedReader br = new BufferedReader(inputStream);
                    String aa = "";
                    while (true) {
                        try {
                            if (!((aa = br.readLine()) != null)) break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String item[] = aa.split(",");
                        /** 讀取 **/
                        String data0 = item[0];
                        String data1 = item[1];
                        String data2 = item[2];
                        String data3 = item[3];
                        String data4 = item[4];
                        String data5 = item[5];
                        if (gps2m(a0,a1, Double.parseDouble(data4), Double.parseDouble(data5)) < 1850) {
                            System.out.print(data1 + "\t" + data4 + "\t" + data5 + "\n");
                            String[] from = {"store", "address", "phone", "yes/no"};
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put(from[0], data1);
                            hashMap.put(from[1], data2);
                            hashMap.put(from[2], data3);
                            arrayList.add(hashMap);

                        }
                    }
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(() -> {
                        dialog.dismiss();
                        RecyclerView recyclerView;
                        MyAdapter myAdapter;
                        recyclerView = findViewById(R.id.rv);
                        recyclerView.setLayoutManager(new LinearLayoutManager(medicine_store.this));
                        recyclerView.addItemDecoration(new DividerItemDecoration(medicine_store.this, DividerItemDecoration.VERTICAL));
                        myAdapter = new MyAdapter();
                        recyclerView.setAdapter(myAdapter);

                    });
                    Looper.loop();


                }).start();
                return null;
            }

        });


        ImageButton back2 = (ImageButton) findViewById(R.id.back2);
        back2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(medicine_store.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left, R.anim.slide_left);
            }
        });
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView store1, address1, phone1, yes1;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                store1 = itemView.findViewById(R.id.store1);
                address1 = itemView.findViewById(R.id.address1);
                phone1 = itemView.findViewById(R.id.phone1);
                yes1 = itemView.findViewById(R.id.yes1);
            }
        }

        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.store1.setText(arrayList.get(position).get("store"));
            holder.address1.setText("地址：" + arrayList.get(position).get("address"));
            holder.phone1.setText("電話：" + arrayList.get(position).get("phone"));
            holder.yes1.setText("健保：" + arrayList.get(position).get("yes/no"));


        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }


}