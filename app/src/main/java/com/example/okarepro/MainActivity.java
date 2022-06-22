package com.example.okarepro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageButton cloud, help, healthybutton, dial1, dial2, medicinestore,settingbutton;

    private AlertDialog alert;
    private Timer timerl;
    private TextView contactperson1,contactperson2;
    FirebaseDatabase rootNode;
    DatabaseReference reference, r1;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    private RequestQueue queue;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    //測試用資料集



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp=getSharedPreferences("contactperson",MODE_PRIVATE);
        Intent intent = getIntent();
        contactperson1=(TextView)findViewById(R.id.contactperson1);
        contactperson2=(TextView)findViewById(R.id.contactperson2);
        contactperson1.setText(sp.getString("TextPersonName1",""));
        contactperson2.setText(sp.getString("TextPersonName2",""));



        settingbutton=(ImageButton)findViewById(R.id.settingbutton);
        settingbutton.setOnClickListener(new View.OnClickListener() {
                                             public void onClick(View view) {
                                                 Intent intent1 = new Intent();
                                                 intent1.setClass(MainActivity.this, setting.class);
                                                 startActivity(intent1);

                                             }
                                         }
        );


        help = (ImageButton) findViewById(R.id.main_btn_7);
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
                reference = rootNode.getReference("WARNING_MESSAGE/older_side/sos");
                reference.setValue(true);
                r1 = rootNode.getReference("WARNING_MESSAGE/older_side/datetime");
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
        healthybutton = (ImageButton) findViewById(R.id.main_btn_8);
        healthybutton.setOnClickListener(new View.OnClickListener() {
                                             public void onClick(View view) {
                                                 Intent intent1 = new Intent();
                                                 intent1.setClass(MainActivity.this, healthractivity.class);
                                                 startActivity(intent1);

                                             }
                                         }
        );
//        cloud = (ImageButton) findViewById(R.id.main_btn_10);
//        cloud.setOnClickListener(new View.OnClickListener() {
//                                     public void onClick(View view) {
//
//                                         Intent intent = new Intent();
//                                         intent.setClass(MainActivity.this, cloud.class);
//                                         startActivity(intent);
//
//                                     }
//                                 }
//        );


        medicinestore = (ImageButton) findViewById(R.id.main_btn_9);
        medicinestore.setOnClickListener(new View.OnClickListener() {
                                             public void onClick(View view) {
                                                 Intent intent = new Intent();
                                                 intent.setClass(MainActivity.this, medicine_store.class);
                                                 startActivity(intent);

                                             }
                                         }
        );


        dial1 = (ImageButton) findViewById(R.id.contact1);
        dial1.setOnClickListener(new View.OnClickListener() {


                                     public void onClick(View view) {
                                         String phoneNumber = sp.getString("TextPersonPhone1","");;
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

        dial2 = (ImageButton) findViewById(R.id.contact2);
        dial2.setOnClickListener(new View.OnClickListener() {
                                     public void onClick(View view) {

                                         String phoneNumber1 = sp.getString("TextPersonPhone2","");;
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

}