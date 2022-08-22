package com.example.okarepro;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageButton cloud, settingbutton;
    private TextView test1, test2, test3, test4;
    private TextView help;
    private myReceiver myreceiver1;
    LogoutButton logoutButton;
    ImageView healthybutton, medicinestore, dial1, dial2, cloud1;
    List<Drawable> drawableList = new ArrayList<Drawable>();//存放圖片

    private AlertDialog alert;
    private Timer timerl;
    private TextView contactperson1, contactperson2;
    FirebaseDatabase rootNode;
    DatabaseReference reference, r1;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    private RequestQueue queue;
    private SharedPreferences sp,sp1;
    private SharedPreferences.Editor editor;
    String key;

    //測試用資料集
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logoutButton = new LogoutButton(findViewById(R.id.logout), this);
//        cloud 宣告
        test1 = (TextView) findViewById(R.id.textView9);
        test2 = (TextView) findViewById(R.id.textView10);
        test3 = (TextView) findViewById(R.id.textView11);
        test4 = (TextView) findViewById(R.id.textView8);
        queue = Volley.newRequestQueue(this);
        drawableList.add(getResources().getDrawable(R.drawable.sun));//圖片01
        drawableList.add(getResources().getDrawable(R.drawable.ic_baseline_cloud));//圖片02
        settingbutton = (ImageButton) findViewById(R.id.setting);
        settingbutton.setOnClickListener(new View.OnClickListener() {
                                             public void onClick(View view) {
                                                 Intent intent1 = new Intent();
                                                 intent1.setClass(MainActivity.this, setting.class);
                                                 startActivity(intent1);

                                             }
                                         }
        );


        sp = getSharedPreferences("contactperson", MODE_PRIVATE);
        sp1=getSharedPreferences("setting",MODE_PRIVATE);
        Intent intent = getIntent();
        contactperson1 = (TextView) findViewById(R.id.textView12);
        contactperson2 = (TextView) findViewById(R.id.textView13);
        contactperson1.setText(sp.getString("TextPersonName1", ""));
        contactperson2.setText(sp.getString("TextPersonName2", ""));
        key =sp1.getString("product_key",null);
        Log.d("Tag", "here: "+key);
        help = (TextView) findViewById(R.id.textView14);
        help.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {
                                        Toast toast = Toast.makeText(MainActivity.this, "若要使用SOS請長按「SOS」方格", Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0);

                                        toast.show();
                                    }
                                }
        );
//長按SOS觸發對話框
        help.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference(key+"/older_side/sos");


                reference.setValue(true);
                r1 = rootNode.getReference(key+"/older_side/datetime");
                Date date = new Date(System.currentTimeMillis());
                r1.setValue(formatter.format(date));
                AlertDialog.Builder alertDialog =
                        new AlertDialog.Builder(MainActivity.this);
                alertDialog.setMessage("SOS警示系統已開啟");
                alertDialog.setPositiveButton("關閉", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(), "關閉", Toast.LENGTH_SHORT).show();
                        Date date = new Date(System.currentTimeMillis());
                        reference.setValue(false);
                        r1.setValue(formatter.format(date));
                    }
                });
                alertDialog.setCancelable(false);
                alertDialog.show();
                return true;
            }
        });
        healthybutton = (ImageView) findViewById(R.id.imageView4);
        healthybutton.setOnClickListener(new View.OnClickListener() {
                                             public void onClick(View view) {
                                                 Intent intent1 = new Intent();
                                                 intent1.setClass(MainActivity.this, healthractivity.class);
                                                 startActivity(intent1);

                                             }
                                         }
        );



        medicinestore = (ImageView) findViewById(R.id.imageView3);
        medicinestore.setOnClickListener(new View.OnClickListener() {
                                             public void onClick(View view) {
                                                 Intent intent = new Intent();
                                                 intent.setClass(MainActivity.this, medicine_store.class);
                                                 startActivity(intent);

                                             }
                                         }
        );


        dial1 = (ImageView) findViewById(R.id.newphone1);
        dial1.setOnClickListener(new View.OnClickListener() {


                                     public void onClick(View view) {
                                         String phoneNumber = sp.getString("TextPersonPhone1", "");
                                         ;
                                         Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                                         String aa = "tel:" + phoneNumber;
                                         phoneIntent.setData(Uri.parse(aa));
                                         try {
                                             startActivity(phoneIntent);
                                             finish();
                                             System.out.print("Finished making a call...");
                                         } catch (android.content.ActivityNotFoundException ex) {
                                             Toast.makeText(MainActivity.this,
                                                     "Call faild, please try again later.", Toast.LENGTH_SHORT).show();
                                         }

                                     }
                                 }
        );

        dial2 = (ImageView) findViewById(R.id.newphone2);
        dial2.setOnClickListener(new View.OnClickListener() {
                                     public void onClick(View view) {

                                         String phoneNumber1 = sp.getString("TextPersonPhone2", "");
                                         ;
                                         Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                                         String aa = "tel:" + phoneNumber1;
                                         phoneIntent.setData(Uri.parse(aa));
                                         try {
                                             startActivity(phoneIntent);
                                             finish();
                                             System.out.print("Finished making a call...");
                                         } catch (android.content.ActivityNotFoundException ex) {
                                             Toast.makeText(MainActivity.this,
                                                     "Call faild, please try again later.", Toast.LENGTH_SHORT).show();
                                         }

                                     }
                                 }
        );


    }


        public void onStart() {
        super.onStart();
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, ifinite_cloud.class);
        startService(intent);
        myreceiver1 = new myReceiver();
        IntentFilter filter = new IntentFilter("fromService");
        registerReceiver(myreceiver1, filter);

    }

    private class myReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {

            String result = intent.getStringExtra("result");
            String result1 = intent.getStringExtra("result1");
            String result2 = intent.getStringExtra("result2");
            String result3 = intent.getStringExtra("result3");
            test1.setText(result + "°C");
            test2.setText(result1 + "%");
            test4.setText(result2 + "%");
            test3.setText(result3 + "°C");

            if (!TextUtils.isEmpty(result2) && TextUtils.isDigitsOnly(result2)) {
                int aa = Integer.parseInt(result2);
                if (aa <20) {
                    cloud1 = (ImageView) findViewById(R.id.iconcloud);
                    cloud1.setImageResource(R.drawable.sun);
                }
                else if(aa<=40&&aa>=20){
                    cloud1 = (ImageView) findViewById(R.id.iconcloud);
                    cloud1.setImageResource(R.drawable.ic_baseline_cloud);
                }
                else{

                    cloud1 = (ImageView) findViewById(R.id.iconcloud);
                    cloud1.setImageResource(R.drawable.cloud_bad);


                }


            }

        }

    }
}

