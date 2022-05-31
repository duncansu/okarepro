package com.example.okarepro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
//用bundle+intent將資料傳出去

public class setting extends AppCompatActivity {
    EditText editTextTextPersonName, editTextPhone, editTextTextPersonName2, editTextPhone2;
    Button savebutton;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sp=getSharedPreferences("contactperson",MODE_PRIVATE);
        editor=sp.edit();


        editTextTextPersonName = (EditText) findViewById(R.id.editTextTextPersonName);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextTextPersonName2 = (EditText) findViewById(R.id.editTextTextPersonName2);
        editTextPhone2 = (EditText) findViewById(R.id.editTextPhone2);
        savebutton = (Button) findViewById(R.id.savebutton);
        savebutton.setOnClickListener(new View.OnClickListener() {
                                          public void onClick(View view) {
                                              editor.putString("TextPersonName1", editTextTextPersonName.getText().toString());
                                              editor.putString("TextPersonPhone1", editTextPhone.getText().toString());
                                              editor.putString("TextPersonName2", editTextTextPersonName2.getText().toString());
                                              editor.putString("TextPersonPhone2", editTextPhone2.getText().toString());
                                              editor.commit();
                                              Intent intent1 = new Intent();
                                              System.out.print(editTextPhone.getText().toString());
                                              intent1.setClass(setting.this, MainActivity.class);
//                                              intent1.putExtra("TextPersonName1", editTextTextPersonName.getText().toString());
//                                              intent1.putExtra("TextPersonPhone1", editTextPhone.getText().toString());
//                                              intent1.putExtra("TextPersonName2", editTextTextPersonName2.getText().toString());
//                                              intent1.putExtra("TextPersonPhone2", editTextPhone2.getText().toString());
                                              startActivity(intent1);
                                              overridePendingTransition(R.anim.slide_left, R.anim.slide_left);

                                          }
                                      }
        );


    }
}