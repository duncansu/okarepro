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

public class MainActivity extends AppCompatActivity  {

    private List<Msg> msgList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageButton cloud,help,healthybutton;
    private MyAdapter adapter;
    private AlertDialog alert;
    private Timer timerl;
    FirebaseDatabase rootNode;
    DatabaseReference reference,r1;
    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    private RequestQueue queue;

    //測試用資料集
    private LinkedList<HashMap<String,String>> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化聊天室
        msgList.add(new Msg("請問有甚麼需要幫忙的嗎?",Msg.TYPE_RECEIVED));

        recyclerView=findViewById(R.id.recycLerview);
        recyclerView.setHasFixedSize(true);
        // recycleview 裡面都要設定控管排版的LayoutManager
        mLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        //將自訂義好的Adapter 接上recycleView 的Adapter
        adapter=new MyAdapter(msgList);
        recyclerView.setAdapter(adapter);
        help=(ImageButton)findViewById(R.id.main_btn_7);
        help.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Toast toast = Toast.makeText(MainActivity.this, "若要使用SOS請長按「SOS」方格", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);

                toast.show();
            }
            }
        );
//長按SOS觸發對話框
       help.setOnLongClickListener(new View.OnLongClickListener(){
            public boolean onLongClick(View view){
                rootNode=FirebaseDatabase.getInstance();
                reference=rootNode.getReference("WARNING_MESSAGE/older_side/sos");
                reference.setValue(true);
                r1=rootNode.getReference("WARNING_MESSAGE/older_side/datetime");
                Date date = new Date(System.currentTimeMillis());
                r1.setValue(formatter.format(date));
                AlertDialog.Builder alertDialog =
                        new AlertDialog.Builder(MainActivity.this);
                alertDialog.setMessage("SOS警示系統已開啟");
                alertDialog.setPositiveButton("關閉", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(),"關閉",Toast.LENGTH_SHORT).show();
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
        healthybutton=(ImageButton)findViewById(R.id.main_btn_8);
        healthybutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent1 = new Intent();
                intent1.setClass(MainActivity.this, healthractivity.class);
                startActivity(intent1);

            }
        }
        );
        cloud = (ImageButton)findViewById(R.id.main_btn_10);
        cloud.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(MainActivity.this,cloud.class);
                startActivity(intent);

                }
            }
        );




    }





    //做Adapter 的部分
    public class Msg {
        public static final int TYPE_RECEIVED = 0;
        public static final int TYPE_SENT = 1;
        private String content;
        private int type;
        public Msg(String content, int type){
            this.content = content;
            this.type = type;
        }
        public String getContent(){
            return content;
        }
        public int getType(){
            return type;
        }
    }
    //自訂義myadapter
    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
        private List<Msg> mMsgList;
        //屬性中間的間接
        class MyViewHolder extends RecyclerView.ViewHolder{
            // public View mTextView;
            //public TextView title,date;
            LinearLayout leftlayout;
            LinearLayout rightlayout;
            TextView leftMsg;
            TextView rightMsg;
            TextView nameTxt, messageTxt,msgtext;
            public MyViewHolder(View v){
                //mTextView=v;
                super(v);
                leftlayout=v.findViewById(R.id.left_layout);
                rightlayout=v.findViewById(R.id.right_layout);
                leftMsg=v.findViewById(R.id.receivedTxt);
                rightMsg=v.findViewById(R.id.sentTxt);
            }
        }
        public MyAdapter(List<Msg> msgList){
            mMsgList = msgList;
        }
        @NonNull
        @Override
        //產生holder 和view 對接的介面
        public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //把view take 出來後跟viewholder 對接
            View itemview= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
            MyViewHolder aa=new MyViewHolder(itemview);
            return aa;
        }

        @Override
        //處理資料處理的細節
        public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder  holder, int position) {
            //holder.title.setText(data.get(position).get("title"));
            //holder.date.setText(data.get(position).get("date"));
            Msg msg = mMsgList.get(position);
            if(msg.getType() == Msg.TYPE_RECEIVED){
//如果是收到的訊息，則顯示左邊的訊息佈局，將右邊的訊息佈局隱藏
                holder.leftlayout.setVisibility(View.VISIBLE);
                holder.rightlayout.setVisibility(View.GONE);
                holder.leftMsg.setText(msg.getContent());
            }else if(msg.getType() == Msg.TYPE_SENT){
//如果是發出的訊息，則顯示右邊的訊息佈局，將左邊的訊息佈局隱藏
                holder.rightlayout.setVisibility(View.VISIBLE);
                holder.leftlayout.setVisibility(View.GONE);
                holder.rightMsg.setText(msg.getContent());
            }
        }
        @Override
        //總共有幾筆資料
        public int getItemCount() {
            return mMsgList.size();
        }
    }


    public void medicinemethod(View view) {
    }

    public void cloudmethod(View view) {
    }
}