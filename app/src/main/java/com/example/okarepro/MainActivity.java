package com.example.okarepro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Msg> msgList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageButton send;
    private MyAdapter adapter;
    //測試用資料集
    private LinkedList<HashMap<String,String>> data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initMsgs();

        recyclerView=findViewById(R.id.recycLerview);
        recyclerView.setHasFixedSize(true);
        // recycleview 裡面都要設定控管排版的LayoutManager
        mLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        //將自訂義好的Adapter 接上recycleView 的Adapter
        adapter=new MyAdapter(msgList);
        recyclerView.setAdapter(adapter);
        send = (ImageButton)findViewById(R.id.main_btn_7);
        send.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                String content="hello duncan";
                if(!"".equals(content)){
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    Msg msg1=new Msg("沒問題",Msg.TYPE_RECEIVED);
                    msgList.add(msg);
                    msgList.add(msg1);
                    adapter.notifyItemInserted(msgList.size()-1);//當有訊息時，重新整理ListView中的顯示
                   //adapter.notifyDataSetChanged();

                    recyclerView.scrollToPosition(msgList.size()-1);//將ListView定位到最後一行
                }
            }
        });

    }


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

        public MyViewHolder(View v){
            //mTextView=v;
            super(v);
            leftlayout=v.findViewById(R.id.left_layout);
            rightlayout=v.findViewById(R.id.right_layout);
            leftMsg=v.findViewById(R.id.left_msg);
            rightMsg=v.findViewById(R.id.right_msg);
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
    private void initMsgs(){
        Msg msg1 = new Msg("Hello guy.", Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2 = new Msg("Hello. Who is that?", Msg.TYPE_SENT);
        msgList.add(msg2);
        Msg msg3 = new Msg("This is Tom, Nice talking to you. ", Msg.TYPE_RECEIVED);
        msgList.add(msg3);
    }


    public void healthymethod(View view) {
    }

    public void medicinemethod(View view) {
    }

    public void cloudmethod(View view) {
    }
}