package com.example.okarepro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


public class Login extends AppCompatActivity implements View.OnClickListener {
    RequestQueue queue ;

    Button registerButton, loginButton;
    EditText accountEditText, passwordEditText;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String account_username, account_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponent();
        loginButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (sharedPreferences.getString("username", null) != null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void initComponent(){
        queue = Volley.newRequestQueue(Login.this);

        loginButton = findViewById(R.id.login_button);
        accountEditText = findViewById(R.id.login_account);
        passwordEditText = findViewById(R.id.login_password);
        sharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        account_username = sharedPreferences.getString("username", null);
        account_password = sharedPreferences.getString("password", null);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_button:
                postData(accountEditText.getText().toString(), passwordEditText.getText().toString());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    private void postData(String un, String pw){
        String url = "https://okareproserver.lionfree.net/api/v1.0.0/login.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {

            try {
                JSONObject respObj = new JSONObject(response);
                String msg = respObj.getString("msg");

                if (msg.equals("登入成功")){
                    Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
                    editor.putString("username", un);
                    editor.commit();
                    getProductKey(un);
                }
                else {
                    Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(Login.this, "Fail to post data = " + error, Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", un);
                params.put("password", pw);
                return params;
            }
        };
        queue.add(stringRequest);
    }
    private void getProductKey(String un){
        String url = "https://okareproserver.lionfree.net/api/v1.0.0/getUserProductKey.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject respObj = new JSONObject(response);
                int resp = respObj.getInt("response");
                String msg = respObj.getString("msg");
                if (resp == 100){
                    editor.putString("product_key", msg);
                    editor.commit();
                    Intent intent2 = new Intent(this, MainActivity.class);
                    startActivity(intent2);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(Login.this, "Fail to post data = " + error, Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", un);
                return params;
            }
        };
        queue.add(stringRequest);
    }

}